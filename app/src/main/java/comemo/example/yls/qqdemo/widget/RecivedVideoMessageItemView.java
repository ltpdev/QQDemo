package comemo.example.yls.qqdemo.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMVideoMessageBody;
import com.hyphenate.util.DateUtils;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import comemo.example.yls.qqdemo.R;
import comemo.example.yls.qqdemo.model.User;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by yls on 2016/12/30.
 */

public class RecivedVideoMessageItemView extends RelativeLayout {
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.animl)
    ImageView animl;
    @BindView(R.id.videoplayer)
    JCVideoPlayerStandard videoplayer;
    @BindView(R.id.avatar)
    ImageView avatar;
    private Context context;

    public RecivedVideoMessageItemView(Context context) {
        this(context, null);
        this.context=context;
    }

    public RecivedVideoMessageItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_recived_video_message, this);
        ButterKnife.bind(this, this);
    }

    public void bindView(EMMessage emMessage) {
        long time = emMessage.getMsgTime();
        tvTime.setText(DateUtils.getTimestampString(new Date(time)));

        //findHeadImg(emMessage.getUserName());
        final EMMessageBody body = emMessage.getBody();
        if (body instanceof EMVideoMessageBody) {
            videoplayer.setUp(((EMVideoMessageBody) body).getRemoteUrl(), JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "");
            Glide.with(getContext()).load(((EMVideoMessageBody) body).getThumbnailUrl()).into(videoplayer.thumbImageView);
            //Toast.makeText(getContext(),((EMVideoMessageBody) body).getRemoteUrl(), Toast.LENGTH_SHORT).show();
            //System.out.println("---"+((EMVideoMessageBody) body).getRemoteUrl()+"/"+((EMVideoMessageBody) body).getFileName());
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

    private void findHeadImg(String name) {
        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("username", name);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    User user = list.get(0);
                    if (user.getHead() != null && !user.getHead().equals("")) {
                        Glide.with(context).load(user.getHead()).into(avatar);
                    } else {
                        Glide.with(context).load(R.mipmap.avatar_9).into(avatar);
                    }
                } else {
                    Glide.with(context).load(R.mipmap.avatar_9).into(avatar);
                }
            }
        });
    }
}
