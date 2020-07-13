package com.fwing.compot.base.presenter;

import com.fwing.compot.base.view.BaseView;

import io.reactivex.disposables.Disposable;

public interface AbstractPresenter<T extends BaseView> {

    /**
     * 注入View
     *
     * @param view view
     */
    void attachView(T view);

    /**
     * 回收View
     */
    void detachView();

}
