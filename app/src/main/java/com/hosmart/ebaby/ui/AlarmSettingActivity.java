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
import android.widget.CompoundButton;
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

public class AlarmSettingActivity extends BaseActivity implements CheckPepeatAdapter.onRepeatItemClick, CheckColorAdapter.onItemClick, CompoundButton.OnCheckedChangeListener {

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

    @BindView(R.id.tv_cancel)
    TextView tvCancel;

    @BindView(R.id.sw_turn_on)
    Switch swTurnOn;

    @BindView(R.id.sw_turn_off)
    Switch swTurnOff;

    @BindView(R.id.sw_only)
    Switch swOnly;

    private CustomDialog checkTimeDialog;
    private CheckPepeatAdapter adapter;
    private List<CheckColorBean> dataColorBean;
    private List<CheckColorBean> dataMusicBean;
    private List<CheckColorBean> dataWeekBean;
    private CheckColorAdapter checkColorAdapter;

    private AlarmSettingBean alarmSettingBean;
    private StringBuilder checkWeek = new StringBuilder("00000000");
    private int Hours, Minute;
    private int[] rgb = new int[]{-1, -1, -1, -1};
    private int music;

    private String alarmOnly;

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

        swTurnOn.setOnCheckedChangeListener(this);

    }

    private void initAlarmSetting() {
        swTurnOn.setChecked(alarmSettingBean.isTurnOn());
        swTurnOff.setChecked(alarmSettingBean.isTurnOff());
        swOnly.setChecked(alarmSettingBean.isAlarmOnly());
        checkWeek = alarmSettingBean.getWeek();
        Hours = alarmSettingBean.getHours();
        Minute = alarmSettingBean.getMinutes();
        music = alarmSettingBean.getMusic();
        rgb = alarmSettingBean.getColors();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @OnClick({R.id.rl_alarm_time, R.id.rl_repeat, R.id.ll_light, R.id.ll_sound, R.id.tv_save, R.id.tv_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_alarm_time:
                showCheckTimer();
                break;
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.rl_repeat:
                showCheckWeek();
                break;
            case R.id.ll_light:
                showCheckColor(CheckColorAdapter.checkColor);
                break;
            case R.id.ll_sound:
                showCheckColor(CheckColorAdapter.checkVoice);
                break;
            case R.id.tv_save:
                switch (postion) {
                    case 1:
                        setAlarmBean();
                        PreferUtil.getInstance().setAlarmSrttingDataList(PreferUtil.CHECKALARMSETTINGBEAN1, alarmSettingBean);
                        break;
                    case 2:
                        setAlarmBean();
                        PreferUtil.getInstance().setAlarmSrttingDataList(PreferUtil.CHECKALARMSETTINGBEAN2, alarmSettingBean);
                        break;
                    case 3:
                        setAlarmBean();
                        PreferUtil.getInstance().setAlarmSrttingDataList(PreferUtil.CHECKALARMSETTINGBEAN3, alarmSettingBean);
                        break;
                }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setAlarmBean() {

        if (Hours == 0 && Minute == 0) {
            ToastUtils.showShortToast("请先设置时间");
        } else if (checkWeek.toString().equals("10000000")) {
            ToastUtils.showShortToast("请先设置星期");
        } else if (rgb[0] == -1 && rgb[1] == -1 && rgb[2] == -1 && rgb[3] == -1) {
            ToastUtils.showShortToast("请先设置颜色");
        } else if (music <= 0) {
            ToastUtils.showShortToast("请先设置音乐");
        } else {

            if (alarmSettingBean.isAlarmOnly()) {
                alarmOnly = "01";
            } else {
                alarmOnly = "02";
            }

            alarmSettingBean.setTurnOff(swTurnOff.isChecked());
            alarmSettingBean.setTurnOn(swTurnOn.isChecked());
            alarmSettingBean.setAlarmOnly(swOnly.isChecked());
            alarmSettingBean.setHours(Hours);
            alarmSettingBean.setMinutes(Minute);
            alarmSettingBean.setWeek(checkWeek);
            alarmSettingBean.setColors(rgb);
            alarmSettingBean.setMusic(music);

            alarmSettingBean.setWeekBean(dataWeekBean);
            alarmSettingBean.setColorBean(dataColorBean);
            alarmSettingBean.setMuiscBean(dataMusicBean);

            int i = Integer.parseUnsignedInt(alarmSettingBean.getWeek().toString(), 2);
            String wk = addZeroForNum(Integer.toHexString(i), 2);
            String music = addZeroForNum(Integer.toHexString(alarmSettingBean.getMusic()), 2);
            String Hours = addZeroForNum(Integer.toHexString(alarmSettingBean.getHours()), 2);
            String Minutes = addZeroForNum(Integer.toHexString(alarmSettingBean.getMinutes()), 2);
            String rgb = addZeroForNum(Integer.toHexString(alarmSettingBean.getColors()[0]), 2) +
                    addZeroForNum(Integer.toHexString(alarmSettingBean.getColors()[1]), 2) +
                    addZeroForNum(Integer.toHexString(alarmSettingBean.getColors()[2]), 2) +
                    addZeroForNum(Integer.toHexString(alarmSettingBean.getColors()[3]), 2);

            String byteDate = "05" + "aa" + wk + music + Hours + Minutes + rgb + alarmOnly;
            Logger.e(byteDate);
            write(stringToBytes(byteDate));
            finish();
        }
    }

    private void showCheckWeek() {
        hideDialog();
        dataWeekBean = alarmSettingBean.getWeekBean();
        if (dataWeekBean == null || dataWeekBean.size() <= 0) {
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
                for (int i = 0; i < dataWeekBean.size(); i++) {
                    if (dataWeekBean.get(i).isCheckStart()) {
                        checkWeek.setCharAt(i + 1, '1');
                    } else {
                        checkWeek.setCharAt(i + 1, '0');
                    }
                    adapter.notifyDataSetChanged();
                }
                hideDialog();
            }
        });
        RecyclerView rvCheckRepeat = (RecyclerView) view.findViewById(R.id.rv_check_repeat);
        LinearLayoutManager layoutManager = new LinearLayoutManager(AlarmSettingActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rvCheckRepeat.setLayoutManager(layoutManager);
        adapter = new CheckPepeatAdapter(dataWeekBean);
        rvCheckRepeat.setAdapter(adapter);
        adapter.onRepeatItemClick(AlarmSettingActivity.this);

        checkTimeDialog = new CustomDialog(this, view, R.style.ActivityDialogStyle);
        checkTimeDialog.show();
        checkTimeDialog.setCancelable(true);
        checkTimeDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    private void initCheckRepeatData() {
        if (dataWeekBean == null)
            dataWeekBean = new ArrayList<>();
        else
            dataWeekBean.clear();
        for (int i = 0; i < Constant.selectedRepeatDrawable.length; i++) {
            CheckColorBean checkColorBean = new CheckColorBean();
            checkColorBean.setId(i);
            checkColorBean.setCheckStart(false);
            checkColorBean.setSelectedDrawable(Constant.selectedRepeatDrawable[i]);
            checkColorBean.setUnSelectedDrawable(Constant.unSelectedRepeatDrawable[i]);
            dataWeekBean.add(checkColorBean);
        }
    }

    @Override
    public void onRepeatItemClick(CheckColorBean item) {
        for (int i = 0; i < dataWeekBean.size(); i++) {
            if (item.getId() == dataWeekBean.get(i).getId()) {
                if (item.isCheckStart()) {
                    dataWeekBean.get(i).setCheckStart(false);
                } else {
                    dataWeekBean.get(i).setCheckStart(true);
                }
                adapter.notifyDataSetChanged();
            }
        }
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
                hideDialog();
            }
        });
        CustomNumberPicker npHours = (CustomNumberPicker) view.findViewById(R.id.np_hours);
        npHours.setMinValue(0);
        npHours.setMaxValue(23);
        npHours.setValue(alarmSettingBean.getHours());
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
        npMinute.setMaxValue(59);
        npMinute.setValue(alarmSettingBean.getMinutes());
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
        View view = View.inflate(BaseApplication.getContext(), R.layout.dialog_check_color, null);
        RecyclerView rvCheckColor = view.findViewById(R.id.rv_check_color);
        rvCheckColor.setLayoutManager(new GridLayoutManager(AlarmSettingActivity.this, 3));

        if (type == CheckColorAdapter.checkColor) {
            dataColorBean = alarmSettingBean.getColorBean();
            if (dataColorBean == null || dataColorBean.size() <= 0) {
                initCheckColorData(type);
            }
            checkColorAdapter = new CheckColorAdapter(dataColorBean, type);
        } else if (type == CheckColorAdapter.checkVoice) {
            dataMusicBean = alarmSettingBean.getMuiscBean();
            if (dataMusicBean == null || dataMusicBean.size() <= 0) {
                initCheckColorData(type);
            }
            checkColorAdapter = new CheckColorAdapter(dataMusicBean, type);
        }

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
                    hideDialog();
                } else if (CheckColorAdapter.getType() == CheckColorAdapter.checkVoice) {
                    hideDialog();
                }
            }
        });

        checkTimeDialog = new CustomDialog(this, view, R.style.ActivityDialogStyle);
        checkTimeDialog.show();
        checkTimeDialog.setCancelable(true);

    }

    private void initCheckColorData(int type) {
        if (type == CheckColorAdapter.checkColor) {
            if (dataColorBean == null) {
                dataColorBean = new ArrayList<>();
            } else {
                dataColorBean.clear();
            }
            for (int i = 0; i < Constant.selectedColorDrawable.length; i++) {
                CheckColorBean checkColorBean = new CheckColorBean();
                checkColorBean.setId(i);
                checkColorBean.setCheckStart(false);
                checkColorBean.setSelectedDrawable(Constant.selectedColorDrawable[i]);
                checkColorBean.setUnSelectedDrawable(Constant.unSelectedColorDrawable[i]);
                dataColorBean.add(checkColorBean);
            }
        } else if (type == CheckColorAdapter.checkVoice) {
            if (dataMusicBean == null) {
                dataMusicBean = new ArrayList<>();
            } else {
                dataMusicBean.clear();
            }
            for (int i = 0; i < Constant.selectedVoiceDrawable.length; i++) {
                CheckColorBean checkColorBean = new CheckColorBean();
                checkColorBean.setId(i);
                checkColorBean.setCheckStart(false);
                checkColorBean.setSelectedDrawable(Constant.selectedVoiceDrawable[i]);
                checkColorBean.setUnSelectedDrawable(Constant.unSelectedVoiceDrawable[i]);
                dataMusicBean.add(checkColorBean);
            }
        }
    }

    @Override
    public void onItemClick(CheckColorBean item) {
        if (CheckColorAdapter.getType() == CheckColorAdapter.checkColor) {
            if (item.getId() == dataColorBean.size() - 1) {
                showPickerDialog();
                dataColorBean.get(dataColorBean.size() - 2).setCheckStart(true);
            } else
                for (int i = 0; i < dataColorBean.size(); i++) {
                    if (item.getId() == dataColorBean.get(i).getId()) {
                        dataColorBean.get(i).setCheckStart(true);
                        rgb = Constant.wRGB[dataColorBean.get(i).getId()];
                    } else {
                        dataColorBean.get(i).setCheckStart(false);
                    }
                }
        } else if (CheckColorAdapter.getType() == CheckColorAdapter.checkVoice) {
            for (int i = 0; i < dataMusicBean.size(); i++) {
                if (item.getId() == dataMusicBean.get(i).getId()) {
                    dataMusicBean.get(i).setCheckStart(true);
                    music = Constant.selectedVoiceMusic[dataMusicBean.get(i).getId()];
                } else {
                    dataMusicBean.get(i).setCheckStart(false);
                }
            }
        }
        checkColorAdapter.notifyDataSetChanged();
    }

    private void showPickerDialog() {
        View view = View.inflate(BaseApplication.getContext(), R.layout.dialog_picker, null);
        ColorPickerView colorPicker = (ColorPickerView) view.findViewById(R.id.colorPicker);

        colorPicker.setCornorCircleType(ColorPickerView.TYPE_FILL);
        colorPicker.setDrawMagnifyBounds(false);
        colorPicker.setDrawMagnifyCircle(false);

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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            checkWeek.setCharAt(0, '1');
        } else {
            checkWeek.setCharAt(0, '0');
        }
    }
}
