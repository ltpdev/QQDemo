package comemo.example.yls.qqdemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
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

/**
 * Created by asus- on 2017/1/1.
 */

public class ConverSationListItemView extends RelativeLayout {
    @BindView(R.id.avatar)
    ImageView avatar;
    @BindView(R.id.tvname)
    TextView tvname;
    @BindView(R.id.tvmsg)
    TextView tvmsg;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvUnReadCount)
    TextView tvUnReadCount;
    Context context;

    public ConverSationListItemView(Context context) {
        this(context, null);
        this.context=context;
    }

    public ConverSationListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.conversationlistitem, this);
        ButterKnife.bind(this, this);

    }


    public void bindView(EMConversation emConversation) {
        tvname.setText(emConversation.getUserName());
        EMMessage lastMessage=emConversation.getLastMessage();
        EMMessageBody body=lastMessage.getBody();
        findHeadImg(emConversation.getUserName());
        if(body instanceof EMTextMessageBody){
            tvmsg.setText(((EMTextMessageBody) body).getMessage());
        }
        else if(body instanceof EMImageMessageBody){
            tvmsg.setText("图片消息");
        }else if(body instanceof EMVideoMessageBody){
            tvmsg.setText("视频消息");
        } else {
            tvmsg.setText("语音消息");
        }

        tvTime.setText(DateUtils.getTimestampString(new Date(lastMessage.getMsgTime())));
        int unreadMsgCount=emConversation.getUnreadMsgCount();
        if(unreadMsgCount==0){
            tvUnReadCount.setVisibility(GONE);
        }
        else {
            tvUnReadCount.setVisibility(VISIBLE);
            tvUnReadCount.setText(String.valueOf(unreadMsgCount));
        }

    }

    private void findHeadImg(String name) {
        BmobQuery<User> query=new BmobQuery<User>();
        query.addWhereEqualTo("username",name);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e==null){
                    User user=list.get(0);
                    if (user.getHead()!=null&&!user.getHead().equals("")){
                        Glide.with(context).load(user.getHead()).into(avatar);
                    }else {
                        Glide.with(context).load(R.mipmap.avatar7).into(avatar);
                    }
                }else {
                    Glide.with(context).load(R.mipmap.avatar7).into(avatar);
                }
            }
        });
    }
}
