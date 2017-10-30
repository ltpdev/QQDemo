package comemo.example.yls.qqdemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import comemo.example.yls.qqdemo.R;
import comemo.example.yls.qqdemo.utils.ThreadUtils;
import comemo.example.yls.qqdemo.widget.MyGridView;

public class GroupDetailActivity extends BaseActivity {


    @BindView(R.id.name_group)
    TextView nameGroup;
    @BindView(R.id.name_desc)
    TextView nameDesc;
    @BindView(R.id.title)
    TextView mTextView;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.add)
    ImageView add;
    @BindView(R.id.noti_group)
    TextView notiGroup;
    @BindView(R.id.invite)
    TextView invite;
    @BindView(R.id.logout_group)
    Button logoutGroup;
    @BindView(R.id.mineMainview)
    MyGridView mineMainview;
    private List<String> names=new ArrayList<>();
    PicAdapter picAdapter;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_group_detail;
    }

    @Override
    protected void init() {
        super.init();
        mTextView.setText("群聊信息" + "(" + getIntent().getIntExtra("memberCount", 0) + ")");
        back.setVisibility(View.VISIBLE);
        nameGroup.setText(getIntent().getStringExtra("groupName"));
        nameDesc.setText(getIntent().getStringExtra("groupDesc"));
        names=getIntent().getStringArrayListExtra("memberList");
        mineMainview.setHaveScrollbar(false);
        picAdapter=new PicAdapter();
        mineMainview.setAdapter(picAdapter);

    }


    @OnClick({R.id.back, R.id.logout_group,R.id.name_desc,R.id.name_group,R.id.invite})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.invite:
                inviteNewUser();
                break;
            case R.id.logout_group:
                String groupOwner=getIntent().getStringExtra("groupOwner");
                if (groupOwner.equals(EMClient.getInstance().getCurrentUser())){
                    ThreadUtils.runOnBackgroundThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMClient.getInstance().groupManager().destroyGroup(getIntent().getStringExtra("groupId"));//需异步处理
                                ThreadUtils.rinOnMainThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(GroupDetailActivity.this, "解散群组成功", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            } catch (HyphenateException e) {
                                ThreadUtils.rinOnMainThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(GroupDetailActivity.this, "解散群组失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                e.printStackTrace();
                            }
                        }
                    });

                }else {
                    ThreadUtils.runOnBackgroundThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMClient.getInstance().groupManager().leaveGroup(getIntent().getStringExtra("groupId"));//需异步处理
                                ThreadUtils.rinOnMainThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(GroupDetailActivity.this, "退出群组成功", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            } catch (HyphenateException e) {
                                ThreadUtils.rinOnMainThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(GroupDetailActivity.this, "退出群组失败", Toast.LENGTH_SHORT).show();

                                    }
                                });
                                e.printStackTrace();
                            }
                        }
                    });

                }
            break;
            case R.id.name_desc:
               /* if (EMClient.getInstance().getCurrentUser().equals( getIntent().getStringExtra("groupOwner"))) {
                    Intent intent = new Intent(this, UpdateInfoActivity.class);
                    intent.putExtra("type", 1);
                    intent.putExtra("updateName", "群介绍");
                    intent.putExtra("groupId", getIntent().getStringExtra("groupId"));
                    startActivity(intent);
                }*/
                break;
            case R.id.name_group:
                if (EMClient.getInstance().getCurrentUser().equals( getIntent().getStringExtra("groupOwner"))) {
                    Intent intent2 = new Intent(this, UpdateInfoActivity.class);
                    intent2.putExtra("type", 2);
                    intent2.putExtra("updateName", "群名称");
                    intent2.putExtra("groupName", getIntent().getStringExtra("groupName"));
                    intent2.putExtra("groupId", getIntent().getStringExtra("groupId"));
                    startActivity(intent2);
                }

                break;
        }
    }

    private void inviteNewUser() {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().groupManager().addUsersToGroup(getIntent().getStringExtra("groupId"), new String[]{"lin"});
                    //EMClient.getInstance().groupManager().inviteUser(getIntent().getStringExtra("groupId"),new String[]{"lin"}, null);//需异步处理
                    ThreadUtils.rinOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GroupDetailActivity.this, "邀请成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    class PicAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return names.size();
        }

        @Override
        public Object getItem(int position) {
            return names.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String name=names.get(position);
            ViewHolder viewHolder;
            if (convertView==null){
                viewHolder=new ViewHolder();
                convertView=View.inflate(GroupDetailActivity.this,R.layout.item_pic,null);
                viewHolder.name= (TextView) convertView.findViewById(R.id.title);
                viewHolder.head= (ImageView) convertView.findViewById(R.id.image);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }
            viewHolder.name.setText(name);
            return convertView;
        }

        class ViewHolder{
            TextView name;
            ImageView head;
        }
    }
}
