package com.example.paramveerjamhal.food4kids.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.paramveerjamhal.food4kids.R;

import butterknife.BindView;
import butterknife.OnClick;

public class ContactUsFragment extends Fragment {

    TextView contact;
    Intent callIntent ;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Contact Us");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.contactus, container, false);
        contact=rootView.findViewById(R.id.contact);
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_no= contact.getText().toString().replaceAll("-", "");
                callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse(phone_no));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(callIntent);
            }
        });
        return rootView;

    }


}
