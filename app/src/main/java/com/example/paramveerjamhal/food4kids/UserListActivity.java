package com.example.paramveerjamhal.food4kids;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paramveerjamhal.food4kids.LoginActivity;
import com.example.paramveerjamhal.food4kids.R;
import com.example.paramveerjamhal.food4kids.TokenManager;
import com.example.paramveerjamhal.food4kids.adapter.EventAdapter;
import com.example.paramveerjamhal.food4kids.adapter.UserAdapter;
import com.example.paramveerjamhal.food4kids.entities.Event;
import com.example.paramveerjamhal.food4kids.entities.EventResponse;
import com.example.paramveerjamhal.food4kids.entities.UserWithEventTAsk;
import com.example.paramveerjamhal.food4kids.entities.UserWithEventTaskResponse;
import com.example.paramveerjamhal.food4kids.entities.WeeklyEvent;
import com.example.paramveerjamhal.food4kids.entities.Weekly_EventResponse;
import com.example.paramveerjamhal.food4kids.network.ApiService;
import com.example.paramveerjamhal.food4kids.network.RetrofitBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class UserListActivity extends AppCompatActivity {

    private static final String TAG = "UserListActivity";
    ApiService service;
    TokenManager tokenManager;
    Call<UserWithEventTaskResponse> call;
    Call<Weekly_EventResponse> call1;
    UserAdapter mUserAdapter;
    ArrayList<UserWithEventTAsk> userwithTaskList;
    @BindView(R.id.rv_my_events)
     RecyclerView mMyUserRV;
    @BindView(R.id.tv_no_result)
    TextView tv_noResult;

    ArrayList<WeeklyEvent> aListModel=new ArrayList<WeeklyEvent>();
    int position,event_id;
    String date,weekly_task;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event_layout);
        ButterKnife.bind(this);
        tokenManager = TokenManager.getInstance(this.getSharedPreferences("pref", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AppBarLayout appBarLayout=(AppBarLayout)findViewById(R.id.appbar);
        appBarLayout.setVisibility(View.VISIBLE);
        toolbar.setVisibility(View.VISIBLE);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
        toolbar.setBackgroundColor(getResources().getColor(R.color.green_theme));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mMyUserRV.setLayoutManager(mLayoutManager);
        date=this.getIntent().getStringExtra("date");
        weekly_task=this.getIntent().getStringExtra("task");
        this.setTitle(weekly_task+" User Participated");
        userwithTaskList=new ArrayList<>();
        CallUserInfoService(date);
    }

    public void CallUserInfoService(String date) {
        call = service.ViewUserWithEventTask(date);
        call.enqueue(new Callback<UserWithEventTaskResponse>() {
            @Override
            public void onResponse(Call<UserWithEventTaskResponse> call, Response<UserWithEventTaskResponse> response) {
                Log.w(TAG, "onResponse: " + response);

                if (response.isSuccessful()) {
                    Log.w(TAG, "Repsonse of User ListActivity fragment api ++++:api " + response.body().getData());
                    if (response.body().getData().size() != 0) {
                        List<UserWithEventTAsk> userWithEventTAskList=response.body().getData();
                        userwithTaskList.addAll(userWithEventTAskList);
                        mUserAdapter = new UserAdapter(UserListActivity.this,userwithTaskList);
                        mMyUserRV.setAdapter(mUserAdapter) ;
                        mUserAdapter.notifyDataSetChanged();
                        tv_noResult.setVisibility(View.GONE);
                    } else {
                     //   Toast.makeText(UserListActivity.this, "No data fetched.", Toast.LENGTH_SHORT).show();
                         tv_noResult.setVisibility(View.VISIBLE);
                        tv_noResult.setText("No user participated yet.");
                    }
                } else {
                    tv_noResult.setVisibility(View.VISIBLE);
                    finish();
                    tokenManager.deleteToken();

                }

            }

            @Override
            public void onFailure(Call<UserWithEventTaskResponse> call, Throwable t) {

            }

        });


    }






    @Override
    public void onDestroy() {
        super.onDestroy();
        if (call != null) {
            call.cancel();
            call = null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}