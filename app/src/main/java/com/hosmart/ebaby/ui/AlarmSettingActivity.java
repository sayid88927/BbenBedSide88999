package com.hosmart.ebaby.ui;

import android.graphics.Color;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
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

import static java.lang.Integer.parseInt;

public class AlarmSettingActivity extends BaseActivity implements CheckPepeatAdapter.onRepeatItemClick, CheckColorAdapter.onItemClick, NumberPicker.Formatter {

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

    @BindView(R.id.rl_cancel)
    RelativeLayout tvCancel;

    @BindView(R.id.sw_turn_on)
    Switch swTurnOn;

    @BindView(R.id.sw_turn_off)
    Switch swTurnOff;

    @BindView(R.id.sw_only)
    Switch swOnly;

    @BindView(R.id.rl_half_circle)
    RelativeLayout rlHalfCircle;

    @BindView(R.id.iv_muisc)
    ImageView ivMuisc;

    @BindView(R.id.tv_time)
    TextView tvTime;

    @BindView(R.id.tv_week)
    TextView tvWeek;

    @BindView(R.id.ed_alarm_name)
    EditText edAlarmName;

    private CustomDialog checkTimeDialog;
    private CheckPepeatAdapter adapter;
    private List<CheckColorBean> dataColorBean;
    private List<CheckColorBean> dataMusicBean;
    private List<CheckColorBean> dataWeekBean;
    private CheckColorAdapter checkColorAdapter;

    private AlarmSettingBean alarmSettingBean;
    private StringBuilder checkWeek = new StringBuilder("11111111");
    private int Hours, Minute;
    private int[] rgb = new int[]{0, 0, 0, 0};
    private int music = 41;

    private String alarmOnly;
    private String isOff;

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
            alarmSettingBean = PreferUtil.getInstance().getAlarmSettingDataList(PreferUtil.CHECKALARMSETTINGBEAN1);
            if (alarmSettingBean == null || alarmSettingBean.getAlarmName() == null) {
                edAlarmName.setText("Alarm01");
            } else {
                edAlarmName.setText(alarmSettingBean.getAlarmName());
            }
        } else if (postion == 2) {
            alarmSettingBean = PreferUtil.getInstance().getAlarmSettingDataList(PreferUtil.CHECKALARMSETTINGBEAN2);
            if (alarmSettingBean == null || alarmSettingBean.getAlarmName() == null) {
                edAlarmName.setText("Alarm02");
            } else {
                edAlarmName.setText(alarmSettingBean.getAlarmName());
            }
        } else if (postion == 3) {
            alarmSettingBean = PreferUtil.getInstance().getAlarmSettingDataList(PreferUtil.CHECKALARMSETTINGBEAN3);
            if (alarmSettingBean == null || alarmSettingBean.getAlarmName() == null) {
                edAlarmName.setText("Alarm03");
            } else {
                edAlarmName.setText(alarmSettingBean.getAlarmName());
            }
        }
        if (alarmSettingBean != null) {
            initAlarmSetting();
        } else {
            alarmSettingBean = new AlarmSettingBean();
        }

        if (swTurnOff.isChecked()) {
            swTurnOn.setChecked(false);
        } else {
            swTurnOn.setChecked(true);
        }

        swTurnOn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    swTurnOff.setChecked(false);
                } else {
                    swTurnOff.setChecked(true);
                }
            }
        });

        swTurnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    swTurnOn.setChecked(false);
                } else {
                    swTurnOn.setChecked(true);
                }
            }
        });
        edAlarmName.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                alarmSettingBean.setAlarmName(s.toString());
            }
        });
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
        dataMusicBean = alarmSettingBean.getMuiscBean();
        dataWeekBean = alarmSettingBean.getWeekBean();
        dataColorBean = alarmSettingBean.getColorBean();
        tvTime.setText(addZeroForNum(String.valueOf(Hours), 2) + ":" + addZeroForNum(String.valueOf(Minute), 2));


        final StringBuffer strWeek = new StringBuffer();
        int count = 0;
        if (dataWeekBean != null && dataWeekBean.size() > 0) {
            for (int i = 0; i < dataWeekBean.size(); i++) {
                if (dataWeekBean.get(i).isCheckStart()) {
                    strWeek.append(Constant.selectedWeekString[dataWeekBean.get(i).getId()]).append("  ");
                    count++;
                }
            }
            if (count == 7) {
                tvWeek.setText("Every day");
            } else {
                tvWeek.setText(strWeek.toString());
            }
        }

        if (dataMusicBean != null && dataMusicBean.size() > 0) {
            for (int i = 0; i < dataMusicBean.size(); i++) {
                if (dataMusicBean.get(i).isCheckStart()) {
                    ivMuisc.setBackgroundResource(Constant.SelectedMusicDrawable[dataMusicBean.get(i).getId()]);
                }
            }
        }

    }


    @OnClick({R.id.rl_alarm_time, R.id.rl_repeat, R.id.ll_light, R.id.ll_sound, R.id.tv_save, R.id.rl_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_alarm_time:
                showCheckTimer();
                break;
            case R.id.rl_cancel:
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
                if (Hours == 0 && Minute == 0) {
                    ToastUtils.showShortToast("Alarm time can not be empty");
                    break;
                }
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

    private void setAlarmBean() {

        if (Hours == 0 && Minute == 0) {
            ToastUtils.showShortToast("Alarm time can not be empty");

        } else {

            if (swOnly.isChecked()) {
                alarmOnly = "01";
            } else {
                alarmOnly = "02";
            }

            if (swTurnOff.isChecked()) {
                isOff = "00";
            } else {
                isOff = "aa";
            }

            checkWeek.setCharAt(0, '1');

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

            int i = parseUnsignedInt(alarmSettingBean.getWeek().toString(), 2);
            String wk = addZeroForNum(Integer.toHexString(i), 2);
            String music = addZeroForNum(Integer.toHexString(alarmSettingBean.getMusic()), 2);
            String Hours = addZeroForNum(Integer.toHexString(alarmSettingBean.getHours()), 2);
            String Minutes = addZeroForNum(Integer.toHexString(alarmSettingBean.getMinutes()), 2);
            String rgb = addZeroForNum(Integer.toHexString(alarmSettingBean.getColors()[0]), 2) +
                    addZeroForNum(Integer.toHexString(alarmSettingBean.getColors()[1]), 2) +
                    addZeroForNum(Integer.toHexString(alarmSettingBean.getColors()[2]), 2) +
                    addZeroForNum(Integer.toHexString(alarmSettingBean.getColors()[3]), 2);

            String byteDate = null;
            switch (postion) {
                case 1:
                    byteDate = "05" + "aa" + wk + music + Hours + Minutes + rgb + alarmOnly + isOff;
                    PreferUtil.getInstance().setIsAlarmOff1(1);
                    break;
                case 2:
                    byteDate = "06" + "aa" + wk + music + Hours + Minutes + rgb + alarmOnly + isOff;
                    PreferUtil.getInstance().setIsAlarmOff2(1);
                    break;
                case 3:
                    byteDate = "07" + "aa" + wk + music + Hours + Minutes + rgb + alarmOnly + isOff;
                    PreferUtil.getInstance().setIsAlarmOff3(1);
                    break;
            }
            write(stringToBytes(byteDate));
            finish();
        }
    }

    public static int parseUnsignedInt(String s, int radix)
            throws NumberFormatException {
        if (s == null) {
            throw new NumberFormatException("null");
        }

        int len = s.length();
        if (len > 0) {
            char firstChar = s.charAt(0);
            if (firstChar == '-') {
                throw new
                        NumberFormatException(String.format("Illegal leading minus sign " +
                        "on unsigned string %s.", s));
            } else {
                if (len <= 5 || // Integer.MAX_VALUE in Character.MAX_RADIX is 6 digits
                        (radix == 10 && len <= 9)) { // Integer.MAX_VALUE in base 10 is 10 digits
                    return parseInt(s, radix);
                } else {
                    long ell = Long.parseLong(s, radix);
                    if ((ell & 0xffff_ffff_0000_0000L) == 0) {
                        return (int) ell;
                    } else {
                        throw new
                                NumberFormatException(String.format("String value %s exceeds " +
                                "range of unsigned int.", s));
                    }
                }
            }
        } else {
            throw new
                    NumberFormatException(String.format("Illegal leading minus sign " +
                    "on unsigned string %s.", s));
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
                final StringBuffer strWeek = new StringBuffer();
                int count = 0;
                for (int i = 0; i < dataWeekBean.size(); i++) {
                    if (dataWeekBean.get(i).isCheckStart()) {
                        checkWeek.setCharAt(i + 1, '1');
                        strWeek.append(Constant.selectedWeekString[dataWeekBean.get(i).getId()]).append("  ");
                        count++;
                    } else {
                        checkWeek.setCharAt(i + 1, '0');
                    }
                    adapter.notifyDataSetChanged();
                }
                if (count == 7) {
                    tvWeek.setText("Every day");
                } else {
                    tvWeek.setText(strWeek.toString());
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
        btnOk.setText("DONE");
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideDialog();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTime.setText(addZeroForNum(String.valueOf(Hours), 2) + ":" + addZeroForNum(String.valueOf(Minute), 2));
                hideDialog();
            }
        });
        CustomNumberPicker npHours = (CustomNumberPicker) view.findViewById(R.id.np_hours);
        npHours.setFormatter(this);
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
        npMinute.setFormatter(this);
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
        TextView tvTitle = view.findViewById(R.id.tv_title);
        if (type == CheckColorAdapter.checkColor) {
            tvTitle.setText("Select a Color");
            dataColorBean = alarmSettingBean.getColorBean();
            if (dataColorBean == null || dataColorBean.size() <= 0) {
                initCheckColorData(type);
            }
            checkColorAdapter = new CheckColorAdapter(dataColorBean, type);
        } else if (type == CheckColorAdapter.checkVoice) {
            tvTitle.setText("Select a Track");
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
                    for (int i = 0; i < dataColorBean.size(); i++) {
                        if (dataColorBean.get(i).isCheckStart()) {
                            if (i == dataColorBean.size() - 1) {
                                rgb = Constant.wRGB[dataColorBean.get(i - 2).getId()];
                            } else {
                                rgb = Constant.wRGB[dataColorBean.get(i).getId()];
                            }
                        }
                    }
                    hideDialog();
                } else if (CheckColorAdapter.getType() == CheckColorAdapter.checkVoice) {
                    for (int i = 0; i < dataMusicBean.size(); i++) {
                        if (dataMusicBean.get(i).isCheckStart()) {
                            music = Constant.selectedAlarmMusic[dataMusicBean.get(i).getId()];
                        }
                    }
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
                        rlHalfCircle.setBackgroundResource(Constant.halfCircle[dataColorBean.get(i).getId()]);
                    } else {
                        dataColorBean.get(i).setCheckStart(false);
                    }
                }
        } else if (CheckColorAdapter.getType() == CheckColorAdapter.checkVoice) {
            for (int i = 0; i < dataMusicBean.size(); i++) {
                if (item.getId() == dataMusicBean.get(i).getId()) {
                    dataMusicBean.get(i).setCheckStart(true);
                    ivMuisc.setBackgroundResource(Constant.SelectedMusicDrawable[dataMusicBean.get(i).getId()]);
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
    public String format(int value) {
        String tmpStr = String.valueOf(value);
        if (value < 10) {
            tmpStr = "0" + tmpStr;
        }
        return tmpStr;
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
