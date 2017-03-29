package comemo.example.yls.qqdemo.presenter.impl;

import com.hyphenate.chat.EMClient;

import comemo.example.yls.qqdemo.presenter.SplashPresenter;
import comemo.example.yls.qqdemo.view.SplashView;

/**
 * Created by yls on 2016/12/28.
 */

public class SplashPresenterImpl implements SplashPresenter {
    private SplashView mSplashView;

    public SplashPresenterImpl(SplashView mSplashView) {

        this.mSplashView = mSplashView;
    }

    @Override
    public void checkLoginStatus() {
        if (isLogined()) {
            mSplashView.onLogined();
        } else {
            mSplashView.onNotLogined();
        }
    }

    private boolean isLogined() {

        return EMClient.getInstance().isLoggedInBefore() && EMClient.getInstance().isConnected();

    }
}
