package com.example.yllds.mikonsample.app

import android.app.Activity
import com.example.yllds.mikonsample.di.component.DaggerAppManagerComponent
import com.mikon.mvvmlibrary.base.BaseApplication
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

/*
 * Created by TY on 2019/3/8.
 */
class MyApp : BaseApplication(), HasActivityInjector {

    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        DaggerAppManagerComponent.builder().appComponent(appComponent).build().inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity>? {
        return dispatchingActivityInjector
    }
}
