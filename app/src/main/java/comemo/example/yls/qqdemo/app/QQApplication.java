package comemo.example.yls.qqdemo.app;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BuildConfig;
import comemo.example.yls.qqdemo.MainActivity;
import comemo.example.yls.qqdemo.R;
import comemo.example.yls.qqdemo.factory.DataBaseManager;
import comemo.example.yls.qqdemo.ui.ChartActivity;
import comemo.example.yls.qqdemo.ui.confirmationActivity;

/**
 * Created by yls on 2016/12/28.
 */

public class QQApplication extends Application {
    private SoundPool mSoundPool;
    private Map<Integer, Integer> mDuanSound = new HashMap<Integer, Integer>();

    private void initSoundPool() {
        mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        mDuanSound.put(0, mSoundPool.load(getApplicationContext(), R.raw.yulu, 1));
        mDuanSound.put(1, mSoundPool.load(getApplicationContext(), R.raw.duan, 1));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(getApplicationContext(), "7c6787e1cf575fc20765bed4d8f2cc96");
        initEaseMob();
        initSoundPool();
        DataBaseManager.getInstance().initDataBase(getApplicationContext());
        EMClient.getInstance().chatManager().addMessageListener(emMessageListener);
        EMClient.getInstance().contactManager().setContactListener(emContactListener);
    }


    private void initEaseMob() {
        EMOptions options = new EMOptions();
        options.setAcceptInvitationAlways(false);
        EMClient.getInstance().init(getApplicationContext(), options);
//在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        if (BuildConfig.DEBUG) {
            EMClient.getInstance().setDebugMode(true);
        }

    }

    private EMMessageListener emMessageListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> list) {
            if (isfor()) {
                mSoundPool.play(mDuanSound.get(0), (float) 1, (float) 1, 0, 0, 1);
            } else {
                mSoundPool.play(mDuanSound.get(1), (float) 1, (float) 1, 0, 0, 1);
                    show(list.get(0));
            }
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




    private EMContactListener emContactListener = new EMContactListener() {
        @Override
        public void onContactAdded(String s) {


        }

        @Override
        public void onContactDeleted(final String s) {


        }

        @Override
        public void onContactInvited(final String s, final String s1) {
            if (!isfor()) {
                onContactInvitedshowDialog(s, s1);
            }


        }

        @Override
        public void onContactAgreed(final String s) {


        }

        @Override
        public void onContactRefused(final String s) {

        }
    };

    public void onContactInvitedshowDialog(final String name, final String reason) {
        Intent chat = new Intent(this, confirmationActivity.class);
        chat.putExtra("username", name);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, chat, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.remoteviews);
        remoteViews.setTextViewText(R.id.tvTxt, "好友:" + name + "添加你为好友");
        remoteViews.setOnClickPendingIntent(R.id.look, pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = builder.setSmallIcon(R.mipmap.avatar7).setContent(remoteViews).getNotification();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, notification);

    }

    public void show(EMMessage emMessage) {
        EMMessageBody body = emMessage.getBody();
        Intent chat = new Intent(this, ChartActivity.class);
        chat.putExtra("contact", emMessage.getUserName());
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, chat, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = builder.setContentTitle("你有未读消息").setContentText(emMessage.getUserName() + ":" + ((EMTextMessageBody) body).getMessage()).
                setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.avatar1)).
                setSmallIcon(R.mipmap.avatar7).setAutoCancel(true)
                .setContentIntent(pendingIntent).getNotification();
        notificationManager.notify(0, notification);
    }

    public boolean isfor () {
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
        if (runningAppProcesses == null) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo info : runningAppProcesses) {
            if (info.processName.equals(getPackageName()) && info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }



}
