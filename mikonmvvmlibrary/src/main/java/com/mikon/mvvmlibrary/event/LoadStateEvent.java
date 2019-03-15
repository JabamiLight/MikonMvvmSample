package com.mikon.mvvmlibrary.event;

import androidx.annotation.IntDef;

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
    public static final int STATE_LOADING_MORE_COMPLETE = 5;
    public static final int STATE_LOADING_MORE_ERROR = 6;
    public static final int STATE_LOADING_REFRESH = 7;
    public static final int STATE_LOADING_REFRESH_COMPLETE = 8;
    public static final int STATE_LOADING_REFRESH_ERROR = 9;
    public static final int STATE_LOADING_COMPLETE = 10;


    public LoadStateEvent(int state, String msg) {
        this.state = state;
        this.msg = msg;
    }

    @State
    public int state;
    public String msg;

    @IntDef({STATE_EMPTY, STATE_ERROR, STATE_LOADING, STATE_ERROR_VIEW, STATE_SUCCESS,
            STATE_LOADING_MORE, STATE_LOADING_MORE_COMPLETE, STATE_LOADING_MORE_ERROR,
            STATE_LOADING_REFRESH_COMPLETE, STATE_LOADING_REFRESH_ERROR, STATE_LOADING_REFRESH,
            STATE_LOADING_COMPLETE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
    }
}
