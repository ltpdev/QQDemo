package comemo.example.yls.qqdemo.presenter;

import java.util.List;

/**
 * Created by asus- on 2017/1/2.
 */

public interface AddFriendPresenter {
    void searchFriend(String name);

    List getDataList();

    void destroy();
}
