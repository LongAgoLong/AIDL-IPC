package com.leo.aidl;

import android.os.Parcel;
import android.os.Parcelable;

public class IPCResponse implements Parcelable {

    /**
     * 方法返回值序列化的结果
     */
    private String result;
    /**
     * 是否执行成功
     */
    private boolean success;

    public IPCResponse(String result, boolean success) {
        this.result = result;
        this.success = success;
    }

    protected IPCResponse(Parcel in) {
        result = in.readString();
        success = in.readByte() != 0;
    }

    public static final Creator<IPCResponse> CREATOR = new Creator<IPCResponse>() {
        @Override
        public IPCResponse createFromParcel(Parcel in) {
            return new IPCResponse(in);
        }

        @Override
        public IPCResponse[] newArray(int size) {
            return new IPCResponse[size];
        }
    };

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(result);
        dest.writeByte((byte) (success ? 1 : 0));
    }

    @Override
    public String toString() {
        return "IPCResponse{" +
                "result='" + result + '\'' +
                ", success=" + success +
                '}';
    }
}
