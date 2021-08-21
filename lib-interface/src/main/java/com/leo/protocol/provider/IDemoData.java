package com.leo.protocol.provider;

import com.leo.protocol.BaseListener;
import com.leo.protocol.bean.DataBean;

/**
 * demo
 */
public interface IDemoData extends BaseListener {
    DataBean getData(int length);
}
