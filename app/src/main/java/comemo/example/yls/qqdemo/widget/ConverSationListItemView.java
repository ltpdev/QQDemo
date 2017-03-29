package comemo.example.yls.qqdemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import comemo.example.yls.qqdemo.R;

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

    public ConverSationListItemView(Context context) {
        this(context, null);
    }

    public ConverSationListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
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
        if(body instanceof EMTextMessageBody){
            tvmsg.setText(((EMTextMessageBody) body).getMessage());
        }
        else if(body instanceof EMImageMessageBody){
            tvmsg.setText("图片消息");
        }else {
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
}
