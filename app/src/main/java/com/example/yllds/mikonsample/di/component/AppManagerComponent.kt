package com.example.yllds.mikonsample.di.component

import com.example.yllds.mikonsample.app.MyApp
import com.example.yllds.mikonsample.di.module.ActivityBindModule
import com.mikon.mvvmlibrary.di.component.AppComponent
import com.mikon.mvvmlibrary.di.scope.WholeActivityScope
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule

/*
* Created by TY on 2019/3/8.
*      
*/
@WholeActivityScope
@Component(modules = [ActivityBindModule::class,AndroidSupportInjectionModule::class, AndroidInjectionModule::class],dependencies = [AppComponent::class])
interface AppManagerComponent {
    fun inject(app: MyApp)
}



