package com.example.yllds.mikonsample.di.module

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.yllds.mikonsample.mvvm.repository.entity.User
import com.example.yllds.mikonsample.ui.fragment.UserFragment
import com.mikon.mvvmlibrary.di.scope.FragmentScope
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.Module
import dagger.Provides
import java.util.*

/*
 * Created by TY on 2019/3/8.
 */
@Module
object UserFragmentModule {

    @FragmentScope
    @JvmStatic
    @Provides
    fun provideRxPermissions(userFragment: UserFragment): RxPermissions {
        return RxPermissions(userFragment)
    }

    @FragmentScope
    @JvmStatic
    @Provides
    fun provideUserList(): List<User> {
        return ArrayList<User>()
    }

    @FragmentScope
    @JvmStatic
    @Provides
    fun provideLayoutManager(userFragment: UserFragment): RecyclerView.LayoutManager {
        return GridLayoutManager(userFragment.context, 2)
    }

}
