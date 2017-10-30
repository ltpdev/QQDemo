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
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import comemo.example.yls.qqdemo.R;
import comemo.example.yls.qqdemo.model.User;

/**
 * Created by yls on 2016/12/30.
 */

public class SendMessageItemView extends RelativeLayout {
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvTxt)
    TextView tvTxt;
    @BindView(R.id.animl)
    ImageView animl;
    Context context;
    @BindView(R.id.avatar)
    ImageView avatar;

    public SendMessageItemView(Context context) {
        this(context, null);
        this.context = context;
    }

    public SendMessageItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_send_message, this);
        ButterKnife.bind(this, this);
    }

    public void bindView(EMMessage emMessage) {
        long time = emMessage.getMsgTime();
        //findHeadImg();
        tvTime.setText(DateUtils.getTimestampString(new Date(time)));
        EMMessageBody body = emMessage.getBody();
       /* if (body instanceof EMImageMessageBody){

        }*/
        if (body instanceof EMTextMessageBody) {
            tvTxt.setText(((EMTextMessageBody) body).getMessage());
        } else {
            tvTxt.setText(R.string.no_txt_message);

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
