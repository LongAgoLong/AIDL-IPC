package com.leo.aidl;

import android.os.Parcel;
import android.os.Parcelable;

public class IPCParameter implements Parcelable {

    /**
     * 参数值
     */
    private String value;
    /**
     * 参数类型
     */
    private Class<?> parameterType;

    public IPCParameter(String value, Class<?> parameterType) {
        this.value = value;
        this.parameterType = parameterType;
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
        return "{\"name\":" + value +
                ", \"parameterType=\":" + parameterType + "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.value);
        dest.writeSerializable(this.parameterType);
    }

    public void readFromParcel(Parcel source) {
        this.value = source.readString();
        this.parameterType = (Class<?>) source.readSerializable();
    }

    protected IPCParameter(Parcel in) {
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
