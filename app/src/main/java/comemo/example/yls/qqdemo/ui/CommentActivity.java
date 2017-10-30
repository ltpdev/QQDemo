package comemo.example.yls.qqdemo.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import comemo.example.yls.qqdemo.R;
import comemo.example.yls.qqdemo.model.Comment;
import comemo.example.yls.qqdemo.model.Dynamic;
import comemo.example.yls.qqdemo.model.Favour;
import comemo.example.yls.qqdemo.model.User;
import de.hdodenhof.circleimageview.CircleImageView;

public class CommentActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.content)
    EditText content;
    @BindView(R.id.send)
    Button send;
    private List<Comment>commentList=new ArrayList<>();
    private CommentAdapter commentAdapter;
    private TextWatcher textWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            send.setEnabled(s.toString().length() > 0);
        }
    };

    @Override
    public int getLayoutResId() {
        return R.layout.activity_comment;
    }

    @Override
    protected void init() {
        super.init();
        //hideKeyBoard();
        title.setText("评论");
        back.setVisibility(View.VISIBLE);
        commentAdapter=new CommentAdapter();
        listview.setAdapter(commentAdapter);
        content.addTextChangedListener(textWatcher);
        initData();
    }

    private void initData() {
        Dynamic dynamic= (Dynamic) getIntent().getSerializableExtra("dynamic");
        BmobQuery<Comment> query = new BmobQuery<Comment>();
        query.addWhereEqualTo("dynamic",dynamic);
        query.order("-createdAt");
        query.include("commenter");
        query.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> list, BmobException e) {
                if(e==null){
                    commentList.clear();
                    for (Comment comment:list){
                        commentList.add(comment);
                    }
                    commentAdapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(CommentActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @OnClick({R.id.send,R.id.back})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.send:
                publishComment();
            break;
            case R.id.back:
                finish();
                break;
        }
    }

    private void publishComment() {
        Dynamic dynamic= (Dynamic) getIntent().getSerializableExtra("dynamic");
        User user= BmobUser.getCurrentUser(User.class);
        Comment comment=new Comment();
        comment.setContent(content.getText().toString());
        comment.setCommenter(user);
        comment.setDynamic(dynamic);
        comment.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e==null){
                    initData();
                    content.getText().clear();
                    Toast.makeText(CommentActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(CommentActivity.this, "评论失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


     class CommentAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return commentList.size();
        }

        @Override
        public Object getItem(int position) {
            return commentList.get(position);
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
                convertView= LayoutInflater.from(CommentActivity.this).inflate(R.layout.item_comment,null);
                viewHolder.circleImageView= (CircleImageView) convertView.findViewById(R.id.cv_user);
                viewHolder.name= (TextView) convertView.findViewById(R.id.tv_username);
                viewHolder.time= (TextView) convertView.findViewById(R.id.tv_time);
                viewHolder.content= (TextView) convertView.findViewById(R.id.tv_content);
                viewHolder.zan= (ImageView) convertView.findViewById(R.id.iv_zan);
                viewHolder.num= (TextView) convertView.findViewById(R.id.num_zan);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }
            final Comment comment=commentList.get(position);
            viewHolder.name.setText(comment.getCommenter().getUsername());
            viewHolder.time.setText(comment.getCreatedAt());
            viewHolder.content.setText(comment.getContent());
            if (comment.getCommenter().getHead()!=null&&!comment.getCommenter().getHead().equals("")){
                Glide.with(CommentActivity.this).load(comment.getCommenter().getHead()).placeholder(R.drawable.user).into(viewHolder.circleImageView);
            }else {
                Glide.with(CommentActivity.this).load(R.drawable.user).into(viewHolder.circleImageView);
            }
            if (comment.getLikes()==null){
                viewHolder.num.setText("0");
            }else {
                viewHolder.num.setText(comment.getLikes()+"");
            }


            //changenum(comment,viewHolder.num);
            viewHolder.zan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        //添加数据到赞表
                        saveFavour(comment,v);
                }
            });
            return convertView;
        }

        class ViewHolder{
            CircleImageView circleImageView;
            TextView name;
            TextView time;
            TextView content;
            ImageView zan;
            TextView num;
        }
    }

    private void saveFavour(final Comment comment, final View view) {
        User user=BmobUser.getCurrentUser(User.class);
        //判断用户有没有赞过
        BmobQuery<Favour> query = new BmobQuery<Favour>();
        query.addWhereEqualTo("comment",comment);
        query.addWhereEqualTo("favourer",user);
        query.findObjects(new FindListener<Favour>() {
            @Override
            public void done(List<Favour> list, BmobException e) {
                if (e==null){
                    if (list.size()>0){
                        Toast.makeText(CommentActivity.this, "你已经赞过了！", Toast.LENGTH_SHORT).show();
                    }else {
                        saveRealFavour(comment,view);
                    }
                }else {

                }

            }
        });


    }

    private void saveRealFavour(final Comment comment, final View view) {
        User user=BmobUser.getCurrentUser(User.class);
        Favour favour=new Favour();
        favour.setComment(comment);
        favour.setFavourer(user);
        favour.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e==null){
                    startAnimation(view);
                    updateComment(comment);
                }else {

                }
            }
        });
    }

    private void updateComment(Comment comment) {
        int num=0;
        if (comment.getLikes()==null){
            num=0;
        }else {
            num=comment.getLikes();
        }
        Comment commentUpdate=new Comment();
        commentUpdate.setLikes(num+1);
        commentUpdate.update(comment.getObjectId(),new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    initData();
                }else {

                }
            }
        });
    }

    private void startAnimation(final View view) {
        ObjectAnimator animatorX=ObjectAnimator.ofFloat(view,"scaleX",1.0f,1.5f);
        ObjectAnimator animatorY=ObjectAnimator.ofFloat(view,"scaleY",1.0f,1.5f);
        AnimatorSet set=new AnimatorSet();
        set.setDuration(200);
        set.playTogether(animatorX,animatorY);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                   view.setScaleX(1.0f);
                    view.setScaleY(1.0f);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.start();
    }

    private void changenum(Comment comment, final TextView num) {
        BmobQuery<Favour> query = new BmobQuery<Favour>();
        query.addWhereEqualTo("comment",comment);
        query.findObjects(new FindListener<Favour>() {
            @Override
            public void done(List<Favour> list, BmobException e) {
                if (e==null){
                    if (list.size()>0){
                        num.setText(list.size()+"");
                    }else {
                        //num.setText("0");
                    }
                }else {

                }

            }
        });

    }


}
