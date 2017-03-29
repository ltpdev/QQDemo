package comemo.example.yls.qqdemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.List;

import butterknife.BindView;
import comemo.example.yls.qqdemo.factory.FragmentFactory;
import comemo.example.yls.qqdemo.listener.OnLongClickItemListener;
import comemo.example.yls.qqdemo.ui.BaseActivity;
import comemo.example.yls.qqdemo.utils.ThreadUtils;

public class MainActivity extends BaseActivity {
    private FragmentManager mFragmentManager;
    @BindView(R.id.fragment)
    FrameLayout mFragment;
    @BindView(R.id.bottomBar)
    BottomBar mBottomBar;
    private BottomBarTab bottomBarTab;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }
    @Override
    protected void init() {
        super.init();
        mFragmentManager = getSupportFragmentManager();
        initBadge();
        mBottomBar.setOnTabSelectListener(tabSelectListener);
        EMClient.getInstance().contactManager().setContactListener(emContactListener);
        EMClient.getInstance().chatManager().addMessageListener(emMessageListener);
    }

    private void initBadge() {
        bottomBarTab = mBottomBar.getTabWithId(R.id.conversion);
        updateBadge();
    }

    public void updateBadge() {
        int count= EMClient.getInstance().chatManager().getUnreadMsgsCount();
        bottomBarTab.setBadgeCount(count);
    }

    @Override
    protected void onResume() {
        updateBadge();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        EMClient.getInstance().contactManager().removeContactListener(emContactListener);
        EMClient.getInstance().chatManager().removeMessageListener(emMessageListener);
        super.onDestroy();
    }
  private   EMMessageListener emMessageListener= new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> list) {
            ThreadUtils.rinOnMainThread(new Runnable() {
                @Override
                public void run() {
                    updateBadge();
                }
            });
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {

        }
    };
    private OnTabSelectListener tabSelectListener=new OnTabSelectListener() {
        @Override
        public void onTabSelected(@IdRes int tabId) {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment, FragmentFactory.getInstance().getFragment(tabId));
            fragmentTransaction.commit();

        }
    };
    private EMContactListener emContactListener = new EMContactListener() {
        @Override
        public void onContactAdded(String s) {

           // mContactsPresenter.refresh();


        }

        @Override
        public void onContactDeleted(final String s) {

            //mContactsPresenter.refresh();


        }

        @Override
        public void onContactInvited(final String s, final String s1) {
            ThreadUtils.rinOnMainThread(new Runnable() {
                @Override
                public void run() {
                    onContactInvitedshowDialog(s,s1);
                }
            });


        }

        @Override
        public void onContactAgreed(final String s) {
            ThreadUtils.rinOnMainThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "好友"+s+"同意你的请求", Toast.LENGTH_SHORT).show();
                }
            });

        }

        @Override
        public void onContactRefused(final String s) {
            ThreadUtils.rinOnMainThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "好友："+s+"拒绝你的请求", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };



    public void onContactInvitedshowDialog(final String name,final String reason) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("添加好友请求").setMessage("来自:"+name+"理由："+reason).
                setPositiveButton("添加", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            EMClient.getInstance().contactManager().acceptInvitation(name);
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                        }

                    }
                }).setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    EMClient.getInstance().contactManager().declineInvitation(name);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }).show();

    }


}
