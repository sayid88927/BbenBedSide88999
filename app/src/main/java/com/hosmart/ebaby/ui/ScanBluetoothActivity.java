package com.hosmart.ebaby.ui;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.blankj.utilcode.utils.ConvertUtils;
import com.blankj.utilcode.utils.TimeUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.hosmart.ebaby.R;
import com.hosmart.ebaby.base.BaseActivity;
import com.hosmart.ebaby.ui.apadter.DeviceAdapter;
import com.hosmart.ebaby.utils.BluetoothUtil;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.beacon.Beacon;
import com.inuker.bluetooth.library.connect.listener.BluetoothStateListener;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.model.BleGattCharacter;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.model.BleGattService;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.inuker.bluetooth.library.utils.BluetoothLog;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;

public class ScanBluetoothActivity extends BaseActivity implements DeviceAdapter.onItemClick {


    @BindView(R.id.rl_scan)
    RelativeLayout rlScan;

    @BindView(R.id.rv_device)
    RecyclerView rvDevice;

    @BindView(R.id.ll_again)
    LinearLayout llAgain;

    @BindView(R.id.btu_again)
    Button btuAgain;

    private DeviceAdapter adapter;
    private List<SearchResult> beaconList = new ArrayList<>();
    private SearchRequest request;
    private int intYear, intDay, intHours, intMinutes, intMonth;
    private String byteDate;


    @Override
    public int getLayoutId() {
        return R.layout.activity_scan_bluetooth;
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }


    @Override
    public void initView() {
        setSwipeBackEnable(false);
        scanDevice();

        BluetoothUtil.getClient().registerBluetoothStateListener(new BluetoothStateListener() {
            @Override
            public void onBluetoothStateChanged(boolean openOrClosed) {
                BluetoothLog.v(String.format("onBluetoothStateChanged %b", openOrClosed));
            }
        });

    }

    @OnClick({R.id.btu_again})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btu_again:
                scanDevice();
                break;
        }
    }

    private void scanDevice() {

        if (llAgain.getVisibility() == View.VISIBLE) {
            llAgain.setVisibility(View.GONE);
        }
        rlScan.setVisibility(View.VISIBLE);
        if (!BluetoothUtil.getClient().isBluetoothOpened()) {
            BluetoothUtil.getClient().openBluetooth();
        }
        SearchRequest request = new SearchRequest.Builder()
                .searchBluetoothLeDevice(2000, 1).build();
        BluetoothUtil.getClient().search(request, new SearchResponse() {
            @Override
            public void onSearchStarted() {
                beaconList.clear();
            }

            @Override
            public void onDeviceFounded(SearchResult device) {
                if (device.getName().contains("HL-701")) {
                    if (!beaconList.contains(device)) {
                        beaconList.add(device);
                        Mac = device.getAddress();
                    }
                }
            }

            @Override
            public void onSearchStopped() {
                if (beaconList.size() <= 0) {
                    llAgain.setVisibility(View.VISIBLE);
                    rlScan.setVisibility(View.GONE);
                } else {
                    rlScan.setVisibility(View.GONE);
                    rvDevice.setVisibility(View.VISIBLE);
                    rvDevice.setLayoutManager(new LinearLayoutManager(ScanBluetoothActivity.this));
                    adapter = new DeviceAdapter(beaconList);
                    rvDevice.setAdapter(adapter);
                    BasebeaconList = beaconList;
                    connect(beaconList.get(0));

//                adapter.onItemClick(ScanBluetoothActivity.this);

                }
            }

            @Override
            public void onSearchCanceled() {
                scanDevice();
            }
        });
    }



    private  void  connect(final SearchResult item){
        showLoadingDialog("Connecting....");
        BleConnectOptions options = new BleConnectOptions.Builder()
                .setConnectRetry(1)
                .setConnectTimeout(1000)
                .setServiceDiscoverRetry(1)
                .setServiceDiscoverTimeout(1000)
                .build();

        BluetoothUtil.getClient().connect(item.getAddress(), options, new BleConnectResponse() {
            @Override
            public void onResponse(int code, BleGattProfile profile) {
                dismissLoadingDialog();
                if (code == REQUEST_SUCCESS) {
                    searchResult = item;
                    registerConnectStatusListener();
                    notifyRsp();

                    Calendar c = Calendar.getInstance();
                    intYear = c.get(Calendar.YEAR);
                    intMonth = c.get(Calendar.MONTH) + 1;
                    intDay = c.get(Calendar.DAY_OF_MONTH);
                    intHours = c.get(Calendar.HOUR_OF_DAY);
                    intMinutes = c.get(Calendar.MINUTE);

                    byteDate = "0E" + "aa"+addZeroForNum(Integer.toHexString(intYear), 2) +
                            addZeroForNum(Integer.toHexString(intMonth), 2) +
                            addZeroForNum(Integer.toHexString(intDay), 2) +
                            addZeroForNum(Integer.toHexString(intHours), 2) +
                            addZeroForNum(Integer.toHexString(intMinutes), 2);

                    write(stringToBytes(byteDate));
                    startActivityIn(new Intent(ScanBluetoothActivity.this, MainActivity.class), ScanBluetoothActivity.this);
                    finish();
                }
            }
        });
    }

    @Override
    public void onItemClick(final SearchResult item) {
        showLoadingDialog("Connecting....");
        BleConnectOptions options = new BleConnectOptions.Builder()
                .setConnectRetry(3)
                .setConnectTimeout(20000)
                .setServiceDiscoverRetry(3)
                .setServiceDiscoverTimeout(10000)
                .build();

        BluetoothUtil.getClient().connect(item.getAddress(), options, new BleConnectResponse() {
            @Override
            public void onResponse(int code, BleGattProfile profile) {
                dismissLoadingDialog();
                if (code == REQUEST_SUCCESS) {
                    searchResult = item;
                    registerConnectStatusListener();
                    notifyRsp();

                    Calendar c = Calendar.getInstance();
                    intYear = c.get(Calendar.YEAR);
                    intMonth = c.get(Calendar.MONTH) + 1;
                    intDay = c.get(Calendar.DAY_OF_MONTH);
                    intHours = c.get(Calendar.HOUR_OF_DAY);
                    intMinutes = c.get(Calendar.MINUTE);

                    byteDate = "0E" + "aa"+addZeroForNum(Integer.toHexString(intYear), 2) +
                            addZeroForNum(Integer.toHexString(intMonth), 2) +
                            addZeroForNum(Integer.toHexString(intDay), 2) +
                            addZeroForNum(Integer.toHexString(intHours), 2) +
                            addZeroForNum(Integer.toHexString(intMinutes), 2);

                    write(stringToBytes(byteDate));
                    startActivityIn(new Intent(ScanBluetoothActivity.this, MainActivity.class), ScanBluetoothActivity.this);
                    finish();
                }
            }
        });
    }
}
