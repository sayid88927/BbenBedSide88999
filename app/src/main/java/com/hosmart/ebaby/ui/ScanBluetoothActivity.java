package com.hosmart.ebaby.ui;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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

import org.greenrobot.greendao.annotation.Convert;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
    private String strYear, strDay, strHours, strMinutes, strMonth;


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
//        scanDevice();

        Date date = TimeUtils.getNowTimeDate();

        Calendar c = Calendar.getInstance();
        intYear = c.get(Calendar.YEAR); // 获取当前年份
        intMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
        intDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
        intHours = c.get(Calendar.HOUR_OF_DAY);//时
        intMinutes = c.get(Calendar.MINUTE);//分

        strYear = addZeroForNum(Integer.toHexString(intYear), 2);
        strMonth = addZeroForNum(Integer.toHexString(intMonth), 2);
        strDay = addZeroForNum(Integer.toHexString(intDay), 2);
        strHours = addZeroForNum(Integer.toHexString(intHours), 2);
        strMinutes = addZeroForNum(Integer.toHexString(intMinutes), 2);

        BluetoothUtil.getClient().registerBluetoothStateListener(new BluetoothStateListener() {
            @Override
            public void onBluetoothStateChanged(boolean openOrClosed) {
                BluetoothLog.v(String.format("onBluetoothStateChanged %b", openOrClosed));
            }
        });

    }

    public static String addZeroForNum(String str, int strLength) {
        int strLen = str.length();
        if (strLen < strLength) {
            while (strLen < strLength) {
                StringBuffer sb = new StringBuffer();
                sb.append("0").append(str);//左补0  
//    sb.append(str).append("0");//右补0  
                str = sb.toString();
                strLen = str.length();
            }
        }

        return str;
    }


    public static byte[] stringToBytes(String text) {
        int len = text.length();
        byte[] bytes = new byte[(len + 1) / 2];
        for (int i = 0; i < len; i += 2) {
            int size = Math.min(2, len - i);
            String sub = text.substring(i, i + size);
            bytes[i / 2] = (byte) Integer.parseInt(sub, 16);
        }
        return bytes;
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
                .searchBluetoothLeDevice(5000, 2).build();
        BluetoothUtil.getClient().search(request, new SearchResponse() {
            @Override
            public void onSearchStarted() {
//                Logger.e("onSearchStarted");
                beaconList.clear();
            }

            @Override
            public void onDeviceFounded(SearchResult device) {
                Beacon beacon = new Beacon(device.scanRecord);
//                Logger.e(String.format("beacon for %s\n%s \n %s", device.getAddress(), beacon.toString(), device.getName()));
                if (device.getName().contains("HL-701")) {
                    if (!beaconList.contains(device)) {
                        beaconList.add(device);
                        Mac = device.getAddress();
                    }
                }
            }

            @Override
            public void onSearchStopped() {
//                Logger.e("onSearchStopped");
                if (beaconList.size() <= 0) {
                    llAgain.setVisibility(View.VISIBLE);
                    rlScan.setVisibility(View.GONE);
                } else {
                    rlScan.setVisibility(View.GONE);
                    rvDevice.setVisibility(View.VISIBLE);
                    rvDevice.setLayoutManager(new LinearLayoutManager(ScanBluetoothActivity.this));
                    adapter = new DeviceAdapter(beaconList);
                    rvDevice.setAdapter(adapter);
                    adapter.onItemClick(ScanBluetoothActivity.this);

                }
            }

            @Override
            public void onSearchCanceled() {
//                Logger.e("onSearchCanceled");
                scanDevice();
            }
        });
    }

    @Override
    public void onItemClick(final SearchResult item) {
        showLoadingDialog();
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
                    ToastUtils.showShortToast("连接成功");
                    searchResult = item;


//                    UUID = profile.getServices().get(0).getUUID();
                    registerConnectStatusListener();
                    notifyRsp();
//                    write();
//                    List<BleGattService> services = profile.getServices();

//                    for (BleGattService service : services) {
////                        items.add(new DetailItem(DetailItem.TYPE_SERVICE, service.getUUID(), null));
//                        service.getUUID();
//                        Logger.e("service.getUUID()  ==   "+ service.getUUID().toString());
//                        List<BleGattCharacter> characters = service.getCharacters();
//                        for (BleGattCharacter character : characters) {
////                            items.add(new DetailItem(DetailItem.TYPE_CHARACTER, character.getUuid(), service.getUUID()));
//                            Logger.e("character.getUuid() ==  "+ character.getUuid());
//                        }
//                    }

                    startActivityIn(new Intent(ScanBluetoothActivity.this, MainActivity.class), ScanBluetoothActivity.this);
                }
            }
        });
    }
}