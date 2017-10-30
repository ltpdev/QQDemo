package comemo.example.yls.qqdemo.adaper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import comemo.example.yls.qqdemo.R;
import comemo.example.yls.qqdemo.listener.ItemListener;

/**
 * Created by asus- on 2017/10/25.
 */

public class SelectPhotoAdapter extends BaseAdapter{
    private Context context;
    private List<String>photoList;
    private ItemListener itemListener;
    public SelectPhotoAdapter(Context context,List<String>photoList){
        this.context=context;
        this.photoList=photoList;
    }

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }

    @Override
    public int getCount() {
        return photoList.size();
    }

    @Override
    public Object getItem(int position) {
        return photoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.item_photo,null);
            viewHolder.photo= (ImageView) convertView.findViewById(R.id.photo);
            viewHolder.del= (ImageView) convertView.findViewById(R.id.iv_del);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        Glide.with(context).load(photoList.get(position)).into(viewHolder.photo);
        viewHolder.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListener.preview(position);
            }
        });
        viewHolder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListener.del(position);
            }
        });
        return convertView;
    }

    class ViewHolder{
        ImageView photo;
        ImageView del;
    }
}
