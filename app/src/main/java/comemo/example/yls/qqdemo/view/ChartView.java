package comemo.example.yls.qqdemo.view;

/**
 * Created by yls on 2016/12/30.
 */

public interface ChartView {
    void sendMessageSucucess();

    void sendMessageFailed();

    void onStartMessage();

    void onMessageLoaded();

    void sendPicMessageSucucess();

    void sendPicMessageFailed();

    void sendVoiceMessageSucucess();

    void sendVoiceMessageFailed();

    void sendVideoMessageSucucess();

    void sendVideoMessageFailed();
}
