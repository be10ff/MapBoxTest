package ru.abelov.mapboxtest.data.entity;

import com.google.gson.annotations.SerializedName;

public class WeatherEntity {

    @SerializedName("location")
    public Location location;

    @SerializedName("current")
    public Current current;

    public class Current {
        @SerializedName("temp_c")
        public String tempC;
    }

    public class Location {
        @SerializedName("lon")
        public double lon;

        @SerializedName("lat")
        public double lat;
    }
}
