package comemo.example.yls.qqdemo.model;

import cn.bmob.v3.BmobUser;

/**
 * Created by asus- on 2017/10/25.
 */

public class User extends BmobUser{
    private String head;
    private String sex;
    private String address;
    private String signature;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getHead() {
        return head;
    }
}
