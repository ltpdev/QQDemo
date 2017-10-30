package comemo.example.yls.qqdemo.adaper;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;

import java.util.List;

import comemo.example.yls.qqdemo.R;
import comemo.example.yls.qqdemo.listener.ItemListener;

/**
 * Created by asus- on 2017/10/25.
 */

public class DynamicPhotoAdapter extends BaseAdapter{
    private Context context;
    private List<String>photoList;
    public DynamicPhotoAdapter(Context context, List<String>photoList){
        this.context=context;
        this.photoList=photoList;

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
            convertView= LayoutInflater.from(context).inflate(R.layout.item_photo_dynamic,null);
            viewHolder.photo= (ImageView) convertView.findViewById(R.id.photo);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        if (Util.isOnMainThread()) {
            Glide.with(context).load(photoList.get(position)).into(viewHolder.photo);
        }
        return convertView;
    }

    class ViewHolder{
        ImageView photo;
    }
}
