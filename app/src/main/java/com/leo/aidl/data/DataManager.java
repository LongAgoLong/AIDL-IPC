package com.leo.aidl.data;

import com.leo.lib_interface.bean.DataBean;
import com.leo.lib_interface.provider.IData;

public class DataManager implements IData {

    private DataManager() {
    }

    private static class SingleHold {
        public static final DataManager INSTANCE = new DataManager();
    }

    public static DataManager getInstance() {
        return SingleHold.INSTANCE;
    }

    @Override
    public DataBean getData() {
        DataBean dataBean = new DataBean();
        dataBean.setName("随机测试");
        dataBean.setTime(System.currentTimeMillis());
        return dataBean;
    }
}
