package comemo.example.yls.qqdemo.manager;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaMetadata;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import comemo.example.yls.qqdemo.R;

/**
 * Created by asus- on 2017/3/19.
 */

public class DialogManager {
    private Dialog dialog;
    private ImageView mIcon;
    private ImageView mVoice;
    private TextView mLable;
    private Context mContext;

    public DialogManager(Context mContext) {
        this.mContext = mContext;
    }

    public void showRecordingDialog() {
        dialog = new Dialog(mContext, R.style.Theme_AudioDialog);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_recorder, null);
        dialog.setContentView(view);
        mIcon = (ImageView) view.findViewById(R.id.id_recorder_dialog_icon);
        mVoice = (ImageView) view.findViewById(R.id.id_recorder_dialog_voice);
        mLable = (TextView) view.findViewById(R.id.id_recorder_di);
        dialog.show();

    }

    public void recording() {
        if (dialog != null && dialog.isShowing()) {
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.VISIBLE);
            mLable.setVisibility(View.VISIBLE);
            mIcon.setImageResource(R.mipmap.recorder);
            mLable.setText("手指上滑，取消发送");

        }
    }

    public void wantToCancel() {
        if (dialog != null && dialog.isShowing()) {
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.GONE);
            mLable.setVisibility(View.VISIBLE);
            mIcon.setImageResource(R.mipmap.cancel);
            mLable.setText("松开手指，取消发送");
        }
    }

    public void tooShort() {
        if (dialog != null && dialog.isShowing()) {
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.GONE);
            mLable.setVisibility(View.VISIBLE);
            mIcon.setImageResource(R.mipmap.voice_to_short);
            mLable.setText("录音时间过短");
        }
    }

    public void dimissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public void updateVoiceLevel(int level) {
        if (dialog != null && dialog.isShowing()) {
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.VISIBLE);
            mLable.setVisibility(View.VISIBLE);
            int resId = mContext.getResources().getIdentifier("v" + level, "mipmap", mContext.getPackageName());
            mVoice.setImageResource(resId);
        }
    }
}
