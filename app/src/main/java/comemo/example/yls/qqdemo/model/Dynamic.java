package comemo.example.yls.qqdemo.model;

import java.io.Serializable;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

/**
 * 动态表
 */

public class Dynamic extends BmobObject implements Serializable{
    private User author;
    private String content;
    private List<String>photos;
    private Integer comments;


    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }



    public void setComments(Integer comments) {
        this.comments = comments;
    }

    public User getAuthor() {
        return author;
    }

    public Integer getComments() {
        return comments;
    }



    public String getContent() {
        return content;
    }
}
