package ru.geekbrains.kozirfm.weatherapp;

import ru.geekbrains.kozirfm.weatherapp.data.WeatherRequest;

public class WeatherData {

    private String cityName;
    private float temperature;
    private float windPower;
    private float pressure;

    public WeatherData(WeatherRequest weatherRequest) {
        this.cityName = weatherRequest.getName();
        this.temperature = weatherRequest.getMain().getTemp();
        this.windPower = weatherRequest.getWind().getSpeed();
        this.pressure = weatherRequest.getMain().getPressure();
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
