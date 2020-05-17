package ru.geekbrains.kozirfm.weatherapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class SelectCityFragment extends Fragment {

    private String[] cities;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_select_city, container, false);
        initListCities(view);
        return view;
    }

    private void initListCities(View view) {
        String[] data = getResources().getStringArray(R.array.items);
        cities = getResources().getStringArray(R.array.cities);
        RecyclerView recyclerView = view.findViewById(R.id.listCities);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        ListCitiesAdapter adapter = new ListCitiesAdapter(data);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ListCitiesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                setWeatherOnDisplay(position);
                getActivity().
                        getSupportFragmentManager().
                        beginTransaction().
                        hide(getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentPart)).
                        commit();
            }
        });
    }

    private void setWeatherOnDisplay(int position){
        new DownloadWeatherData(cities[position], new DownloadWeatherData.Callback() {
            @Override
            public void setData(WeatherData weatherData) {
                TextView mainCity = getActivity().findViewById(R.id.mainCity);
                TextView mainTemperature = getActivity().findViewById(R.id.mainTemperature);
                TextView mainTemperatureName = getActivity().findViewById(R.id.mainTemperatureName);

                mainCity.setText(weatherData.getCityName());

                if (mainTemperatureName.getText().toString().equals("F˚")) {
                    mainTemperature.setText(Integer.toString(Math.round((weatherData.getTemperature() * 1.8f) + 32)));
                } else {
                    mainTemperature.setText(Integer.toString(Math.round(weatherData.getTemperature())));
                }

                TextView mainPressure = getActivity().findViewById(R.id.mainPressure);
                TextView mainPressureName = getActivity().findViewById(R.id.mainPressureName);
                if (mainPressureName.getText().toString().equals("гПа") || mainPressureName.getText().toString().equals("hPa")) {
                    mainPressure.setText(Integer.toString(Math.round(weatherData.getPressure())));
                } else {
                    mainPressure.setText((Integer.toString(Math.round((weatherData.getPressure() * 0.75f)))));
                }

                TextView mainWindPower = getActivity().findViewById(R.id.mainWindPower);
                mainWindPower.setText(Integer.toString(Math.round(weatherData.getWindPower())));
                Toast.makeText(getContext(), R.string.dataUpdated, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
