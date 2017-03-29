package comemo.example.yls.qqdemo.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import comemo.example.yls.qqdemo.MainActivity;
import comemo.example.yls.qqdemo.R;
import comemo.example.yls.qqdemo.utils.ThreadUtils;

/**
 * Created by asus- on 2017/1/8.
 */

public class confirmationActivity extends BaseActivity {
    @BindView(R.id.tvTxt)
    TextView tvTxt;
    @BindView(R.id.confirm)
    Button confirm;
    @BindView(R.id.refuse)
    Button refuse;
    private String name;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_confirmation;

    }

    @Override
    protected void init() {
        super.init();
        ButterKnife.bind(this);
        name = getIntent().getStringExtra("username");
        tvTxt.setText(name+"：添加你为好友");
        NotificationManager manager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(0);
    }



    @OnClick({R.id.confirm, R.id.refuse})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirm:
                ThreadUtils.runOnBackgroundThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EMClient.getInstance().contactManager().acceptInvitation(name);
                            ThreadUtils.rinOnMainThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(confirmationActivity.this, "成功地同意好友请求", Toast.LENGTH_SHORT).show();
                                    goTo(MainActivity.class);
                                }
                            });
                        } catch (HyphenateException e) {
                            ThreadUtils.rinOnMainThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(confirmationActivity.this, "失败地同意好友请求", Toast.LENGTH_SHORT).show();
                                }
                            });

                            e.printStackTrace();
                        }
                    }
                });

                break;
            case R.id.refuse:
                ThreadUtils.runOnBackgroundThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EMClient.getInstance().contactManager().declineInvitation(name);
                            ThreadUtils.rinOnMainThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(confirmationActivity.this, "成功地拒绝好友请求", Toast.LENGTH_SHORT).show();
                                    goTo(MainActivity.class);
                                }
                            });
                        } catch (HyphenateException e) {
                            ThreadUtils.rinOnMainThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(confirmationActivity.this, "失败地拒绝好友请求", Toast.LENGTH_SHORT).show();
                                }
                            });

                            e.printStackTrace();
                        }
                    }
                });
                break;
        }
    }
}
