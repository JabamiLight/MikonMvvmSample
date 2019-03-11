/**
 * Copyright 2017 JessYan
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mikon.mvvmlibrary.resulthandler;

import android.net.ParseException;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.mikon.mvvmlibrary.integration.AppManager;
import com.mikon.mvvmlibrary.resulthandler.handler.ProgressDialogHandler;
import com.mikon.mvvmlibrary.resulthandler.listener.ProcessCallback;
import com.mikon.mvvmlibrary.resulthandler.listener.ProgressCancelListener;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import org.json.JSONException;
import retrofit2.HttpException;
import timber.log.Timber;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * ================================================
 * Created by JessYan on 9/2/2016 14:41
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public  class ProgressHandleSubscriber<T> implements Observer<T>, ProgressCancelListener {

    private  ProcessCallback<T> callback;
    private Disposable disposable;
    private boolean isUseCache = false;
    private boolean isCancelAble=false;
    private ProgressDialogHandler mProgressDialogHandler;

    public void setDisposable(Disposable disposable) {
        this.disposable = disposable;
    }

    public void setUseCache(boolean useCache) {
        isUseCache = useCache;
    }

    public void setCancelAble(boolean cancelAble) {
        isCancelAble = cancelAble;
    }


    public ProgressHandleSubscriber() {
        init();
    }

    private void init() {
        mProgressDialogHandler = new ProgressDialogHandler(AppManager.getAppManager().getTopActivity(), this, true);
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        this.disposable = d;

    }

    @Override
    public void onNext(T t) {
        callback.onNext(t);
    }


    @Override
    public void onComplete() {

    }


    @Override
    public void onError(@NonNull Throwable t) {
        t.printStackTrace();
        if (callback != null) {
            callback.onError(handleResponseError(t));
        }
    }

    @Override
    public void onCancelProgress() {
        if (disposable!=null&&!disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public String handleResponseError(Throwable t) {
        Timber.tag("Catch-Error").w(t.getMessage());
        //这里不光只能打印错误, 还可以根据不同的错误做出不同的逻辑处理
        //这里只是对几个常用错误进行简单的处理, 展示这个类的用法, 在实际开发中请您自行对更多错误进行更严谨的处理
        String msg = "未知错误";
        if (t instanceof UnknownHostException) {
            msg = "网络不可用";
        } else if (t instanceof SocketTimeoutException) {
            msg = "请求网络超时";
        } else if (t instanceof HttpException) {
            HttpException httpException = (HttpException) t;
            msg = convertStatusCode(httpException);
        } else if (t instanceof JsonParseException || t instanceof ParseException || t instanceof JSONException || t instanceof JsonIOException) {
            msg = "数据解析错误";
        }
        return msg;

    }

    private String convertStatusCode(HttpException httpException) {
        String msg;
        if (httpException.code() == 500) {
            msg = "服务器发生错误";
        } else if (httpException.code() == 404) {
            msg = "请求地址不存在";
        } else if (httpException.code() == 403) {
            msg = "请求被服务器拒绝";
        } else if (httpException.code() == 307) {
            msg = "请求被重定向到其他页面";
        } else {
            msg = httpException.message();
        }
        return msg;
    }


    public static class Builder<T>{
        private boolean isUseCache ;
        private boolean isCancelAble;
        private  ProcessCallback<T> callback;

        public Builder setUseCache(boolean useCache) {
            isUseCache = useCache;
            return this;
        }

        public Builder<T> setCancelAble(boolean cancelAble) {
            isCancelAble = cancelAble;
            return this;
        }

        public Builder<T> setCallback(ProcessCallback<T> callback) {
            this.callback = callback;
            return this;
        }

        public ProgressHandleSubscriber<T> build(ProcessCallback callback){
            ProgressHandleSubscriber subscriber=new ProgressHandleSubscriber();
            subscriber.isCancelAble=isCancelAble;
            subscriber.isUseCache=isUseCache;
            subscriber.callback=callback;
            return  subscriber;
        }


    }
}

