package com.leo.lib_interface.provider;

/**
 * 连接状态回调给Service
 */
public interface IBindStatusListener {

    void onBindStatus(boolean isSuccess);
}
