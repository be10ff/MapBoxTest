package ru.abelov.mapboxtest.data.net;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.abelov.mapboxtest.data.entity.WeatherEntity;

public interface Api {

    @GET("v1/current.json")
    Flowable<WeatherEntity> getWeatherByLonLat(
            @Query("key") String apiKey,
            @Query("q") String query);
}
