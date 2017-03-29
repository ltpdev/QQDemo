package comemo.example.yls.qqdemo.adaper;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.List;

import comemo.example.yls.qqdemo.ui.ChartActivity;
import comemo.example.yls.qqdemo.ui.ConversionFragment;
import comemo.example.yls.qqdemo.widget.ConverSationListItemView;

/**
 * Created by asus- on 2017/1/1.
 */

public class ConverSationAdapter extends RecyclerView.Adapter <ConverSationAdapter.ConverSationListItemViewHolder>{
    private Context context;
    private List<EMConversation> list;

    public ConverSationAdapter(Context context, List<EMConversation> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ConverSationAdapter.ConverSationListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ConverSationListItemViewHolder(new ConverSationListItemView(context));
    }

    @Override
    public void onBindViewHolder(ConverSationAdapter.ConverSationListItemViewHolder holder, final int position) {
        holder.converSationListItemView.bindView(list.get(position));
        holder.converSationListItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ChartActivity.class);
                intent.putExtra("contact",list.get(position).getUserName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public class ConverSationListItemViewHolder extends RecyclerView.ViewHolder {
        private ConverSationListItemView converSationListItemView;
        public ConverSationListItemViewHolder(ConverSationListItemView converSationListItemView) {
            super(converSationListItemView);
            this.converSationListItemView=converSationListItemView;
        }
    }
}
