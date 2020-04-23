package ru.geekbrains.kozirfm.weatherapp;

import org.parceler.Parcel;

@Parcel
class WeatherCity {
    String city;
    String temperature;
    String pressure;
    String windPower;

    WeatherCity() {
    }

    WeatherCity(String city, String temperature, String pressure, String windPower) {
        this.city = city;
        this.temperature = temperature;
        this.pressure = pressure;
        this.windPower = windPower;
    }
}
