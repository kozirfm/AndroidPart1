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

import java.util.Arrays;

public class SelectCityFragment extends Fragment {

    private String selectedCity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_select_city, container, false);
        initListCities(view);
        return view;
    }

    private void initListCities(View view) {
        final String[] data = getResources().getStringArray(R.array.items);
        final String[] cities = getResources().getStringArray(R.array.cities);
        RecyclerView recyclerView = view.findViewById(R.id.listCities);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        ListCitiesAdapter adapter = new ListCitiesAdapter(data);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ListCitiesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String city = ((TextView) view).getText().toString();
                int num = Arrays.asList(data).indexOf(city);
                selectedCity = cities[num];
                DownloadWeatherData downloadWeatherData = new DownloadWeatherData(selectedCity);
                downloadWeatherData.downloadWeather();
                String downloadCityName;
                do {
                    downloadCityName = downloadWeatherData.getCityName();
                } while (downloadCityName == null);
                TextView mainCity = getActivity().findViewById(R.id.mainCity);
                mainCity.setText(downloadWeatherData.getCityName());
                TextView mainTemperature = getActivity().findViewById(R.id.mainTemperature);
                TextView mainTemperatureName = getActivity().findViewById(R.id.mainTemperatureName);

                if (mainTemperatureName.getText().toString().equals("F˚")) {
                    mainTemperature.setText(Integer.toString(Math.round((downloadWeatherData.getTemperature() * 1.8f) + 32)));
                } else {
                    mainTemperature.setText(Integer.toString(Math.round(downloadWeatherData.getTemperature())));
                }

                TextView mainPressure = getActivity().findViewById(R.id.mainPressure);
                TextView mainPressureName = getActivity().findViewById(R.id.mainPressureName);
                if (mainPressureName.getText().toString().equals("гПа") || mainPressureName.getText().toString().equals("hPa")) {
                    mainPressure.setText(Integer.toString(Math.round(downloadWeatherData.getPressure())));
                } else {
                    mainPressure.setText((Integer.toString(Math.round((downloadWeatherData.getPressure() * 0.75f)))));
                }

                TextView mainWindPower = getActivity().findViewById(R.id.mainWindPower);
                mainWindPower.setText(Integer.toString(Math.round(downloadWeatherData.getWindPower())));
                Toast.makeText(getContext(), R.string.dataUpdated, Toast.LENGTH_SHORT).show();
                getActivity().
                        getSupportFragmentManager().
                        beginTransaction().
                        hide(getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentPart)).
                        commit();
            }
        });
    }
}
