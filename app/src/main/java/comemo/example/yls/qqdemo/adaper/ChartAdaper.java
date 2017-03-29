package comemo.example.yls.qqdemo.adaper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.EMVideoMessageBody;
import com.hyphenate.chat.EMVoiceMessageBody;

import java.util.List;

import comemo.example.yls.qqdemo.widget.RecivedMessageItemView;
import comemo.example.yls.qqdemo.widget.RecivedPicMessageItemView;
import comemo.example.yls.qqdemo.widget.RecivedVoiceMessageItemView;
import comemo.example.yls.qqdemo.widget.SendMessageItemView;
import comemo.example.yls.qqdemo.widget.SendPicMessageItemView;
import comemo.example.yls.qqdemo.widget.SendVoiceMessageItemView;

/**
 * Created by yls on 2016/12/30.
 */

public class ChartAdaper extends RecyclerView.Adapter {
    private int SEND = 0;
    private int RECIVERD = 1;
    private int SENDTXT = 2;
    private int RECIVERDTXT = 3;
    private int SENDPIC = 4;
    private int RECIVERDPIC = 5;
    private int SENDVOICE = 6;
    private int RECIVERDVOICE = 7;
    private Context mContext;
    private List<EMMessage> mEMMessages;

    public ChartAdaper(Context mContext, List<EMMessage> mEMMessages) {
        this.mContext = mContext;
        this.mEMMessages = mEMMessages;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == SENDTXT) {
            return new sendMessageItemViewHolder(new SendMessageItemView(mContext));
        } else if (viewType == RECIVERDTXT) {
            return new RecivedMessageItemViewHolder(new RecivedMessageItemView(mContext));
        } else if (viewType == SENDPIC) {
            return new SendPicMessageItemViewHolder(new SendPicMessageItemView(mContext));
        }else if (viewType == RECIVERDPIC) {
            return new RecivedPicMessageItemViewHolder(new RecivedPicMessageItemView(mContext));
        }else if (viewType == SENDVOICE) {
            return new SendVoiceMessageItemViewHolder(new SendVoiceMessageItemView(mContext));
        }else if (viewType == RECIVERDVOICE) {
            return new RecivedVoiceMessageItemViewHolder(new RecivedVoiceMessageItemView(mContext));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof sendMessageItemViewHolder) {
            ((sendMessageItemViewHolder) holder).mSendMessageItemView.bindView(mEMMessages.get(position));

        } else if (holder instanceof RecivedMessageItemViewHolder) {
            ((RecivedMessageItemViewHolder) holder).mRecivedMessageItemView.bindView(mEMMessages.get(position));
        } else if (holder instanceof SendPicMessageItemViewHolder) {
            ((SendPicMessageItemViewHolder) holder).mSendPicMessageItemView.bindView(mEMMessages.get(position));
        }
        else if(holder instanceof RecivedPicMessageItemViewHolder){
            ((RecivedPicMessageItemViewHolder) holder).mRecivedPicMessageItemView.bindView(mEMMessages.get(position));
        }else if(holder instanceof SendVoiceMessageItemViewHolder){
            ((SendVoiceMessageItemViewHolder) holder).mSendVoiceMessageItemView.bindView(mEMMessages.get(position));
        }else if(holder instanceof RecivedVoiceMessageItemViewHolder){
            ((RecivedVoiceMessageItemViewHolder) holder).mRecivedVoiceMessageItemView.bindView(mEMMessages.get(position));
        }

    }

    @Override
    public int getItemCount() {

        return mEMMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        EMMessage emMessage = mEMMessages.get(position);
        EMMessageBody body = emMessage.getBody();
        //发送界面
        if (emMessage.direct() == EMMessage.Direct.SEND) {
            if (body instanceof EMTextMessageBody) {
                return SENDTXT;
            } else if (body instanceof EMVoiceMessageBody) {
                return SENDVOICE;
            } else {
                return SENDPIC;
            }
            //接受界面
        } else {
            if (body instanceof EMTextMessageBody) {
                return RECIVERDTXT;
            } else if (body instanceof EMVoiceMessageBody) {
                return RECIVERDVOICE;
            } else {
                return RECIVERDPIC;
            }
        }

    }

    public void addEMMessage(EMMessage emMessage) {
        mEMMessages.add(emMessage);
        notifyDataSetChanged();
    }

    public class sendMessageItemViewHolder extends RecyclerView.ViewHolder {
        private SendMessageItemView mSendMessageItemView;

        public sendMessageItemViewHolder(SendMessageItemView mSendMessageItemView) {
            super(mSendMessageItemView);
            this.mSendMessageItemView = mSendMessageItemView;
        }
    }

    public class SendPicMessageItemViewHolder extends RecyclerView.ViewHolder {
        private SendPicMessageItemView mSendPicMessageItemView;

        public SendPicMessageItemViewHolder(SendPicMessageItemView mSendPicMessageItemView) {
            super(mSendPicMessageItemView);
            this.mSendPicMessageItemView = mSendPicMessageItemView;
        }
    }
    public class RecivedPicMessageItemViewHolder extends RecyclerView.ViewHolder {
        private RecivedPicMessageItemView mRecivedPicMessageItemView;

        public RecivedPicMessageItemViewHolder(RecivedPicMessageItemView mRecivedPicMessageItemView) {
            super(mRecivedPicMessageItemView);
            this.mRecivedPicMessageItemView = mRecivedPicMessageItemView;
        }
    }
    public class SendVoiceMessageItemViewHolder extends RecyclerView.ViewHolder {
        private SendVoiceMessageItemView mSendVoiceMessageItemView;

        public SendVoiceMessageItemViewHolder(SendVoiceMessageItemView mSendVoiceMessageItemView) {
            super(mSendVoiceMessageItemView);
            this.mSendVoiceMessageItemView = mSendVoiceMessageItemView;
        }
    }

    public class RecivedVoiceMessageItemViewHolder extends RecyclerView.ViewHolder {
        private RecivedVoiceMessageItemView mRecivedVoiceMessageItemView;

        public RecivedVoiceMessageItemViewHolder(RecivedVoiceMessageItemView mRecivedVoiceMessageItemView) {
            super(mRecivedVoiceMessageItemView);
            this.mRecivedVoiceMessageItemView = mRecivedVoiceMessageItemView;
        }
    }
    public class RecivedMessageItemViewHolder extends RecyclerView.ViewHolder {
        private RecivedMessageItemView mRecivedMessageItemView;

        public RecivedMessageItemViewHolder(RecivedMessageItemView mRecivedMessageItemView) {
            super(mRecivedMessageItemView);
            this.mRecivedMessageItemView = mRecivedMessageItemView;
        }
    }
}
