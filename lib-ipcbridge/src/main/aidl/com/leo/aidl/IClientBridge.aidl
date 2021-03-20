// IClientBridge.aidl
package com.leo.aidl;

import com.leo.aidl.IPCResponse;
import com.leo.aidl.IPCRequest;
// Declare any non-default types here with import statements

interface IClientBridge {

	IPCResponse sendRequest(in IPCRequest request);
}