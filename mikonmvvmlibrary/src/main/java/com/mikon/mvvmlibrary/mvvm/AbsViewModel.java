package com.mikon.mvvmlibrary.mvvm;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import com.mikon.mvvmlibrary.event.LiveBus;
import com.mikon.mvvmlibrary.event.LoadStateEvent;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import javax.inject.Inject;
import java.util.List;

import static com.mikon.mvvmlibrary.event.LoadStateEvent.STATE_EMPTY;


/**
 * @author：tqzhang on 18/7/26 16:15
 */
public class AbsViewModel<T extends AbsRepository> extends AndroidViewModel {

    @Inject
    public T mRepository;

    public MutableLiveData<LoadStateEvent> loadState = new MutableLiveData<>();
    private CompositeDisposable mCompositeDisposable;

    public AbsViewModel(@NonNull Application application) {
        super(application);
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

    @Override
    public void onCleared() {
        super.onCleared();
        unDisposable();
    }

}
