package comemo.example.yls.qqdemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import cn.bmob.v3.listener.ValueEventListener;
import comemo.example.yls.qqdemo.event.DynamicDanEvent;
import comemo.example.yls.qqdemo.factory.FragmentFactory;
import comemo.example.yls.qqdemo.model.User;
import comemo.example.yls.qqdemo.ui.AddFriendActivity;
import comemo.example.yls.qqdemo.ui.BaseActivity;
import comemo.example.yls.qqdemo.ui.GroupListActivity;
import comemo.example.yls.qqdemo.ui.LoginActivity;
import comemo.example.yls.qqdemo.ui.LookPictureActivity;
import comemo.example.yls.qqdemo.ui.UpdateSignatureActivity;
import comemo.example.yls.qqdemo.utils.ThreadUtils;
import me.iwf.photopicker.PhotoPicker;

public class MainActivity extends BaseActivity {
    @BindView(R.id.toolbal)
    Toolbar toolbal;
    @BindView(R.id.navigationView)
    NavigationView navigationView;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.title)
    TextView title;
    private FragmentManager mFragmentManager;
    @BindView(R.id.fragment)
    FrameLayout mFragment;
    @BindView(R.id.bottomBar)
    BottomBar mBottomBar;
    private BottomBarTab bottomBarTab;
    private BmobRealTimeData data = new BmobRealTimeData();
    private ImageView ivcon;
    private TextView tvName;
    private PopupWindow popupWindow;
    private TextView signature;
    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        super.init();
        initToolBar();
        initActionBarDrawerToggle();
        initNavigationViewListener();
        View view = navigationView.getHeaderView(0);
        initHeaderViewListener(view);
        mFragmentManager = getSupportFragmentManager();
        initBadge();
        mBottomBar.setOnTabSelectListener(tabSelectListener);
        EMClient.getInstance().contactManager().setContactListener(emContactListener);
        EMClient.getInstance().chatManager().addMessageListener(emMessageListener);
        listto();
    }

    private void initHeaderViewListener(View view) {
        ivcon = (ImageView) view.findViewById(R.id.icon_user);
        tvName = (TextView) view.findViewById(R.id.name_user);
        signature= (TextView) view.findViewById(R.id.signature);
        User user = BmobUser.getCurrentUser(User.class);
        if (user.getHead()!=null&&!user.getHead().equals("")){
            Glide.with(this).load(user.getHead()).into(ivcon);
        }else {
            Glide.with(this).load(R.drawable.user).into(ivcon);
        }

        signature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,UpdateSignatureActivity.class);
                startActivity(intent);
            }
        });
        tvName.setText(user.getUsername());
        ivcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();
            }
        });
    }






    //更新头像
    private void pickPhoto() {
        PhotoPicker.builder()
                .setPhotoCount(1)
                .setShowCamera(true)
                .setShowGif(false)
                .setPreviewEnabled(false)
                .start(this, PhotoPicker.REQUEST_CODE);
    }

    private void initNavigationViewListener() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu01:
                        logoutEasyMob();
                        drawerLayout.closeDrawers();
                        return true;
                }

                return false;
            }
        });
    }

    /*退出环信*/
    private void logoutEasyMob() {
        EMClient.getInstance().logout(true, new EMCallBack() {
            @Override
            public void onSuccess() {
                logoutBmob();
            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                updateUserInfo(photos.get(0));
            }
        }
    }


    private void updateUserInfo(String picPath) {
        showProgressDialog("上传中...");
        final BmobFile bmobFile = new BmobFile(new File(picPath));
        bmobFile.uploadblock(new UploadFileListener() {

            @Override
            public void done(BmobException e) {
                if (e == null) {
                    hideProgressDialog();
                    updateRealUserInfo(bmobFile.getFileUrl());
                } else {
                    Toast.makeText(MainActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                    hideProgressDialog();
                }

            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }
        });

    }

    private void updateRealUserInfo(final String fileUrl) {
        User user = BmobUser.getCurrentUser(User.class);
        User newUser = new User();
        newUser.setHead(fileUrl);
        newUser.update(user.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Glide.with(MainActivity.this).load(fileUrl).into(ivcon);
                } else {

                }
            }
        });
    }


    /*退出bmob*/
    private void logoutBmob() {
        BmobUser.logOut();
        goTo(LoginActivity.class);
    }

    /*初始化toolbar连接drawerLayout*/
    private void initActionBarDrawerToggle() {
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbal, 0, 0);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    /*初始化toolbar*/
    private void initToolBar() {
        toolbal.setTitle("");
        setSupportActionBar(toolbal);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    //监听表
    private void listto() {
        data.start(new ValueEventListener() {

            @Override
            public void onConnectCompleted(Exception e) {
                if (data.isConnected()) {
                    data.subTableUpdate("Dynamic");
                }
            }

            @Override
            public void onDataChange(JSONObject jsonObject) {
                EventBus.getDefault().post(new DynamicDanEvent(true));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toobar, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public void setTitle(String text) {
         title.setText(text);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.setting01:
                Intent intent=new Intent(MainActivity.this,AddFriendActivity.class);
                startActivity(intent);
                return true;
            case R.id.setting02:
                createGroup();
                return true;
            case R.id.setting03:
                Intent intent2 = new Intent(MainActivity.this, GroupListActivity.class);
                startActivity(intent2);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void createGroup() {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                EMGroupManager.EMGroupOptions option = new EMGroupManager.EMGroupOptions();
                option.maxUsers = 200;
                option.style = EMGroupManager.EMGroupStyle.EMGroupStylePrivateMemberCanInvite;
                String[]strings={EMClient.getInstance().getCurrentUser()};
                try {
                    EMClient.getInstance().groupManager().createGroup(EMClient.getInstance().getCurrentUser()+"创建的群", "请多多关照!", strings, "你如何找到这个群", option);
                    ThreadUtils.rinOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "创建群成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (HyphenateException e) {
                    ThreadUtils.rinOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "创建群失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                    e.printStackTrace();
                }
            }
        });

    }


    private void initBadge() {
        bottomBarTab = mBottomBar.getTabWithId(R.id.conversion);
        updateBadge();
    }

    public void updateBadge() {
        int count = EMClient.getInstance().chatManager().getUnreadMsgsCount();
        bottomBarTab.setBadgeCount(count);
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)){
            drawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        User user = BmobUser.getCurrentUser(User.class);
        if (user.getSignature()!=null&&!user.getSignature().equals("")){
            signature.setText(user.getSignature());
        }
        updateBadge();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        EMClient.getInstance().contactManager().removeContactListener(emContactListener);
        EMClient.getInstance().chatManager().removeMessageListener(emMessageListener);
        super.onDestroy();
    }

    private EMMessageListener emMessageListener = new EMMessageListener() {
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
    private OnTabSelectListener tabSelectListener = new OnTabSelectListener() {
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
                    onContactInvitedshowDialog(s, s1);
                }
            });


        }

        @Override
        public void onContactAgreed(final String s) {
            ThreadUtils.rinOnMainThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "好友" + s + "同意你的请求", Toast.LENGTH_SHORT).show();
                }
            });

        }

        @Override
        public void onContactRefused(final String s) {
            ThreadUtils.rinOnMainThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "好友：" + s + "拒绝你的请求", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };


    public void onContactInvitedshowDialog(final String name, final String reason) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("添加好友请求").setMessage("来自:" + name + "理由：" + reason).
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


    @Override
    protected void destroy() {
        super.destroy();
        FragmentFactory.getInstance().destory();
    }


    private void showPopupWindow() {
        //设置contentView
        if (popupWindow == null) {
            View contentView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_option_photo, null);
            popupWindow = new PopupWindow(contentView);
            popupWindow.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
            popupWindow.setHeight(ViewGroup.LayoutParams.FILL_PARENT);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setOutsideTouchable(true);
            TextView add = (TextView) contentView.findViewById(R.id.add_friend);
            TextView find = (TextView) contentView.findViewById(R.id.find);
            TextView create = (TextView) contentView.findViewById(R.id.create);
            RelativeLayout relativeLayout = (RelativeLayout) contentView.findViewById(R.id.relativeLayout);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pickPhoto();
                    popupWindow.dismiss();

                }
            });
            create.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lookBigPhoto();
                    popupWindow.dismiss();
                }
            });

            relativeLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popupWindow.dismiss();
                    return true;
                }
            });

        }

        View rootview = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main, null);
        popupWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);

    }

    private void lookBigPhoto() {
        User user = BmobUser.getCurrentUser(User.class);
        Intent intent = new Intent(MainActivity.this, LookPictureActivity.class);
        intent.putExtra("url", user.getHead());
        startActivity(intent);
    }



}
