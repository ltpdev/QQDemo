package comemo.example.yls.qqdemo.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bumptech.glide.Glide;

import cn.bmob.v3.http.bean.Init;
import comemo.example.yls.qqdemo.R;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class LookPictureActivity extends AppCompatActivity {
    private PhotoView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_picture);
        initView();
    }

    private void initView() {
        photoView = (PhotoView) findViewById(R.id.photoview);
        String url = getIntent().getStringExtra("url");
        Glide.with(LookPictureActivity.this).load(url).into(photoView);
    }
}
