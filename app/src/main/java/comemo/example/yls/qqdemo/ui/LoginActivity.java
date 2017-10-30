package comemo.example.yls.qqdemo.ui;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import comemo.example.yls.qqdemo.MainActivity;
import comemo.example.yls.qqdemo.R;
import comemo.example.yls.qqdemo.model.User;
import comemo.example.yls.qqdemo.utils.ThreadUtils;
import comemo.example.yls.qqdemo.widget.CommonVideoView;

/**
 * Created by yls on 2016/12/28.
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.user_name)
    EditText mUserName;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.login)
    Button mLogin;
    @BindView(R.id.new_user)
    TextView mNewUser;
    @BindView(R.id.videoView)
    CommonVideoView videoView;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    protected void init() {
        super.init();
        playVideoView();
    }

    private void playVideoView() {
        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video));
        //播放
        videoView.start();
        //循环播放
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoView.start();
            }
        });
    }

    //返回重启加载
    @Override
    protected void onRestart() {
        playVideoView();
        super.onRestart();
    }

    //防止锁屏或者切出的时候，音乐在播放
    @Override
    protected void onStop() {
        videoView.stopPlayback();
        super.onStop();
    }



    @OnClick({R.id.login, R.id.new_user})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                login();
                showProgressDialog(getString(R.string.logining));
                hideKeyBoard();
                break;
            case R.id.new_user:
                goTo(RegisterActivity.class);
                break;
        }
    }

    private void login() {
        BmobUser user=new BmobUser();
        user.setUsername(mUserName.getText().toString());
        user.setPassword(mPassword.getText().toString());
        user.login(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser user, BmobException e) {
                if(e==null){
                    loginToEaseMob();
                }else{
                   Toast.makeText(LoginActivity.this, "登录失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void loginToEaseMob() {
        EMClient.getInstance().login(mUserName.getText().toString(), mPassword.getText().toString(), new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                ThreadUtils.rinOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();
                        hideProgressDialog();
                        goTo(MainActivity.class);
                    }
                });

                Log.d("main", "登录聊天服务器成功！");
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Toast.makeText(LoginActivity.this, "登录聊天服务器失败！", Toast.LENGTH_SHORT).show();
                Log.d("main", "登录聊天服务器失败！");
            }
        });
    }


}
