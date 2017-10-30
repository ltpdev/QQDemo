package comemo.example.yls.qqdemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import comemo.example.yls.qqdemo.R;
import comemo.example.yls.qqdemo.model.User;

public class PersonInfoActivity extends BaseActivity {


    @BindView(R.id.fruit_image_view)
    ImageView fruitImageView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toobar)
    CollapsingToolbarLayout collapsingToobar;
    @BindView(R.id.signature)
    TextView signature;
    @BindView(R.id.send_msg)
    Button sendMsg;
    @BindView(R.id.phone_num)
    TextView phoneNum;
    @BindView(R.id.sex)
    TextView sex;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.email)
    TextView email;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_person_info;
    }

    @Override
    protected void init() {
        super.init();
        initToolbar();
        initData();
    }

    private void initData() {
        String username = getIntent().getStringExtra("contact");
        collapsingToobar.setTitle(username);
        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("username", username);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> object, BmobException e) {
                if (e == null) {
                    //toast("查询用户成功:"+object.size());
                    User user = object.get(0);
                    if (user.getHead() != null && !user.getHead().equals("")) {
                        Glide.with(PersonInfoActivity.this).load(user.getHead()).into(fruitImageView);
                    } else {
                        Glide.with(PersonInfoActivity.this).load(R.drawable.defalut_bg).into(fruitImageView);
                    }
                    if (user.getSignature() != null && !user.getSignature().equals("")) {
                        signature.setText("个性签名：" + user.getSignature());
                    }
                    if (user.getMobilePhoneNumber() != null && !user.getMobilePhoneNumber().equals("")) {
                        phoneNum.setText("手机号：" +user.getMobilePhoneNumber());
                    }
                    if (user.getSex() != null && !user.getSex().equals("")) {
                        sex.setText("性别：" +user.getSex());
                    }
                    if (user.getEmail() != null && !user.getEmail().equals("")) {
                        email.setText("邮箱：" +user.getEmail());
                    }
                    if (user.getAddress() != null && !user.getAddress().equals("")) {
                        address.setText("地址：" +user.getAddress());
                    }

                } else {

                }
            }
        });
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    @OnClick(R.id.send_msg)
    public void onClick() {
        Intent intent = new Intent(this, ChartActivity.class);
        intent.putExtra("contact", getIntent().getStringExtra("contact"));
        startActivity(intent);
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
