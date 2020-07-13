package com.fwing.compot.base.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.fwing.compot.R;
import com.fwing.compot.base.presenter.SimpleBasePresenter;
import com.fwing.compot.base.view.BaseView;
import com.fwing.compot.component.ActivityCollector;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * @Author: Fwing
 * @CreateDate: 2020/6/29 14:52
 */
public abstract class SimpleBaseActivity<T extends SimpleBasePresenter> extends SupportActivity implements BaseView {

    private Unbinder unBinder;

    protected SimpleBaseActivity mActivity;
    protected T mPresenter;

    private Dialog loadingDailog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mPresenter = initPresenter();
        unBinder = ButterKnife.bind(this);
        mActivity = this;
        ActivityCollector.getInstance().addActivity(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view=inflater.inflate(R.layout.dialog_loading_alert,null);
        loadingDailog=new Dialog(this);
        loadingDailog.setContentView(view);
        loadingDailog.setCancelable(false);
        loadingDailog.setCanceledOnTouchOutside(false);
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        onViewCreated();
        initEventAndData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(loadingDailog != null){
            loadingDailog.cancel();
            loadingDailog = null;
        }
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
        ActivityCollector.getInstance().removeActivity(this);
        if (unBinder != null && unBinder != Unbinder.EMPTY) {
            unBinder.unbind();
            unBinder = null;
        }
    }


    @Override
    public void showNormalView() {
        loadingDailog.dismiss();
    }

    @Override
    public void showLoadingView() {
        loadingDailog.show();
    }

    @Override
    public void showErrorView() {
        loadingDailog.dismiss();
        Toast.makeText(this,"很抱歉，请稍后再试",Toast.LENGTH_SHORT).show();
    }

    protected abstract void initEventAndData();

    protected abstract void onViewCreated();

    protected abstract int getLayoutId();

    protected abstract T initPresenter();
}
