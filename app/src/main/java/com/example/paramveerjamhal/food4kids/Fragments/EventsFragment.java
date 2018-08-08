package com.example.paramveerjamhal.food4kids.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.paramveerjamhal.food4kids.entities.Event;
import com.example.paramveerjamhal.food4kids.entities.EventResponse;
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

public class EventsFragment  extends Fragment {

    private RecyclerView mMyAnswerRV;
    private static final String TAG = "PostActivity";
    ApiService service;
    TokenManager tokenManager;
    Call<EventResponse> call;
    Call<Weekly_EventResponse> call1;
    @BindView(R.id.post_title)
    TextView title;
    TextView post_body;
    ArrayList<Event> eventsBeanList;
    EventAdapter mEventAdapter;
    ArrayList<WeeklyEvent> weekly_eventlist;
    int event_id;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Home");

           }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.activity_event_layout, container, false);
        ButterKnife.bind(getActivity());
        mMyAnswerRV=rootView.findViewById(R.id.rv_my_events);


        AppBarLayout appBarLayout = (AppBarLayout) rootView.findViewById(R.id.appbar);
        appBarLayout.setVisibility(View.GONE);
        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("pref", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);

        eventsBeanList = new ArrayList<>();
        weekly_eventlist=new ArrayList<>();

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mMyAnswerRV.setLayoutManager(mLayoutManager);
        callWeeklyEventService();
        call = service.showevents();
        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                Log.w(TAG, "onResponse: " + response);

                if (response.isSuccessful()) {

                    Log.w(TAG, "Repsonse of post fragment api ++++:api " + response.body().getData());
                    if (response.body().getData().size() != 0) {

                        List<Event> eventBeanListdata = response.body().getData();
                        eventsBeanList.clear();
                        //eventsBeanList.addAll(eventBeanListdata);

                        for(int i=0;i<eventBeanListdata.size();i++)
                        {
                            for(int j=0;j<weekly_eventlist.size();j++)
                            {
                                Log.e("event id ++++" , String.valueOf(eventBeanListdata.get(i).getEventId()));

                                if((eventBeanListdata.get(i).getEventId())==(weekly_eventlist.get(j).getEvent_id()))
                                {
                                    eventsBeanList.add(eventBeanListdata.get(i));
                                    Log.e("weekly id " , String.valueOf(weekly_eventlist.get(j).getEvent_id()));
                                    Log.e("size  " , String.valueOf(eventsBeanList.size()));
                                }
                            }
                        }

                        mEventAdapter = new EventAdapter(getActivity(), eventsBeanList,weekly_eventlist);
                        mMyAnswerRV.setAdapter(mEventAdapter);

                    } else {
                        Toast.makeText(getActivity(), "No data fetched.", Toast.LENGTH_SHORT).show();

                    }



                } else {
                   /*  if(response.code()==401)
                     {
*/
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                    tokenManager.deleteToken();
                }

            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {

            }

        });









        return rootView;
    }

    private ArrayList<WeeklyEvent> callWeeklyEventService() {

            call1 = service.showWeeklyevents();
            call1.enqueue(new Callback<Weekly_EventResponse>() {
                @Override
                public void onResponse(Call<Weekly_EventResponse> call, Response<Weekly_EventResponse> response) {
                    Log.d(TAG, "+++++++++++++++=onResponse from weekly event : " + response);
                    if (response.isSuccessful()) {


                        Log.w(TAG, "response : " +response.body().getData());
                        if(response.body()!=null) {
                            List<WeeklyEvent> list = response.body().getData();
                            weekly_eventlist.addAll(list);
                        }
                    } else {
                        Toast.makeText(getActivity(), "No data fetched", Toast.LENGTH_SHORT).show();
                               }
                }

                @Override
                public void onFailure(Call<Weekly_EventResponse> call, Throwable t) {
                    Log.w(TAG, "onFailure: " + t+ " personalised message : not working");
                }
            });
            return weekly_eventlist;
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}