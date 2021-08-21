package com.leo.aidl;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.leo.aidl.data.IpcLinkStatus;
import com.leo.aidl.service.IpcService;
import com.leo.aidl.util.IpcLog;
import com.leo.protocol.bean.PoiBean;
import com.leo.protocol.client.IDemoPoiListener;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "service-MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        Button button = findViewById(R.id.pushBtn);
        button.setOnClickListener(v -> {
            if (!IpcLinkStatus.getInstance().isInit()) {
                IpcLog.e(TAG, "Not Init.");
                return;
            }
            IDemoPoiListener iPoiListener = IpcService.getInstance().getClient(IDemoPoiListener.class);
            if (null == iPoiListener) {
                IpcLog.e(TAG, "IPoiListener is NULL");
                return;
            }
            ArrayList<PoiBean> poiBeans = new ArrayList<>();
            Random random = new Random(100);
            for (int i = 0; i < 5; i++) {
                PoiBean poiBean = new PoiBean(random.nextInt(), random.nextInt(), "测试" + i);
                poiBeans.add(poiBean);
            }
            iPoiListener.receivePoi(poiBeans);
        });
    }
}
