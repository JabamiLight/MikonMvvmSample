package com.example.yllds.mikonsample.ui.activity

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import com.example.yllds.mikonsample.R
import com.example.yllds.mikonsample.mvvm.repository.entity.User
import com.example.yllds.mikonsample.mvvm.viewmodel.UserViewModel
import com.example.yllds.mikonsample.ui.adapter.UserAdapter
import com.mikon.mvvmlibrary.base.AbsLifecycleActivity
import com.mikon.mvvmlibrary.utils.ArmsUtils
import com.mikon.mvvmlibrary.utils.PermissionUtil
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_user.*
import me.jessyan.rxerrorhandler.core.RxErrorHandler
import javax.inject.Inject

class UserActivity : AbsLifecycleActivity<UserViewModel>() {


    @Inject
    lateinit var mRxPermissions: RxPermissions
    @Inject
    lateinit var mUsers: MutableList<User>
    @Inject
    lateinit var mLayoutManager: RecyclerView.LayoutManager
    @Inject
    lateinit var mAdapter: UserAdapter
    @Inject
    lateinit var mErrorHandler: RxErrorHandler


    private var isLoadingMore: Boolean = false
    private var lastUserId: Int = -1
    private var pullToRefresh: Boolean = true
    private var isFirst = true
    private var preEndIndex: Int = 0


    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_user
    }


    override fun activityInit(savedInstanceState: Bundle?) {

        swipeRefreshLayout.setOnRefreshListener {
            pullToRefresh = true
            mViewModel?.requestUsers(lastUserId, pullToRefresh)
        }

        ArmsUtils.configRecyclerView(recyclerView, mLayoutManager)
        recyclerView.adapter = mAdapter
        //请求外部存储权限用于适配android6.0的权限管理机制
        PermissionUtil.externalStorage(object : PermissionUtil.RequestPermission {
            override fun onRequestPermissionSuccess() {
                //request permission success, do something.
                if (pullToRefresh) lastUserId = 1//下拉刷新默认只请求第一页
                mViewModel?.requestUsers(lastUserId, pullToRefresh)

            }

            override fun onRequestPermissionFailure(permissions: List<String>) {
                toastError("Request permissions failure")
            }

            override fun onRequestPermissionFailureWithAskNeverAgain(permissions: List<String>) {
                toastSuccess("Need to go to the settings")
            }
        }, mRxPermissions, mErrorHandler)


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
        registerObservers(User::class.java).observe(this, Observer { users ->
            lastUserId = users!![users.size - 1].getId()//记录最后一个id,用于下一次请求
            if (pullToRefresh) mUsers.clear()//如果是下拉刷新则清空列表
            preEndIndex = mUsers.size//更新之前列表总长度,用于确定加载更多的起始位置
            mUsers.addAll(users)
            if (pullToRefresh)
                mAdapter.notifyDataSetChanged()
            else
                mAdapter.notifyItemRangeInserted(preEndIndex, users.size)
        })
    }

}
