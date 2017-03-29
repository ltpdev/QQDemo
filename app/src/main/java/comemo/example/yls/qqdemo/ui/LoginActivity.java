package comemo.example.yls.qqdemo.ui;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import butterknife.BindView;
import butterknife.OnClick;
import comemo.example.yls.qqdemo.MainActivity;
import comemo.example.yls.qqdemo.R;
import comemo.example.yls.qqdemo.utils.ThreadUtils;

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

    @Override
    public int getLayoutResId() {

        return R.layout.activity_login;
    }

    @Override
    protected void init() {
        super.init();

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
                Log.d("main", "登录聊天服务器失败！");
            }
        });
    }
}
