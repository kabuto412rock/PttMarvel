package com.blogspot.zongjia.pttmarvel.api;

import pl.droidsonroids.retrofit2.JspoonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class RetrofitManager {
    private static RetrofitManager mInstance = new RetrofitManager();
    private Retrofit retrofit;
    private PttService pttService;

    private RetrofitManager() {
        retrofit = new Retrofit.Builder()
                .baseUrl(PttService.BASE_URL)
                .addConverterFactory(JspoonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        pttService = retrofit.create(PttService.class);
    }

    public static PttService getPttService() {
        return mInstance.pttService;
    }
}
