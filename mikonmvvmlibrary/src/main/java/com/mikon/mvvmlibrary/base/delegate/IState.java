package com.mikon.mvvmlibrary.base.delegate;

/**
 * Class:IState
 * Author: JabamiLight
 * Description:
 * Date:  2019/3/12
 * 重庆锐云科技有限公司
 */
public interface IState {
    default void toastError(String msg){

    }

    default void toastSuccess(String msg){

    }

    default void showLoading(){

    }

    default void showErrorView(String msg){

    }

    default void showLoadingMore(){

    }

    default void showRefresh(){

    }

    default void refreshComplete(){

    }

    default void refreshError(){

    }

    default void loadingMoreError(){

    }

    default void loadingMoreComplete(){

    }

    default void showEmpty(){

    }

    default void showLoadingComplete(){

    }
}
