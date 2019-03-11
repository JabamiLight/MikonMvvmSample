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
package com.example.yllds.mikonsample.ui.adapter;

import android.widget.ImageView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.yllds.mikonsample.R;
import com.example.yllds.mikonsample.mvvm.repository.entity.User;
import com.mikon.imageloader.glide.ImageConfigImpl;
import com.mikon.mvvmlibrary.http.imageloader.ImageLoader;

import javax.inject.Inject;
import java.util.List;


/**
 * ================================================
 * <p>
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class UserAdapter extends BaseQuickAdapter<User, BaseViewHolder> {

    private final ImageLoader mImageLoader;

    @Inject
    public UserAdapter(List<User> infos, ImageLoader imageLoader) {
        super(R.layout.recycle_list, infos);
        this.mImageLoader = imageLoader;
    }


    @Override
    protected void convert(BaseViewHolder helper, User data) {

        helper.setText(R.id.tv_name, data.getLogin());
        ImageView mAvatar = helper.getView(R.id.iv_avatar);
        //itemView 的 Context 就是 Activity, Glide 会自动处理并和该 Activity 的生命周期绑定
        mImageLoader.loadImage(helper.itemView.getContext(),
                ImageConfigImpl
                        .builder()
                        .url(data.getAvatarUrl())
                        .imageView(mAvatar)
                        .build());

    }


}
