/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mikon.mvvmlibrary.base;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mikon.basiccomponent.toast.ToastUtils;
import com.mikon.mvvmlibrary.base.delegate.IActivity;
import com.mikon.mvvmlibrary.di.component.AppComponent;
import com.mikon.mvvmlibrary.event.LiveBus;
import com.mikon.mvvmlibrary.event.LoadStateEvent;
import com.mikon.mvvmlibrary.integration.lifecycle.ActivityLifecycleable;
import com.mikon.mvvmlibrary.mvvm.AbsViewModel;
import dagger.android.AndroidInjection;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static com.mikon.mvvmlibrary.event.LoadStateEvent.*;

public abstract class AbsLifecycleActivity<P extends AbsViewModel> extends BaseActivity implements IActivity, ActivityLifecycleable {

    @Inject
    @Nullable
    protected P mViewModel;

    private List<Object> eventKeys = new ArrayList<>();

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        AndroidInjection.inject(this);
        if (mViewModel != null) {
            registerStateObserver();
            dataObserver();
        }
    }
    protected <T> MutableLiveData<T> registerObserver(Class<T> tClass) {
        String event = mViewModel.getClass().getSimpleName().concat(tClass.getSimpleName());
        eventKeys.add(event);
        return LiveBus.getDefault().subscribe(event);
    }
    protected <T> MutableLiveData<List<T>> registerObservers(Class<T> tClass) {
        String event = mViewModel.getClass().getSimpleName().concat(tClass.getSimpleName()).concat("list");
        eventKeys.add(event);
        return LiveBus.getDefault().subscribe(event);
    }

    //注册view视图
    private void registerStateObserver() {
        Observer observer = new Observer<LoadStateEvent>() {
            @Override
            public void onChanged(@Nullable LoadStateEvent state) {

                switch (state.state){
                    case STATE_ERROR_VIEW:
                        showErrorView(state.msg);
                        break;
                    case STATE_LOADING:
                        showLoading();
                        break;
                    case STATE_SUCCESS:
                        toastSuccess(state.msg);
                        break;
                    case STATE_ERROR:
                        toastError(state.msg);
                        break;
                    case STATE_EMPTY:
                        showEmpty();
                        break;
                    case STATE_LOADING_MORE:
                        showLoadingMore();
                        break;
                    case STATE_LOADING_REFRESH:
                        showRefresh();
                        break;
                    case STATE_LOADING_COMPLETE:
                        showLoadingComplete();
                        break;
                }
            }
        };
        mViewModel.loadState.observe(this,observer);
    }

    @Override
    public void toastError(String msg) {
        ToastUtils.show(this,msg,ToastUtils.ERROR_TYPE);
    }

    @Override
    public void toastSuccess(String msg) {
        ToastUtils.show(this,msg,ToastUtils.SUCCESS_TYPE);
    }

    @Override
    public void showErrorView(String msg) {

    }

    @Override
    public void showLoadingMore() {

    }

    @Override
    public void showRefresh() {

    }

    @Override
    public void showEmpty() {

    }
    @Override
    public void showLoading() {
    }

    @Override
    public void showLoadingComplete() {

    }

    protected void dataObserver() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (eventKeys != null && eventKeys.size() > 0) {
            for (int i = 0; i < eventKeys.size(); i++) {
                LiveBus.getDefault().clear(eventKeys.get(i));
            }
        }
    }
}
