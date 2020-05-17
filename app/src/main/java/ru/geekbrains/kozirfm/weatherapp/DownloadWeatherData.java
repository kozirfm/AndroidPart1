package ru.geekbrains.kozirfm.weatherapp;


import android.os.Handler;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import ru.geekbrains.kozirfm.weatherapp.data.WeatherRequest;

public class DownloadWeatherData implements Constants {

    public DownloadWeatherData(String city, final Callback callback) {
        if(city.equals("")){
            city = "saint petersburg";
        }

        try {
            final URL uri = new URL(String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&lang=ru&appid=", city) + BuildConfig.WEATHER_API_KEY);
            final Handler handler = new Handler();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpsURLConnection urlConnection = null;
                    try {
                        urlConnection = (HttpsURLConnection) uri.openConnection();
                        urlConnection.setConnectTimeout(3000);
                        urlConnection.setRequestMethod("GET");
                        urlConnection.setReadTimeout(3000);
                        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        String result = resultInputStream(in);
                        Gson gson = new Gson();
                        final WeatherRequest weatherRequest = gson.fromJson(result, WeatherRequest.class);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.getData(new WeatherData(weatherRequest));
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (urlConnection != null) {
                            urlConnection.disconnect();
                        }
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    interface Callback{
        void getData(WeatherData weatherData);
    }

    private String resultInputStream(BufferedReader in) throws IOException {
        String line;
        StringBuilder lines = new StringBuilder();
        while ((line = in.readLine()) != null) {
            lines.append(line);
        }
        return lines.toString();
    }

}
