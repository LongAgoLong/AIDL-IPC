package com.leo.protocol.client;

import com.leo.protocol.BaseListener;
import com.leo.protocol.bean.PoiBean;

import java.util.List;

/**
 * demo
 */
public interface IDemoPoiListener extends BaseListener {
    void receivePoi(List<PoiBean> list);
}
