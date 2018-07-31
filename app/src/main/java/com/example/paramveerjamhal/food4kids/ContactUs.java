package com.example.paramveerjamhal.food4kids;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class ContactUs extends AppCompatActivity{
    TextView rel_contact;
    Intent callIntent;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contactus);
         rel_contact=(TextView) findViewById(R.id.contact);
        rel_contact.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                String phone_no= rel_contact.getText().toString().replaceAll("-", "");
                callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse(phone_no));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(callIntent);
            }
        });

    }
}
