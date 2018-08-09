package com.example.paramveerjamhal.food4kids.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
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
import com.example.paramveerjamhal.food4kids.adapter.SpecialEventAdapter;
import com.example.paramveerjamhal.food4kids.entities.Event;
import com.example.paramveerjamhal.food4kids.entities.EventResponse;
import com.example.paramveerjamhal.food4kids.entities.SpecialEvent;
import com.example.paramveerjamhal.food4kids.entities.SpecialEvent_Response;
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

public class SpecialFragment extends Fragment {

    private RecyclerView mMyAnswerRV;
    private static final String TAG = "PostActivity";
    ApiService service;
    TokenManager tokenManager;
    Call<EventResponse> call;
    Call<SpecialEvent_Response> call1;
    @BindView(R.id.post_title)
    TextView title;
    TextView post_body;
    ArrayList<Event> eventsBeanList;
    SpecialEventAdapter specialEventAdapter;
    ArrayList<SpecialEvent> specialEventsList;



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       // getActivity().setTitle("Special Events");
        specialEventAdapter = new SpecialEventAdapter(getActivity(), eventsBeanList,specialEventsList);
        mMyAnswerRV.setAdapter(specialEventAdapter);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.activity_event_layout, container, false);
        ButterKnife.bind(getActivity());
        mMyAnswerRV = rootView.findViewById(R.id.rv_my_events);
        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("pref", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        // toolbar.setVisibility(View.GONE);
        AppBarLayout appBarLayout = (AppBarLayout) rootView.findViewById(R.id.appbar);
        appBarLayout.setVisibility(View.GONE);



        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mMyAnswerRV.setLayoutManager(mLayoutManager);

        eventsBeanList = new ArrayList<>();
        specialEventsList =new ArrayList<>();



        callSpecialEventService();
        call = service.showevents();
        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                Log.w(TAG, "onResponse: " + response);

                if (response.isSuccessful()) {

                    Log.e(TAG, "Repsonse of special fragment api ++++:api " + response.body().getData());
                    if (response.body().getData().size() != 0) {

                        List<Event> eventBeanListdata = response.body().getData();
                        eventsBeanList.clear();
                        //eventsBeanList.addAll(eventBeanListdata);

                        for(int i=0;i<eventBeanListdata.size();i++)
                        {
                            for(int j=0;j<specialEventsList.size();j++)
                            {
                                Log.e("event id in s_fragment" , String.valueOf(eventBeanListdata.get(i).getEventId()));

                                if((eventBeanListdata.get(i).getEventId())==(specialEventsList.get(j).getEvent_id()))
                                {
                                    eventsBeanList.add(eventBeanListdata.get(i));
                                    Log.e("special id " , String.valueOf(specialEventsList.get(j).getEvent_id()));
                                    Log.e("size  " , String.valueOf(eventsBeanList.size()));
                                }
                            }
                        }

                       /* specialEventAdapter = new SpecialEventAdapter(getActivity(), eventsBeanList,specialEventsList);
                        mMyAnswerRV.setAdapter(specialEventAdapter);*/
                       specialEventAdapter.notifyDataSetChanged();

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



    private ArrayList<SpecialEvent> callSpecialEventService() {

        call1 = service.showSpecialevents();
        call1.enqueue(new Callback<SpecialEvent_Response>() {
            @Override
            public void onResponse(Call<SpecialEvent_Response> call, Response<SpecialEvent_Response> response) {
                Log.e(TAG, "+++++++++++++++=onResponse from special event : " + response);
                if (response.isSuccessful()) {

                    Log.w(TAG, "response : " +response.body().getData());
                    if(response.body().getData().size()!=0) {
                        List<SpecialEvent> list = response.body().getData();
                        specialEventsList.addAll(list);
                    }
                } else {
                    Toast.makeText(getActivity(), "No data fetched", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SpecialEvent_Response> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t+ " personalised message : not working");
            }
        });
        return specialEventsList;
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