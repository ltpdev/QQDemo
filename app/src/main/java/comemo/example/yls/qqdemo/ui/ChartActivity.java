package comemo.example.yls.qqdemo.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.PathUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import comemo.example.yls.qqdemo.R;
import comemo.example.yls.qqdemo.adaper.ChartAdaper;
import comemo.example.yls.qqdemo.event.SendMessageEvent;
import comemo.example.yls.qqdemo.presenter.ChartPresenter;
import comemo.example.yls.qqdemo.presenter.impl.ChartPresenterImpl;
import comemo.example.yls.qqdemo.utils.ThreadUtils;
import comemo.example.yls.qqdemo.view.ChartView;
import comemo.example.yls.qqdemo.widget.AudioRecorderButton;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

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
    @BindView(R.id.img_more)
    ImageView imgMore;
    @BindView(R.id.add)
    ImageView add;
    @BindView(R.id.bottomBar)
    RelativeLayout bottomBar;
    private String mContact;
    private ChartPresenter mChartPresenter;
    private ChartAdaper mChartAdaper;
    private ImageView imgQiehuan;
    private RelativeLayout textRelativeLayout;
    private RelativeLayout soundRelativeLayout;
    private boolean isQiehuan = false;
    private AudioRecorderButton mAudioRecorderButton;
    private PopupWindow popupWindow;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

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


    private void showPopupWindow() {
        //设置contentView
        if (popupWindow == null) {
            View contentView = LayoutInflater.from(ChartActivity.this).inflate(R.layout.layout_menu, null);
            popupWindow = new PopupWindow(contentView);
            popupWindow.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
            popupWindow.setHeight(ViewGroup.LayoutParams.FILL_PARENT);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setOutsideTouchable(true);
            RelativeLayout relativeLayout = (RelativeLayout) contentView.findViewById(R.id.relativeLayout);
            TextView photo = (TextView) contentView.findViewById(R.id.photo);
            TextView camera = (TextView) contentView.findViewById(R.id.camera);
            photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ContextCompat.checkSelfPermission(ChartActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ChartActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    } else {
                        openAlbum();
                    }
                }
            });
            camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openCamera();
                }
            });
            ///
            relativeLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popupWindow.dismiss();
                    return true;
                }
            });
        }
//
        View rootview = LayoutInflater.from(ChartActivity.this).inflate(R.layout.activity_chart, null);
        popupWindow.showAtLocation(rootview, Gravity.CENTER, 0, 0);

    }

    private void initView() {
        add.setVisibility(View.VISIBLE);
        add.setImageResource(R.drawable.user2);
        imgQiehuan = (ImageView) findViewById(R.id.qiehuan);
        textRelativeLayout = (RelativeLayout) findViewById(R.id.text_relativeLayout);
        soundRelativeLayout = (RelativeLayout) findViewById(R.id.sound_relativeLayout);
        imgQiehuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isQiehuan) {
                    imgQiehuan.setImageResource(R.mipmap.sound);
                    textRelativeLayout.setVisibility(View.VISIBLE);
                    soundRelativeLayout.setVisibility(View.GONE);
                    isQiehuan = false;

                } else {
                    imgQiehuan.setImageResource(R.mipmap.write);
                    textRelativeLayout.setVisibility(View.GONE);
                    soundRelativeLayout.setVisibility(View.VISIBLE);
                    isQiehuan = true;
                }
            }
        });

        mAudioRecorderButton = (AudioRecorderButton) findViewById(R.id.btn_record);
        mAudioRecorderButton.setAudioFinshRecorderListener(new AudioRecorderButton.AudioFinshRecorderListener() {
            @Override
            public void OnFinish(float seconds, String filePath) {
                mChartPresenter.sendVoiceMessage(filePath, seconds, mContact);
            }
        });
    }

    private void initRecyclerView() {
        mChartAdaper = new ChartAdaper(this, mChartPresenter.getDataList());
        mChartRecyclerView.setHasFixedSize(true);
        mChartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mChartRecyclerView.setAdapter(mChartAdaper);
    }


    @OnClick({R.id.send, R.id.back, R.id.img_more,R.id.add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.send:
                mChartPresenter.send(mChartText.getText().toString().trim(), mContact);
                break;

            case R.id.img_more:
                showPopupWindow();
                break;

            case R.id.add:
                Intent intent = new Intent(this, PersonInfoActivity.class);
                intent.putExtra("contact", getIntent().getStringExtra("contact"));
                startActivity(intent);
                break;
        }
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

//在这里的QUALITY参数，值为两个，一个是0，一个是1，代表录制视频的清晰程度，0最不清楚，1最清楚

//没有0-1的中间值，另外，使用1也是比较占内存的，测试了一下，录制1分钟，大概内存是43M多

//使用0，录制1分钟大概内存是几兆
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        // 限制时长 ，参数61代表61秒，可以根据需求自己调，最高应该是2个小时。

//当在这里设置时长之后，录制到达时间，系统会自动保存视频，停止录制
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 61);
        // 限制大小 限制视频的大小，这里是100兆。当大小到达的时候，系统会自动停止录制
        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 1024 * 1024 * 100);

//在这里有录制完成之后的操作，系统会默认把视频放到照片的文件夹中
        startActivityForResult(intent, 1001);
    }


    @Override
    public void sendMessageSucucess() {
        Toast.makeText(ChartActivity.this, getString(R.string.send_succuess), Toast.LENGTH_LONG).show();
        mChartAdaper.notifyDataSetChanged();
        mChartText.getText().clear();
        updateConversion();
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
        updateConversion();

    }


    private void updateConversion() {
        EventBus.getDefault().post(new SendMessageEvent(true));
    }

    @Override
    public void sendPicMessageFailed() {
        Toast.makeText(ChartActivity.this, "发送图片消息失败", Toast.LENGTH_SHORT).show();
        mChartAdaper.notifyDataSetChanged();
    }

    @Override
    public void sendVoiceMessageSucucess() {
        Toast.makeText(ChartActivity.this, "发送语音消息成功", Toast.LENGTH_SHORT).show();
        mChartAdaper.notifyDataSetChanged();
        mChartRecyclerView.smoothScrollToPosition(mChartPresenter.getDataList().size() - 1);
        updateConversion();
    }

    @Override
    public void sendVoiceMessageFailed() {
        Toast.makeText(ChartActivity.this, "发送语音消息失败", Toast.LENGTH_SHORT).show();
        mChartAdaper.notifyDataSetChanged();
    }

    @Override
    public void sendVideoMessageSucucess() {
        Toast.makeText(ChartActivity.this, "发送视频消息成功", Toast.LENGTH_SHORT).show();
        mChartAdaper.notifyDataSetChanged();
        mChartRecyclerView.smoothScrollToPosition(mChartPresenter.getDataList().size() - 1);
        updateConversion();
    }

    @Override
    public void sendVideoMessageFailed() {
        Toast.makeText(ChartActivity.this, "发送视频消息失败", Toast.LENGTH_SHORT).show();
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
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();

            }
            //判断系统版本号
            if (Build.VERSION.SDK_INT >= 19) {
                //4.4版本及以上获取路径的方法
                handleImageOnKitKat(data);
            } else {
                //4.4版本以下获取路径的方法
                handleImageBeforeKitKat(data);
            }
        }
        if (requestCode == 1001) {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
            if (data == null) {
                return;
            }
            String videopath = null;
            int length = 0;
            Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    videopath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                    length = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                }
                cursor.close();
            }
            File file = new File(PathUtil.getInstance().getImagePath(), "thvideo" + System.currentTimeMillis());
            try {
                FileOutputStream fos = new FileOutputStream(file);
                Bitmap ThumbBitmap = ThumbnailUtils.createVideoThumbnail(videopath, 3);
                ThumbBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            mChartPresenter.sendVideoMessage(videopath, file.getAbsolutePath(), length, mContact);
            //Toast.makeText(this, "路径："+videopath, Toast.LENGTH_SHORT).show();
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        if (data == null) {
            return;
        }
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
        mChartPresenter.sendPictureMessage(imagePath, mContact);


    }

    private void handleImageBeforeKitKat(Intent data) {
        if (data == null) {
            return;
        }
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        mChartPresenter.sendPictureMessage(imagePath, mContact);
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
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JCVideoPlayer.releaseAllVideos();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Chart Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }




}
