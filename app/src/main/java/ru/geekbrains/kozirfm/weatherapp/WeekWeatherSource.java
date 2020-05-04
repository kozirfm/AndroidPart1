package ru.geekbrains.kozirfm.weatherapp;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

public class WeekWeatherSource {
    private List<WeekWeather> dataSource;
    private Resources resources;

    public WeekWeatherSource(Resources resources) {
        this.dataSource = new ArrayList<>(7);
        this.resources = resources;
    }

    public WeekWeather getWeekWeather(int position){
        return dataSource.get(position);
    }

    public int size(){
        return dataSource.size();
    }

    public WeekWeatherSource build() {
        String[] weekDay = resources.getStringArray(R.array.weekDay);
        String[] weekTemp = resources.getStringArray(R.array.weekTemp);
        for (int i = 0; i < weekDay.length; i++) {
            dataSource.add(new WeekWeather(weekDay[i],weekTemp[i]));
        }
        return this;
    }
}
