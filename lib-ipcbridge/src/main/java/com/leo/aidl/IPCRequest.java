package com.leo.aidl;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

public class IPCRequest implements Parcelable {

    // 服务端处理请求的类名
    private String interfacesName;
    // 服务端处理请求的方法名
    private String methodName;
    // 服务端处理请求的方法的参数
    private IPCParameter[] parameters;

    public IPCRequest() {
    }

    public IPCRequest(String interfacesName, String methodName, IPCParameter[] parameters) {
        this.interfacesName = interfacesName;
        this.methodName = methodName;
        this.parameters = parameters;
    }

    protected IPCRequest(Parcel in) {
        interfacesName = in.readString();
        methodName = in.readString();
        Parcelable[] parcelables = in.readParcelableArray(IPCParameter.class.getClassLoader());
        if (parcelables != null) {
            parameters = Arrays.copyOf(parcelables, parcelables.length, IPCParameter[].class);
        }
    }

    public static final Creator<IPCRequest> CREATOR = new Creator<IPCRequest>() {
        @Override
        public IPCRequest createFromParcel(Parcel in) {
            return new IPCRequest(in);
        }

        @Override
        public IPCRequest[] newArray(int size) {
            return new IPCRequest[size];
        }
    };

    public String getInterfacesName() {
        return interfacesName;
    }

    public void setInterfacesName(String interfacesName) {
        this.interfacesName = interfacesName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public IPCParameter[] getParameters() {
        return parameters;
    }

    public void setParameters(IPCParameter[] parameters) {
        this.parameters = parameters;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(interfacesName);
        dest.writeString(methodName);
        dest.writeParcelableArray(parameters, flags);
    }

    @Override
    public String toString() {
        return "IPCRequest{" +
                "className='" + interfacesName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", parameters=" + Arrays.toString(parameters) +
                '}';
    }
}
