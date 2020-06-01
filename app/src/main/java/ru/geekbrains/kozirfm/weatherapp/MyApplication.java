package ru.geekbrains.kozirfm.weatherapp;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyApplication extends Application {

    private static IOpenWeather openWeather;

    @Override
    public void onCreate() {
        super.onCreate();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        openWeather = retrofit.create(IOpenWeather.class);
    }

    public static IOpenWeather getOpenWeather() {
        return openWeather;
    }
}
