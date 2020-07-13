package com.fwing.compot.base.presenter;

import com.fwing.compot.base.view.BaseView;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


public class SimpleBasePresenter<T extends BaseView> implements AbstractPresenter<T> {

    protected T mView;
    private CompositeDisposable compositeDisposable;
    public SimpleBasePresenter() {

    }

    @Override
    public void attachView(T view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }

    protected void addSubscribe(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    protected void removeSubscribe(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.remove(disposable);
    }

}
