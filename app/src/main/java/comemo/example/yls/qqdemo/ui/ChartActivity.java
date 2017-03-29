package comemo.example.yls.qqdemo.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import comemo.example.yls.qqdemo.R;
import comemo.example.yls.qqdemo.adaper.ChartAdaper;
import comemo.example.yls.qqdemo.presenter.ChartPresenter;
import comemo.example.yls.qqdemo.presenter.impl.ChartPresenterImpl;
import comemo.example.yls.qqdemo.utils.ThreadUtils;
import comemo.example.yls.qqdemo.view.ChartView;
import comemo.example.yls.qqdemo.widget.AudioRecorderButton;

import static android.R.attr.name;

/**
 * Created by yls on 2016/12/30.
 */

public class ChartActivity extends BaseActivity implements ChartView {
    @BindView(R.id.chartRecyclerView)
    RecyclerView mChartRecyclerView;
    @BindView(R.id.chartText)
    EditText mChartText;
    @BindView(R.id.send)
    Button mSend;
    @BindView(R.id.title)
    TextView mTextView;
    @BindView(R.id.back)
    ImageView mImageView;
    private String mContact;
    private ChartPresenter mChartPresenter;
    private ChartAdaper mChartAdaper;
    private ImageView imgQiehuan;
    private RelativeLayout textRelativeLayout;
    private RelativeLayout soundRelativeLayout;
    private boolean isQiehuan=false;
    private ImageView imgMore;
    private AudioRecorderButton mAudioRecorderButton;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_chart;
    }

    @Override
    protected void init() {
        super.init();
        initView();
        mChartPresenter = new ChartPresenterImpl(this);
        initRecyclerView();
        mImageView.setVisibility(View.VISIBLE);
        mContact = getIntent().getStringExtra("contact");
        String str = String.format(getString(R.string.contact), mContact);
        mTextView.setText(str);
        mChartText.addTextChangedListener(textWatcher);
        mChartPresenter.loadMessages(mContact);
        EMClient.getInstance().chatManager().addMessageListener(emMessageListener);
    }

    private void initView() {
        imgQiehuan= (ImageView) findViewById(R.id.qiehuan);
        textRelativeLayout= (RelativeLayout) findViewById(R.id.text_relativeLayout);
        soundRelativeLayout= (RelativeLayout) findViewById(R.id.sound_relativeLayout);
        imgQiehuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isQiehuan){
                    imgQiehuan.setImageResource(R.mipmap.sound);
                    textRelativeLayout.setVisibility(View.VISIBLE);
                    soundRelativeLayout.setVisibility(View.GONE);
                    isQiehuan=false;
                }else {
                    imgQiehuan.setImageResource(R.mipmap.write);
                    textRelativeLayout.setVisibility(View.GONE);
                    soundRelativeLayout.setVisibility(View.VISIBLE);
                    isQiehuan=true;
                }
            }
        });
        imgMore= (ImageView) findViewById(R.id.img_more);
        imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选择图库
                if (ContextCompat.checkSelfPermission(ChartActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(ChartActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else {
                    openAlbum();
                }
            }
        });
        mAudioRecorderButton= (AudioRecorderButton) findViewById(R.id.btn_record);
        mAudioRecorderButton.setAudioFinshRecorderListener(new AudioRecorderButton.AudioFinshRecorderListener() {
            @Override
            public void OnFinish(float seconds, String filePath) {
                mChartPresenter.sendVoiceMessage(filePath,seconds,mContact);
            }
        });
    }

    private void initRecyclerView() {
        mChartAdaper = new ChartAdaper(this, mChartPresenter.getDataList());
        mChartRecyclerView.setHasFixedSize(true);
        mChartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mChartRecyclerView.setAdapter(mChartAdaper);
    }


    @OnClick({R.id.send, R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.send:
                mChartPresenter.send(mChartText.getText().toString().trim(), mContact);
                break;
        }
    }

    @Override
    public void sendMessageSucucess() {
        Toast.makeText(ChartActivity.this, getString(R.string.send_succuess), Toast.LENGTH_LONG).show();
        mChartAdaper.notifyDataSetChanged();
        mChartText.getText().clear();
    }

    @Override
    public void sendMessageFailed() {
        Toast.makeText(ChartActivity.this, getString(R.string.send_failed), Toast.LENGTH_LONG).show();
        mChartAdaper.notifyDataSetChanged();
    }

    @Override
    public void onStartMessage() {
        mChartAdaper.notifyDataSetChanged();
        mChartRecyclerView.smoothScrollToPosition(mChartPresenter.getDataList().size() - 1);
    }

    @Override
    public void onMessageLoaded() {
        Toast.makeText(ChartActivity.this, getString(R.string.load_message_finished), Toast.LENGTH_SHORT).show();
        mChartAdaper.notifyDataSetChanged();
        mChartRecyclerView.smoothScrollToPosition(mChartPresenter.getDataList().size() - 1);
    }

    @Override
    public void sendPicMessageSucucess() {
        Toast.makeText(ChartActivity.this, "发送图片消息成功", Toast.LENGTH_SHORT).show();
        mChartAdaper.notifyDataSetChanged();
        mChartRecyclerView.smoothScrollToPosition(mChartPresenter.getDataList().size() - 1);


    }

    @Override
    public void sendPicMessageFailed() {
        Toast.makeText(ChartActivity.this, "发送图片消息失败", Toast.LENGTH_SHORT).show();
        mChartAdaper.notifyDataSetChanged();
    }

    @Override
    public void sendVoiceMessageSucucess() {
        Toast.makeText(ChartActivity.this, "发送语言消息成功", Toast.LENGTH_SHORT).show();
        mChartAdaper.notifyDataSetChanged();
        mChartRecyclerView.smoothScrollToPosition(mChartPresenter.getDataList().size() - 1);
    }

    @Override
    public void sendVoiceMessageFailed() {
        Toast.makeText(ChartActivity.this, "发送语言消息成功", Toast.LENGTH_SHORT).show();
        mChartAdaper.notifyDataSetChanged();
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            mSend.setEnabled(s.toString().length() > 0);
        }
    };
    private EMMessageListener emMessageListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(final List<EMMessage> list) {
            ThreadUtils.rinOnMainThread(new Runnable() {
                @Override
                public void run() {
                    EMMessage emMessage = list.get(0);
                    if (emMessage.getFrom().equals(mContact)) {
                        mChartAdaper.addEMMessage(emMessage);
                        mChartRecyclerView.smoothScrollToPosition(mChartPresenter.getDataList().size() - 1);
                    }
                }
            });
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> list) {


        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1002) {

            //判断系统版本号
            if (Build.VERSION.SDK_INT >= 19) {
                //4.4版本及以上获取路径的方法
                handleImageOnKitKat(data);
            } else {
                //4.4版本以下获取路径的方法
                handleImageBeforeKitKat(data);
            }



        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);

            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }

        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null);

        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
        mChartPresenter.sendPictureMessage(imagePath,mContact);


    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        mChartPresenter.sendPictureMessage(imagePath,mContact);
        displayImage(imagePath);

    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            /*img.setImageBitmap(bitmap);*/
        }
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    private void openAlbum() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1002);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }
                break ;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }





}
