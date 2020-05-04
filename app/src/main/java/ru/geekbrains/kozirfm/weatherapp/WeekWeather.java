package ru.geekbrains.kozirfm.weatherapp;

public class WeekWeather {
    private String weekDay;
    private String weekTemp;

    public WeekWeather(String weekDay, String weekTemp) {
        this.weekDay = weekDay;
        this.weekTemp = weekTemp;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public String getWeekTemp() {
        return weekTemp;
    }
}
