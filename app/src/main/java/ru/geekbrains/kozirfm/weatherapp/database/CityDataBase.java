package ru.geekbrains.kozirfm.weatherapp.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {City.class}, version = 1)
public abstract class CityDataBase extends RoomDatabase {
    public abstract CityListDao getCityListDao();
}
