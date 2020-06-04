package ru.geekbrains.kozirfm.weatherapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CityListDao {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
     void insertCity (City city);

    @Update
    void updateCity(City city);

    @Delete
    void deleteCity(City city);

    @Query("SELECT * FROM city")
    List<City> getAllCity();

    @Query("SELECT COUNT() FROM city")
    long getCountCity();
}
