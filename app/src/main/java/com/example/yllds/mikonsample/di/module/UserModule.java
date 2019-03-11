package com.example.yllds.mikonsample.di.module;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.example.yllds.mikonsample.mvvm.repository.UserRepository;
import com.example.yllds.mikonsample.mvvm.repository.entity.User;
import com.example.yllds.mikonsample.mvvm.viewmodel.UserViewModel;
import com.example.yllds.mikonsample.ui.activity.UserActivity;
import com.example.yllds.mikonsample.ui.adapter.UserAdapter;
import com.mikon.mvvmlibrary.di.scope.ActivityScope;
import com.tbruyelle.rxpermissions2.RxPermissions;
import dagger.Module;
import dagger.Provides;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by TY on 2019/3/8.
 *
 *
 *
 *          ┌─┐       ┌─┐
 *       ┌──┘ ┴───────┘ ┴──┐
 *       │                 │
 *       │       ───       │
 *       │  ─┬┘       └┬─  │
 *       │                 │
 *       │       ─┴─       │
 *       │                 │
 *       └───┐         ┌───┘
 *           │         │
 *           │         │
 *           │         │
 *           │         └──────────────┐
 *           │                        │
 *           │                        ├─┐
 *           │                        ┌─┘
 *           │                        │
 *           └─┐  ┐  ┌───────┬──┐  ┌──┘
 *             │ ─┤ ─┤       │ ─┤ ─┤
 *             └──┴──┘       └──┴──┘
 *                 神兽保佑
 *                 代码无BUG!
 */
@Module
public class UserModule {

    @ActivityScope
    @Provides
    static RxPermissions provideRxPermissions(UserActivity mainActivity) {
        return new RxPermissions(mainActivity);
    }

    @ActivityScope
    @Provides
    static UserViewModel provideUserViewModel(UserActivity mainActivity, UserRepository repository, RxErrorHandler errorHandler) {
        return (UserViewModel) ViewModelProviders.of(mainActivity).get(UserViewModel.class).bindRepository(repository).bindErrorHandler(errorHandler);
    }

    @ActivityScope
    @Provides
    static List<User> provideUserList() {
        return new ArrayList<User>();
    }

    @ActivityScope
    @Provides
    static RecyclerView.LayoutManager provideLayoutManager(UserActivity mainActivity) {
        return new GridLayoutManager(mainActivity, 2);
    }

    @ActivityScope
    @Provides
    static RecyclerView.Adapter<?> provideUserAdapter(List<User> list) {
        return new UserAdapter(list);
    }

}
