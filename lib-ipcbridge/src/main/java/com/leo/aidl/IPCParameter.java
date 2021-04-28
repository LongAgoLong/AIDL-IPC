package com.leo.aidl;

import android.os.Parcel;
import android.os.Parcelable;

public class IPCParameter implements Parcelable {

    // 参数class类型
    private Class<?> type;
    // 参数值 序列化后的字符串
    private String value;
    // parameterType
    private Class<?> parameterType;


    public IPCParameter(Class<?> type, String value, Class<?> parameterType) {
        this.type = type;
        this.value = value;
        this.parameterType = parameterType;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Class<?> getParameterType() {
        return parameterType;
    }

    public void setParameterType(Class<?> parameterType) {
        this.parameterType = parameterType;
    }

    @Override
    public String toString() {
        return "IPCParameter{" +
                "type='" + type + '\'' +
                ", name='" + value + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.type);
        dest.writeString(this.value);
        dest.writeSerializable(this.parameterType);
    }

    public void readFromParcel(Parcel source) {
        this.type = (Class<?>) source.readSerializable();
        this.value = source.readString();
        this.parameterType = (Class<?>) source.readSerializable();
    }

    protected IPCParameter(Parcel in) {
        this.type = (Class<?>) in.readSerializable();
        this.value = in.readString();
        this.parameterType = (Class<?>) in.readSerializable();
    }

    public static final Creator<IPCParameter> CREATOR = new Creator<IPCParameter>() {
        @Override
        public IPCParameter createFromParcel(Parcel source) {
            return new IPCParameter(source);
        }

        @Override
        public IPCParameter[] newArray(int size) {
            return new IPCParameter[size];
        }
    };
}
