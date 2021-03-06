package comemo.example.yls.qqdemo.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
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
import comemo.example.yls.qqdemo.ui.LookPictureActivity;

/**
 * Created by yls on 2016/12/30.
 */

public class RecivedPicMessageItemView extends RelativeLayout {
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.img_message)
    ImageView imgmessage;
    @BindView(R.id.animl)
    ImageView animl;
    @BindView(R.id.avatar)
    ImageView avatar;
    private Context context;

    public RecivedPicMessageItemView(Context context) {
        this(context, null);
        this.context=context;
    }

    public RecivedPicMessageItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_recived_pic_message, this);
        ButterKnife.bind(this, this);
    }

    public void bindView(EMMessage emMessage) {
        long time = emMessage.getMsgTime();
        tvTime.setText(DateUtils.getTimestampString(new Date(time)));
        //findHeadImg(emMessage.getUserName());
        final EMMessageBody body = emMessage.getBody();
        if (body instanceof EMImageMessageBody) {
            Glide.with(getContext()).load(((EMImageMessageBody) body).getRemoteUrl()).into(imgmessage);
        }
        imgmessage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getContext(), LookPictureActivity.class);
                intent.putExtra("url", ((EMImageMessageBody) body).getRemoteUrl());
                getContext().startActivity(intent);
            }
        });
        /*if (body instanceof EMTextMessageBody) {
            tvTxt.setText(((EMImageMessageBody) body).getRemoteUrl());
        } else {
            tvTxt.setText(R.string.no_txt_message);

        }*/
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
