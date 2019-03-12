package com.example.yllds.mikonsample.ui.activity

import android.os.Bundle
import android.support.v4.app.Fragment
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
        return 0
    }

    override fun activityInit(savedInstanceState: Bundle?) {
        try {
            val intent = intent
            val fragmentClazz = intent
                .getStringExtra("fragment")
            var fragment: Fragment? = null
            fragment = Class.forName(fragmentClazz)
                .newInstance() as Fragment
            fragment.arguments = intent.extras
            supportFragmentManager.beginTransaction()
                .add(android.R.id.content, fragment).commitAllowingStateLoss()

        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }

    }
}
