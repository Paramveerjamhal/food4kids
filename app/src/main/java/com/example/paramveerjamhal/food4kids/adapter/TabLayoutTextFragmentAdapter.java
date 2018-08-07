package com.example.paramveerjamhal.food4kids.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.paramveerjamhal.food4kids.Fragments.EventsFragment;
import com.example.paramveerjamhal.food4kids.Fragments.SpecialFragment;

public class TabLayoutTextFragmentAdapter extends FragmentStatePagerAdapter {

    private static final int TAB_1 = 0;
    private static final int TAB_2 = 1;

    private int nbTabs;

    public TabLayoutTextFragmentAdapter(FragmentManager fragmentManager, int nbTabs) {

        super(fragmentManager);

        this.nbTabs = nbTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case TAB_1:
                EventsFragment tab1 = new EventsFragment();
                return tab1;

            case TAB_2:
                SpecialFragment tab2 = new SpecialFragment();
                return tab2;


            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return nbTabs;
    }
}

