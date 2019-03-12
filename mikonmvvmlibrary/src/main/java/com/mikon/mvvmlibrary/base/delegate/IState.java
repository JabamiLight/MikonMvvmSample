package com.mikon.mvvmlibrary.base.delegate;

/**
 * Class:IState
 * Author: JabamiLight
 * Description:
 * Date:  2019/3/12
 * 重庆锐云科技有限公司
 */
public interface IState {
    void toastError(String msg);

    void toastSuccess(String msg);

    void showLoading();

    void showErrorView(String msg);

    void showLoadingMore();

    void showRefresh();

    void refreshComplete();

    void refreshError();

    void loadingMoreError();

    void loadingMoreComplete();

    void showEmpty();

    void showLoadingComplete();
}
