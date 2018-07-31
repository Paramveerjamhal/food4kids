package com.example.paramveerjamhal.food4kids;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paramveerjamhal.food4kids.Fragments.AboutUsFragment;
import com.example.paramveerjamhal.food4kids.Fragments.ContactUsFragment;
import com.example.paramveerjamhal.food4kids.Fragments.CreateEventFragment;
import com.example.paramveerjamhal.food4kids.Fragments.HomeFragment;
import com.example.paramveerjamhal.food4kids.Fragments.ScheduleFragment;
import com.example.paramveerjamhal.food4kids.Tags.AppConstant;
import com.example.paramveerjamhal.food4kids.adapter.EventAdapter;
import com.example.paramveerjamhal.food4kids.entities.User;
import com.example.paramveerjamhal.food4kids.entities.UserResponse;
import com.example.paramveerjamhal.food4kids.network.ApiService;
import com.example.paramveerjamhal.food4kids.network.RetrofitBuilder;
import com.infideap.drawerbehavior.Advance3DDrawerLayout;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.paramveerjamhal.food4kids.Tags.AppConstant.*;

public class Advance3DDrawer1Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "UserHome";
    private Advance3DDrawerLayout drawer;
    public static int userId_TAG = 0;

    public static int Profile = 0;
    TextView userId;
    TextView userEmail;
    NavigationView navigationView;
    ApiService service;
    TokenManager tokenManager;
    Call<UserResponse> call;
    TextView user_name;
    TextView user_email;
    RelativeLayout edit_rel;
    //arraylist of user type
    private ArrayList<User> user = new ArrayList<User>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tokenManager = TokenManager.getInstance(this.getSharedPreferences("pref", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
        callService();
        setContentView(R.layout.activity_advance_3d_1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.back_button);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        user_name = header.findViewById(R.id.user_name);
        user_email = header.findViewById(R.id.user_email);
        edit_rel = header.findViewById(R.id.edit_relative);

        edit_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Profile = 1;
                Intent newIntent=new Intent(Advance3DDrawer1Activity.this,UserProfile.class);
                newIntent.putExtra("user",user);
                startActivity(newIntent);

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawer = (Advance3DDrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        drawer.setViewScale(Gravity.START, 0.96f);
        drawer.setRadius(Gravity.START, 20);
        drawer.setViewElevation(Gravity.START, 8);
        drawer.setViewRotation(Gravity.START, 15);
        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_home));


    }

    private void callService() {


        call = service.user();
        final ProgressDialog m_Dialog = new ProgressDialog(this);
        m_Dialog.setMessage("Please wait while logging...");
        m_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        m_Dialog.setCancelable(false);
        m_Dialog.show();
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                Log.w(TAG, "onResponse: " + response);

                if (response.isSuccessful()) {
                    Log.w(TAG, "Repsonse of user api ++++:api " + response.body().getData());
                    m_Dialog.dismiss();
                    // title.setText(response.body().getData().get(0).getTitle());
                    if (response.body().getData() != null) {
                        user_Type = response.body().getData().get(0).getUserType();
                        userId_TAG = response.body().getData().get(0).getId();

                        if (response.body().getData().get(0).getUserType().equals("0")) {
                            hideItem();
                        }
                        //  Toast.makeText(Advance3DDrawer1Activity.this,response.body().getData().get(0).getId(),Toast.LENGTH_SHORT).show();
                        user_name.setText(response.body().getData().get(0).getName());
                        user_email.setText((response.body().getData().get(0).getEmail()));

                        user.addAll(response.body().getData());
                    } else {
                        Toast.makeText(Advance3DDrawer1Activity.this, "No data fetched.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                   /*  if(response.code()==401)
                     {
*/
                    startActivity(new Intent(Advance3DDrawer1Activity.this, LoginActivity.class));
                    finish();
                    tokenManager.deleteToken();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();
        // NavUtils.navigateUpFromSameTask(this);
        //   startActivity(new Intent(Advance3DDrawer1Activity.this,Advance3DDrawer1Activity.class));
        displaySelectedScreen(id);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_right_drawer:
                drawer.openDrawer(Gravity.END);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void displaySelectedScreen(int id) {

        Fragment fragment = null;
        switch (id) {
            case R.id.nav_home:
                fragment = new HomeFragment();
                break;
            case R.id.nav_schedule:
                fragment = new ScheduleFragment();
                break;
            case R.id.nav_createEvent:
                fragment = new CreateEventFragment();
                break;
           /*case R.id.nav_manageEvent:
               fragment=new CreateEventFragment();
               break;*/
            case R.id.nav_contactUs:
                fragment = new ContactUsFragment();
                break;
            case R.id.nav_aboutUs:
                fragment = new AboutUsFragment();
                break;
            case R.id.nav_logout:
                tokenManager.deleteToken();
                drawer.openDrawer(Gravity.START);
                logout();
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_content, fragment);
            ft.addToBackStack(null);
            ft.commit();

        }
        drawer.closeDrawer(GravityCompat.START);
    }

    private void logout() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Advance3DDrawer1Activity.this);

        // set title
        alertDialogBuilder.setTitle("Are you sure you want to logout?");

        // set dialog message
        alertDialogBuilder
                .setMessage("Click yes to logout!")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        // After logout redirect user to Loing Activity
                        Intent i = new Intent(Advance3DDrawer1Activity.this, LoginActivity.class);
                        startActivity(i);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });
        alertDialogBuilder.show();
    }

    private void hideItem() {
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_createEvent).setVisible(false);
        edit_rel.setVisibility(View.VISIBLE);
        // nav_Menu.findItem(R.id.nav_manageEvent).setVisible(false);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (EventAdapter.DELETE_TAG.equals("deleted")) {
            Toast.makeText(Advance3DDrawer1Activity.this, "Event Deleted Successfully", Toast.LENGTH_LONG).show();
            EventAdapter.DELETE_TAG = "event_delete";
        }
    }

}
