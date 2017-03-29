package comemo.example.yls.qqdemo.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
 * Created by yls on 2016/12/30.
 */

public class SendMessageItemView extends RelativeLayout {
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvTxt)
    TextView tvTxt;
    @BindView(R.id.animl)
    ImageView animl;

    public SendMessageItemView(Context context) {
        this(context, null);
    }

    public SendMessageItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_send_message, this);
        ButterKnife.bind(this, this);
    }

    public void bindView(EMMessage emMessage) {
        long time = emMessage.getMsgTime();
        tvTime.setText(DateUtils.getTimestampString(new Date(time)));
        EMMessageBody body = emMessage.getBody();
       /* if (body instanceof EMImageMessageBody){

        }*/
        if (body instanceof EMTextMessageBody) {
            tvTxt.setText(((EMTextMessageBody) body).getMessage());
        } else {
            tvTxt.setText(R.string.no_txt_message);

        }
        AnimationDrawable animationDrawable= (AnimationDrawable) animl.getDrawable();
        switch (emMessage.status()){
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
}
