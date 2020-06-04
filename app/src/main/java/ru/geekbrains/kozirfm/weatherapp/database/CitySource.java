package ru.geekbrains.kozirfm.weatherapp.database;

import java.util.List;

public class CitySource {

    private final CityListDao cityListDao;
    private List<City> cityList;

    public CitySource(CityListDao cityListDao) {
        this.cityListDao = cityListDao;
    }

    public List<City> getCityList(){
        if (cityList == null){
            LoadCities();
        }
        return cityList;
    }

    public void LoadCities(){
       cityList = cityListDao.getAllCity();
    }

    public long getCountCities(){
        return cityListDao.getCountCity();
    }

    public void addCity(City city){
        cityListDao.insertCity(city);
        LoadCities();
    }

    public void updateCity(City city){
        cityListDao.updateCity(city);
        LoadCities();
    }

    public void removeCity(City city){
        cityListDao.deleteCity(city);
        LoadCities();
    }


}
