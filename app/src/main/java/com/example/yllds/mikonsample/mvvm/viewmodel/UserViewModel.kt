package com.example.yllds.mikonsample.mvvm.viewmodel

import android.app.Application
import com.example.yllds.mikonsample.mvvm.repository.UserRepository
import com.example.yllds.mikonsample.mvvm.repository.entity.User
import com.mikon.mvvmlibrary.event.LoadStateEvent
import com.mikon.mvvmlibrary.event.LoadStateEvent.STATE_LOADING_COMPLETE
import com.mikon.mvvmlibrary.mvvm.AbsViewModel
import com.mikon.mvvmlibrary.utils.PermissionUtil
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber
import me.jessyan.rxerrorhandler.handler.RetryWithDelay

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
class UserViewModel (app:Application): AbsViewModel<UserRepository>(app) {
    private var lastUserId = 1
    private var isFirst = true
    private var preEndIndex: Int = 0

    fun requestUsers(mRxPermissions:RxPermissions,pullToRefresh:Boolean){
        //请求外部存储权限用于适配android6.0的权限管理机制
        PermissionUtil.externalStorage(object : PermissionUtil.RequestPermission {
            override fun onRequestPermissionSuccess() {
                //request permission success, do something.
                requestFromRepository(pullToRefresh)
            }

            override fun onRequestPermissionFailure(permissions: List<String>) {
                showPageState(LoadStateEvent.STATE_ERROR,"Request permissions failure")
//                mRootView.hideLoading()//隐藏下拉刷新的进度条
            }

            override fun onRequestPermissionFailureWithAskNeverAgain(permissions: List<String>) {
                showPageState(LoadStateEvent.STATE_ERROR,"Need to go to the settings")
//                mRootView.hideLoading()//隐藏下拉刷新的进度条
            }
        }, mRxPermissions, mErrorHandler)

    }

    fun  requestFromRepository(pullToRefresh:Boolean){
        if (pullToRefresh) lastUserId = 1//下拉刷新默认只请求第一页

        //关于RxCache缓存库的使用请参考 http://www.jianshu.com/p/b58ef6b0624b
        var isEvictCache = pullToRefresh//是否驱逐缓存,为ture即不使用缓存,每次下拉刷新即需要最新数据,则不使用缓存

        if (pullToRefresh && isFirst) {//默认在第一次下拉刷新时使用缓存
            isFirst = false
            isEvictCache = false
        }

        mRepository.getUsers(lastUserId, isEvictCache)
            .subscribeOn(Schedulers.io())
            .retryWhen(RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
            .doOnSubscribe({ disposable ->
                addDisposable(disposable)
                if (pullToRefresh)
                    showPageState(LoadStateEvent.STATE_LOADING)
                else
                    showPageState(LoadStateEvent.STATE_LOADING)
            }).subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally{
                showPageState(STATE_LOADING_COMPLETE)
            }
            .subscribe(object : ErrorHandleSubscriber<List<User>>(mErrorHandler) {
                override fun onNext(users: List<User>) {
                    processData(users);
//                    lastUserId = users[users.size - 1].getId()//记录最后一个id,用于下一次请求
//                    if (pullToRefresh) mUsers.clear()//如果是下拉刷新则清空列表
//                    preEndIndex = mUsers.size//更新之前列表总长度,用于确定加载更多的起始位置
//                    mUsers.addAll(users)
//                    if (pullToRefresh)
//                        mAdapter.notifyDataSetChanged()
//                    else
//                        mAdapter.notifyItemRangeInserted(preEndIndex, users.size)
                }

                override fun onError(t: Throwable) {
                    super.onError(t)
                }
            })



    }

    override fun onCleared() {
        super.onCleared()
        unDisposable()
    }

}