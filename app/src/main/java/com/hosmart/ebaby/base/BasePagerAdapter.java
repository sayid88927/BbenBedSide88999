package com.hosmart.ebaby.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Mervin on 2018/5/10 0010.
 */

public class BasePagerAdapter extends FragmentPagerAdapter {
    String[] titles;
    private List<Fragment> listfragment; //创建一个List<Fragment>

    public BasePagerAdapter(FragmentManager fm, List<Fragment> list, String[] title) {
        super(fm);
        this.titles = title;
        this.listfragment = list;
    }

    @Override
    public Fragment getItem(int position) {
        return listfragment.get(position);
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
