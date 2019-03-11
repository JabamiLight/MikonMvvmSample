package com.mikon.mvvmlibrary.mvvm;


import com.mikon.mvvmlibrary.integration.IRepositoryManager;

/**
 * @author：tqzhang on 18/7/26 16:15
 */
public abstract class AbsRepository {

    protected IRepositoryManager mRepositoryManager;//用于管理网络请求层, 以及数据缓存层

    public AbsRepository(IRepositoryManager repositoryManager) {
        this.mRepositoryManager = repositoryManager;
    }

}
