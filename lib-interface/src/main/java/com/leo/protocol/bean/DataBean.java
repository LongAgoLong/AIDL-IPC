package com.leo.protocol.bean;

import java.io.Serializable;

public class DataBean implements Serializable {
    private String name;
    private long time;

    public DataBean() {
    }

    public DataBean(String name, long time) {
        this.name = name;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "DataBean{" +
                "name='" + name + '\'' +
                ", time=" + time +
                '}';
    }
}
