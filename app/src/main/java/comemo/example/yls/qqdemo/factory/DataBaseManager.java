package comemo.example.yls.qqdemo.factory;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import comemo.example.yls.qqdemo.database.Contact;
import comemo.example.yls.qqdemo.database.ContactDao;
import comemo.example.yls.qqdemo.database.DaoMaster;
import comemo.example.yls.qqdemo.database.DaoSession;
import comemo.example.yls.qqdemo.model.Constant;

/**
 * Created by asus- on 2017/1/2.
 */

public class DataBaseManager {
    private static DataBaseManager dataBaseManager;
    private DaoSession mDaoSession;

    public static DataBaseManager getInstance(){
        if(dataBaseManager==null){
            synchronized (DataBaseManager.class){
                dataBaseManager=new DataBaseManager();
            }
        }
        return dataBaseManager;
    }
    public void save(Contact contact){
        ContactDao contactDao=mDaoSession.getContactDao();
        contactDao.save(contact);
    }
    public void initDataBase(Context context) {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, Constant.Database.DATABASE_NAME, null);
        SQLiteDatabase writableDatabase = devOpenHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(writableDatabase);
        mDaoSession = daoMaster.newSession();
    }

    public List<String> queryContacts() {
        ContactDao contactDao=mDaoSession.getContactDao();
        QueryBuilder<Contact> contactQueryBuilder=contactDao.queryBuilder();
        List<Contact> list=contactQueryBuilder.list();
        ArrayList<String> contacts = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            String contact = list.get(i).getUserName();
            contacts.add(contact);
        }
        return contacts;
    }
    public void deleteAllContacts() {
        ContactDao contactDao = mDaoSession.getContactDao();
        contactDao.deleteAll();
    }
}
