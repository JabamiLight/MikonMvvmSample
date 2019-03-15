package com.mikon.mvvmlibrary.resulthandler.handler;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import com.mikon.mvvmlibrary.base.delegate.IState;
import com.mikon.mvvmlibrary.event.LoadStateEvent;

import static com.mikon.mvvmlibrary.event.LoadStateEvent.*;

/**
 * Class:StateObserver
 * Author: JabamiLight
 * Description:
 * Date:  2019/3/12
 * 重庆锐云科技有限公司
 */
public class StateObserver implements Observer<LoadStateEvent> {

    IState loadState;

    public StateObserver(IState state) {
        this.loadState = state;
    }


    @Override
    public void onChanged(@Nullable LoadStateEvent state) {

        switch (state.state) {
            case STATE_ERROR_VIEW:
                loadState.showErrorView(state.msg);
                break;
            case STATE_LOADING:
                loadState.showLoading();
                break;
            case STATE_SUCCESS:
                loadState.toastSuccess(state.msg);
                break;
            case STATE_ERROR:
                loadState.toastError(state.msg);
                break;
            case STATE_EMPTY:
                loadState.showEmpty();
                break;
            case STATE_LOADING_MORE:
                loadState.showLoadingMore();
                break;
            case STATE_LOADING_MORE_ERROR:
                loadState.loadingMoreError();
                break;
            case STATE_LOADING_MORE_COMPLETE:
                loadState.loadingMoreComplete();
                break;
            case STATE_LOADING_REFRESH:
                loadState.showRefresh();
            case STATE_LOADING_REFRESH_ERROR:
                loadState.refreshError();
            case STATE_LOADING_REFRESH_COMPLETE:
                loadState.refreshComplete();
                break;
            case STATE_LOADING_COMPLETE:
                loadState.showLoadingComplete();
                break;
        }

    }
}
