package com.hosmart.ebaby.ui;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.hosmart.ebaby.R;
import com.hosmart.ebaby.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class ProgramsActivity extends BaseActivity {


    @BindView(R.id.rl_time1)
    RelativeLayout rlTime1;

    @BindView(R.id.rl_time2)
    RelativeLayout rlTime2;

    @BindView(R.id.rl_time3)
    RelativeLayout rlTime3;

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

    @OnClick({R.id.rl_time1, R.id.rl_time2, R.id.rl_time3})
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

        }
    }

}
