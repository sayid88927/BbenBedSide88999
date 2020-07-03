package com.hosmart.ebaby.ui;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.hosmart.ebaby.R;
import com.hosmart.ebaby.base.BaseActivity;
import com.hosmart.ebaby.bean.AlarmSettingBean;
import com.hosmart.ebaby.utils.PreferUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class ProgramsActivity extends BaseActivity {


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

    private AlarmSettingBean alram1,alram2,alram3;

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
        alram1 = PreferUtil.getInstance().getAlarmSettingDataList(PreferUtil.CHECKALARMSETTINGBEAN1);
        if(alram1==null || !alram1.isTurnOn()){
            swAlarm1.setChecked(false);
        }else {
            swAlarm1.setChecked(true);
        }
        alram2 = PreferUtil.getInstance().getAlarmSettingDataList(PreferUtil.CHECKALARMSETTINGBEAN2);
        if(alram2==null || !alram2.isTurnOn()){
            swAlarm2.setChecked(false);
        }else {
            swAlarm2.setChecked(true);
        }
        alram3 = PreferUtil.getInstance().getAlarmSettingDataList(PreferUtil.CHECKALARMSETTINGBEAN3);
        if(alram3==null || !alram3.isTurnOn()){
            swAlarm3.setChecked(false);
        }else {
            swAlarm3.setChecked(true);
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

}
