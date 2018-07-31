package com.example.paramveerjamhal.food4kids.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paramveerjamhal.food4kids.LoginActivity;
import com.example.paramveerjamhal.food4kids.PostActivity;
import com.example.paramveerjamhal.food4kids.R;
import com.example.paramveerjamhal.food4kids.TokenManager;
import com.example.paramveerjamhal.food4kids.adapter.TabLayoutTextFragmentAdapter;
import com.example.paramveerjamhal.food4kids.adapter.ViewPagerAdapter;
import com.example.paramveerjamhal.food4kids.entities.PostResponse;
import com.example.paramveerjamhal.food4kids.network.ApiService;
import com.example.paramveerjamhal.food4kids.network.RetrofitBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class PostFragment extends Fragment {

    private static final String TAG = "PostActivity";
    ApiService service;
    TokenManager tokenManager;
    Call<PostResponse> call;
    @BindView(R.id.post_title)
    TextView title;
    TextView post_body;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Home");

    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.activity_post, container, false);
        title=rootView.findViewById(R.id.post_title);
        post_body=rootView.findViewById(R.id.post_body);

        ButterKnife.bind(getActivity());
        tokenManager=TokenManager.getInstance(getActivity().getSharedPreferences("pref",MODE_PRIVATE));
        service= RetrofitBuilder.createServiceWithAuth(ApiService.class,tokenManager);

        call=service.posts();
        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                Log.w(TAG, "onResponse: "+response );

                if(response.isSuccessful()){

                    Log.w(TAG, "Repsonse of post fragment api ++++:api "+response.body().getData() );
if(response.body().getData().size()!=0){

                     //   Toast.makeText(getActivity(),response.body().getData().get(0).getTitle(),Toast.LENGTH_LONG).show();
                        title.setText(response.body().getData().get(0).getTitle());
                        post_body.setText(response.body().getData().get(0).getBody());

                    }
                    else{
                        Toast.makeText(getActivity(),"No data fetched.",Toast.LENGTH_SHORT).show();

                    }

                    // title.setText(response.body().getData().get(0).getTitle());



                }
                else{
                   /*  if(response.code()==401)
                     {
*/
                    startActivity(new Intent(getActivity(),LoginActivity.class));
                    getActivity().finish();
                    tokenManager.deleteToken();

                }

            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage() );
            }
        });

         return rootView;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(call != null) {
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