package com.pycoj.service.abstracts;

/**
 * Created by Heyman on 2017/5/16.
 * 对应增删改查业务
 */
public abstract class AbstractService<T> {
    protected abstract boolean save(T object);
    protected abstract boolean delete(int id);
    protected abstract boolean list();
    protected abstract boolean update(T object);
}
