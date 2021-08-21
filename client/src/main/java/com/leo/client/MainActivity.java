package com.leo.client;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.leo.aidl.IpcDataCenter;
import com.leo.aidl.client.IpcClient;
import com.leo.aidl.util.IpcLog;
import com.leo.client.data.IpcLinkStatus;
import com.leo.protocol.bean.DataBean;
import com.leo.protocol.bean.PoiBean;
import com.leo.protocol.client.IDemoPoiListener;
import com.leo.protocol.provider.IDemoData;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, IDemoPoiListener {
    private static final String TAG = "client-MainActivity";
    private TextView mResultTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        IpcDataCenter.getInstance().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IpcClient.getInstance().unbindService(this);
        IpcDataCenter.getInstance().unRegister(this);
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
                if (!IpcLinkStatus.getInstance().isInit()) {
                    IpcLog.e(TAG, "Not Init.");
                    return;
                }
                IDemoData iData = IpcClient.getInstance().getService(IDemoData.class);
                if (null == iData) {
                    IpcLog.e(TAG, "iData is NULL");
                    return;
                }
                DataBean data = iData.getData(3);
                if (data == null) {
                    IpcLog.e(TAG, "data is null.");
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
