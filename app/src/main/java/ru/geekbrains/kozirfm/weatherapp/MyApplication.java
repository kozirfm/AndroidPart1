package ru.geekbrains.kozirfm.weatherapp;

import android.app.Application;

import androidx.room.Room;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.geekbrains.kozirfm.weatherapp.database.CityDataBase;
import ru.geekbrains.kozirfm.weatherapp.database.CityListDao;

public class MyApplication extends Application {

    private static MyApplication myApplication;

    private IOpenWeather openWeather;

    private CityDataBase dataBase;

    public static MyApplication getInstance(){
        return myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        myApplication = this;

        dataBase = Room.databaseBuilder(
                getApplicationContext(),
                CityDataBase.class,
                "city_list_database")
                .allowMainThreadQueries()
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        openWeather = retrofit.create(IOpenWeather.class);
    }

    public CityListDao getCityListDao(){
        return dataBase.getCityListDao();
    }

    public IOpenWeather getOpenWeather() {
        return openWeather;
    }
}
