package ru.abelov.mapboxtest.presentation.view;

import ru.abelov.mapboxtest.data.entity.WeatherEntity;

public interface CurrentWeatherView {
    void onResult(WeatherEntity model);
    void onError(String message);
}
