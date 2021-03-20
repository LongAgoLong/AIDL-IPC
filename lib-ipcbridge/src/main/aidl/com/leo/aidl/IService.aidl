// IService.aidl
package com.leo.aidl;

import com.leo.aidl.IPCResponse;
import com.leo.aidl.IPCRequest;
import com.leo.aidl.IClientBridge;
// Declare any non-default types here with import statements

interface IService {
    IPCResponse sendRequest(in IPCRequest request);

    void attach(in IClientBridge iClientBridge);
}
