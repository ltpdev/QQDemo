package comemo.example.yls.qqdemo.presenter;

import java.util.List;

import comemo.example.yls.qqdemo.model.ContactListItem;

/**
 * Created by yls on 2016/12/30.
 */

public interface ContactsPresenter {
    void loadContacts();

    List<ContactListItem> getDataList();

    void refresh();

    void del(String name);


}
