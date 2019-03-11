package com.mikon.mvvmlibrary.event;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/*
 * Created by TY on 2019/3/9.
 *
 *
 */
public class LoadStateEvent {
    //整体状态码
    public static final int STATE_ERROR_VIEW = -1;
    public static final int STATE_LOADING = 0;
    public static final int STATE_SUCCESS = 1;
    public static final int STATE_ERROR = 2;
    public static final int STATE_EMPTY = 3;
    //上拉下拉加载状态码
    public static final int STATE_LOADING_MORE = 4;
    public static final int STATE_LOADING_REFRESH = 5;
    public static final int STATE_LOADING_COMPLETE = 6;


    public LoadStateEvent(int state, String msg) {
        this.state = state;
        this.msg = msg;
    }

    @State
    public int state;
    public String msg;

    @IntDef({STATE_EMPTY, STATE_ERROR, STATE_LOADING, STATE_ERROR_VIEW, STATE_SUCCESS, STATE_LOADING_MORE, STATE_LOADING_REFRESH, STATE_LOADING_COMPLETE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
    }
}
