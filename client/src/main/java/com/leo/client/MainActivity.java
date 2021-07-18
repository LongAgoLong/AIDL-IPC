package com.leo.client;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.leo.aidl.client.IPCBridge;
import com.leo.aidl.util.XLog;
import com.leo.lib_interface.bean.DataBean;
import com.leo.lib_interface.bean.PoiBean;
import com.leo.lib_interface.client.IPoiListener;
import com.leo.lib_interface.provider.IData;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, IPoiListener {
    private TextView mResultTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        IPCBridge.getInstance().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IPCBridge.getInstance().unbindService(this);
        IPCBridge.getInstance().unRegister(this);
    }

    private void initView() {
        findViewById(R.id.sendRequestBtn).setOnClickListener(this);
        findViewById(R.id.clearBtn).setOnClickListener(this);
        mResultTv = findViewById(R.id.resultTv);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clearBtn:
                mResultTv.setText(null);
                break;
            case R.id.sendRequestBtn:
                if (!InitResult.getInstance().isInit()) {
                    XLog.e("LEO-TEST", "Not Init.");
                    return;
                }
                IData iData = IPCBridge.getInstance().get(IData.class);
                if (null == iData) {
                    XLog.e("LEO-TEST", "iData is NULL");
                    return;
                }
                DataBean data = iData.getData();
                if (data == null) {
                    XLog.e("LEO-TEST", "data is null.");
                    return;
                }
                mResultTv.append(data.toString() + "\n");
                break;
        }
    }

    @Override
    public void receivePoi(List<PoiBean> list) {
        mResultTv.append(list.toString() + "\n");
    }
}
