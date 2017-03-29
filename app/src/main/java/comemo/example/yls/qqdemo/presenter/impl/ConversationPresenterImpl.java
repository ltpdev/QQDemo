package comemo.example.yls.qqdemo.presenter.impl;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import comemo.example.yls.qqdemo.presenter.ConversationPresenter;
import comemo.example.yls.qqdemo.utils.ThreadUtils;
import comemo.example.yls.qqdemo.view.ConversationView;

/**
 * Created by asus- on 2017/1/1.
 */

public class ConversationPresenterImpl implements ConversationPresenter {
    private ConversationView conversationView;
    private List<EMConversation>emConversations;
    public ConversationPresenterImpl(ConversationView conversationView){
        this.conversationView=conversationView;
        emConversations=new ArrayList<EMConversation>();
    }

    @Override
    public List<EMConversation> getDataList() {

        return emConversations;
    }

    @Override
    public void loadConversation() {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
                emConversations.clear();
                emConversations.addAll(conversations.values());

                Collections.sort(emConversations, new Comparator<EMConversation>() {
                    @Override
                    public int compare(EMConversation o1, EMConversation o2) {
                        return (int) (o2.getLastMessage().getMsgTime()-o1.getLastMessage().getMsgTime());
                    }
                });

                ThreadUtils.rinOnMainThread(new Runnable() {
                    @Override
                    public void run() {

                        conversationView.onAllConversationsLoaded();
                    }
                });
            }
        });
    }
}
