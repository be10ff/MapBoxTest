package ru.abelov.mapboxtest.domain;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ru.abelov.mapboxtest.data.net.ApiClient;
import ru.abelov.mapboxtest.presentation.view.CurrentWeatherView;


public class WeatherInteractor {

    private CurrentWeatherView view;
    private CompositeDisposable subscription = new CompositeDisposable();


    public WeatherInteractor() {
    }

    public void setView(CurrentWeatherView view) {  this.view = view;   }

    public void destroy() {
        subscription.dispose();
    }

    public void getCurrentWeather(double lon, double lat) {
        String query = new StringBuilder()
                .append(lon).append(",").append(lat).toString();

        subscription.add(new ApiClient().getApi()
                .getWeatherByLonLat("7fbe786e26cc4f9180181133190602", query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(r -> view.onResult(r), e -> view.onError(e.toString()))
        );

    }

}
