package com.example.paramveerjamhal.food4kids.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.example.paramveerjamhal.food4kids.entities.PostResponse;
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
    @BindView(R.id.post_title)
    TextView title;
    TextView post_body;
    ArrayList<Event> eventsBeanList;
    EventAdapter mEventAdapter;

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
        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("pref", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);

        eventsBeanList = new ArrayList<>();


        call = service.showevents();
        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                Log.w(TAG, "onResponse: " + response);

                if (response.isSuccessful()) {

                    Log.w(TAG, "Repsonse of post fragment api ++++:api " + response.body().getData());
                    if (response.body().getData().size() != 0) {

                        List<Event> eventBeanListdata = response.body().getData();

                        //   Toast.makeText(getActivity(),response.body().getData().get(0).getTitle(),Toast.LENGTH_LONG).show();
                        eventsBeanList.addAll(eventBeanListdata);

                    } else {
                        Toast.makeText(getActivity(), "No data fetched.", Toast.LENGTH_SHORT).show();

                    }

                    // title.setText(response.body().getData().get(0).getTitle());
                    mEventAdapter = new EventAdapter(getActivity(), eventsBeanList);
                    mMyAnswerRV.setAdapter(mEventAdapter);

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

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mMyAnswerRV.setLayoutManager(mLayoutManager);

        return rootView;
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