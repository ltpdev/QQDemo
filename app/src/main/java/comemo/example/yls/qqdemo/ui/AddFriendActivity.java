package comemo.example.yls.qqdemo.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import comemo.example.yls.qqdemo.R;
import comemo.example.yls.qqdemo.adaper.AddRecyclerViewAdapter;
import comemo.example.yls.qqdemo.presenter.AddFriendPresenter;
import comemo.example.yls.qqdemo.presenter.impl.AddFriendPresenterImpl;
import comemo.example.yls.qqdemo.view.AddFriendView;

/**
 * Created by asus- on 2017/1/2.
 */

public class AddFriendActivity extends BaseActivity implements AddFriendView{
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.friend_name)
    EditText friendName;
    @BindView(R.id.search)
    ImageView search;
    @BindView(R.id.addRecyclerView)
    RecyclerView addRecyclerView;
private AddFriendPresenter addFriendPresenter;
    private AddRecyclerViewAdapter addRecyclerViewAdapter;
    @Override
    public int getLayoutResId() {
        return R.layout.activity_addfriend;
    }

    @Override
    protected void init() {
        super.init();
        addFriendPresenter=new AddFriendPresenterImpl(this);
        title.setText(getString(R.string.add_friend));
        initRecyclerView();
    }

    private void initRecyclerView() {
        addRecyclerViewAdapter=new AddRecyclerViewAdapter(this,addFriendPresenter.getDataList());
        addRecyclerView.setHasFixedSize(true);
        addRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        addRecyclerView.setAdapter(addRecyclerViewAdapter);
    }


    @OnClick(R.id.search)
    public void onClick() {
        String name=friendName.getText().toString().trim();
        showProgressDialog(getString(R.string.searching));
        addFriendPresenter.searchFriend(name);
    }

    @Override
    public void onsearchEmpty() {
        hideProgressDialog();
        addRecyclerViewAdapter.notifyDataSetChanged();
        Toast.makeText(AddFriendActivity.this, getString(R.string.search_empty), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onsearchSucess() {
        hideProgressDialog();
        addRecyclerViewAdapter.notifyDataSetChanged();
        Toast.makeText(AddFriendActivity.this, getString(R.string.search_suceess), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onsearchFailed() {
        hideProgressDialog();
        addRecyclerViewAdapter.notifyDataSetChanged();
        Toast.makeText(AddFriendActivity.this, getString(R.string.search_failed), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSendAddFriendQuestSuess() {
        Toast.makeText(AddFriendActivity.this, "添加好友请求成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSendAddFriendQuestFailed() {
        Toast.makeText(AddFriendActivity.this, "添加好友请求失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        addFriendPresenter.destroy();
        super.onDestroy();
    }
}
