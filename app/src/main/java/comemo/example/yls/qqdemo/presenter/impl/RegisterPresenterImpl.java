package comemo.example.yls.qqdemo.presenter.impl;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import comemo.example.yls.qqdemo.presenter.RegisterPresenter;
import comemo.example.yls.qqdemo.utils.StringUtils;
import comemo.example.yls.qqdemo.utils.ThreadUtils;
import comemo.example.yls.qqdemo.view.RegisterView;

/**
 * Created by yls on 2016/12/29.
 */

public class RegisterPresenterImpl implements RegisterPresenter {
    private RegisterView mRegisterView;

    public RegisterPresenterImpl(RegisterView mRegisterView) {
        this.mRegisterView = mRegisterView;

    }

    @Override
    public void register(String name, String password, String confirmpassword) {
        if (StringUtils.isVailedUserName(name)) {
            if (StringUtils.isVailedPassword(password)) {
                if (password.equals(confirmpassword)) {

                    mRegisterView.onStartRegister();
                    registerToBmob(name, password);
                } else {
                    mRegisterView.onConfirmpasswordError();
                }
            } else {
                mRegisterView.onPasswordError();
            }
        } else {
            mRegisterView.onUserNameError();
        }

    }

    public void registerToBmob(final String name, final String password) {
        BmobUser bmobUser = new BmobUser();
        bmobUser.setUsername(name);
        bmobUser.setPassword(password);
        bmobUser.signUp(new SaveListener<BmobUser>() {

            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                if (e == null) {
                    registerToEaseMob(name, password);
                } else {

                    mRegisterView.onRegisterFailed();
                }
            }
        });
    }

    public void registerToEaseMob(final String name, final String password) {

        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().createAccount(name, password);
                    ThreadUtils.rinOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mRegisterView.onRegisterSuccessed();
                        }
                    });

                } catch (HyphenateException e) {
                    e.printStackTrace();
                    ThreadUtils.rinOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mRegisterView.onRegisterFailed();
                        }
                    });

                }
            }
        });
    }
}
