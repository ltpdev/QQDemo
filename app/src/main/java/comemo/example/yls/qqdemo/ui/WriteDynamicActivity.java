package comemo.example.yls.qqdemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;
import comemo.example.yls.qqdemo.R;
import comemo.example.yls.qqdemo.adaper.SelectPhotoAdapter;
import comemo.example.yls.qqdemo.listener.ItemListener;
import comemo.example.yls.qqdemo.model.Dynamic;
import comemo.example.yls.qqdemo.model.User;
import comemo.example.yls.qqdemo.widget.CustomGridView;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

public class WriteDynamicActivity extends BaseActivity implements ItemListener{


    @BindView(R.id.edt_content)
    EditText edtContent;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.update)
    TextView update;
    @BindView(R.id.gridView)
    CustomGridView gridView;
    @BindView(R.id.addphoto)
    ImageView add;
    private int count=9;
    private List<String> photoList = new ArrayList<>();
    private SelectPhotoAdapter selectPhotoAdapter;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_write_dynamic;
    }

    @Override
    protected void init() {
        super.init();
        title.setText("发表说说");
        back.setVisibility(View.VISIBLE);
        update.setVisibility(View.VISIBLE);
        update.setText("发送");
        selectPhotoAdapter = new SelectPhotoAdapter(this, photoList);
        selectPhotoAdapter.setItemListener(this);
        gridView.setAdapter(selectPhotoAdapter);

    }


    @OnClick({R.id.back, R.id.update,R.id.addphoto})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.addphoto:
                PhotoPicker.builder()
                        .setPhotoCount(count)
                        .setShowCamera(true)
                        .setShowGif(false)
                        .setPreviewEnabled(false)
                        .start(this, PhotoPicker.REQUEST_CODE);
                break;
            case R.id.update:
                if (photoList.size()!=0){
                    showProgressDialog("正在发表....");
                    updatePhoto();
                }else {
                    showProgressDialog("正在发表....");
                    publishDynamic(null);
                }

                break;
        }
    }

    private void publishDynamic(List<String>images) {
            String content=edtContent.getText().toString();
            if (content.length()==0){
                Toast.makeText(WriteDynamicActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            User user= BmobUser.getCurrentUser(User.class);
            Dynamic dynamic=new Dynamic();
            dynamic.setContent(content);
            dynamic.setPhotos(images);
            dynamic.setAuthor(user);
            dynamic.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e==null){
                    hideProgressDialog();
                    Toast.makeText(WriteDynamicActivity.this, "发表成功", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(WriteDynamicActivity.this, "发表失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void updatePhoto() {
        int num=photoList.size();
        final String[] filePaths = new String[num];
        for (int i = 0; i <num; i++) {
            filePaths[i]= photoList.get(i);
        }
        BmobFile.uploadBatch(filePaths, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> list, List<String> urls) {
                if(urls.size()==filePaths.length){//如果数量相等，则代表文件全部上传完成
                    //do something
                    publishDynamic(urls);
                }else {
                    hideProgressDialog();
                    Toast.makeText(WriteDynamicActivity.this, "有些图片上传失败", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onProgress(int i, int i1, int i2, int i3) {

            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(WriteDynamicActivity.this, "错误", Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                for (int i = 0; i < photos.size(); i++) {
                    photoList.add(photos.get(i));
                }
                count=count-photos.size();
                if (count==0){
                  add.setVisibility(View.GONE);
                }
                selectPhotoAdapter.notifyDataSetChanged();
            }
        }
    }


    @Override
    public void del(int position) {
        count++;
        add.setVisibility(View.VISIBLE);
        photoList.remove(position);
        selectPhotoAdapter.notifyDataSetChanged();
    }

    @Override
    public void preview(int position) {
        PhotoPreview.builder()
                .setPhotos((ArrayList<String>) photoList)
                .setCurrentItem(position)
                .setShowDeleteButton(false)
                .start(this);
    }
}
