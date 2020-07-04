/**
 * Copyright(C)2012-2013 深圳市掌星立意科技有限公司版权所有
 * 创 建 人:Jacky
 * 修 改 人:
 * 创 建日期:2013-7-25
 * 描    述:xml储存数据
 * 版 本 号:
 */
package com.hosmart.ebaby.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hosmart.ebaby.base.BaseApplication;
import com.hosmart.ebaby.bean.AlarmSettingBean;
import com.hosmart.ebaby.bean.CheckColorBean;

import java.util.ArrayList;
import java.util.List;


public final class PreferUtil {


    private static Context context;

    public static PreferUtil INSTANCE;
    private static SharedPreferences mPrefer;
    private static final String APP_NAME = "com.hosmart.ebaby";

    public static final String CHECKCOLORBEAN = "CheckColorBean";
    public static final String CHECKVOICEBEAN = "CheckVoiceBean";
    public static final String CHECKREPEATBEAN = "CheckRepeatBean";

    public static final String CHECKALARMSETTINGBEAN1 = "checkAlarmSettingBean1";
    public static final String CHECKALARMSETTINGBEAN2 = "checkAlarmSettingBean2";
    public static final String CHECKALARMSETTINGBEAN3 = "checkAlarmSettingBean3";
    public  static  final  String ISALARMOFF1 ="isAlarmOff1";
    public  static  final  String ISALARMOFF2 ="isAlarmOff2";
    public  static  final  String ISALARMOFF3 ="isAlarmOff3";

    private PreferUtil() {
    }

    public static PreferUtil getInstance() {
        if (INSTANCE == null) {
            context = BaseApplication.getContext();
            return new PreferUtil();
        }
        return INSTANCE;
    }

    public void init(Context ctx) {
        mPrefer = ctx.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE
                | Context.MODE_PRIVATE);
        mPrefer.edit().commit();
    }


    /**
     * 保存List
     *
     * @param tag
     * @param datalist
     */
    public void setDataList(String tag, List<CheckColorBean> datalist) {
        if (null == datalist || datalist.size() <= 0)
            return;
        Gson gson = new Gson();
        //转换成json数据，再保存
        mPrefer.edit().remove(tag).commit();
        String strJson = gson.toJson(datalist);
        mPrefer.edit().putString(tag, strJson).commit();

    }

    /**
     * 获取List
     *
     * @param tag
     * @return
     */
    public List<CheckColorBean> getDataList(String tag) {
        List<CheckColorBean> datalist = new ArrayList<>();
        String strJson = mPrefer.getString(tag, null);
        if (null == strJson) {
            return datalist;
        }
        Gson gson = new Gson();
        datalist = gson.fromJson(strJson, new TypeToken<List<CheckColorBean>>() {
        }.getType());
        return datalist;
    }


    /**
     * 保存List
     *
     * @param tag
     * @param datalist
     */
    public void setAlarmSrttingDataList(String tag, AlarmSettingBean datalist) {
        Gson gson = new Gson();
        mPrefer.edit().remove(tag).commit();
        String strJson = gson.toJson(datalist);
        mPrefer.edit().putString(tag, strJson).commit();

    }

    /**
     * 获取List
     *
     * @param tag
     * @return
     */
    public AlarmSettingBean getAlarmSettingDataList(String tag) {
        AlarmSettingBean datalist;
        String strJson = mPrefer.getString(tag, null);
        Gson gson = new Gson();
        datalist = gson.fromJson(strJson, AlarmSettingBean.class);
        return datalist;
    }

    public  int getIsAlarmOff1(){
        return mPrefer.getInt(ISALARMOFF1, -1);
    }
    public  void setIsAlarmOff1(int tag){  // 0 关闭
        putInt(ISALARMOFF1,tag);
    }

    public  int getIsAlarmOff2(){
        return mPrefer.getInt(ISALARMOFF2, -1);
    }
    public  void setIsAlarmOff2(int tag){  // 0 关闭
        putInt(ISALARMOFF2,tag);
    }

    public  int getIsAlarmOff3(){
        return mPrefer.getInt(ISALARMOFF3, -1);
    }
    public  void setIsAlarmOff3(int tag){  // 0 关闭
        putInt(ISALARMOFF3,tag);
    }

    public String getString(String key, String defValue) {
        return mPrefer.getString(key, defValue);
    }

    public int getInt(String key, int defValue) {
        return mPrefer.getInt(key, defValue);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return mPrefer.getBoolean(key, defValue);
    }

    public void putString(String key, String value) {
        mPrefer.edit().putString(key, value).commit();
    }

    public void putInt(String key, int value) {
        mPrefer.edit().putInt(key, value).commit();
    }

    public void putBoolean(String key, boolean value) {
        mPrefer.edit().putBoolean(key, value).commit();
    }

    public void putLong(String key, long value) {
        mPrefer.edit().putLong(key, value).commit();
    }

    public long getLong(String key, long defValue) {
        return mPrefer.getLong(key, defValue);
    }

    public void removeKey(String key) {
        mPrefer.edit().remove(key).commit();
    }


}
