package com.hosmart.ebaby.utils;

import com.hosmart.ebaby.base.BaseApplication;
import com.inuker.bluetooth.library.BluetoothClient;

public class BluetoothUtil {

    private static BluetoothClient mClient;

    public static BluetoothClient getClient() {
        if (mClient == null) {
            synchronized (BluetoothUtil.class) {
                if (mClient == null) {
                    mClient = new BluetoothClient(BaseApplication.getContext());
                }
            }
        }
        return mClient;
    }

}
