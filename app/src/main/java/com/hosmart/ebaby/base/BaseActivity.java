package com.hosmart.ebaby.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

import com.blankj.utilcode.utils.ToastUtils;
import com.hosmart.ebaby.R;
import com.hosmart.ebaby.ui.MainActivity;
import com.hosmart.ebaby.utils.BluetoothUtil;
import com.hosmart.ebaby.view.SwipeBackActivity.SwipeBackActivity;
import com.hosmart.ebaby.view.SwipeBackActivity.SwipeBackLayout;
import com.hosmart.ebaby.view.dialog.CustomDialog;
import com.hosmart.ebaby.view.dialog.LoadingDialog;
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.utils.BluetoothLog;
import com.inuker.bluetooth.library.utils.ByteUtils;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;
import static com.inuker.bluetooth.library.Constants.STATUS_CONNECTED;

public abstract class BaseActivity extends SwipeBackActivity {

    public final static List<AppCompatActivity> mActivities = new LinkedList<>();
    private SwipeBackLayout mSwipeBackLayout;
    private Unbinder bind;
    private LoadingDialog loadingDialog;
    public static SearchResult searchResult = null;

    private boolean mConnected;
    public static java.util.UUID characterUUID = java.util.UUID.fromString("0000ae01-0000-1000-8000-00805f9b34fb");
    public static java.util.UUID characterNotifyUUID = java.util.UUID.fromString("0000ae02-0000-1000-8000-00805f9b34fb");

    public static UUID serviceUUID = UUID.fromString("0000ae00-0000-1000-8000-00805f9b34fb");
    public static String Mac;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(getLayoutId());

        bind = ButterKnife.bind(this);
        //沉浸式状态栏
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimaryDark), 10);
        attachView();
        initView();

        synchronized (mActivities) {
            mActivities.add(this);
        }

    }


    public void killAll() {
        // 复制了一份mActivities 集合
        List<AppCompatActivity> copy;
        synchronized (mActivities) {
            copy = new LinkedList<>(mActivities);
        }
        for (AppCompatActivity activity : copy) {
            activity.finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        synchronized (mActivities) {
            mActivities.remove(this);
        }
        if (bind != null)
            bind.unbind();
        detachView();
    }


    public void showLoadingDialog(String tip) {
        dismissLoadingDialog();
        LoadingDialog.Builder builder = new LoadingDialog.Builder(this)
                .setMessage(tip)
                .setCancelable(true);
        loadingDialog = builder.create();
        loadingDialog.show();

    }

    public void dismissLoadingDialog() {
        if (null != loadingDialog && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    @SuppressWarnings("deprecation")
    public void initSwipeBackLayout() {
        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
    }

    public void setEdgeTrackingEnabled(int size, int position) {
        if (size == 0) {
        }
        // 只有一个fragment  - 左右滑关闭
        else if (size == 1 && position == 0) {
            mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_ALL);
        }
        // 多个fragment  - 位于左侧尽头 - 只可左滑关闭
        else if (size != 1 && position == 0) {
            mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        }
        // 多个fragment  - 位于右侧尽头 - 只可右滑关闭
        else if (size != 1 && position == size - 1) {
            mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_RIGHT);
        }
        // 多个fragment  - 位于中间 - 屏蔽所有左右滑关闭事件
        else {
            mSwipeBackLayout.setEdgeTrackingEnabled(0);
        }
    }

    protected void finishActivity() {
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }

    public void startActivityIn(Intent intent, Activity curAct) {
        if (intent != null) {
            curAct.startActivity(intent);
            curAct.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
        }
    }

    @Override
    public void finish() {
        super.finish();
        finishActivity();
    }

    public void registerConnectStatusListener() {
        if (searchResult != null)
            BluetoothUtil.getClient().registerConnectStatusListener(searchResult.getAddress(), mConnectStatusListener);
    }

    public void notifyRsp() {
        BluetoothUtil.getClient().notify(Mac, serviceUUID, characterNotifyUUID, mNotifyRsp);
    }

    private final BleConnectStatusListener mConnectStatusListener = new BleConnectStatusListener() {
        @Override
        public void onConnectStatusChanged(String mac, int status) {
            BluetoothLog.v(String.format("DeviceDetailActivity onConnectStatusChanged %d in %s",
                    status, Thread.currentThread().getName()));
            mConnected = (status == STATUS_CONNECTED);
        }
    };

    private final BleNotifyResponse mNotifyRsp = new BleNotifyResponse() {
        @Override
        public void onResponse(int code) {
            if (code == REQUEST_SUCCESS) {
                Logger.e("success");
            } else {
                Logger.e("failed");
            }
        }

        @Override
        public void onNotify(UUID service, UUID character, byte[] value) {
            if (service.equals(serviceUUID) && character.equals(characterUUID)) {
                Logger.e(String.format("%s", ByteUtils.byteToString(value)));
            }
        }
    };

    public void write(byte[] bytes) {
        if (!BluetoothUtil.getClient().isBluetoothOpened()) {
            ToastUtils.showShortToast("Please open on Bluetooth");
        }

//        else if (!mConnected) {
//            ToastUtils.showShortToast("Please connect the device");
//        }
        else {
            Logger.e("發送數據  ===   " + bytesToHexStr(bytes));

            BluetoothUtil.getClient().write(Mac, serviceUUID, characterUUID,
                    bytes, mWriteRsp);
        }
    }

    private final BleWriteResponse mWriteRsp = new BleWriteResponse() {
        @Override
        public void onResponse(int code) {
            if (code == REQUEST_SUCCESS) {
                Logger.e(" 發送數據  ===   success");
            } else {
                Logger.e("  發送數據  ===   failed");
            }
        }
    };


    // byte[]转十六进制("BE B0 BC 92")
    public String bytesToHexStr(byte[] bytes) {

        String stmp;
        StringBuilder sb = new StringBuilder("");
        for (byte aByte : bytes) {
            stmp = Integer.toHexString(aByte & 0xFF);
            sb.append((stmp.length() == 1) ? "0" + stmp : stmp);
            sb.append(" ");
        }
        return sb.toString().toUpperCase().trim();
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


    public abstract int getLayoutId();

    public abstract void attachView();

    public abstract void detachView();

    public abstract void initView();

}
