package com.mi.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TourAdapter extends FragmentPagerAdapter{

    final int PAGE_COUNT = 4;
    Context context;
    private int PageKey;

    /** Constructor of the class */
    public TourAdapter(FragmentManager fm, int PageKey, Context context) {
        super(fm);
        this.PageKey = PageKey;
        this.context = context;
    }

    /** This method will be invoked when a page is requested to create */
    @Override
    public Fragment getItem(int arg0) {

        switch(arg0)
        {
            case 0 :
                System.out.println(":::::::::::::::::Case 0::::");
                Tour_one tour_one = new Tour_one();
                Bundle data = new Bundle();
                data.putInt("current_page", arg0+1);
                tour_one.setArguments(data);
                return tour_one;

            case 1 :
                System.out.println(":::::::::::::::::Case 1::::");
                Tour_two tour_two = new Tour_two();
                Bundle tour_twoData = new Bundle();
                tour_twoData.putInt("current_page", arg0+1);
                tour_two.setArguments(tour_twoData);
                return tour_two;

            case 2 :

                System.out.println(":::::::::::::::::Case 2::::");
                Tour_three tour_three = new Tour_three();
                Bundle tour_threeData = new Bundle();
                tour_threeData.putInt("current_page", arg0+1);
                tour_three.setArguments(tour_threeData);
                return tour_three;

            case 3 :

                System.out.println(":::::::::::::::::Case 3::::");
                Tour_four tour_four = new Tour_four();
                Bundle tour_fourData = new Bundle();
                tour_fourData.putInt("current_page", arg0+1);
                tour_four.setArguments(tour_fourData);
                return tour_four;

            default:
                return null;
        }

    }

    /** Returns the number of pages */
    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        String Title = null;

        String titles[] = { "Tour 1", "Tour 2" , "Tour 3" , "Tour 4"};

        Title = titles[position];

        return Title;
    }

}
