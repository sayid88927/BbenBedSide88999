package com.hosmart.ebaby.ui;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.blankj.utilcode.utils.ToastUtils;
import com.hosmart.ebaby.R;
import com.hosmart.ebaby.base.BaseActivity;
import com.hosmart.ebaby.base.BaseApplication;
import com.hosmart.ebaby.base.Constant;
import com.hosmart.ebaby.bean.AlarmSettingBean;
import com.hosmart.ebaby.bean.CheckColorBean;
import com.hosmart.ebaby.ui.apadter.CheckColorAdapter;
import com.hosmart.ebaby.ui.apadter.CheckPepeatAdapter;
import com.hosmart.ebaby.utils.PreferUtil;
import com.hosmart.ebaby.view.ColorPickerView;
import com.hosmart.ebaby.view.CustomNumberPicker;
import com.hosmart.ebaby.view.dialog.CustomDialog;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AlarmSettingActivity extends BaseActivity implements CheckPepeatAdapter.onRepeatItemClick, CheckColorAdapter.onItemClick {

    private int postion;

    @BindView(R.id.tv_alarm_postion)
    TextView tvAlarmPostion;

    @BindView(R.id.rl_alarm_time)
    RelativeLayout rlAlarmTime;

    @BindView(R.id.rl_repeat)
    RelativeLayout rlRepeat;

    @BindView(R.id.ll_light)
    LinearLayout llLight;

    @BindView(R.id.tv_save)
    TextView tvSave;

    @BindView(R.id.sw_turn_on)
    Switch swTurnOn;

    @BindView(R.id.sw_turn_off)
    Switch swTurnOff;

    @BindView(R.id.sw_only)
    Switch swOnly;

    private CustomDialog checkTimeDialog;
    private CheckPepeatAdapter adapter;
    private List<CheckColorBean> data = new ArrayList<>();
    private CheckColorAdapter checkColorAdapter;


    private AlarmSettingBean alarmSettingBean;
    private StringBuilder checkWeek = new StringBuilder("00000000");
    private int Hours, Minute;
    private int[] rgb;
    private int music;

    @Override
    public int getLayoutId() {
        return R.layout.activity_alarm_setting;
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
        postion = getIntent().getIntExtra("data", 1);
        if (postion == 1) {
            tvAlarmPostion.setText("Alarm01");
            alarmSettingBean = PreferUtil.getInstance().getAlarmSettingDataList(PreferUtil.CHECKALARMSETTINGBEAN1);
        } else if (postion == 2) {
            tvAlarmPostion.setText("Alarm02");
            alarmSettingBean = PreferUtil.getInstance().getAlarmSettingDataList(PreferUtil.CHECKALARMSETTINGBEAN2);
        } else if (postion == 3) {
            tvAlarmPostion.setText("Alarm03");
            alarmSettingBean = PreferUtil.getInstance().getAlarmSettingDataList(PreferUtil.CHECKALARMSETTINGBEAN3);
        }
        if (alarmSettingBean != null) {
            initAlarmSetting();
        } else {
            alarmSettingBean = new AlarmSettingBean();
        }


    }

    private void initAlarmSetting() {

        swTurnOn.setChecked(alarmSettingBean.isTurnOn());
        swTurnOff.setChecked(alarmSettingBean.isTurnOff());
        swOnly.setChecked(alarmSettingBean.isAlarmOnly());
        checkWeek = alarmSettingBean.getWeek();

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @OnClick({R.id.rl_alarm_time, R.id.rl_repeat, R.id.ll_light, R.id.ll_sound, R.id.tv_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_alarm_time:
                showCheckTimer();
                break;
            case R.id.rl_repeat:
                showCheckRepeat();
                break;
            case R.id.ll_light:
                showCheckColor(CheckColorAdapter.checkColor);
                break;
            case R.id.ll_sound:
                showCheckColor(CheckColorAdapter.checkVoice);
                break;
            case R.id.tv_save:
                if (!swTurnOn.isChecked()) {
                    ToastUtils.showShortToast("请先设置开启");
                    break;
                }
                if (alarmSettingBean.getHours() == 0 && alarmSettingBean.getMinutes() == 0) {
                    ToastUtils.showShortToast("请先设置时间");
                    break;
                }

                switch (postion) {
                    case 1:
                        data = PreferUtil.getInstance().getDataList(PreferUtil.CHECKALARMSETTINGWEEKBEAN1);
                        if (data == null || data.size() <= 0) {
                            ToastUtils.showShortToast("请先设置星期");
                            break;
                        } else {
                            int count = 0;
                            for (int i = 0; i < data.size(); i++) {
                                if (!data.get(i).isCheckStart()) {
                                    count++;
                                }
                            }
                            if (count == 7) {
                                ToastUtils.showShortToast("请先设置星期");
                                break;
                            }
                        }


                        PreferUtil.getInstance().setAlarmSrttingDataList(PreferUtil.CHECKALARMSETTINGBEAN1, alarmSettingBean);
                        String cWeek = alarmSettingBean.getWeek().toString();
                        int i = Integer.parseUnsignedInt(cWeek, 2);
                        String fd = addZeroForNum(Integer.toHexString(i), 2);

//                        String byteDate = "05" + "aa" +
                        break;
                    case 2:
                        PreferUtil.getInstance().setAlarmSrttingDataList(PreferUtil.CHECKALARMSETTINGBEAN2, alarmSettingBean);
                        break;
                    case 3:
                        PreferUtil.getInstance().setAlarmSrttingDataList(PreferUtil.CHECKALARMSETTINGBEAN3, alarmSettingBean);
                        break;
                }

                alarmSettingBean.setTurnOff(swTurnOff.isChecked());
                alarmSettingBean.setTurnOn(swTurnOn.isChecked());
                alarmSettingBean.setAlarmOnly(swOnly.isChecked());
                alarmSettingBean.setHours(Hours);
                alarmSettingBean.setMinutes(Minute);
                alarmSettingBean.setWeek(checkWeek);

               finish();
                break;
        }
    }

    private void showCheckRepeat() {
        hideDialog();

        this.data.clear();
       data =  PreferUtil.getInstance().getDataList(PreferUtil.CHECKALARMSETTINGWEEKBEAN1);
       if(data==null || data.size()<=0){
           initCheckRepeatData();
       }
        View view = View.inflate(BaseApplication.getContext(), R.layout.dialog_repeat, null);
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        Button btnOk = view.findViewById(R.id.btn_ok);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideDialog();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (postion){
                    case 1:
                        PreferUtil.getInstance().setDataList(PreferUtil.CHECKALARMSETTINGWEEKBEAN1,data);
                        alarmSettingBean.setWeek(checkWeek);
                        break;
                    case 2:
                        PreferUtil.getInstance().setDataList(PreferUtil.CHECKALARMSETTINGWEEKBEAN2,data);
                        break;
                    case 3:
                        PreferUtil.getInstance().setDataList(PreferUtil.CHECKALARMSETTINGWEEKBEAN3,data);
                        break;
                }
                hideDialog();
            }
        });
        RecyclerView rvCheckRepeat = (RecyclerView) view.findViewById(R.id.rv_check_repeat);
        LinearLayoutManager layoutManager = new LinearLayoutManager(AlarmSettingActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rvCheckRepeat.setLayoutManager(layoutManager);
        adapter = new CheckPepeatAdapter(data);
        rvCheckRepeat.setAdapter(adapter);
        adapter.onRepeatItemClick(AlarmSettingActivity.this);

        checkTimeDialog = new CustomDialog(this, view, R.style.ActivityDialogStyle);
        checkTimeDialog.show();
        checkTimeDialog.setCancelable(true);
        checkTimeDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    private void initCheckRepeatData() {
        for (int i = 0; i < Constant.selectedRepeatDrawable.length; i++) {
            CheckColorBean checkColorBean = new CheckColorBean();
            checkColorBean.setId(i);
            checkColorBean.setCheckStart(false);
            checkColorBean.setSelectedDrawable(Constant.selectedRepeatDrawable[i]);
            checkColorBean.setUnSelectedDrawable(Constant.unSelectedRepeatDrawable[i]);
            data.add(checkColorBean);
        }
        PreferUtil.getInstance().setDataList(PreferUtil.CHECKREPEATBEAN, data);
    }

    private void showCheckTimer() {
        hideDialog();
        View view = View.inflate(BaseApplication.getContext(), R.layout.dialog_check_timer, null);
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        Button btnOk = view.findViewById(R.id.btn_ok);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideDialog();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmSettingBean.setHours(Hours);
                alarmSettingBean.setMinutes(Minute);
                hideDialog();
            }
        });
        CustomNumberPicker npHours = (CustomNumberPicker) view.findViewById(R.id.np_hours);
        npHours.setMinValue(0);
        npHours.setMaxValue(24);
        npHours.setNumberPickerDividerColor(npHours);
        npHours.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        CustomNumberPicker npMinute = (CustomNumberPicker) view.findViewById(R.id.np_minute);
        npHours.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Hours = newVal;
            }
        });

        npMinute.setMinValue(0);
        npMinute.setMaxValue(60);
        npMinute.setNumberPickerDividerColor(npMinute);
        npMinute.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        npMinute.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Minute = newVal;
            }
        });
        checkTimeDialog = new CustomDialog(this, view, R.style.ActivityDialogStyle);
        checkTimeDialog.show();
        checkTimeDialog.setCancelable(true);

    }

    private void hideDialog() {
        if (checkTimeDialog != null) {
            checkTimeDialog.dismiss();
            checkTimeDialog = null;
        }
    }

    private void showCheckColor(int type) {
        hideDialog();
        this.data.clear();
        if (type == CheckColorAdapter.checkColor) {
            data = PreferUtil.getInstance().getDataList(PreferUtil.CHECKCOLORALARMBEAN);
            if (data == null || data.size() <= 0)
                initCheckColorData(type);
        } else if (type == CheckColorAdapter.checkVoice) {
            data = PreferUtil.getInstance().getDataList(PreferUtil.CHECKVOICEALARMBEAN);
            if (data == null || data.size() <= 0)
                initCheckColorData(type);
        }

        View view = View.inflate(BaseApplication.getContext(), R.layout.dialog_check_color, null);
        RecyclerView rvCheckColor = view.findViewById(R.id.rv_check_color);
        rvCheckColor.setLayoutManager(new GridLayoutManager(AlarmSettingActivity.this, 3));
        checkColorAdapter = new CheckColorAdapter(data, type);
        rvCheckColor.setAdapter(checkColorAdapter);
        checkColorAdapter.onItemClick(AlarmSettingActivity.this);

        Button btnCancel = view.findViewById(R.id.btn_cancel);
        Button btnOk = view.findViewById(R.id.btn_ok);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideDialog();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckColorAdapter.getType() == CheckColorAdapter.checkColor) {
                    PreferUtil.getInstance().setDataList(PreferUtil.CHECKCOLORALARMBEAN, data);
                    hideDialog();
                    alarmSettingBean.setColors(rgb);
                } else if (CheckColorAdapter.getType() == CheckColorAdapter.checkVoice) {
                    PreferUtil.getInstance().setDataList(PreferUtil.CHECKVOICEALARMBEAN, data);
                    hideDialog();
                    alarmSettingBean.setMusic(music);
                }
            }
        });

        checkTimeDialog = new CustomDialog(this, view, R.style.ActivityDialogStyle);
        checkTimeDialog.show();
        checkTimeDialog.setCancelable(true);

    }

    private void initCheckColorData(int type) {
        this.data.clear();
        if (type == CheckColorAdapter.checkColor) {
            for (int i = 0; i < Constant.selectedColorDrawable.length; i++) {
                CheckColorBean checkColorBean = new CheckColorBean();
                checkColorBean.setId(i);
                checkColorBean.setCheckStart(false);
                checkColorBean.setSelectedDrawable(Constant.selectedColorDrawable[i]);
                checkColorBean.setUnSelectedDrawable(Constant.unSelectedColorDrawable[i]);
                data.add(checkColorBean);
            }
            PreferUtil.getInstance().setDataList(PreferUtil.CHECKCOLORALARMBEAN, data);
        } else if (type == CheckColorAdapter.checkVoice) {
            for (int i = 0; i < Constant.selectedVoiceDrawable.length; i++) {
                CheckColorBean checkColorBean = new CheckColorBean();
                checkColorBean.setId(i);
                checkColorBean.setCheckStart(false);
                checkColorBean.setSelectedDrawable(Constant.selectedVoiceDrawable[i]);
                checkColorBean.setUnSelectedDrawable(Constant.unSelectedVoiceDrawable[i]);
                data.add(checkColorBean);
            }
            PreferUtil.getInstance().setDataList(PreferUtil.CHECKVOICEALARMBEAN, data);
        }
    }

    @Override
    public void onItemClick(CheckColorBean item) {
        for (int i = 0; i < data.size(); i++) {
            if (item.getId() == data.get(i).getId()) {
                data.get(i).setCheckStart(true);
                if (CheckColorAdapter.getType() == CheckColorAdapter.checkColor) {
                    rgb = Constant.wRGB[data.get(i).getId()];
                } else {
                    music = Constant.selectedVoiceMusic[data.get(i).getId()];
                }
            } else {
                data.get(i).setCheckStart(false);
            }
        }
        checkColorAdapter.notifyDataSetChanged();

        if (CheckColorAdapter.getType() == CheckColorAdapter.checkColor && item.getId() == data.size() - 1) {
            showPickerDialog();
        }
    }

    private void showPickerDialog() {
        View view = View.inflate(BaseApplication.getContext(), R.layout.dialog_picker, null);
        ColorPickerView colorPicker = (ColorPickerView) view.findViewById(R.id.colorPicker);

        colorPicker.setCornorCircleType(ColorPickerView.TYPE_FILL);
        colorPicker.setDrawMagnifyBounds(false);
        colorPicker.setDrawMagnifyCircle(false);
        rgb = new int[4];
        colorPicker.onCheckRgbClick(new ColorPickerView.onCheckRgbClick() {
            @Override
            public void onCheckRgbClick(int item) {
                rgb[1] = 00;
                rgb[1] = Color.red(item);
                rgb[2] = Color.green(item);
                rgb[3] = Color.blue(item);
                Constant.wRGB[10] = rgb;
                alarmSettingBean.setColors(rgb);
            }
        });

        final CustomDialog dialog = new CustomDialog(this, view, R.style.ActivityDialogStyle);
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        Button btnOk = view.findViewById(R.id.btn_ok);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
        dialog.setCancelable(true);

    }

    @Override
    public void onRepeatItemClick(CheckColorBean item) {

        for (int i = 0; i < data.size(); i++) {
            if (item.getId() == data.get(i).getId()) {
                if (item.isCheckStart()) {
                    data.get(i).setCheckStart(false);
                    checkWeek.setCharAt(i + 1, '0');
                } else {
                    checkWeek.setCharAt(i + 1, '1');
                    data.get(i).setCheckStart(true);
                }
                adapter.notifyDataSetChanged();
                alarmSettingBean.setWeek(checkWeek);

            }
        }

    }
}
