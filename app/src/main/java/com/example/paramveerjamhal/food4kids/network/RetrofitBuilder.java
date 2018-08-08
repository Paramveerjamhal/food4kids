package com.example.paramveerjamhal.food4kids.network;

import android.util.Log;

import com.example.paramveerjamhal.food4kids.BuildConfig;
import com.example.paramveerjamhal.food4kids.TokenManager;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class RetrofitBuilder {


   // private static final String BASE_URL="http://10.192.235.51/food4kids/web/public/api/";
      private static final String BASE_URL="http://192.168.0.13/food4kids/web/public/api/";


    private final static  OkHttpClient client=buildClient();

    private final static Retrofit retrofit=buildRetrofit(client);



    private static OkHttpClient buildClient(){
        OkHttpClient.Builder builder=new OkHttpClient.Builder()
                .connectTimeout(3000, TimeUnit.SECONDS)
                .readTimeout(3000,TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request=chain.request();
                        Request.Builder builder=request.newBuilder()
                                .addHeader("Accept","application/json")
                                .addHeader("Connection","close");
                        request=builder.build();
                        Log.w("api calling++", "+++request generated++++ : "+request);
                        return chain.proceed(request);
                    }
                });
        if(BuildConfig.DEBUG){

            builder.addNetworkInterceptor(new StethoInterceptor());
        }
        return builder.build();
    }

    private static Retrofit buildRetrofit(OkHttpClient client){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();
    }

    public static <T> T createService(Class<T> service)
    {
        return retrofit.create(service);
    }

    public static <T> T createServiceWithAuth(Class<T> service, final TokenManager tokenManager){
        OkHttpClient newClient=client.newBuilder().addInterceptor( new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
              Request request=chain.request();
              Request.Builder builder=request.newBuilder();
              if(tokenManager.getToken().getAccessToken()!=null)
              {
                  builder.addHeader("Authorization","Bearer "+ tokenManager.getToken().getAccessToken());
              }
               request=builder.build();
                return chain.proceed(request);
            }
        }).build();/*.authenticator(CustomAuthenticator.getINSTANCE(tokenManager))*/

        Retrofit newretrofit=retrofit.newBuilder().client(newClient).build();
        return newretrofit.create(service);
    }

    public static Retrofit getRetrofit(){
        return retrofit;
    }

}


