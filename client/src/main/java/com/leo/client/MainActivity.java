package com.leo.client;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.leo.aidl.client.ClientManager;
import com.leo.aidl.client.IPCBridge;
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

        ClientManager.getInstance().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IPCBridge.getInstance().unbindService(this);
        ClientManager.getInstance().unRegister(this);
    }

    private void initView() {
        findViewById(R.id.bindBtn).setOnClickListener(this);
        findViewById(R.id.unbindBtn).setOnClickListener(this);
        findViewById(R.id.sendRequestBtn).setOnClickListener(this);
        findViewById(R.id.clearBtn).setOnClickListener(this);
        mResultTv = findViewById(R.id.resultTv);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clearBtn:
                mResultTv.setText(null);
                break;
            case R.id.bindBtn:
                IPCBridge.getInstance().init(MainActivity.this);
                mResultTv.append("绑定服务\n");
                break;
            case R.id.unbindBtn:
                IPCBridge.getInstance().unbindService(MainActivity.this);
                mResultTv.append("解除绑定\n");
                break;
            case R.id.sendRequestBtn:
                IData iData = ClientManager.getInstance().get(IData.class);
                if (null == iData) {
                    Log.e("LEO-TEST", "iData is NULL");
                    return;
                }
                DataBean data = iData.getData();
                mResultTv.append(data.toString() + "\n");
                break;
        }
    }

    @Override
    public void receivePoi(List<PoiBean> list) {
        mResultTv.append(list.toString() + "\n");
    }
}
