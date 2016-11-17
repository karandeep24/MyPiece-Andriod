package com.mi.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by karandsingh on 16-09-16.
 */
public class My_PagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;


    public My_PagerAdapter(FragmentManager fm, List<Fragment> fragments) {

        super(fm);
        this.fragments = fragments;

    }

    @Override

    public Fragment getItem(int position) {

        return this.fragments.get(position);
    }


    @Override

    public int getCount() {

        return this.fragments.size();
    }

}
