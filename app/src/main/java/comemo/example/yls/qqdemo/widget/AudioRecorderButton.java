package comemo.example.yls.qqdemo.widget;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import comemo.example.yls.qqdemo.R;
import comemo.example.yls.qqdemo.manager.AudioManager;
import comemo.example.yls.qqdemo.manager.DialogManager;

/**
 * Created by asus- on 2017/3/19.
 */

public class AudioRecorderButton extends Button implements AudioManager.AudioPreparedListener {
    private static final int DISTANCE_Y_CANCEL=50;
    private static final int STATE_NORMAL = 1;
    private static final int STATE_RECORDERING = 2;
    private static final int STATE_WANT_TO_CANCEL = 3;
    private int mCurState = STATE_NORMAL;
    private boolean isRecordering = false;
    private DialogManager dialogManager;
    private AudioManager audioManager;
    private static final int MSG_AUDIO_PREPARED=0X110;
    private static final int MSG_VOICE_CHANGE=0X111;
    private static final int MSG_DIALOG_DIMISS=0X112;
    private float mTime;
    private boolean isReady;
    public AudioRecorderButton(Context context) {
        super(context, null);
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_AUDIO_PREPARED:
                    dialogManager.showRecordingDialog();
                    isRecordering=true;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (isRecordering){
                                try {

                                    Thread.sleep(100);
                                    mTime+=0.1f;
                                    handler.sendEmptyMessage(MSG_VOICE_CHANGE);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();
                break;
                case MSG_VOICE_CHANGE:
                    dialogManager.updateVoiceLevel(audioManager.getVoiceLevel(7));
                    break;
                case MSG_DIALOG_DIMISS:
                    dialogManager.dimissDialog();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public AudioRecorderButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        dialogManager=new DialogManager(getContext());
        String dir= Environment.getExternalStorageDirectory()+"/recorder";
        audioManager=AudioManager.getInstance(dir);
        audioManager.setAudioPreparedListener(this);
        /*setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return false;
            }
        });*/

    }
public interface AudioFinshRecorderListener{
    void OnFinish(float seconds,String filePath);
}
    private AudioFinshRecorderListener mAudioFinshRecorderListener;
    public void setAudioFinshRecorderListener(AudioFinshRecorderListener mAudioFinshRecorderListener){

        this.mAudioFinshRecorderListener=mAudioFinshRecorderListener;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                isReady=true;
                audioManager.prepareAudio();
                changeState(STATE_RECORDERING);
                break;
            case MotionEvent.ACTION_MOVE:
                if (isRecordering) {
                    if (wantToCancel(x, y)) {
                        changeState(STATE_WANT_TO_CANCEL);
                    } else {
                        changeState(STATE_RECORDERING);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!isReady){
                    reset();
                    return super.onTouchEvent(event);
                }
                if (!isRecordering||mTime<0.6f){
                    dialogManager.tooShort();
                    audioManager.cancel();
                    handler.sendEmptyMessageDelayed(MSG_DIALOG_DIMISS,1300);
                    return super.onTouchEvent(event);
                }

                if (mCurState == STATE_RECORDERING) {
                    //release
                    //保存录音
                    dialogManager.dimissDialog();
                    if (mAudioFinshRecorderListener!=null){
                        mAudioFinshRecorderListener.OnFinish(mTime,audioManager.getCurrentPath());
                    }
                    audioManager.release();
                } else if (mCurState == STATE_WANT_TO_CANCEL) {
                    //cancel
                    dialogManager.dimissDialog();
                    audioManager.cancel();
                }
                reset();
                break;
        }
        return super.onTouchEvent(event);
    }

    private void reset() {
        isRecordering = false;
        isReady=false;
        changeState(STATE_NORMAL);
        mTime=0;

    }

    private boolean wantToCancel(int x, int y) {
        if (x<0||x>getWidth()){
            return true;
        }
        if(y<-DISTANCE_Y_CANCEL||y>getHeight()+DISTANCE_Y_CANCEL){
            return true;
        }
      return false;
    }

    private void changeState(int state) {
        if(mCurState!=state){
            mCurState=state;
            switch (state){
                case STATE_NORMAL:
                    setBackgroundResource(R.drawable.btn_recorder_normal);
                    setText(R.string.recorder_normal);
                break;
                case STATE_RECORDERING:
                    setBackgroundResource(R.drawable.btn_recordering);
                    setText(R.string.recorder_recordering);
                    if (isRecordering){
                        dialogManager.recording();
                    }
                    break;
                case STATE_WANT_TO_CANCEL:
                    setBackgroundResource(R.drawable.btn_recordering);
                    setText(R.string.recorder_want_cancel);
                    dialogManager.wantToCancel();
                    break;
            }
        }

    }

    @Override
    public void wellPrepared() {
     handler.sendEmptyMessage(MSG_AUDIO_PREPARED);
    }
}
