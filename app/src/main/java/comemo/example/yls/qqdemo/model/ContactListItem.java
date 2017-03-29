package comemo.example.yls.qqdemo.model;

/**
 * Created by yls on 2016/12/30.
 */

public class ContactListItem {
    public String contact;
    public boolean showFirstLetter;

    public String getFirstLetter() {

        return String.valueOf(contact.charAt(0)).toUpperCase();
    }
}
