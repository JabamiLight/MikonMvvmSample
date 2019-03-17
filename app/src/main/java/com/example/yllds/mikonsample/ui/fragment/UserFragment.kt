package com.example.yllds.mikonsample.ui.fragment

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.yllds.mikonsample.R
import com.example.yllds.mikonsample.mvvm.repository.entity.User
import com.example.yllds.mikonsample.mvvm.viewmodel.UserViewModel
import com.example.yllds.mikonsample.ui.adapter.UserAdapter
import com.example.yllds.mikonsample.ui.view.CustomLoadMoreView
import com.mikon.mvvmlibrary.base.AbsLifecycleFragment
import com.mikon.mvvmlibrary.utils.ArmsUtils
import com.mikon.mvvmlibrary.utils.PermissionUtil
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_user.*
import me.jessyan.rxerrorhandler.core.RxErrorHandler
import javax.inject.Inject

/**
 * Class:UserFragment
 * Author: JabamiLight
 * Description:
 * Date:  2019/3/12
 * 重庆锐云科技有限公司

 */
class UserFragment : AbsLifecycleFragment<UserViewModel>() {

    @Inject
    lateinit var mRxPermissions: RxPermissions
    @Inject
    lateinit var mLayoutManager: RecyclerView.LayoutManager
    @Inject
    lateinit var mAdapter: UserAdapter
    @Inject
    lateinit var mErrorHandler: RxErrorHandler

    private var pullToRefresh: Boolean = true

    override fun getLayoutId(): Int {
        return R.layout.activity_user
    }

    override fun initData(savedInstanceState: Bundle?) {

        swipeRefreshLayout.setOnRefreshListener {
            pullToRefresh = true
            mViewModel?.requestUsers(true)
        }
        ArmsUtils.configRecyclerView(recyclerView,mLayoutManager)
        recyclerView.adapter = mAdapter
        mAdapter.setOnItemClickListener { adapter, view, position ->
            start(UserFragment())
        }
        //请求外部存储权限用于适配android6.0的权限管理机制
        PermissionUtil.externalStorage(object : PermissionUtil.RequestPermission {
            override fun onRequestPermissionSuccess() {
                //request permission success, do something.
                pullToRefresh = true
                mViewModel?.requestUsers(pullToRefresh)

            }

            override fun onRequestPermissionFailure(permissions: List<String>) {
                toastError("Request permissions failure")
            }

            override fun onRequestPermissionFailureWithAskNeverAgain(permissions: List<String>) {
                toastSuccess("Need to go to the settings")
            }
        }, mRxPermissions, mErrorHandler)

        initPage()
    }

    private fun initPage() {
        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener({
            pullToRefresh = false
            mViewModel?.requestUsers(pullToRefresh)
        }, recyclerView)
    }


    override fun showLoading() {
        mAdapter.setEnableLoadMore(false)//这里的作用是防止下拉刷新的时候还可以上拉加载
        swipeRefreshLayout.isRefreshing = true
    }

    override fun refreshComplete() {
        swipeRefreshLayout.isRefreshing = false
        mAdapter.setEnableLoadMore(true)
    }

    override fun refreshError() {
        swipeRefreshLayout.isRefreshing = false
        mAdapter.setEnableLoadMore(true)
        toastError("加载失败")
    }

    override fun loadingMoreComplete() {
        mAdapter.loadMoreComplete()
    }

    override fun loadingMoreError() {
        mAdapter.loadMoreFail()
    }


    override fun dataObserver() {
        registerObservers(User::class.java).observe(this, Observer { users ->
            if (pullToRefresh) {
                mAdapter.setNewData(users)
            } else {
                mAdapter.addData(users!!)
            }
        })
    }

}