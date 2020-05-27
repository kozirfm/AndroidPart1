package ru.geekbrains.kozirfm.weatherapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import ru.geekbrains.kozirfm.weatherapp.data.WeatherRequest;


public class MainDisplayFragment extends Fragment implements Constants {

    public MainDisplayFragment() {

    }

    public MainDisplayFragment(String city) {
        downloadWeatherData(city);
    }

    private TextView mainCity;
    private TextView mainTemperature;
    private TextView mainWindPower;
    private TextView mainPressure;
    private TextView mainTemperatureName;
    private TextView mainWindPowerName;
    private TextView mainPressureName;
    private TextView description;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_display_fragment, container, false);
        initView(view);
        initWeekWeather();
        setMetrics();
        return view;
    }

    private void initView(View view) {
        mainCity = view.findViewById(R.id.mainCity);
        mainTemperature = view.findViewById(R.id.mainTemperature);
        mainWindPower = view.findViewById(R.id.mainWindPower);
        mainPressure = view.findViewById(R.id.mainPressure);
        description = view.findViewById(R.id.weatherDescription);
        mainTemperatureName = view.findViewById(R.id.mainTemperatureName);
        mainWindPowerName = view.findViewById(R.id.mainWindPowerName);
        mainPressureName = view.findViewById(R.id.mainPressureName);
        recyclerView = view.findViewById(R.id.weekWeather);
    }

    private void initWeekWeather() {
        WeekWeatherSource weekWeatherSource = new WeekWeatherSource(getResources()).build();
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        WeekWeatherAdapter adapter = new WeekWeatherAdapter(weekWeatherSource);
        recyclerView.setAdapter(adapter);
    }

    private void setMetrics() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean(IS_METRIC_SETTINGS, false)) {
            mainTemperatureName.setText(sharedPreferences.getString(METRICS_TEMPERATURE_VALUE, mainTemperatureName.getText().toString()));
            mainWindPowerName.setText(sharedPreferences.getString(METRICS_WIND_POWER_VALUE, mainWindPowerName.getText().toString()));
            mainPressureName.setText(sharedPreferences.getString(METRICS_PRESSURE_VALUE, mainPressureName.getText().toString()));
        }
    }

    private void isDownloadError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(R.string.Error).setMessage(R.string.ServerError);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void downloadWeatherData(String city) {
        if (city.equals("")) {
            city = "saint petersburg";
        }

        MyApplication.getOpenWeather().loadWeather(city, "metric", "ru", BuildConfig.WEATHER_API_KEY)
                .enqueue(new retrofit2.Callback<WeatherRequest>() {
                    @Override
                    public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                        if (response.body() != null && response.isSuccessful()) {
                            setMainDisplayInfo(new WeatherData(response.body()));
                        } else {
                            ResponseBody responseBody = response.errorBody();
                            try {
                                Log.d("ERROR", responseBody.string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherRequest> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
    }

    private void setMainDisplayInfo(WeatherData weatherData) {
        if (weatherData.getCod() != 200) {
            isDownloadError();
        }
        mainCity.setText(weatherData.getCityName());
        if (mainTemperatureName.getText().toString().equals("F˚")) {
            mainTemperature.setText(Integer.toString(Math.round((weatherData.getTemperature() * 1.8f) + 32)));
        } else {
            mainTemperature.setText(Integer.toString(Math.round(weatherData.getTemperature())));
        }
        if (mainPressureName.getText().toString().equals("гПа") || mainPressureName.getText().toString().equals("hPa")) {
            mainPressure.setText(Integer.toString(Math.round(weatherData.getPressure())));
        } else {
            mainPressure.setText((Integer.toString(Math.round((weatherData.getPressure() * 0.75f)))));
        }
        mainWindPower.setText(Integer.toString(Math.round(weatherData.getWindPower())));
        description.setText(weatherData.getDescription());
    }

}
