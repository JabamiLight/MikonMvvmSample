/*
 * Copyright 2017 JessYan
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
package com.example.yllds.mikonsample.ui.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.yllds.mikonsample.R
import com.example.yllds.mikonsample.mvvm.repository.entity.User
import com.mikon.imageloader.glide.ImageConfigImpl
import com.mikon.mvvmlibrary.http.imageloader.ImageLoader

import javax.inject.Inject


/**
 * ================================================
 *
 *
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
 */
class UserAdapter @Inject
constructor(infos: MutableList<User>) :
    BaseQuickAdapter<User, BaseViewHolder>(R.layout.recycle_list, infos) {

    @Inject
    lateinit var mImageLoader: ImageLoader

    override fun convert(helper: BaseViewHolder, data: User) {

        helper.setText(R.id.tv_name, data.login)
        val mAvatar = helper.getView<ImageView>(R.id.iv_avatar)
        //itemView 的 Context 就是 Activity, Glide 会自动处理并和该 Activity 的生命周期绑定
        mImageLoader.loadImage(
            helper.itemView.context,
            ImageConfigImpl
                .builder()
                .url(data.avatarUrl)
                .imageView(mAvatar)
                .build()
        )

    }


}
