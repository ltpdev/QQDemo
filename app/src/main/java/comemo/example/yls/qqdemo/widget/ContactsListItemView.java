package comemo.example.yls.qqdemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import comemo.example.yls.qqdemo.R;
import comemo.example.yls.qqdemo.model.ContactListItem;

/**
 * Created by yls on 2016/12/30.
 */

public class ContactsListItemView extends RelativeLayout {
    @BindView(R.id.first_letter)
    TextView mFirstLetter;
    @BindView(R.id.contact)
    TextView mContact;

    public ContactsListItemView(Context context) {
        this(context, null);
    }

    public ContactsListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.contactslistitem, this);
        ButterKnife.bind(this,this);
    }

    public void bind(ContactListItem contactListItem) {
        mContact.setText(contactListItem.contact);
        if(contactListItem.showFirstLetter){
            mFirstLetter.setVisibility(VISIBLE);
            mFirstLetter.setText(contactListItem.getFirstLetter());
        }
        else {
            mFirstLetter.setVisibility(GONE);
        }
    }
}
