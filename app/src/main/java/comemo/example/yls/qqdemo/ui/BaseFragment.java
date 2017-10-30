package comemo.example.yls.qqdemo.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import butterknife.ButterKnife;
import comemo.example.yls.qqdemo.R;

/**
 * Created by yls on 2016/12/28.
 */

public abstract class BaseFragment extends Fragment {
    View root;
    private Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (root==null){
            root=inflater.inflate(getLayoutResId(),null);
            ButterKnife.bind(this,root);
            init();
            //Toast.makeText(getActivity(), "init.....", Toast.LENGTH_SHORT).show();
        }
        initTitle();
        return root;

    }

    protected void initTitle() {

    }

    public abstract int getLayoutResId();
    protected  void init(){

    }
    public void goTo(Class activity){
        Intent intent = new Intent(getContext(), activity);
        startActivity(intent);
        getActivity().finish();
    }
    public void goTo(Class activity,boolean isFinished){
        Intent intent = new Intent(getContext(), activity);
        startActivity(intent);
        if(isFinished){
            getActivity().finish();
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected void showLoadingDialog() {
        if (dialog==null) {
            dialog = new Dialog(getActivity(), R.style.Theme_LoadDialog);
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_loading, null);
            dialog.setContentView(view);
        }
        dialog.show();
    }

    protected void dimissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

}
