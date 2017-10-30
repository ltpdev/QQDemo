package comemo.example.yls.qqdemo.adaper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import comemo.example.yls.qqdemo.MainActivity;
import comemo.example.yls.qqdemo.R;
import comemo.example.yls.qqdemo.model.Dynamic;
import comemo.example.yls.qqdemo.ui.CommentActivity;
import comemo.example.yls.qqdemo.widget.CustomGridView;
import de.hdodenhof.circleimageview.CircleImageView;
import me.iwf.photopicker.PhotoPreview;

/**
 * Created by asus- on 2017/10/25.
 */

public class DynamicAdapter extends BaseAdapter{
    private Context context;
    private List<Dynamic>dynamicList;
    private Activity activity;
    public DynamicAdapter(Context context,List<Dynamic>dynamicList,Activity activity){
        this.context=context;
        this.dynamicList=dynamicList;
        this.activity=activity;
    }
    @Override
    public int getCount() {
        return dynamicList.size();
    }

    @Override
    public Object getItem(int position) {
        return dynamicList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.item_dynamic,null);
            viewHolder.circleImageView= (CircleImageView) convertView.findViewById(R.id.cv_user);
            viewHolder.name= (TextView) convertView.findViewById(R.id.tv_username);
            viewHolder.time= (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.content= (TextView) convertView.findViewById(R.id.tv_content);
            viewHolder.photoGridView= (CustomGridView) convertView.findViewById(R.id.photoGridView);
            viewHolder.comment= (ImageView) convertView.findViewById(R.id.iv_comment);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        final Dynamic dynamic=dynamicList.get(position);
        viewHolder.name.setText(dynamic.getAuthor().getUsername());
        viewHolder.time.setText(dynamic.getCreatedAt());
        viewHolder.content.setText(dynamic.getContent());
        if (dynamic.getAuthor().getHead()!=null&&!dynamic.getAuthor().getHead().equals("")){
            Glide.with(context).load(dynamic.getAuthor().getHead()).placeholder(R.drawable.user).into(viewHolder.circleImageView);
        }else {
            Glide.with(context).load(R.drawable.user).into(viewHolder.circleImageView);
        }
        if (dynamic.getPhotos()!=null){
            viewHolder.photoGridView.setAdapter(new DynamicPhotoAdapter(context,dynamic.getPhotos()));
            viewHolder.photoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    PhotoPreview.builder()
                            .setPhotos((ArrayList<String>) dynamic.getPhotos())
                            .setCurrentItem(position)
                            .setShowDeleteButton(false)
                            .start(activity);
                }
            });
        }else {
            viewHolder.photoGridView.setAdapter(null);
        }

        viewHolder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,CommentActivity.class);
                intent.putExtra("dynamic",dynamic);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    class ViewHolder{
        CircleImageView circleImageView;
        TextView name;
        TextView time;
        TextView content;
        CustomGridView photoGridView;
        ImageView comment;
    }
}
