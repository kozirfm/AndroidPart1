package ru.geekbrains.kozirfm.weatherapp;


import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import ru.geekbrains.kozirfm.weatherapp.data.WeatherRequest;

public class DownloadWeatherData implements Constants {

    private String cityName;
    private float temperature;
    private float windPower;
    private float pressure;
    private boolean download = true;
    private String city;

    public DownloadWeatherData() {
        city = "saint petersburg";
    }

    public DownloadWeatherData(String city) {
        this.city = city;
    }

    public void downloadWeather() {
        try {
            final URL uri = new URL(String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&lang=ru&appid=", city) + BuildConfig.WEATHER_API_KEY);
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
                        WeatherRequest weatherRequest = gson.fromJson(result, WeatherRequest.class);
                        setWeatherParams(weatherRequest);
                    } catch (Exception e) {
                        e.printStackTrace();
                        download = false;
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

    private String resultInputStream(BufferedReader in) throws IOException {
        String line;
        StringBuilder lines = new StringBuilder();
        while ((line = in.readLine()) != null) {
            lines.append(line);
        }
        return lines.toString();
    }

    private void setWeatherParams(WeatherRequest weatherRequest) {
        this.cityName = weatherRequest.getName();
        this.temperature = weatherRequest.getMain().getTemp();
        this.windPower = weatherRequest.getWind().getSpeed();
        this.pressure = weatherRequest.getMain().getPressure();
    }

    public boolean isDownload() {
        return download;
    }

    public String getCityName() {
        return cityName;
    }

    public float getTemperature() {
        return temperature;
    }

    public float getWindPower() {
        return windPower;
    }

    public float getPressure() {
        return pressure;
    }
}
