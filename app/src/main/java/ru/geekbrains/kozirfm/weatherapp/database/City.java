package ru.geekbrains.kozirfm.weatherapp.database;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"first_name_city", "last_name_city"})})
public class City {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "first_name_city")
    public String firstNameCity;

    @ColumnInfo(name = "last_name_city")
    public String lastNameCity;



}
