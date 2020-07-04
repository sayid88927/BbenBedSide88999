package com.hosmart.ebaby.ui;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.hosmart.ebaby.R;
import com.hosmart.ebaby.base.BaseActivity;
import com.hosmart.ebaby.base.Constant;
import com.hosmart.ebaby.bean.AlarmSettingBean;
import com.hosmart.ebaby.utils.PreferUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class ProgramsActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.rl_time1)
    RelativeLayout rlTime1;

    @BindView(R.id.rl_time2)
    RelativeLayout rlTime2;

    @BindView(R.id.rl_time3)
    RelativeLayout rlTime3;

    @BindView(R.id.tv_done)
    TextView tvDone;

    @BindView(R.id.sw_alarm1)
    Switch swAlarm1;
    @BindView(R.id.sw_alarm2)
    Switch swAlarm2;
    @BindView(R.id.sw_alarm3)
    Switch swAlarm3;

    @BindView(R.id.tv_name1)
    TextView tvName1;
    @BindView(R.id.tv_name2)
    TextView tvName2;
    @BindView(R.id.tv_name3)
    TextView tvName3;

    @BindView(R.id.iv_muisc1)
    ImageView ivMuisc1;
    @BindView(R.id.iv_check_color1)
    ImageView ivCheckColor1;
    @BindView(R.id.tv_time1)
    TextView tvTime1;
    @BindView(R.id.tv_on)
    TextView tvOn1;
    @BindView(R.id.tv_day1)
    TextView tvDay1;

    @BindView(R.id.iv_muisc2)
    ImageView ivMuisc2;
    @BindView(R.id.iv_check_color2)
    ImageView ivCheckColor2;
    @BindView(R.id.tv_time2)
    TextView tvTime2;
    @BindView(R.id.tv_on2)
    TextView tvOn2;
    @BindView(R.id.tv_day2)
    TextView tvDay2;

    @BindView(R.id.iv_muisc3)
    ImageView ivMuisc3;
    @BindView(R.id.iv_check_color3)
    ImageView ivCheckColor3;
    @BindView(R.id.tv_time3)
    TextView tvTime3;
    @BindView(R.id.tv_on3)
    TextView tvOn3;
    @BindView(R.id.tv_day3)
    TextView tvDay3;

    private AlarmSettingBean alram1, alram2, alram3;

    @Override
    public int getLayoutId() {
        return R.layout.activity_programs;
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        swAlarm1.setOnCheckedChangeListener(this);
        swAlarm2.setOnCheckedChangeListener(this);
        swAlarm3.setOnCheckedChangeListener(this);
        alram1 = PreferUtil.getInstance().getAlarmSettingDataList(PreferUtil.CHECKALARMSETTINGBEAN1);
        if (alram1 == null) {
            swAlarm1.setChecked(false);
            tvName1.setText("Time to rise");
        } else {
            if (PreferUtil.getInstance().getIsAlarmOff1() == 0) {
                swAlarm1.setChecked(false);
            } else {
                swAlarm1.setChecked(true);
            }
            tvName1.setText("Alarm01");
            initAlarm(1);
        }
        alram2 = PreferUtil.getInstance().getAlarmSettingDataList(PreferUtil.CHECKALARMSETTINGBEAN2);
        if (alram2 == null) {
            swAlarm2.setChecked(false);
            tvName2.setText("Time to rise");
        } else {
            if (PreferUtil.getInstance().getIsAlarmOff2() == 0) {
                swAlarm2.setChecked(false);
            } else {
                swAlarm2.setChecked(true);
            }
            tvName2.setText("Alarm02");
            initAlarm(2);
        }
        alram3 = PreferUtil.getInstance().getAlarmSettingDataList(PreferUtil.CHECKALARMSETTINGBEAN3);
        if (alram3 == null) {
            swAlarm3.setChecked(false);
            tvName3.setText("Time to rise");
        } else {
            if (PreferUtil.getInstance().getIsAlarmOff3() == 0) {
                swAlarm3.setChecked(false);
            } else {
                swAlarm3.setChecked(true);
            }
            tvName3.setText("Alarm03");
            initAlarm(3);
        }
    }

    @OnClick({R.id.rl_time1, R.id.rl_time2, R.id.rl_time3, R.id.tv_done})
    public void onClick(View view) {
        Intent intent = new Intent(ProgramsActivity.this, AlarmSettingActivity.class);
        switch (view.getId()) {
            case R.id.rl_time1:
                intent.putExtra("data", 1);
                startActivityIn(intent, this);
                break;
            case R.id.rl_time2:
                intent.putExtra("data", 2);
                startActivityIn(intent, this);
                break;
            case R.id.rl_time3:
                intent.putExtra("data", 3);
                startActivityIn(intent, this);
                break;
            case R.id.tv_done:
                finish();
                break;

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == swAlarm1) {
            if (alram1 != null) {
                checkStart(isChecked, alram1);
            }
        } else if (buttonView == swAlarm2) {
            if (alram2 != null) {
                checkStart(isChecked, alram2);
            }
        } else if (buttonView == swAlarm3) {
            checkStart(isChecked, alram3);
        }
    }

    private void initAlarm(int type) {
        AlarmSettingBean alarmSettingBean;
        ImageView ivMuisc;
        TextView tvDay;
        TextView tvTime;
        TextView tvOn;
        ImageView ivCheckColor;
        final StringBuffer strWeek = new StringBuffer();
        int count = 0;
        switch (type) {
            case 1:
                alarmSettingBean = alram1;
                ivMuisc = ivMuisc1;
                tvDay = tvDay1;
                tvTime = tvTime1;
                tvOn = tvOn1;
                ivCheckColor = ivCheckColor1;
                break;
            case 2:
                alarmSettingBean = alram2;
                ivMuisc = ivMuisc2;
                tvDay = tvDay2;
                tvTime = tvTime2;
                tvOn = tvOn2;
                ivCheckColor = ivCheckColor2;
                break;
            case 3:
                alarmSettingBean = alram3;
                ivMuisc = ivMuisc3;
                tvDay = tvDay3;
                tvTime = tvTime3;
                tvOn = tvOn3;
                ivCheckColor = ivCheckColor3;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        for (int i = 0; i < Constant.selectedVoiceMusic.length; i++) {
            if (alarmSettingBean.getMusic() == Constant.selectedVoiceMusic[i]) {
                ivMuisc.setBackgroundResource(Constant.unSelectedVoiceDrawable[i]);
            }
        }

        for (int i = 0; i < alarmSettingBean.getWeekBean().size(); i++) {
            if (alarmSettingBean.getWeekBean().get(i).isCheckStart()) {
                strWeek.append(Constant.selectedWeekString[alarmSettingBean.getWeekBean().get(i).getId()]).append("  ");
                count++;
            }
        }
        if (count == 7) {
            tvDay.setText("Every day");
        } else {
            tvDay.setText(strWeek.toString());
        }
        tvTime.setText(addZeroForNum(String.valueOf(alarmSettingBean.getHours()), 2) + ":" +
                addZeroForNum(String.valueOf(alarmSettingBean.getMinutes()), 2));
        if (alarmSettingBean.isTurnOn()) {
            tvOn.setText("Turn on");
        } else {
            tvOn.setText("Turn off");
        }

        for (int i = 0; i < alarmSettingBean.getColorBean().size(); i++) {
            if (alarmSettingBean.getColorBean().get(i).isCheckStart()) {
                ivCheckColor.setBackgroundResource(Constant.selectedColorDrawable[alarmSettingBean.getColorBean().get(i).getId()]);
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkStart(boolean isChecked, AlarmSettingBean alarmSettingBean) {
        String alarmOnly;
        String isOff;
        if (isChecked) {
            alarmSettingBean.getWeek().setCharAt(0, '1');
            PreferUtil.getInstance().setIsAlarmOff1(1);
        } else {
            alarmSettingBean.getWeek().setCharAt(0, '0');
            PreferUtil.getInstance().setIsAlarmOff1(0);
        }
        int i = Integer.parseUnsignedInt(alarmSettingBean.getWeek().toString(), 2);
        String wk = addZeroForNum(Integer.toHexString(i), 2);
        String music = addZeroForNum(Integer.toHexString(alarmSettingBean.getMusic()), 2);
        String Hours = addZeroForNum(Integer.toHexString(alarmSettingBean.getHours()), 2);
        String Minutes = addZeroForNum(Integer.toHexString(alarmSettingBean.getMinutes()), 2);
        String rgb = addZeroForNum(Integer.toHexString(alarmSettingBean.getColors()[0]), 2) +
                addZeroForNum(Integer.toHexString(alarmSettingBean.getColors()[1]), 2) +
                addZeroForNum(Integer.toHexString(alarmSettingBean.getColors()[2]), 2) +
                addZeroForNum(Integer.toHexString(alarmSettingBean.getColors()[3]), 2);
        if (alarmSettingBean.isAlarmOnly()) {
            alarmOnly = "01";
        } else {
            alarmOnly = "02";
        }
        if (alarmSettingBean.isTurnOff()) {
            isOff = "00";
        } else {
            isOff = "aa";
        }
        String byteDate = "06" + "aa" + wk + music + Hours + Minutes + rgb + alarmOnly + isOff;
        write(stringToBytes(byteDate));
    }

}
