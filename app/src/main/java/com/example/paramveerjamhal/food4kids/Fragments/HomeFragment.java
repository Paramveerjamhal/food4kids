package com.example.paramveerjamhal.food4kids.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.example.paramveerjamhal.food4kids.R;
import com.example.paramveerjamhal.food4kids.adapter.TabLayoutTextFragmentAdapter;
import com.example.paramveerjamhal.food4kids.adapter.ViewPagerAdapter;

public class HomeFragment extends Fragment {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Home");

    }


    TabLayout tabLayout;
    ViewPager viewPager;
    View rootview;
    TabLayoutTextFragmentAdapter fragmentAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.home_fragment, container, false);

        viewPager = (ViewPager) rootView.findViewById(R.id.pager1);
       // setupViewPager(viewPager);

        tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout1);
        tabLayout.addTab(tabLayout.newTab().setText("Events"));
        tabLayout.addTab(tabLayout.newTab().setText("Special Events"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
       // tabLayout.setupWithViewPager(viewPager);


        fragmentAdapter = new TabLayoutTextFragmentAdapter(getChildFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(fragmentAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab LayoutTab) {

                viewPager.setCurrentItem(LayoutTab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab LayoutTab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab LayoutTab) {

            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void setupViewPager(ViewPager viewPager) {
        FragmentManager FManager = getChildFragmentManager();
        ViewPagerAdapter adapter = new ViewPagerAdapter(FManager);

        viewPager.setAdapter(adapter);


/*
        rootview=inflater.inflate(R.layout.home_fragment,container,false);


        tabLayout = (TabLayout)rootview.findViewById(R.id.tab_layout1);
        viewPager = (ViewPager) rootview.findViewById(R.id.pager1);

        tabLayout.addTab(tabLayout.newTab().setText("Events"));
        tabLayout.addTab(tabLayout.newTab().setText("Special Events"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        fragmentAdapter = new TabLayoutTextFragmentAdapter(getChildFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(fragmentAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab LayoutTab) {

                viewPager.setCurrentItem(LayoutTab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab LayoutTab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab LayoutTab) {

            }
        });
        return rootview;*/

    }

    @Override
    public void onResume() {
        super.onResume();
        viewPager.setAdapter(fragmentAdapter);
    }


}