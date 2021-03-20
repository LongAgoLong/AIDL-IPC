package com.leo.aidl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.leo.aidl.data.DataManager;
import com.leo.aidl.service.ServiceManager;
import com.leo.lib_interface.bean.PoiBean;
import com.leo.lib_interface.client.IPoiListener;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        ServiceManager.getInstance().register(DataManager.getInstance());
    }

    private void initView() {
        Button button = findViewById(R.id.pushBtn);
        button.setOnClickListener(v -> {
            IPoiListener iPoiListener = ServiceManager.getInstance().get(IPoiListener.class);
            if (null == iPoiListener) {
                Log.e("LEO-TEST", "IPoiListener is NULL");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ServiceManager.getInstance().unRegister(DataManager.getInstance());
    }
}
