package ru.geekbrains.kozirfm.weatherapp;

public interface Constants {
    //Main Activity
    String MAIN_CITY = "MAIN_CITY";
    String MAIN_TEMPERATURE = "MAIN_TEMPERATURE";
    String MAIN_WIND_POWER = "MAIN_WIND_POWER";
    String MAIN_PRESSURE = "MAIN_PRESSURE";
    String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?q=saint%20petersburg&units=metric&lang=ru&appid=";
    String WEATHER_API_KEY = "d21c48b5bc0c4591e9a8085dd9c45454";

    //Settings
    String SHARED_PREFERENCES_NAME = "SETTINGS";
    String IS_DARK_THEME_KEY ="IS_DARK_THEME";
    String IS_METRIC_SETTINGS = "IS_METRIC_SETTINGS";
    String METRICS_TEMPERATURE_ID = "METRICS_TEMPERATURE_ID";
    String METRICS_TEMPERATURE_VALUE = "METRICS_TEMPERATURE_VALUE";
    String METRICS_PRESSURE_ID = "METRICS_PRESSURE_ID";
    String METRICS_PRESSURE_VALUE = "METRICS_PRESSURE_VALUE";
    String METRICS_WIND_POWER_ID = "METRICS_WIND_POWER_ID";
    String METRICS_WIND_POWER_VALUE = "METRICS_WIND_POWER_VALUE";
}
