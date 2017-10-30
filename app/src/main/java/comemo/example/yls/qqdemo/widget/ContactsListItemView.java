package comemo.example.yls.qqdemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import comemo.example.yls.qqdemo.R;
import comemo.example.yls.qqdemo.model.ContactListItem;
import comemo.example.yls.qqdemo.model.User;

/**
 * Created by yls on 2016/12/30.
 */

public class ContactsListItemView extends RelativeLayout {
    @BindView(R.id.first_letter)
    TextView mFirstLetter;
    @BindView(R.id.contact)
    TextView mContact;
    @BindView(R.id.iv_head)
    ImageView ivHead;
    Context context;
    public ContactsListItemView(Context context) {
        this(context, null);
        this.context=context;
    }

    public ContactsListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.contactslistitem, this);
        ButterKnife.bind(this, this);
    }

    public void bind(ContactListItem contactListItem) {
        mContact.setText(contactListItem.contact);
        if (contactListItem.showFirstLetter) {
            mFirstLetter.setVisibility(VISIBLE);
            mFirstLetter.setText(contactListItem.getFirstLetter());
        } else {
            mFirstLetter.setVisibility(GONE);
        }
        findHeadImg(contactListItem.contact);
    }

    private void findHeadImg(String name) {
        BmobQuery<User>query=new BmobQuery<User>();
        query.addWhereEqualTo("username",name);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e==null){
                    User user=list.get(0);
                    if (user.getHead()!=null&&!user.getHead().equals("")){
                        Glide.with(context).load(user.getHead()).into(ivHead);
                    }else {
                        Glide.with(context).load(R.mipmap.avatar_9).into(ivHead);
                    }
                }else {
                    Glide.with(context).load(R.mipmap.avatar_9).into(ivHead);
                }
            }
        });
    }
}
