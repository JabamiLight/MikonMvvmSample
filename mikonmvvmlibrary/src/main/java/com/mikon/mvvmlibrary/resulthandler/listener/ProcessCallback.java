package com.mikon.mvvmlibrary.resulthandler.listener;

/*
 * Created by TY on 2019/3/10.
 *
 *
 */
public interface ProcessCallback<T> {
    /**
     */
    void onNext(T result);

    /**
     */
    void onError(String msg);

}
