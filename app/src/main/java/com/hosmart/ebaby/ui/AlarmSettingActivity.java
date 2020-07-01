package com.hosmart.ebaby.ui;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hosmart.ebaby.R;
import com.hosmart.ebaby.base.BaseActivity;
import com.hosmart.ebaby.base.BaseApplication;
import com.hosmart.ebaby.base.Constant;
import com.hosmart.ebaby.bean.CheckColorBean;
import com.hosmart.ebaby.ui.apadter.CheckColorAdapter;
import com.hosmart.ebaby.ui.apadter.CheckPepeatAdapter;
import com.hosmart.ebaby.utils.PreferUtil;
import com.hosmart.ebaby.view.CustomNumberPicker;
import com.hosmart.ebaby.view.dialog.CustomDialog;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.defaults.colorpicker.ColorObserver;
import top.defaults.colorpicker.ColorPickerView;

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

    private CustomDialog checkTimeDialog;
    private CheckPepeatAdapter adapter;
    private List<CheckColorBean> data = new ArrayList<>();


    private CheckColorAdapter checkColorAdapter;


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
        } else if (postion == 2) {
            tvAlarmPostion.setText("Alarm02");
        } else if (postion == 3) {
            tvAlarmPostion.setText("Alarm03");
        }
    }

    @OnClick({R.id.rl_alarm_time, R.id.rl_repeat, R.id.ll_light,R.id.ll_sound})
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
        }
    }

    private void showCheckRepeat() {
        hideDialog();

        this.data.clear();
        initCheckRepeatData();

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
            checkColorBean.setId(i + 1);
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
                hideDialog();
            }
        });
        CustomNumberPicker npHours = (CustomNumberPicker) view.findViewById(R.id.np_hours);
        npHours.setMinValue(0);
        npHours.setMaxValue(24);
        npHours.setNumberPickerDividerColor(npHours);
        npHours.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        CustomNumberPicker npMinute = (CustomNumberPicker) view.findViewById(R.id.np_minute);
        npMinute.setMinValue(0);
        npMinute.setMaxValue(60);
        npMinute.setNumberPickerDividerColor(npMinute);
        npMinute.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

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
                } else if (CheckColorAdapter.getType() == CheckColorAdapter.checkVoice) {
                    PreferUtil.getInstance().setDataList(PreferUtil.CHECKVOICEALARMBEAN, data);
                    hideDialog();
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
                checkColorBean.setId(i + 1);
                checkColorBean.setCheckStart(false);
                checkColorBean.setSelectedDrawable(Constant.selectedColorDrawable[i]);
                checkColorBean.setUnSelectedDrawable(Constant.unSelectedColorDrawable[i]);
                data.add(checkColorBean);
            }
            PreferUtil.getInstance().setDataList(PreferUtil.CHECKCOLORALARMBEAN, data);
        } else if (type == CheckColorAdapter.checkVoice) {
            for (int i = 0; i < Constant.selectedVoiceDrawable.length; i++) {
                CheckColorBean checkColorBean = new CheckColorBean();
                checkColorBean.setId(i + 1);
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
            if (item.getId() == data.get(i).getId())
                data.get(i).setCheckStart(true);
            else
                data.get(i).setCheckStart(false);
        }
        checkColorAdapter.notifyDataSetChanged();

        if (CheckColorAdapter.getType() == CheckColorAdapter.checkColor && item.getId() == 12) {
            showPickerDialog();
        }
    }

    private void showPickerDialog() {
        View view = View.inflate(BaseApplication.getContext(), R.layout.dialog_picker, null);
        ColorPickerView colorPicker = (ColorPickerView) view.findViewById(R.id.colorPicker);

        colorPicker.subscribe(new ColorObserver() {
            @Override
            public void onColor(int color, boolean fromUser, boolean shouldPropagate) {
                Logger.e(String.valueOf(color));
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
                } else {
                    data.get(i).setCheckStart(true);
                }
                adapter.notifyDataSetChanged();
            }
        }
    }
}
