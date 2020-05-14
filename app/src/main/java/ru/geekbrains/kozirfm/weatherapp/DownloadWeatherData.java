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


    public boolean downloadWeather() {
        HttpsURLConnection urlConnection = null;
        try {
            URL uri = new URL(WEATHER_URL + BuildConfig.WEATHER_API_KEY);
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
            return false;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return true;
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
        cityName = weatherRequest.getName();
        temperature = weatherRequest.getMain().getTemp();
        windPower = weatherRequest.getWind().getSpeed();
        pressure = weatherRequest.getMain().getPressure();
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
