package comemo.example.yls.qqdemo.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import comemo.example.yls.qqdemo.R;
import comemo.example.yls.qqdemo.model.User;

public class UpdateSignatureActivity extends BaseActivity {

    @BindView(R.id.edt_signature)
    EditText edtSignature;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.update)
    TextView update;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_update_signature;
    }

    @Override
    protected void init() {
        super.init();
        title.setText("个性签名");
        back.setVisibility(View.VISIBLE);
        update.setVisibility(View.VISIBLE);
        User user= BmobUser.getCurrentUser(User.class);
        edtSignature.setText(user.getSignature());
    }



    @OnClick({R.id.update,R.id.back})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.update:
                String signature= edtSignature.getText().toString();
                if (signature.length()==0){
                    Toast.makeText(this, "签名不能为空", Toast.LENGTH_SHORT).show();
                }else {
                    updateSignature(signature);
                }
            break;
            case R.id.back:
                finish();
                break;
        }
    }

    private void updateSignature(String signature) {
        showProgressDialog("更新中。。。");
        User user= BmobUser.getCurrentUser(User.class);
        User newUser=new User();
        newUser.setSignature(signature);
        newUser.update(user.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    hideProgressDialog();
                    Toast.makeText(getApplicationContext(), "更新成功", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    hideProgressDialog();
                    Toast.makeText(getApplicationContext(), "更新失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
