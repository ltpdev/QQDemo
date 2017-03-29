package comemo.example.yls.qqdemo.ui;

import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;
import comemo.example.yls.qqdemo.R;
import comemo.example.yls.qqdemo.presenter.RegisterPresenter;
import comemo.example.yls.qqdemo.presenter.impl.RegisterPresenterImpl;
import comemo.example.yls.qqdemo.view.RegisterView;

/**
 * Created by yls on 2016/12/28.
 */
public class RegisterActivity extends BaseActivity implements RegisterView {
    private static final String TAG = "RegisterActivity";
    private RegisterPresenter mRegisterPresenter;
    @BindView(R.id.user_name)
    EditText mUserName;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.confirm_password)
    EditText mConfirmPassword;
    @BindView(R.id.register)
    Button mRegister;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_register;
    }

    @Override
    protected void init() {
        super.init();
        mRegisterPresenter = new RegisterPresenterImpl(this);
        mConfirmPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    register();
                    return true;
                }
                return false;
            }
        });

    }


    @OnClick(R.id.register)
    public void onClick() {

        register();
    }

    private void register() {
        String name = mUserName.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        String confirmpassword = mConfirmPassword.getText().toString().trim();
        mRegisterPresenter.register(name, password, confirmpassword);


    }

    @Override
    public void onRegisterSuccessed() {
        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
        hideProgressDialog();
        goTo(LoginActivity.class);
    }

    @Override
    public void onRegisterFailed() {

        Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUserNameError() {
        mUserName.setError(getString(R.string.user_name_error));
    }

    @Override
    public void onPasswordError() {
        mPassword.setError(getString(R.string.password_error));
    }

    @Override
    public void onConfirmpasswordError() {
        mConfirmPassword.setError(getString(R.string.confirmPassword_error));
    }

    @Override
    public void onStartRegister() {
       showProgressDialog(getString(R.string.registering));
        hideKeyBoard();
    }
}
