package comemo.example.yls.qqdemo.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import comemo.example.yls.qqdemo.R;
import comemo.example.yls.qqdemo.adaper.ContactsAdapter;
import comemo.example.yls.qqdemo.listener.OnLongClickItemListener;
import comemo.example.yls.qqdemo.presenter.ContactsPresenter;
import comemo.example.yls.qqdemo.presenter.impl.ContactsPresenterIpml;
import comemo.example.yls.qqdemo.view.ContactsView;
import comemo.example.yls.qqdemo.widget.SlideBar;

/**
 * Created by yls on 2016/12/29.
 */

public class ContactsFragment extends BaseFragment implements ContactsView {

    @BindView(R.id.contactsRecyclerView)
    RecyclerView mContactsRecyclerView;
    @BindView(R.id.title)
    TextView mTextView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.add)
    ImageView add;
    @BindView(R.id.SlideBar)
    comemo.example.yls.qqdemo.widget.SlideBar SlideBar;
    @BindView(R.id.tvTxt)
    TextView tvTxt;
    private ContactsAdapter mContactsAdapter;
    private ContactsPresenter mContactsPresenter;

    @Override
    public int getLayoutResId() {

        return R.layout.fragment_contacts;
    }

    @Override
    protected void init() {
        super.init();
        mSwipeRefreshLayout.setColorSchemeResources(R.color.qq_blue, R.color.colorPrimary);
        mContactsPresenter = new ContactsPresenterIpml(this);
        mContactsPresenter.loadContacts();
        mContactsAdapter = new ContactsAdapter(getContext(), mContactsPresenter.getDataList());
        mTextView.setText(getString(R.string.contacs));
        add.setVisibility(View.VISIBLE);
        initRecyclerView();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mContactsPresenter.refresh();
            }
        });
        EMClient.getInstance().contactManager().setContactListener(emContactListener);
        mContactsAdapter.setOnLongClickItemListener(onLongClickItemListener);
        SlideBar.setOnSlideChangeLister(onSlideChangeLister);
    }


    private void initRecyclerView() {
        mContactsRecyclerView.setHasFixedSize(true);
        mContactsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mContactsRecyclerView.setAdapter(mContactsAdapter);
    }

    @Override
    public void loadContactsSuccess() {
        Toast.makeText(getContext(), getString(R.string.load_contacts_success), Toast.LENGTH_SHORT).show();
        mContactsAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private comemo.example.yls.qqdemo.widget.SlideBar.OnSlideChangeLister onSlideChangeLister = new SlideBar.OnSlideChangeLister() {
        @Override
        public void onSlideChange(String fristLetter) {
            tvTxt.setVisibility(View.VISIBLE);
            tvTxt.setText(fristLetter);
            for (int i = 0; i <mContactsPresenter.getDataList().size() ; i++) {
                 if(fristLetter.equals(mContactsPresenter.getDataList().get(i).getFirstLetter())){
                     mContactsRecyclerView.smoothScrollToPosition(i);
                     break;
                 }
            }

        }
        @Override
        public void onSlideFinshed() {
            tvTxt.setVisibility(View.GONE);
        }
    };

    @Override
    public void loadContactsFalied() {
        Toast.makeText(getContext(), getString(R.string.load_contacts_failed), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void delSuccess() {
        Toast.makeText(getContext(), getString(R.string.del_contacts_success), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void delFailed() {
        Toast.makeText(getContext(), getString(R.string.del_contacts_failed), Toast.LENGTH_SHORT).show();
    }


    @OnClick(R.id.add)
    public void onClick() {

        goTo(AddFriendActivity.class, false);
    }

    private OnLongClickItemListener onLongClickItemListener = new OnLongClickItemListener() {
        @Override
        public void del(String name) {
            showDialog(name);
        }
    };

    public void showDialog(final String name) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("温馨提示").setMessage("确定要删除好友:" + name).
                setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mContactsPresenter.del(name);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();

    }

    private EMContactListener emContactListener = new EMContactListener() {
        @Override
        public void onContactAdded(String s) {
            mContactsPresenter.refresh();

        }

        @Override
        public void onContactDeleted(final String s) {

            mContactsPresenter.refresh();


        }

        @Override
        public void onContactInvited(final String s, final String s1) {


        }

        @Override
        public void onContactAgreed(final String s) {


        }

        @Override
        public void onContactRefused(final String s) {

        }
    };


    @Override
    public void onDestroy() {
        EMClient.getInstance().contactManager().removeContactListener(emContactListener);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
