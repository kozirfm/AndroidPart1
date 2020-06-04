package ru.geekbrains.kozirfm.weatherapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.geekbrains.kozirfm.weatherapp.database.City;
import ru.geekbrains.kozirfm.weatherapp.database.CitySource;

public class SelectCityFragment extends Fragment implements Constants {

    private Callback callback;
    private CitySource citySource;

    public SelectCityFragment() {

    }

    public SelectCityFragment(Callback callback) {
        this.callback = callback;
    }

    interface Callback {
        void click(String string);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_select_city, container, false);
        initListCities(view);
        return view;
    }

    private void initListCities(View view) {
        citySource = new CitySource(MyApplication.getInstance().getCityListDao());
        if(citySource.getCountCities() == 0){
            downloadFromArray();
        }

        RecyclerView recyclerView = view.findViewById(R.id.listCities);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        ListCitiesAdapter adapter = new ListCitiesAdapter(citySource);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((view1, position) -> callback.click(citySource.getCityList().get(position).lastNameCity));
    }

    private void downloadFromArray(){
        String[] items = getResources().getStringArray(R.array.items);
        String[] cities = getResources().getStringArray(R.array.cities);
        City city = new City();
        for (int i = 0; i < items.length; i++){
             city.firstNameCity = items[i];
             city.lastNameCity = cities[i];
             citySource.addCity(city);
        }

    }

}
