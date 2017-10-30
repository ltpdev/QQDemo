package comemo.example.yls.qqdemo.event;

/**
 * Created by asus- on 2017/10/25.
 */

public class SendMessageEvent {
        private boolean isUpdate;
        public SendMessageEvent(boolean isUpdate){
            this.isUpdate=isUpdate;
        }

    public boolean isUpdate() {
        return isUpdate;
    }
}
