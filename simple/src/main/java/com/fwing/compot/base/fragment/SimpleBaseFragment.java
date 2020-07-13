package com.fwing.compot.base.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.fwing.compot.R;
import com.fwing.compot.base.presenter.SimpleBasePresenter;
import com.fwing.compot.base.view.BaseView;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * @Author: Fwing
 * @CreateDate: 2020/6/29 15:04
 */
public abstract class SimpleBaseFragment<T extends SimpleBasePresenter> extends SupportFragment implements BaseView {

    private Unbinder unBinder;
    protected T mPresenter;
    private Dialog loadingDailog;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mPresenter = initPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        unBinder = ButterKnife.bind(this, view);
        initView();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        View dialog=inflater.inflate(R.layout.dialog_loading_alert,null);
        loadingDailog=new Dialog(_mActivity);
        loadingDailog.setContentView(dialog);
        loadingDailog.setCancelable(false);
        loadingDailog.setCanceledOnTouchOutside(false);
        return view;
    }

    @Override
    public void onDestroyView() {
        if(loadingDailog != null){
            loadingDailog.cancel();
            loadingDailog = null;
        }
        if (mPresenter != null) {
            mPresenter.detachView();
        }

        super.onDestroyView();

        if (unBinder != null && unBinder != Unbinder.EMPTY) {
            unBinder.unbind();
            unBinder = null;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mPresenter != null) {
            mPresenter = null;
        }
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initEventAndData();
    }


    @Override
    public boolean onBackPressedSupport() {
        if (getChildFragmentManager().getBackStackEntryCount() > 1) {
            popChild();
        } else {
            _mActivity.finish();
        }
        return true;
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
        Toast.makeText(_mActivity,"很抱歉，请稍后再试",Toast.LENGTH_SHORT).show();
    }


    protected abstract void initView();

    /**
     * 获取当前Activity的UI布局
     *
     * @return 布局id
     */
    protected abstract int getLayoutId();

    /**
     * 初始化数据
     */
    protected abstract void initEventAndData();

    protected abstract T initPresenter();
}
