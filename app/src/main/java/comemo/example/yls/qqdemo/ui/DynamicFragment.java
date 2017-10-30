package comemo.example.yls.qqdemo.ui;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.widget.ListView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import comemo.example.yls.qqdemo.MainActivity;
import comemo.example.yls.qqdemo.R;
import comemo.example.yls.qqdemo.adaper.DynamicAdapter;
import comemo.example.yls.qqdemo.event.DynamicDanEvent;
import comemo.example.yls.qqdemo.model.Dynamic;

/**
 * Created by yls on 2016/12/29.
 */

public class DynamicFragment extends BaseFragment {
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.floatbutton)
    FloatingActionButton floatbutton;
    private List<Dynamic> dynamicList = new ArrayList<>();
    private DynamicAdapter dynamicAdapter;



    @Override
    public int getLayoutResId() {
        return R.layout.fragment_dynamic;
    }

    @Override
    protected void init() {
        super.init();
        dynamicAdapter = new DynamicAdapter(getActivity(), dynamicList, getActivity());
        listview.setAdapter(dynamicAdapter);
        showLoadingDialog();
        initData();
    }


    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    private void initData() {
        BmobQuery<Dynamic> query = new BmobQuery<Dynamic>();
        query.order("-createdAt");
        query.include("author");
        query.findObjects(new FindListener<Dynamic>() {
            @Override
            public void done(List<Dynamic> list, BmobException e) {
                if (e == null) {
                    dynamicList.clear();
                    //Toast.makeText(getActivity(), list.size()+"查询"+list.get(0).getAuthor().getUsername(), Toast.LENGTH_SHORT).show();
                    for (Dynamic dynamic : list) {
                        dynamicList.add(dynamic);
                    }
                    dimissDialog();
                    dynamicAdapter.notifyDataSetChanged();
                } else {
                    dimissDialog();
                    Toast.makeText(getActivity(), "查询失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUploadDynamicEvent(DynamicDanEvent event) {
        if (event.isUpdate()) {
            initData();
        }
    }


    @Override
    protected void initTitle() {
        super.initTitle();
        MainActivity activity = (MainActivity) getActivity();
        activity.setTitle("动态");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.floatbutton)
    public void onClick() {
        Intent intent=new Intent(getActivity(),WriteDynamicActivity.class);
        startActivity(intent);
    }
}
