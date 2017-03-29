package comemo.example.yls.qqdemo.adaper;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import comemo.example.yls.qqdemo.listener.OnLongClickItemListener;
import comemo.example.yls.qqdemo.model.ContactListItem;
import comemo.example.yls.qqdemo.ui.ChartActivity;
import comemo.example.yls.qqdemo.widget.ContactsListItemView;

/**
 * Created by yls on 2016/12/30.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsListItemViewHolder> {
    private Context mContext;
    private List<ContactListItem> mContactListItems;
    private OnLongClickItemListener onLongClickItemListener;
    public ContactsAdapter(Context mContext, List<ContactListItem> mContactListItems) {
        this.mContext = mContext;
        this.mContactListItems = mContactListItems;
    }

    @Override
    public ContactsAdapter.ContactsListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactsListItemViewHolder(new ContactsListItemView(mContext));
    }

    @Override
    public void onBindViewHolder(ContactsAdapter.ContactsListItemViewHolder holder, final int position) {
        holder.mContactsListItemView.bind(mContactListItems.get(position));
        holder.mContactsListItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChartActivity.class);
                intent.putExtra("contact", mContactListItems.get(position).contact);
                mContext.startActivity(intent);
            }
        });
        holder.mContactsListItemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onLongClickItemListener.del(mContactListItems.get(position).contact);
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {

        return mContactListItems.size();
    }

    public class ContactsListItemViewHolder extends RecyclerView.ViewHolder {
        private ContactsListItemView mContactsListItemView;

        public ContactsListItemViewHolder(ContactsListItemView mContactsListItemView) {
            super(mContactsListItemView);
            this.mContactsListItemView = mContactsListItemView;
        }
    }

public  void setOnLongClickItemListener(OnLongClickItemListener onLongClickItemListener){
    this.onLongClickItemListener=onLongClickItemListener;
}

}
