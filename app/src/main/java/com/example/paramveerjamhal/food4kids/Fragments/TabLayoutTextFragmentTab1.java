package com.example.paramveerjamhal.food4kids.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.paramveerjamhal.food4kids.R;

public class TabLayoutTextFragmentTab1 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_login, container, false);

    }
}
