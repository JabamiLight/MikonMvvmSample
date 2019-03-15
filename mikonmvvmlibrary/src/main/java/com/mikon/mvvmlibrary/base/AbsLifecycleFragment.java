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

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelStore;
import com.mikon.basiccomponent.toast.ToastUtils;
import com.mikon.mvvmlibrary.event.LiveBus;
import com.mikon.mvvmlibrary.mvvm.AbsViewModel;
import com.mikon.mvvmlibrary.resulthandler.handler.StateObserver;

import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class AbsLifecycleFragment<P extends AbsViewModel> extends BaseFragment {

    @Inject
    @Nullable
    protected P mViewModel;

    private List<Object> eventKeys = new ArrayList<>();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (mViewModel != null) {
            registerViewModel();
            registerStateObserver();
            dataObserver();
        }
    }

    private void registerViewModel() {
        try {
            ViewModelStore store = getViewModelStore();
            Method method = store.getClass().getDeclaredMethod("put", String.class, ViewModel.class);
            method.setAccessible(true);
            method.invoke(store, getClass().getCanonicalName() + mViewModel.getClass().getCanonicalName(), mViewModel);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
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
        mViewModel.loadState.observe(this, new StateObserver(this));
    }

    @Override
    public void toastError(String msg) {
        ToastUtils.show(getContext(), msg, ToastUtils.ERROR_TYPE);
    }

    @Override
    public void toastSuccess(String msg) {
        ToastUtils.show(getContext(), msg, ToastUtils.SUCCESS_TYPE);
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
    public void onDestroy() {
        super.onDestroy();
        if (eventKeys != null && eventKeys.size() > 0) {
            for (int i = 0; i < eventKeys.size(); i++) {
                LiveBus.getDefault().clear(eventKeys.get(i));
            }
        }
    }
}
