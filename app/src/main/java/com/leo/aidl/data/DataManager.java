package com.leo.aidl.data;

import com.leo.lib_interface.bean.DataBean;
import com.leo.lib_interface.provider.IDemoData;

public class DataManager implements IDemoData {

    private DataManager() {
    }

    @Override
    public DataBean getData(int i) {
        DataBean dataBean = new DataBean();
        dataBean.setName(getRandomName(i));
        dataBean.setTime(System.currentTimeMillis());
        return dataBean;
    }

    private static class SingleHold {
        public static final DataManager INSTANCE = new DataManager();
    }

    public static DataManager getInstance() {
        return SingleHold.INSTANCE;
    }

    private String getRandomName(int length) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(getRandomChar());
        }
        return builder.toString();
    }

    private char getRandomChar() {
        return (char) (0x4e00 + (int) (Math.random() * (0x9fa5 - 0x4e00 + 1)));
    }
}
