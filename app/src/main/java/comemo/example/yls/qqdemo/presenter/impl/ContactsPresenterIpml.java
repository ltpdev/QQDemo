package comemo.example.yls.qqdemo.presenter.impl;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import comemo.example.yls.qqdemo.database.Contact;
import comemo.example.yls.qqdemo.factory.DataBaseManager;
import comemo.example.yls.qqdemo.model.ContactListItem;
import comemo.example.yls.qqdemo.presenter.ContactsPresenter;
import comemo.example.yls.qqdemo.utils.ThreadUtils;
import comemo.example.yls.qqdemo.view.ContactsView;

/**
 * Created by yls on 2016/12/30.
 */

public class ContactsPresenterIpml implements ContactsPresenter{
    private ContactsView mContactsView;
    private List<ContactListItem> mContactListItems;

    public ContactsPresenterIpml(ContactsView mContactsView) {
        this.mContactsView = mContactsView;
        mContactListItems = new ArrayList<ContactListItem>();
    }

    @Override
    public void loadContacts() {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                try {
                    DataBaseManager.getInstance().deleteAllContacts();
                    List<String> usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    for (int i = 0; i < usernames.size(); i++) {
                        ContactListItem contactListItem = new ContactListItem();
                        contactListItem.contact = usernames.get(i);
                        mContactListItems.add(contactListItem);
                    }

                    Collections.sort(mContactListItems, new Comparator<ContactListItem>() {
                        @Override
                        public int compare(ContactListItem o1, ContactListItem o2) {
                            return o1.contact.charAt(0) - o2.contact.charAt(0);
                        }
                    });

                    for (int i = 0; i < mContactListItems.size(); i++) {
                        ContactListItem contactListItem = mContactListItems.get(i);
                        if (i > 0 && contactListItem.getFirstLetter().equals(mContactListItems.get(i - 1).getFirstLetter())) {
                            contactListItem.showFirstLetter=false;
                        }
                        else {
                            contactListItem.showFirstLetter=true;
                        }
                        Contact contact=new Contact();
                        contact.setUserName(usernames.get(i));
                        DataBaseManager.getInstance().save(contact);
                    }


                    ThreadUtils.rinOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mContactsView.loadContactsSuccess();
                        }
                    });
                } catch (HyphenateException e) {
                    ThreadUtils.rinOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mContactsView.loadContactsFalied();
                        }
                    });
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    public List<ContactListItem> getDataList() {
        return mContactListItems;
    }

    @Override
    public void refresh() {
        mContactListItems.clear();
        loadContacts();
    }

    @Override
    public void del(final String name) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().deleteContact(name);
                    ThreadUtils.rinOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mContactsView.delSuccess();
                        }
                    });

                } catch (HyphenateException e) {
                    e.printStackTrace();
                    mContactsView.delFailed();

                }
            }
        });
    }

}
