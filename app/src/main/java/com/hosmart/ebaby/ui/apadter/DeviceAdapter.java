package com.hosmart.ebaby.ui.apadter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hosmart.ebaby.R;
import com.hosmart.ebaby.bean.CheckColorBean;
import com.inuker.bluetooth.library.beacon.Beacon;
import com.inuker.bluetooth.library.search.SearchResult;

import java.util.List;

public class DeviceAdapter extends BaseQuickAdapter<SearchResult, BaseViewHolder> {

    public DeviceAdapter(List<SearchResult> data) {
        super(R.layout.item_device, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, final SearchResult item) {


        helper.setText(R.id.tv_name,item.getName());
        helper.setText(R.id.tv_mac,item.getAddress());

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClick != null) {
                    onItemClick.onItemClick(item);
                }
            }
        });
    }


    private onItemClick onItemClick;

    public interface onItemClick {
        void onItemClick(SearchResult item);
    }

    public void onItemClick(onItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

}
