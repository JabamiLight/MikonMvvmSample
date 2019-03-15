package com.example.yllds.mikonsample.di.module

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yllds.mikonsample.mvvm.repository.entity.User
import com.example.yllds.mikonsample.ui.activity.UserActivity
import com.mikon.mvvmlibrary.di.scope.ActivityScope
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.Module
import dagger.Provides
import java.util.*

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
@Module
object UserModule {

    @ActivityScope
    @Provides
    @JvmStatic
    fun provideRxPermissions(mainActivity: UserActivity): RxPermissions {
        return RxPermissions(mainActivity)
    }

    /**
     *这里返回List需要用mutableList接受 @inject
     */
    @ActivityScope
    @Provides
    @JvmStatic

    fun provideUserList(): List<User> {
        return ArrayList<User>()
    }

    @ActivityScope
    @JvmStatic
    @Provides
    fun provideLayoutManager(mainActivity: UserActivity): RecyclerView.LayoutManager {
        return GridLayoutManager(mainActivity, 2)
    }


}
