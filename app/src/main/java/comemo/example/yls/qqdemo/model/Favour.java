package comemo.example.yls.qqdemo.model;

import cn.bmob.v3.BmobObject;

/**
 * 点赞表
 */

public class Favour extends BmobObject{
    private Comment comment;
    private User favourer;

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public User getFavourer() {
        return favourer;
    }

    public void setFavourer(User favourer) {
        this.favourer = favourer;
    }
}
