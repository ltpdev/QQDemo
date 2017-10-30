package comemo.example.yls.qqdemo.ui;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import comemo.example.yls.qqdemo.MainActivity;
import comemo.example.yls.qqdemo.R;
import comemo.example.yls.qqdemo.adaper.ConverSationAdapter;
import comemo.example.yls.qqdemo.event.DynamicDanEvent;
import comemo.example.yls.qqdemo.event.SendMessageEvent;
import comemo.example.yls.qqdemo.presenter.ConversationPresenter;
import comemo.example.yls.qqdemo.presenter.impl.ConversationPresenterImpl;
import comemo.example.yls.qqdemo.utils.ThreadUtils;
import comemo.example.yls.qqdemo.view.ConversationView;

/**
 * Created by yls on 2016/12/29.
 */

public class ConversionFragment extends BaseFragment implements ConversationView {
    @BindView(R.id.converSationRecyclerView)
    RecyclerView converSationRecyclerView;
    private ConverSationAdapter mconverSationAdapter;

    private ConversationPresenter conversationPresenter;
    private EMMessageListener emMessageListener=new EMMessageListener() {
        @Override
        public void onMessageReceived(final List<EMMessage> list) {
            ThreadUtils.rinOnMainThread(new Runnable() {
                @Override
                public void run() {
                    conversationPresenter.loadConversation();
                }
            });


        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {

        }

        };
    @Override
    public int getLayoutResId() {

        return R.layout.fragment_conversion;

    }


    @Override
    protected void initTitle() {
        super.initTitle();
        MainActivity activity= (MainActivity) getActivity();
        activity.setTitle("消息");
    }

    @Override
    protected void init() {
        super.init();
        showLoadingDialog();
        conversationPresenter=new ConversationPresenterImpl(this);
        initRecyclerView();
        EMClient.getInstance().chatManager().addMessageListener(emMessageListener);
        conversationPresenter.loadConversation();
    }

    private void initRecyclerView() {
        mconverSationAdapter=new ConverSationAdapter(getActivity(),conversationPresenter.getDataList());
        converSationRecyclerView.setHasFixedSize(true);
        converSationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        converSationRecyclerView.setAdapter(mconverSationAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdate(SendMessageEvent event) {
        if (event.isUpdate()) {
            conversationPresenter.loadConversation();
        }
    }


    @Override
    public void onAllConversationsLoaded() {
        //Toast.makeText(getActivity(), getString(R.string.onAllConversationsLoaded), Toast.LENGTH_SHORT).show();
        dimissDialog();
        mconverSationAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        EMClient.getInstance().chatManager().removeMessageListener(emMessageListener);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
