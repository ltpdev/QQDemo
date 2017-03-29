package comemo.example.yls.qqdemo.ui;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import butterknife.BindView;
import butterknife.OnClick;
import comemo.example.yls.qqdemo.R;
import comemo.example.yls.qqdemo.utils.ThreadUtils;

/**
 * Created by yls on 2016/12/29.
 */

public class DynamicFragment extends BaseFragment {
    @BindView(R.id.logout)
    Button mLogout;
    @BindView(R.id.title)
    TextView mtitle;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_dynamic;
    }

    @Override
    protected void init() {
        super.init();
        mtitle.setText(getString(R.string.dynamic));
        String logout = String.format(getString(R.string.logout), EMClient.getInstance().getCurrentUser());
        mLogout.setText(logout);
    }

    @OnClick(R.id.logout)
    public void onClick() {
        EMClient.getInstance().logout(true, new EMCallBack() {

            @Override
            public void onSuccess() {
                ThreadUtils.rinOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        goTo(LoginActivity.class);
                    }
                });

            }

            @Override
            public void onProgress(int progress, String status) {


            }

            @Override
            public void onError(int code, String message) {
                ThreadUtils.rinOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), getString(R.string.logout_error), Toast.LENGTH_SHORT).show();;
                    }
                });

            }
        });
    }
}
