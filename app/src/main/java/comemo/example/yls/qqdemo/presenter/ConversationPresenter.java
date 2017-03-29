package comemo.example.yls.qqdemo.presenter;

import com.hyphenate.chat.EMConversation;

import java.util.List;

/**
 * Created by asus- on 2017/1/1.
 */

public interface ConversationPresenter {
    List<EMConversation> getDataList();

    void loadConversation();
}
