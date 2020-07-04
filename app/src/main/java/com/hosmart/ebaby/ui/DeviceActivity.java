package com.hosmart.ebaby.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.hosmart.ebaby.R;
import com.hosmart.ebaby.base.BaseActivity;
import com.hosmart.ebaby.ui.apadter.DeviceAdapter;
import com.inuker.bluetooth.library.search.SearchResult;

import butterknife.BindView;
import butterknife.OnClick;

public class DeviceActivity extends BaseActivity {


    @BindView(R.id.rv_device)
    RecyclerView rvDevice;

    @BindView(R.id.rl_cancel)
    RelativeLayout rlBack;


    private DeviceAdapter adapter;


    @Override
    public int getLayoutId() {
        return R.layout.activity_device;
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }

    @Override
    public void initView() {
        if (BasebeaconList != null) {
            rvDevice.setLayoutManager(new LinearLayoutManager(DeviceActivity.this));
            adapter = new DeviceAdapter(BasebeaconList);
            rvDevice.setAdapter(adapter);
//            adapter.onItemClick(DeviceActivity.this);
        }
    }

    @OnClick({R.id.rl_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_cancel:
                finish();
                break;
        }
    }


}
