package com.example.yllds.mikonsample.mvvm.viewmodel

import android.app.Application
import com.example.yllds.mikonsample.mvvm.repository.UserRepository
import com.example.yllds.mikonsample.mvvm.repository.entity.User
import com.mikon.mvvmlibrary.event.LoadStateEvent.*
import com.mikon.mvvmlibrary.mvvm.AbsViewModel
import com.mikon.mvvmlibrary.resulthandler.listener.ProcessCallback
import com.mikon.mvvmlibrary.resulthandler.subscriber.ProgressHandleSubscriber
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.jessyan.rxerrorhandler.handler.RetryWithDelayOfFlowable
import javax.inject.Inject


/*
*/
class UserViewModel @Inject constructor(app: Application) :
    AbsViewModel<UserRepository>(app) {
    private var lastUserId = 1
    private var isFirst: Boolean = true


    fun requestUsers(pullToRefresh: Boolean) {
        if (pullToRefresh) lastUserId = 1//下拉刷新默认只请求第一页

        //关于RxCache缓存库的使用请参考 http://www.jianshu.com/p/b58ef6b0624b

        var isEvictCache = pullToRefresh//是否驱逐缓存,为ture即不使用缓存,每次下拉刷新即需要最新数据,则不使用缓存

        if (pullToRefresh && isFirst) {//默认在第一次下拉刷新时使用缓存
            isFirst = false
            isEvictCache = false
        }

        addDisposable(
            mRepository.getUsers(lastUserId, isEvictCache)
                .subscribeOn(Schedulers.io())
                .retryWhen(RetryWithDelayOfFlowable(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe { _ ->
                    if (pullToRefresh)
                        showPageState(STATE_LOADING)
                }.subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally {
                    if (pullToRefresh) {
                        showPageState(STATE_LOADING_REFRESH_COMPLETE)
                    } else {
                        showPageState(STATE_LOADING_MORE_COMPLETE)
                    }
                }
                .subscribeWith(ProgressHandleSubscriber(object : ProcessCallback<List<User>> {
                    override fun onError(msg: String?) {
                        if (pullToRefresh) {
                            showPageState(STATE_LOADING_REFRESH_ERROR)
                        } else {
                            showPageState(STATE_LOADING_MORE_ERROR)
                        }
                    }

                    override fun onNext(users: List<User>?) {
                        lastUserId = users!!.get(users.size - 1).getId();//记录最后一个id,用于下一次请求
                        processData(users)
                    }
                }))
        )


    }


}