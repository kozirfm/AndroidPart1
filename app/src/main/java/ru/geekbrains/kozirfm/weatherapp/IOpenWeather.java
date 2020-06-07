package ru.geekbrains.kozirfm.weatherapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.geekbrains.kozirfm.weatherapp.data.WeatherRequest;

public interface IOpenWeather {
    @GET("data/2.5/weather")
    Call<WeatherRequest> loadWeather(@Query("q") String city, @Query("units") String units, @Query("lang") String lang, @Query("appid") String keyApi);
    @GET("data/2.5/weather")
    Call<WeatherRequest> loadLocationWeather(@Query("lat") String lat, @Query("lon") String lon, @Query("units") String units, @Query("lang") String lang, @Query("appid") String keyApi);
}
