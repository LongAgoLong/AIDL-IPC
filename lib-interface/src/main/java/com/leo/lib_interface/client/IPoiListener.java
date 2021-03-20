package com.leo.lib_interface.client;

import com.leo.lib_interface.bean.PoiBean;

import java.util.List;

public interface IPoiListener {
    void receivePoi(List<PoiBean> list);
}
