package comemo.example.yls.qqdemo.view;

/**
 * Created by yls on 2016/12/29.
 */

public interface RegisterView {
    void onRegisterSuccessed();

    void onRegisterFailed();

    

    void onUserNameError();

    void onPasswordError();

    void onConfirmpasswordError();

    void onStartRegister();
}
