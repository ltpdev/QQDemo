package comemo.example.yls.qqdemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import comemo.example.yls.qqdemo.R;
import comemo.example.yls.qqdemo.utils.ThreadUtils;

public class GroupListActivity extends BaseActivity {


    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.title)
    TextView mTextView;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.add)
    ImageView add;
    private List<EMGroup> emGroupList = new ArrayList<EMGroup>();
    private GroupListAdapter groupListAdapter;
////

    @Override
    public int getLayoutResId() {
        return R.layout.activity_group_list;
    }

    @Override
    protected void init() {
        super.init();
        mTextView.setText("加入的群");
        back.setVisibility(View.VISIBLE);
        add.setVisibility(View.VISIBLE);
        groupListAdapter=new GroupListAdapter();
        listview.setAdapter(groupListAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EMGroup emGroup=emGroupList.get(position);
                Toast.makeText(GroupListActivity.this, emGroup.getMembers().size()+"", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(GroupListActivity.this,GroupDetailActivity.class);
                intent.putExtra("groupName",emGroup.getGroupName());
                intent.putExtra("groupId",emGroup.getGroupId());
                intent.putExtra("groupDesc",emGroup.getDescription());
                intent.putExtra("memberCount",emGroup.getMemberCount());
                intent.putExtra("groupOwner",emGroup.getOwner());
                intent.putStringArrayListExtra("memberList", (ArrayList<String>) emGroup.getMembers());
                startActivity(intent);
            }
        });

    }

    private void getData() {
        emGroupList.clear();
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                try {

                    List<EMGroup> grouplist = EMClient.getInstance().groupManager().getJoinedGroupsFromServer();//需异步处理
                    for (final EMGroup emGroup : grouplist) {
                        emGroupList.add(emGroup);
                        ThreadUtils.rinOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(getActivity(), emGroup.getGroupName(), Toast.LENGTH_SHORT).show();
                                groupListAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @OnClick({R.id.back,R.id.add})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
            break;
        }
    }


    class GroupListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return emGroupList.size();
        }

        @Override
        public Object getItem(int position) {
            return emGroupList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            EMGroup emGroup=emGroupList.get(position);
            ViewHolder viewHolder;
            if (convertView==null){
                viewHolder=new ViewHolder();
                convertView=View.inflate(GroupListActivity.this,R.layout.item_group,null);
                viewHolder.name= (TextView) convertView.findViewById(R.id.name);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }
            viewHolder.name.setText(emGroup.getGroupName());
            return convertView;
        }

        class ViewHolder{
            TextView name;
        }
    }
}
