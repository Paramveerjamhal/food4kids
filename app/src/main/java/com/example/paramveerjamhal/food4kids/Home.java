package com.example.paramveerjamhal.food4kids;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paramveerjamhal.food4kids.network.ApiService;
import com.example.paramveerjamhal.food4kids.network.RetrofitBuilder;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class Home extends AppCompatActivity {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    TextView display_name;
    String name,type;
    TokenManager tokenManager;
    ApiService service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        ButterKnife.bind(this);
        tokenManager=TokenManager.getInstance(getSharedPreferences("pref",MODE_PRIVATE));
        service= RetrofitBuilder.createServiceWithAuth(ApiService.class,tokenManager);

        pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // retrieving value from Registration
        name = pref.getString("Name", "");
//        display_name.setText(name);
        type=pref.getString("user_type","");

       Toast.makeText(Home.this,type,Toast.LENGTH_LONG).show();
        // Now set these value into textview of second activity



    }
    private void findId() {
        display_name=(TextView) findViewById(R.id.welcome_text);
        // Now set these value into textview of second activity

    }
  @OnClick(R.id.menu_drawer)
    public void onClick(View v){

  }


  /* private void onClickAction() {
        (R.id.menu_drawer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                *//*Intent i = new Intent(Home.this, NavigationDrawer_Screen.class);
                startActivity(i);*//*
               if(type.equals("Volunteer"))
               {

                   final Dialog dialog = new Dialog(Home.this);
                   dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                   dialog.setContentView(R.layout.transparentdata);
                   RelativeLayout logout=(RelativeLayout)dialog.findViewById(R.id.rel_logout) ;
                   logout.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Home.this);

                           // set title
                           alertDialogBuilder.setTitle("Are you sure you want to logout?");

                           // set dialog message
                           alertDialogBuilder
                                   .setMessage("Click yes to logout!")
                                   .setCancelable(false)
                                   .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                                       public void onClick(DialogInterface dialog,int id) {
                                           // if this button is clicked, close
                                           // current activity
                                           editor = pref.edit();
                                           editor.clear();
                                           editor.commit();
                                           // After logout redirect user to Loing Activity
                                           Intent i = new Intent(Home.this, LoginActivity.class);
                                           startActivity(i);
                                       }
                                   })
                                   .setNegativeButton("No",new DialogInterface.OnClickListener() {
                                       public void onClick(DialogInterface dialog,int id) {
                                           // if this button is clicked, just close
                                           // the dialog box and do nothing
                                           dialog.cancel();
                                       }
                                   });

                           // create alert dialog
                           AlertDialog alertDialog = alertDialogBuilder.create();

                           // show it
                           alertDialog.show();

                       }
                   });

                   RelativeLayout contactus=(RelativeLayout) dialog.findViewById(R.id.rel_contactus);
                   contactus.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           Intent i =new Intent(Home.this, ContactUs.class);
                           startActivity(i);
                       }
                   });

                   RelativeLayout profile=(RelativeLayout) dialog.findViewById(R.id.edit_profile);
                   profile.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           Intent i =new Intent(Home.this, UserProfile.class);
                           startActivity(i);
                       }
                   });
                   dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                   dialog.show();
               }
               else
               {
                   final Dialog dialog = new Dialog(Home.this);
                   dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                   dialog.setContentView(R.layout.admindata);
                   RelativeLayout logout=(RelativeLayout)dialog.findViewById(R.id.rel_logout) ;
                   logout.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Home.this);

                           // set title
                           alertDialogBuilder.setTitle("Are you sure you want to logout?");

                           // set dialog message
                           alertDialogBuilder
                                   .setMessage("Click yes to logout!")
                                   .setCancelable(false)
                                   .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                                       public void onClick(DialogInterface dialog,int id) {
                                           // if this button is clicked, close
                                           // current activity
                                           editor = pref.edit();
                                           editor.clear();
                                           editor.commit();
                                           // After logout redirect user to Loing Activity
                                           Intent i = new Intent(Home.this, LoginActivity.class);
                                           startActivity(i);
                                       }
                                   })
                                   .setNegativeButton("No",new DialogInterface.OnClickListener() {
                                       public void onClick(DialogInterface dialog,int id) {
                                           // if this button is clicked, just close
                                           // the dialog box and do nothing
                                           dialog.cancel();
                                       }
                                   });

                           // create alert dialog
                           AlertDialog alertDialog = alertDialogBuilder.create();

                           // show it
                           alertDialog.show();

                       }
                   });

                   RelativeLayout createevent=(RelativeLayout) dialog.findViewById(R.id.rel_event);
                   createevent.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           Intent i =new Intent(Home.this, CreateEvent.class);
                           startActivity(i);
                       }
                   });
                   dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                   dialog.show();
               }

            }
        });
    }*/


}
