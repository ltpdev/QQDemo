package comemo.example.yls.qqdemo.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMVideoMessageBody;
import com.hyphenate.util.DateUtils;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import comemo.example.yls.qqdemo.R;
import comemo.example.yls.qqdemo.model.User;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by yls on 2016/12/30.
 */

public class SendVideoMessageItemView extends RelativeLayout {
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.animl)
    ImageView animl;
    @BindView(R.id.videoplayer)
    JCVideoPlayerStandard videoplayer;
    @BindView(R.id.avatar)
    ImageView avatar;
    private Context context;

    public SendVideoMessageItemView(Context context) {
        this(context, null);
        this.context=context;
    }

    public SendVideoMessageItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_send_video_message, this);
        ButterKnife.bind(this, this);
    }

    public void bindView(EMMessage emMessage) {
        long time = emMessage.getMsgTime();
        tvTime.setText(DateUtils.getTimestampString(new Date(time)));
        //findHeadImg();
        final EMMessageBody body = emMessage.getBody();
        if (body instanceof EMVideoMessageBody) {
            videoplayer.setUp(((EMVideoMessageBody) body).getLocalUrl(), JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "");
            Glide.with(getContext()).load(((EMVideoMessageBody) body).getThumbnailUrl()).into(videoplayer.thumbImageView);
          /*  Toast.makeText(getContext(), ((EMVideoMessageBody) body).getLocalUrl(), Toast.LENGTH_SHORT).show();
            System.out.println("---" + ((EMVideoMessageBody) body).getRemoteUrl() + "/" + ((EMVideoMessageBody) body).getFileName());*/
        }


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
            Glide.with(context).load(user.getHead()).into(avatar);
        } else {
            Glide.with(context).load(R.mipmap.avatar7).into(avatar);
        }
    }
}
