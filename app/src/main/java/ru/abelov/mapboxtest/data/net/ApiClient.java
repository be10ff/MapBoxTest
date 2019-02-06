package ru.abelov.mapboxtest.data.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {


    // Key: 7fbe786e26cc4f9180181133190602
    // http://api.apixu.com/v1/current.json?key=7fbe786e26cc4f9180181133190602&q=55.75,37.62

    private static final String WEBSERVICE_BASE_URL ="http://api.apixu.com/";

    public ApiClient() {

    }

    public Api getApi() {

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        Gson gson = new GsonBuilder().create();

        Retrofit.Builder builder = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(WEBSERVICE_BASE_URL);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();

        builder.client(client);
        return builder.build().create(Api.class);
    }


}
