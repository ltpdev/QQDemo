package comemo.example.yls.qqdemo.model;

import cn.bmob.v3.BmobObject;

/**
 * 评论表
 */

public class Comment extends BmobObject{
    private String content;
    //评论者
    private User commenter;
    private Dynamic dynamic;
    private Integer likes;
    public String getContent() {
        return content;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getCommenter() {
        return commenter;
    }

    public void setCommenter(User commenter) {
        this.commenter = commenter;
    }

    public Dynamic getDynamic() {
        return dynamic;
    }

    public void setDynamic(Dynamic dynamic) {
        this.dynamic = dynamic;
    }
}
