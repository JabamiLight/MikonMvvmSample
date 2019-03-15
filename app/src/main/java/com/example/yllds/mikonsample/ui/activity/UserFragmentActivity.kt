package com.example.yllds.mikonsample.ui.activity

import android.os.Bundle
import com.example.yllds.mikonsample.R
import com.example.yllds.mikonsample.ui.fragment.UserFragment
import com.mikon.mvvmlibrary.base.SupportFragmentActivity

/**
 * Class:UserFragmentActivity
 * Author: JabamiLight
 * Description:
 * Date:  2019/3/12
 * 重庆锐云科技有限公司
 */
class UserFragmentActivity : SupportFragmentActivity() {


    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.nav_activity
    }

    override fun activityInit(savedInstanceState: Bundle?) {
        loadRootFragment(R.id.container,UserFragment())
    }

}
