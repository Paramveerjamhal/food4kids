package com.example.paramveerjamhal.food4kids;

import com.example.paramveerjamhal.food4kids.entities.ApiError;
import com.example.paramveerjamhal.food4kids.network.RetrofitBuilder;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class Utils {

    public static ApiError convertErrors(ResponseBody response)
    {
        Converter<ResponseBody,ApiError> converter= RetrofitBuilder.getRetrofit().responseBodyConverter(ApiError.class,new Annotation[0]);
        ApiError apiError=null;

        try{

            apiError=converter.convert(response);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return apiError;
    }
}
