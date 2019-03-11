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

/*
* Created by TY on 2019/3/8.
*      
*
*      
*          ┌─┐       ┌─┐
*       ┌──┘ ┴───────┘ ┴──┐
*       │                 │
*       │       ───       │
*       │  ─┬┘       └┬─  │
*       │                 │
*       │       ─┴─       │
*       │                 │
*       └───┐         ┌───┘
*           │         │
*           │         │
*           │         │
*           │         └──────────────┐
*           │                        │
*           │                        ├─┐
*           │                        ┌─┘    
*           │                        │
*           └─┐  ┐  ┌───────┬──┐  ┌──┘         
*             │ ─┤ ─┤       │ ─┤ ─┤         
*             └──┴──┘       └──┴──┘ 
*                 神兽保佑 
*                 代码无BUG! 
*/
class UserViewModel(app: Application) : AbsViewModel<UserRepository>(app) {


    private var isFirst: Boolean = false

    fun requestUsers(lastUserId: Int, pullToRefresh: Boolean) {
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
                    else
                        showPageState(STATE_LOADING)
                }.subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally {
                    showPageState(STATE_LOADING_COMPLETE)
                }
                .subscribeWith(ProgressHandleSubscriber(object : ProcessCallback<List<User>> {
                    override fun onError(msg: String?) {
                        showPageState(STATE_ERROR, msg)
                    }

                    override fun onNext(result: List<User>?) {
                        processData(result)
                    }
                }))

        )


    }

    override fun onCleared() {
        super.onCleared()
        unDisposable()
    }

}