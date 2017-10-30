package comemo.example.yls.qqdemo.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import comemo.example.yls.qqdemo.R;
import comemo.example.yls.qqdemo.utils.ThreadUtils;

import static android.R.attr.description;

public class UpdateInfoActivity extends BaseActivity {
    @BindView(R.id.title)
    TextView mTextView;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.add)
    ImageView add;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.update)
    TextView update;


    @Override
    public int getLayoutResId() {
        return R.layout.activity_update_info;
    }

    @Override
    protected void init() {
        super.init();
        mTextView.setText(getIntent().getStringExtra("updateName"));
        edtName.setText(getIntent().getStringExtra("groupName"));
        name.setText(getIntent().getStringExtra("updateName"));
        back.setVisibility(View.VISIBLE);
        update.setVisibility(View.VISIBLE);
    }



    @OnClick({R.id.back, R.id.update})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.update:
                String groupId=getIntent().getStringExtra("groupId");
                if (getIntent().getIntExtra("type",0)==1){
                    updateGroupDesc(groupId);
                }else if (getIntent().getIntExtra("type",0)==2){
                    updateGroupName(groupId);
                }
                break;
        }
    }

    private void updateGroupName(final String groupId) {
        final String changedGroupName=edtName.getText().toString();
        if (changedGroupName.length()==0){
            Toast.makeText(this, "群名称不可以为空", Toast.LENGTH_SHORT).show();
            return;
        }

        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().groupManager().changeGroupName(groupId,changedGroupName);
                    ThreadUtils.rinOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UpdateInfoActivity.this, "修改群名称成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                } catch (HyphenateException e) {
                    ThreadUtils.rinOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UpdateInfoActivity.this, "修改群名称失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                    e.printStackTrace();
                }
            }
        });

    }

    private void updateGroupDesc(final String groupId) {
        final String description=edtName.getText().toString();
        if (description.length()==0){
            Toast.makeText(this, "群介绍不可以为空", Toast.LENGTH_SHORT).show();
            return;
        }
       /* ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().groupManager().changeGroupDescription(groupId, description);//需异步处理
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        });*/

    }
}
