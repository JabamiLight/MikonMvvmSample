package com.example.yllds.mikonsample.mvvm.repository

import com.example.yllds.mikonsample.mvvm.repository.api.cache.CommonCache
import com.example.yllds.mikonsample.mvvm.repository.api.service.UserService
import com.example.yllds.mikonsample.mvvm.repository.entity.User
import com.mikon.mvvmlibrary.integration.IRepositoryManager
import com.mikon.mvvmlibrary.mvvm.AbsRepository
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.annotations.NonNull
import io.reactivex.functions.Function
import io.rx_cache2.DynamicKey
import io.rx_cache2.EvictDynamicKey
import javax.inject.Inject

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
class UserRepository @Inject constructor(repositoryManager: IRepositoryManager) : AbsRepository(repositoryManager) {
    val USERS_PER_PAGE = 10


    fun getUsers(lastIdQueried: Int, update: Boolean): Observable<List<User>> {
        //使用rxcache缓存,上拉刷新则不读取缓存,加载更多读取缓存
        return Flowable.just(
            mRepositoryManager
                .obtainRetrofitService(UserService::class.java)
                .getUsers(lastIdQueried, USERS_PER_PAGE)
        )
            .flatMap(object : Function<Observable<List<User>>, ObservableSource<List<User>>> {
                @Throws(Exception::class)
                override fun apply(@NonNull listObservable: Observable<List<User>>): ObservableSource<List<User>> {
                    return mRepositoryManager.obtainCacheService(CommonCache::class.java)
                        .getUsers(listObservable, DynamicKey(lastIdQueried), EvictDynamicKey(update))
                        .map({ listReply -> listReply.getData() })
                }
            })

    }


}