package comemo.example.yls.qqdemo.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.hyphenate.util.DateUtils;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import comemo.example.yls.qqdemo.R;
import comemo.example.yls.qqdemo.manager.MediaPlayerManager;
import comemo.example.yls.qqdemo.model.User;

/**
 * Created by yls on 2016/12/30.
 */

public class SendVoiceMessageItemView extends RelativeLayout {
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.id_recorder_anim)
    View view;
    @BindView(R.id.id_recorder_time)
    TextView tvLength;
    @BindView(R.id.animl)
    ImageView animl;
    @BindView(R.id.id_icon)
    ImageView idIcon;
    private int mMinItemWidth;
    private int mMaxItemWidth;
    private boolean isPause = false;
    private AnimationDrawable anim;
    private Context context;

    public SendVoiceMessageItemView(Context context) {
        this(context, null);
        this.context=context;
    }

    public SendVoiceMessageItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_send_voice_message, this);
        ButterKnife.bind(this, this);
        WindowManager wm = (WindowManager) getContext().getSystemService(getContext().WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mMaxItemWidth = (int) (outMetrics.widthPixels * 0.7f);
        mMinItemWidth = (int) (outMetrics.widthPixels * 0.15f);
    }

    public void bindView(EMMessage emMessage) {
        final long time = emMessage.getMsgTime();
        //findHeadImg();
        tvTime.setText(DateUtils.getTimestampString(new Date(time)));
        final EMMessageBody body = emMessage.getBody();
        if (body instanceof EMVoiceMessageBody) {
            tvLength.setText(((EMVoiceMessageBody) body).getLength() + "\"");
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            lp.width = (int) (mMinItemWidth + (mMaxItemWidth / 60f * ((EMVoiceMessageBody) body).getLength()));
        }
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MediaPlayerManager.isPlaying()) {
                    view.setBackgroundResource(R.mipmap.adj);
                    MediaPlayerManager.pause();
                } else {
                    //播放动画
                    view.setBackgroundResource(R.drawable.sound_animation);
                    anim = (AnimationDrawable) view.getBackground();
                    anim.start();

                    //播放录音
                    MediaPlayerManager.playSound(((EMVoiceMessageBody) body).getRemoteUrl(), new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            view.setBackgroundResource(R.mipmap.adj);
                        }
                    });
                }


            }
        });

        AnimationDrawable animationDrawable = (AnimationDrawable) animl.getDrawable();
        switch (emMessage.status()) {
            case INPROGRESS:
                animationDrawable.start();
                break;
            case SUCCESS:
                animl.setVisibility(GONE);
                break;
            case FAIL:
                animl.setImageResource(R.mipmap.msg_error);
                break;
        }
    }

    private void findHeadImg() {
        User user = BmobUser.getCurrentUser(User.class);
        if (user.getHead() != null && !user.getHead().equals("")) {
            Glide.with(context).load(user.getHead()).into(idIcon);
        } else {
            Glide.with(context).load(R.mipmap.avatar7).into(idIcon);
        }
    }
}
