package com.example.yllds.mikonsample.ui.activity

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import com.example.yllds.mikonsample.R
import com.example.yllds.mikonsample.mvvm.repository.entity.User
import com.example.yllds.mikonsample.mvvm.viewmodel.UserViewModel
import com.mikon.mvvmlibrary.base.AbsLifecycleActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_user.*
import timber.log.Timber
import javax.inject.Inject

class UserActivity : AbsLifecycleActivity<UserViewModel>() {


    @Inject
    lateinit var mRxPermissions: RxPermissions
    @Inject
    lateinit var mUsers: MutableList<User>
    @Inject
    lateinit var mLayoutManager: RecyclerView.LayoutManager
    @Inject
    lateinit var mAdapter: RecyclerView.Adapter<*>

    private var isLoadingMore: Boolean = false

    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_user
    }

    override fun initData(savedInstanceState: Bundle?) {
        mViewModel?.requestUsers(mRxPermissions,true)
    }


    override fun showLoading() {
        swipeRefreshLayout.setRefreshing(true)
    }

    override fun showLoadingComplete() {
        swipeRefreshLayout.setRefreshing(false)
        isLoadingMore = false

    }

    override fun showLoadingMore() {

    }


    override fun dataObserver() {
        super.dataObserver()
        registerObservers(User::class.java).observe(this, Observer {
            Timber.d("数据")
        })
    }






}
