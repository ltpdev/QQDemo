package comemo.example.yls.qqdemo.widget;

import android.content.Context;
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
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import comemo.example.yls.qqdemo.R;
import comemo.example.yls.qqdemo.model.User;

/**
 * Created by yls on 2016/12/30.
 */

public class RecivedMessageItemView extends RelativeLayout {
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvTxt)
    TextView tvTxt;
    @BindView(R.id.avatar)
    ImageView avatar;
    Context context;

    public RecivedMessageItemView(Context context) {
        this(context, null);
        this.context=context;
    }

    public RecivedMessageItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_recived_message, this);
        ButterKnife.bind(this, this);
    }

    public void bindView(EMMessage emMessage) {
        long time = emMessage.getMsgTime();
        tvTime.setText(DateUtils.getTimestampString(new Date(time)));
        //findHeadImg(emMessage.getUserName());
        EMMessageBody body = emMessage.getBody();
        if (body instanceof EMTextMessageBody) {
            tvTxt.setText(((EMTextMessageBody) body).getMessage());
        } else {
            tvTxt.setText(R.string.no_txt_message);
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
