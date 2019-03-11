package com.mikon.mvvmlibrary.mvvm;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import com.mikon.mvvmlibrary.event.LiveBus;
import com.mikon.mvvmlibrary.event.LoadStateEvent;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import java.util.List;

import static com.mikon.mvvmlibrary.event.LoadStateEvent.STATE_EMPTY;


/**
 * @author：tqzhang on 18/7/26 16:15
 */
public class AbsViewModel<T extends AbsRepository> extends AndroidViewModel {


    public T mRepository;
    public RxErrorHandler mErrorHandler;
    public MutableLiveData<LoadStateEvent> loadState = new MutableLiveData<>();
    private CompositeDisposable mCompositeDisposable;

    public AbsViewModel(@NonNull Application application) {
        super(application);
    }

    public AbsViewModel bindRepository(T mRepository) {
        if (this.mRepository == null) {
            this.mRepository = mRepository;
        }
        return this;
    }

    public AbsViewModel bindErrorHandler(RxErrorHandler errorHandler) {
        if (this.mErrorHandler == null) {
            this.mErrorHandler = errorHandler;
        }
        return this;
    }

    public boolean shouldShowEmpty() {
        return false;
    }

    protected void processData(Object obj) {
        if (obj instanceof List) {
            List list = (List) obj;
            if (list.isEmpty() && shouldShowEmpty()) {
                showPageState(STATE_EMPTY);
            } else {
                String className = list.get(0).getClass().getSimpleName();
                String key = getClass().getSimpleName().concat(className).concat("list");
                LiveBus.getDefault().postEvent(key, obj);
            }
        } else {
            String className = obj.getClass().getSimpleName();
            String key = getClass().getSimpleName().concat(className);
            LiveBus.getDefault().postEvent(key, obj);
        }

    }


    protected void showPageState(@LoadStateEvent.State int state) {
        showPageState(state, null);
    }

    protected void showPageState(@LoadStateEvent.State int state, String msg) {
        loadState.postValue(new LoadStateEvent(state, msg));
    }

    protected void addDisposable(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    public void unDisposable() {
        if (mCompositeDisposable != null && mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.clear();
        }
    }
}
