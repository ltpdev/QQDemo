package comemo.example.yls.qqdemo.presenter.impl;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.List;

import comemo.example.yls.qqdemo.presenter.ChartPresenter;
import comemo.example.yls.qqdemo.utils.ThreadUtils;
import comemo.example.yls.qqdemo.view.ChartView;

/**
 * Created by yls on 2016/12/30.
 */

public class ChartPresenterImpl implements ChartPresenter{
    private ChartView mChartView;
    private List<EMMessage> mEMMessages;

    public ChartPresenterImpl(ChartView mChartView) {
        this.mChartView = mChartView;
        mEMMessages = new ArrayList<EMMessage>();
    }

    @Override
    public void send(final String content, final String name) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                EMMessage message = EMMessage.createTxtSendMessage(content, name);
                mEMMessages.add(message);

                ThreadUtils.rinOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        mChartView.onStartMessage();
                    }
                });
                message.setMessageStatusCallback(new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        ThreadUtils.rinOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                mChartView.sendMessageSucucess();

                            }
                        });
                    }

                    @Override
                    public void onError(int i, String s) {
                        ThreadUtils.rinOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                mChartView.sendMessageFailed();
                            }
                        });

                    }

                    @Override
                    public void onProgress(int i, String s) {


                    }
                });

                EMClient.getInstance().chatManager().sendMessage(message);

            }
        });


    }

    @Override
    public List<EMMessage> getDataList() {
        return mEMMessages;
    }

    @Override
    public void loadMessages(final String userName) {

        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(userName);
                if (conversation != null) {
                    List<EMMessage> messages = conversation.getAllMessages();
                    mEMMessages.addAll(messages);
                    //指定会话消息未读数清零
                    conversation.markAllMessagesAsRead();

                    ThreadUtils.rinOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mChartView.onMessageLoaded();
                        }
                    });
                }


            }
        });
    }

    @Override
    public void sendPictureMessage(final String filePath, final String name) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                EMMessage message = EMMessage.createImageSendMessage(filePath, false, name);
                mEMMessages.add(message);
                message.setMessageStatusCallback(new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        ThreadUtils.rinOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                mChartView.sendPicMessageSucucess();

                            }
                        });
                    }

                    @Override
                    public void onError(int i, String s) {
                        ThreadUtils.rinOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                mChartView.sendPicMessageFailed();

                            }
                        });
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
                EMClient.getInstance().chatManager().sendMessage(message);
            }
        });




    }

    @Override
    public void sendVoiceMessage(final String filePath, final float seconds, final String mContact) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                EMMessage message = EMMessage.createVoiceSendMessage(filePath, (int) seconds, mContact);
                mEMMessages.add(message);

                message.setMessageStatusCallback(new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        ThreadUtils.rinOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                mChartView.sendVoiceMessageSucucess();
                            }
                        });
                    }

                    @Override
                    public void onError(int i, String s) {
                        ThreadUtils.rinOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                mChartView.sendVoiceMessageFailed();

                            }
                        });
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });

                EMClient.getInstance().chatManager().sendMessage(message);
            }
        });

    }

    @Override
    public void sendVideoMessage(final String videopath,final String thumpath, final int length, final String mContact) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                //bhuhhu
                EMMessage message = EMMessage.createVideoSendMessage(videopath,thumpath,length, mContact);
                mEMMessages.add(message);
                message.setMessageStatusCallback(new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        ThreadUtils.rinOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                mChartView.sendVideoMessageSucucess();
                            }
                        });
                    }

                    @Override
                    public void onError(int i, String s) {
                        ThreadUtils.rinOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                mChartView.sendVideoMessageFailed();

                            }
                        });
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });

                EMClient.getInstance().chatManager().sendMessage(message);
            }
        });
    }
}
