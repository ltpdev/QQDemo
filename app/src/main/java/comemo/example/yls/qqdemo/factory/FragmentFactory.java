package comemo.example.yls.qqdemo.factory;

import android.support.v4.app.Fragment;

import comemo.example.yls.qqdemo.R;
import comemo.example.yls.qqdemo.ui.ContactsFragment;
import comemo.example.yls.qqdemo.ui.ConversionFragment;
import comemo.example.yls.qqdemo.ui.DynamicFragment;

/**
 * Created by yls on 2016/12/29.
 */

public class FragmentFactory {
    private static FragmentFactory sFragmentFactory;
    private ConversionFragment mConversionFragment;
    private ContactsFragment mContactsFragment;
    private DynamicFragment mDynamicFragment;

    public static FragmentFactory getInstance() {
        if (sFragmentFactory == null)
            synchronized (FragmentFactory.class) {
            sFragmentFactory = new FragmentFactory();
        }
        return sFragmentFactory;
    }

    public Fragment getFragment(int tabId) {
        switch (tabId) {
            case R.id.conversion:
                return getConversionFragment();
            case R.id.contacts:
                return getContactsFragment();
            case R.id.dynamic:
                return getDynamicFragment();
        }
        return null;
    }


    public Fragment getConversionFragment() {
        if (mConversionFragment == null) {
            mConversionFragment = new ConversionFragment();
        }
        return mConversionFragment;
    }


    public Fragment getContactsFragment() {
        if (mContactsFragment == null) {
            mContactsFragment = new ContactsFragment();
        }
        return mContactsFragment;
    }

    public Fragment getDynamicFragment() {
        if (mDynamicFragment == null) {
            mDynamicFragment = new DynamicFragment();
        }
        return mDynamicFragment;
    }
}
