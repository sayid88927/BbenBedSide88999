package com.hosmart.ebaby.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 6.0动态权限检查
 * Created by THINK on 2017/12/27.
 */

public class Permission {
    /**
     * 6.0动态权限：
     */
    public static final String[] ps = new String[]{
             "android.permission.BLUETOOTH",
             "android.permission.BLUETOOTH_ADMIN",
              "android.permission.ACCESS_FINE_LOCATION",
              "android.permission.ACCESS_COARSE_LOCATION"};
    final public static int REQUEST_CODE_ASK_CALL_PHONE = 123;
    public static List<String> ss = new ArrayList<>();//未授权的权限

    /**
     * 6.0权限申请：如果没有权限先申请
     */
    public static void requestPermission(Activity context) {

        if (Build.VERSION.SDK_INT >= 23) {
            for (String s : ps) {
                int checkCallPhonePermission = ContextCompat.checkSelfPermission(context, s);
                if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                    ss.add(s);
                }
            }
            if (ss.size() == 0) return;
            String[] strings = new String[ss.size()];
            for (int i = 0; i < strings.length; i++) {
                strings[i] = ss.get(i);
            }
            ActivityCompat.requestPermissions(context, strings, REQUEST_CODE_ASK_CALL_PHONE);
        }
    }

    /**
     * 6.0权限检测：如果没有就向跳转到系统设置
     *
     * @param context:Context
     * @param s:权限            return true：已打开权限，
     *                        return false：没打开权限，
     */
    public static boolean checkPublishPermission(final Activity context, String s) {
        if (Build.VERSION.SDK_INT >= 23 ) {
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(context, s)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("权限开启提醒")
                        .setMessage("为了保证APP正常使用，请打开相应权限")
                        .setCancelable(false)
                        .setNegativeButton("退出应用", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //用户取消设置权限
                                System.exit(0);
                            }
                        })
                        .setPositiveButton("前往开启", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Uri packageURI = Uri.parse("package:" + "com.smg.jimu2017");
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                                context.startActivity(intent);
                            }
                        })
                        .show();
                return false;
            }
        }
        return true;
    }

}
