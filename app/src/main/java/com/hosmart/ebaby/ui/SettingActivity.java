package com.hosmart.ebaby.ui;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.hosmart.ebaby.R;
import com.hosmart.ebaby.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.rl_done)
    RelativeLayout rlDone;

    @BindView(R.id.rl_device)
    RelativeLayout rlDevice;


    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }

    @Override
    public void initView() {

    }

    @OnClick({R.id.rl_done,R.id.rl_device})
    public  void  onClick(View view){
        switch (view.getId()){
            case R.id.rl_done:
                finish();
                break;
            case R.id.rl_device:
                startActivityIn(new Intent(SettingActivity.this,DeviceActivity.class),SettingActivity.this);
                break;
        }
    }

}
