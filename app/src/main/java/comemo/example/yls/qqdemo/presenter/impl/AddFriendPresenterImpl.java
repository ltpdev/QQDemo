package comemo.example.yls.qqdemo.presenter.impl;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import comemo.example.yls.qqdemo.factory.DataBaseManager;
import comemo.example.yls.qqdemo.model.AddFriendEvent;
import comemo.example.yls.qqdemo.model.SearchResultItem;
import comemo.example.yls.qqdemo.presenter.AddFriendPresenter;
import comemo.example.yls.qqdemo.utils.ThreadUtils;
import comemo.example.yls.qqdemo.view.AddFriendView;

/**
 * Created by asus- on 2017/1/2.
 */

public class AddFriendPresenterImpl implements AddFriendPresenter {
    private AddFriendView addFriendView;
    private List<SearchResultItem>searchResultItems;
    public AddFriendPresenterImpl(AddFriendView addFriendView){
        this.addFriendView=addFriendView;
        searchResultItems=new ArrayList<SearchResultItem>();
        EventBus.getDefault().register(this);
    }

    @Override
    public void searchFriend(String name) {
        searchResultItems.clear();
        BmobQuery<BmobUser> query = new BmobQuery<BmobUser>();
        //query.addWhereEqualTo("username",name);
       query.addWhereContains("username",name).addWhereNotEqualTo("username", EMClient.getInstance().getCurrentUser());
        query.findObjects(new FindListener<BmobUser>() {
            @Override
            public void done(List<BmobUser> list, BmobException e) {
                   if(e==null){
                      if(list.size()==0){
                         addFriendView.onsearchEmpty();
                      }
                       else {
                         List<String>contacts=DataBaseManager.getInstance().queryContacts();
                          for (int i = 0; i <list.size(); i++) {

                              SearchResultItem searchResultItem=new SearchResultItem();
                              searchResultItem.userName=list.get(i).getUsername();
                              searchResultItem.time=list.get(i).getCreatedAt();
                              searchResultItem.added=contacts.contains(list.get(i).getUsername());
                              searchResultItems.add(searchResultItem);
                          }
                          addFriendView.onsearchSucess();
                      }
                   }
                else{
                       addFriendView.onsearchFailed();
                   }
            }
        });
    }

    @Override
    public List getDataList() {

        return searchResultItems;
    }

    @Override
    public void destroy() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onAddFriendEvent(AddFriendEvent event){
        try {
            EMClient.getInstance().contactManager().addContact(event.userName,event.reason);
            ThreadUtils.rinOnMainThread(new Runnable() {
                @Override
                public void run() {
                    addFriendView.onSendAddFriendQuestSuess();
                }
            });
        } catch (HyphenateException e) {
            ThreadUtils.rinOnMainThread(new Runnable() {
                @Override
                public void run() {
                    addFriendView.onSendAddFriendQuestFailed();
                }
            });
            e.printStackTrace();
        }
    }
}
