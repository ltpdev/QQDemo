package comemo.example.yls.qqdemo.ui;

import android.os.Handler;

import comemo.example.yls.qqdemo.MainActivity;
import comemo.example.yls.qqdemo.R;
import comemo.example.yls.qqdemo.presenter.SplashPresenter;
import comemo.example.yls.qqdemo.presenter.impl.SplashPresenterImpl;
import comemo.example.yls.qqdemo.view.SplashView;

/**
 * Created by yls on 2016/12/28.
 */

public class SplashActivity extends BaseActivity implements SplashView{
    private static final int DELAY = 2000;
    private Handler mHandler = new Handler();
    private SplashPresenter mSplashPresenter;

    @Override
    public int getLayoutResId() {
        return R.layout.splash;
    }

    @Override
    protected void init() {
        super.init();
        mSplashPresenter=new SplashPresenterImpl(this);
        mSplashPresenter.checkLoginStatus();

    }



    private void navigateToLogin() {

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                goTo(LoginActivity.class);

            }
        }, DELAY);
    }

    @Override
    public void onLogined() {

        goTo(MainActivity.class);
    }

    @Override
    public void onNotLogined() {

        navigateToLogin();
    }
}
