package com.gym.fitnesszone;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by sahil on 23/06/2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[];
    int NumbOfTabs;
    FragmentManager fm;

    public ViewPagerAdapter(FragmentManager fa, CharSequence[] titles, int numboftabs) {

        super(fa);
        fm=fa;
        this.Titles = titles;
        this.NumbOfTabs = numboftabs;
    }

    @Override
    public Fragment getItem(int position) {
        Tab1 tab1;
        switch (position) {

            case 0:
                tab1 = new Tab1();
                return tab1;
            case 1:
                Tab2 tab2 = new Tab2();
                return (tab2);
            default:
                tab1 = new Tab1();
                return tab1;
        }
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }
    @Override
    public int getCount() {
        return NumbOfTabs;
    }

}