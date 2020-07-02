package com.hosmart.ebaby.ui.apadter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hosmart.ebaby.R;
import com.hosmart.ebaby.base.Constant;
import com.hosmart.ebaby.bean.CheckColorBean;

import java.util.List;

public class CheckPepeatAdapter extends BaseQuickAdapter<CheckColorBean, BaseViewHolder> {
    private List<CheckColorBean> data;

    public CheckPepeatAdapter(List<CheckColorBean> data) {
        super(R.layout.item_check_repeat, data);
        this.data = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, final CheckColorBean item) {
        ImageView ivCheckColor = helper.getView(R.id.iv_check_color);
        if (item.isCheckStart()) {
            ivCheckColor.setBackgroundResource(Constant.selectedRepeatDrawable[item.getId()]);
        } else {
            ivCheckColor.setBackgroundResource(Constant.unSelectedRepeatDrawable[item.getId()]);
        }

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRepeatItemClick != null) {
                    onRepeatItemClick.onRepeatItemClick(item);
                }
            }
        });
    }


    private onRepeatItemClick onRepeatItemClick;

    public interface onRepeatItemClick {
        void onRepeatItemClick(CheckColorBean item);
    }

    public void onRepeatItemClick(onRepeatItemClick onRepeatItemClick) {
        this.onRepeatItemClick = onRepeatItemClick;
    }

}
