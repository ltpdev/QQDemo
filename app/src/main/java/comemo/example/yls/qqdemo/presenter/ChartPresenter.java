package comemo.example.yls.qqdemo.presenter;

import com.hyphenate.chat.EMMessage;

import java.util.List;

/**
 * Created by yls on 2016/12/30.
 */

public interface ChartPresenter {
    void send(String content, String name);

    List<EMMessage> getDataList();

    void loadMessages(String mContact);
    void  sendPictureMessage(String filePath, String name);

    void sendVoiceMessage(String filePath, float seconds, String mContact);
}
