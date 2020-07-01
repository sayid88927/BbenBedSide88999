
package com.hosmart.ebaby.presenter.contract.activity;


import com.hosmart.ebaby.base.BaseContract;

public interface MainContract {

    interface View extends BaseContract.BaseView {


    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
    }
}
