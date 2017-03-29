package comemo.example.yls.qqdemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import comemo.example.yls.qqdemo.R;
import comemo.example.yls.qqdemo.model.AddFriendEvent;
import comemo.example.yls.qqdemo.model.SearchResultItem;

/**
 * Created by asus- on 2017/1/2.
 */

public class SearchResultItemView extends RelativeLayout {
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.add)
    Button add;

    public SearchResultItemView(Context context) {
        this(context, null);
    }

    public SearchResultItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_search_result, this);
        ButterKnife.bind(this, this);
    }

    @OnClick(R.id.add)
    public void onClick() {
        String name=userName.getText().toString().trim();
        EventBus.getDefault().post(new AddFriendEvent(name,"我是帅哥"));
    }

    public void bindView(SearchResultItem searchResultItem) {
        userName.setText(searchResultItem.userName);
        tvTime.setText(searchResultItem.time);
        boolean isAdd=searchResultItem.added;
        if(isAdd){
            add.setEnabled(false);
            add.setText(R.string.added);
        }
        else {
            add.setEnabled(true);
            add.setText(R.string.add);
        }
    }
}
