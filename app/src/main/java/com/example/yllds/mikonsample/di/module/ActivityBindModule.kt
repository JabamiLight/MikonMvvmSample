package com.example.yllds.mikonsample.di.module

/*
 * Copyright (C) 2016 Johnny Shieh Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.example.yllds.mikonsample.ui.activity.UserActivity
import com.example.yllds.mikonsample.ui.activity.UserFragmentActivity
import com.mikon.mvvmlibrary.di.scope.ActivityScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * description

 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * *
 * @version 1.0
 */


@Module
abstract class ActivityBindModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [UserModule::class])
    abstract fun userActivityInjector(): UserActivity


    @ActivityScope
    @ContributesAndroidInjector(modules = [FragmentBindModule::class])
    abstract fun supportFragmentActivityInjector(): UserFragmentActivity

}
