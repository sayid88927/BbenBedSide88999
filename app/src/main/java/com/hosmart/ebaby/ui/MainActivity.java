package com.hosmart.ebaby.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.utils.ToastUtils;

import com.hosmart.ebaby.R;
import com.hosmart.ebaby.base.BaseActivity;
import com.hosmart.ebaby.base.BaseApplication;
import com.hosmart.ebaby.base.Constant;
import com.hosmart.ebaby.bean.CheckColorBean;
import com.hosmart.ebaby.ui.apadter.CheckColorAdapter;
import com.hosmart.ebaby.utils.Permission;
import com.hosmart.ebaby.utils.PreferUtil;
import com.hosmart.ebaby.view.ColorPickerView;
import com.hosmart.ebaby.view.CustomNumberPicker;
import com.hosmart.ebaby.view.dialog.CustomDialog;
import com.orhanobut.logger.Logger;


import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements CheckColorAdapter.onItemClick, SeekBar.OnSeekBarChangeListener, NumberPicker.Formatter {

    @BindView(R.id.rl_setting)
    RelativeLayout rlSetting;

    @BindView(R.id.ll_light)
    RelativeLayout llLight;

    @BindView(R.id.ll_voice)
    RelativeLayout llVoice;

    @BindView(R.id.ll_timer)
    RelativeLayout llTimer;

    @BindView(R.id.ll_programs)
    RelativeLayout llPrograms;

    @BindView(R.id.tv_timer)
    TextView tvTimer;

    @BindView(R.id.btn_power)
    ImageView ivPower;

    @BindView(R.id.sb_brightness)
    SeekBar sbBrightness;

    @BindView(R.id.sb_volume)
    SeekBar sbVolume;

    @BindView(R.id.rl_half_circle)
    RelativeLayout rlHalfCircle;

    @BindView(R.id.iv_muisc)
    ImageView ivMuisc;

    private boolean powerStart = true;
    private CustomDialog checkDColorDialog;
    private CheckColorAdapter adapter;
    private List<CheckColorBean> data = new ArrayList<>();
    private int Hours, Minute, Seconds;
    private String strHours, strMinutes, strSeconds;
    private ScheduledFuture timerRunnable;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
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

        sbBrightness.setProgress(15);
        sbVolume.setProgress(15);


        sbBrightness.setOnSeekBarChangeListener(this);
        sbVolume.setOnSeekBarChangeListener(this);

        List<CheckColorBean> musicBean = PreferUtil.getInstance().getDataList(PreferUtil.CHECKVOICEBEAN);
        for (int i = 0; i < musicBean.size(); i++) {
            if (musicBean.get(i).isCheckStart()) {
                ivMuisc.setBackgroundResource(Constant.SelectedMusicDrawable[musicBean.get(i).getId()]);
            }
        }
    }

    @OnClick({R.id.rl_setting, R.id.ll_light, R.id.ll_voice, R.id.ll_timer, R.id.ll_programs, R.id.btn_power})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_power:
                if (powerStart) {
                    write(Constant.powerOff);
                    powerStart = false;
                    ivPower.setBackground(getResources().getDrawable(R.drawable.btn_power_off));
                } else {
                    write(Constant.powerOn);
                    powerStart = true;
                    ivPower.setBackground(getResources().getDrawable(R.drawable.btn_power_on));
                }
                break;

            case R.id.rl_setting:
                startActivityIn(new Intent(MainActivity.this, SettingActivity.class), MainActivity.this);
                break;
            case R.id.ll_light:
                showCheckColor(CheckColorAdapter.checkColor);
                break;

            case R.id.ll_voice:
                showCheckColor(CheckColorAdapter.checkVoice);
                break;

            case R.id.ll_timer:
                showCheckTimer();
                break;

            case R.id.ll_programs:
                startActivityIn(new Intent(MainActivity.this, ProgramsActivity.class), MainActivity.this);
                break;

        }
    }

    /**
     * 倒计时计算
     */
    private void computeTime() {
        if (Hours == 0 && Minute == 0 && Seconds == 0) {
            timerRunnable.cancel(false);
        } else {
            Seconds--;
            if (Seconds < 0) {
                Minute--;
                Seconds = 59;
                if (Minute < 0) {
                    Minute = 59;
                    Hours--;
                }
            }
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Message message = Message.obtain();
            message.what = 1;
            timeHandler.sendMessage(message);
        }
    };

    private Handler timeHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                computeTime();
                if (Seconds < 10) {
                    strSeconds = "0" + String.valueOf(Seconds);
                } else {
                    strSeconds = String.valueOf(Seconds);
                }
                if (Minute < 10) {
                    strMinutes = "0" + String.valueOf(Minute);
                } else {
                    strMinutes = String.valueOf(Minute);
                }
                if (Hours < 10) {
                    strHours = "0" + String.valueOf(Hours);
                } else {
                    strHours = String.valueOf(Hours);
                }
                String timer = strHours + ":" + strMinutes + ":" + strSeconds;
                tvTimer.setText(timer);
            }
        }
    };

    private void showCheckTimer() {
        hideDialog();
        Hours =0;
        Minute =0;
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
                if (timerRunnable != null) {
                    timerRunnable.cancel(false);
                    timerRunnable = null;
                }
                Seconds = 0;
                hideDialog();
                Logger.e(String.valueOf(Hours)+"\n"+String.valueOf(Minute));
                timerRunnable = BaseApplication.MAIN_EXECUTOR.scheduleWithFixedDelay(runnable, 0, 1, TimeUnit.SECONDS);

                String byteDate = "08" + "aa" + addZeroForNum(Integer.toHexString(Hours), 2) +
                        addZeroForNum(Integer.toHexString(Minute), 2) +
                        addZeroForNum(Integer.toHexString(0), 2);
                write(stringToBytes(byteDate));

            }
        });
        CustomNumberPicker npHours = (CustomNumberPicker) view.findViewById(R.id.np_hours);
        npHours.setFormatter(this);
        npHours.setMinValue(0);
        npHours.setMaxValue(23);
        npHours.setNumberPickerDividerColor(npHours);
        npHours.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        npHours.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Hours = newVal;
            }
        });
        CustomNumberPicker npMinute = (CustomNumberPicker) view.findViewById(R.id.np_minute);
        npMinute.setFormatter(this);
        npMinute.setMinValue(0);
        npMinute.setMaxValue(59);
        npMinute.setNumberPickerDividerColor(npMinute);
        npMinute.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        npMinute.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Minute = newVal;
            }
        });

        checkDColorDialog = new CustomDialog(this, view, R.style.ActivityDialogStyle);
        checkDColorDialog.show();
        checkDColorDialog.setCancelable(true);

    }

    private void showCheckColor(int type) {
        hideDialog();
        this.data.clear();
        if (type == CheckColorAdapter.checkColor) {
            data = PreferUtil.getInstance().getDataList(PreferUtil.CHECKCOLORBEAN);
            if (data == null || data.size() <= 0)
                initCheckColorData(type);
        } else if (type == CheckColorAdapter.checkVoice) {
            data = PreferUtil.getInstance().getDataList(PreferUtil.CHECKVOICEBEAN);

            if (data == null || data.size() <= 0)
                initCheckColorData(type);
        }

        View view = View.inflate(BaseApplication.getContext(), R.layout.dialog_check_color, null);
        RecyclerView rvCheckColor = view.findViewById(R.id.rv_check_color);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        if (type == CheckColorAdapter.checkColor) {
            tvTitle.setText("Select a Color");
        } else if (type == CheckColorAdapter.checkVoice) {
            tvTitle.setText("Select a Track");
        }
        rvCheckColor.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
        adapter = new CheckColorAdapter(data, type);
        rvCheckColor.setAdapter(adapter);
        adapter.onItemClick(MainActivity.this);

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

                    PreferUtil.getInstance().setDataList(PreferUtil.CHECKCOLORBEAN, data);
                    hideDialog();
                } else if (CheckColorAdapter.getType() == CheckColorAdapter.checkVoice) {
                    PreferUtil.getInstance().setDataList(PreferUtil.CHECKVOICEBEAN, data);
                    hideDialog();
                }
            }
        });

        checkDColorDialog = new CustomDialog(this, view, R.style.ActivityDialogStyle);
        checkDColorDialog.show();
        checkDColorDialog.setCancelable(true);

    }

    private void hideDialog() {
        if (checkDColorDialog != null) {
            checkDColorDialog.dismiss();
            checkDColorDialog = null;
        }
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
            PreferUtil.getInstance().setDataList(PreferUtil.CHECKCOLORBEAN, data);
        } else if (type == CheckColorAdapter.checkVoice) {
            for (int i = 0; i < Constant.selectedVoiceDrawable.length; i++) {
                CheckColorBean checkColorBean = new CheckColorBean();
                checkColorBean.setId(i);
                checkColorBean.setCheckStart(false);
                checkColorBean.setSelectedDrawable(Constant.selectedVoiceDrawable[i]);
                checkColorBean.setUnSelectedDrawable(Constant.unSelectedVoiceDrawable[i]);
                data.add(checkColorBean);
            }
            PreferUtil.getInstance().setDataList(PreferUtil.CHECKVOICEBEAN, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private long firstClick;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - firstClick > 2000) {
                firstClick = System.currentTimeMillis();
                ToastUtils.showShortToast("再按一次退出");
            } else {
                System.exit(0);
            }
            return true;
        }
        return false;
    }

    @Subscribe
    public void getEventBus() {

    }

    @Override
    public void onItemClick(CheckColorBean item) {
        int selectedVoiceMusic = 0;
        int[] wrbg = null;
        for (int i = 0; i < data.size(); i++) {
            if (item.getId() == data.get(i).getId()) {
                data.get(i).setCheckStart(true);
                selectedVoiceMusic = Constant.selectedVoiceMusic[data.get(i).getId()];
                wrbg = Constant.wRGB[data.get(i).getId()];
                if (CheckColorAdapter.getType() == CheckColorAdapter.checkColor && item.getId() != data.size() - 1) {
                    rlHalfCircle.setBackgroundResource(Constant.halfCircle[data.get(i).getId()]);
                } else if (CheckColorAdapter.getType() == CheckColorAdapter.checkVoice) {
                    ivMuisc.setBackgroundResource(Constant.SelectedMusicDrawable[data.get(i).getId()]);
                }
            } else {
                data.get(i).setCheckStart(false);
            }
        }

        adapter.notifyDataSetChanged();
        if (CheckColorAdapter.getType() == CheckColorAdapter.checkColor && item.getId() == data.size() - 1) {
            showPickerDialog();
        } else if (CheckColorAdapter.getType() == CheckColorAdapter.checkVoice) {
            String byteData = "0c" + "aa" + addZeroForNum(Integer.toHexString(selectedVoiceMusic), 2);
            write(stringToBytes(byteData));
        } else if (CheckColorAdapter.getType() == CheckColorAdapter.checkColor) {
            String byteData = "02" + "aa" + addZeroForNum(Integer.toHexString(wrbg[0]), 2) +
                    addZeroForNum(Integer.toHexString(wrbg[1]), 2) +
                    addZeroForNum(Integer.toHexString(wrbg[2]), 2) +
                    addZeroForNum(Integer.toHexString(wrbg[3]), 2);
            write(stringToBytes(byteData));
        }
    }

    private void showPickerDialog() {

        View view = View.inflate(BaseApplication.getContext(), R.layout.dialog_picker, null);
        ColorPickerView colorPicker = (ColorPickerView) view.findViewById(R.id.colorPicker);
        colorPicker.setCornorCircleType(ColorPickerView.TYPE_FILL);
        colorPicker.setDrawMagnifyBounds(false);
        colorPicker.setDrawMagnifyCircle(false);
        final int[] rgb = new int[4];
        colorPicker.onCheckRgbClick(new ColorPickerView.onCheckRgbClick() {
            @Override
            public void onCheckRgbClick(int item) {
                String byteData = "02" + "aa" + "00" + addZeroForNum(Integer.toHexString(Color.red(item)), 2) +
                        addZeroForNum(Integer.toHexString(Color.green(item)), 2) +
                        addZeroForNum(Integer.toHexString(Color.blue(item)), 2);
                write(stringToBytes(byteData));
                rgb[1] = 00;
                rgb[1] = Color.red(item);
                rgb[2] = Color.green(item);
                rgb[3] = Color.blue(item);
                Constant.wRGB[10] = rgb;
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
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar == sbBrightness) {
            if (progress <= 3) {
                sbBrightness.setProgress(3);
                progress = 0;
            }
            if(progress >= 27){
                sbBrightness.setProgress(27);
                progress = 30;
            }
            String byteDate = "04" + "aa" + addZeroForNum(Integer.toHexString(progress), 2);
            write(stringToBytes(byteDate));
        } else if (seekBar == sbVolume) {
            if (progress <= 3) {
                sbVolume.setProgress(3);
                progress = 0;
            }
            if(progress >= 27){
                sbVolume.setProgress(27);
                progress = 30;
            }
            String byteDate = "03" + "aa" + addZeroForNum(Integer.toHexString(progress), 2);
            write(stringToBytes(byteDate));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public String format(int value) {
        String tmpStr = String.valueOf(value);
        if (value < 10) {
            tmpStr = "0" + tmpStr;
        }
        return tmpStr;
    }
}


