package com.example.paramveerjamhal.food4kids;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paramveerjamhal.food4kids.entities.PostResponse;
import com.example.paramveerjamhal.food4kids.network.ApiService;
import com.example.paramveerjamhal.food4kids.network.RetrofitBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostActivity extends AppCompatActivity{

    private static final String TAG = "PostActivity";
    ApiService service;
    TokenManager tokenManager;
    Call<PostResponse> call;
    @BindView(R.id.post_title)
    TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ButterKnife.bind(this);
        tokenManager=TokenManager.getInstance(getSharedPreferences("pref",MODE_PRIVATE));
        service= RetrofitBuilder.createServiceWithAuth(ApiService.class,tokenManager);
    }


    @OnClick(R.id.btn_logout)
    void logout()
    {
       // call=service.logout();
        tokenManager.deleteToken();
        startActivity(new Intent(PostActivity.this,LoginActivity.class));

    }
    @OnClick(R.id.btn_posts)
    void getPosts(){
         call=service.posts();
         call.enqueue(new Callback<PostResponse>() {
             @Override
             public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                 Log.w(TAG, "onResponse: "+response );

                 if(response.isSuccessful()){


                     if(response.body().getData()!=null){
                         Toast.makeText(PostActivity.this,response.body().getData().get(0).getTitle(),Toast.LENGTH_SHORT).show();

                     }
                     else{
                         Toast.makeText(PostActivity.this," Posts No data fetched.",Toast.LENGTH_SHORT).show();

                     }

                        // title.setText(response.body().getData().get(0).getTitle());
                         title.setText("Title");


                 }
                 else{
                   /*  if(response.code()==401)
                     {
*/
                         startActivity(new Intent(PostActivity.this,LoginActivity.class));
                         finish();
                         tokenManager.deleteToken();

                 }

             }

             @Override
             public void onFailure(Call<PostResponse> call, Throwable t) {
                 Log.w(TAG, "onFailure: " + t.getMessage() );
             }
         });




    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(call != null) {
            call.cancel();
            call = null;
        }
    }
}
