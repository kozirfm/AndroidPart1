package ru.geekbrains.kozirfm.weatherapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements Constants {

    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView mainCity;
    private TextView mainTemperature;
    private TextView mainWindPower;
    private TextView mainPressure;
    private TextView mainTemperatureName;
    private TextView mainWindPowerName;
    private TextView mainPressureName;
    private SettingsFragment settingsFragment;
    private SelectCityFragment selectCityFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme();
        setContentView(R.layout.activity_main);
        initView();
        setMainInfoOnDisplay();
        setMetrics();
        initWeekWeather();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(MAIN_CITY, mainCity.getText().toString());
        outState.putString(MAIN_TEMPERATURE, mainTemperature.getText().toString());
        outState.putString(MAIN_WIND_POWER, mainWindPower.getText().toString());
        outState.putString(MAIN_PRESSURE, mainPressure.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mainCity.setText(savedInstanceState.getString(MAIN_CITY));
        mainTemperature.setText(savedInstanceState.getString(MAIN_TEMPERATURE));
        mainWindPower.setText(savedInstanceState.getString(MAIN_WIND_POWER));
        mainPressure.setText(savedInstanceState.getString(MAIN_PRESSURE));
    }

    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (fragmentManager.findFragmentById(R.id.fragmentPart) != fragment) {

            fragmentTransaction.replace(R.id.fragmentPart, fragment).commit();
        } else {
            fragmentTransaction.show(fragment).commit();
        }
    }

    private void initView() {
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(refreshListener);
        mainCity = findViewById(R.id.mainCity);
        mainTemperature = findViewById(R.id.mainTemperature);
        mainWindPower = findViewById(R.id.mainWindPower);
        mainPressure = findViewById(R.id.mainPressure);
        mainTemperatureName = findViewById(R.id.mainTemperatureName);
        mainWindPowerName = findViewById(R.id.mainWindPowerName);
        mainPressureName = findViewById(R.id.mainPressureName);
        settingsFragment = new SettingsFragment();
        selectCityFragment = new SelectCityFragment();
        BottomNavigationView navigationView = findViewById(R.id.navigationMenu);
        navigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
    }

    private void initWeekWeather() {
        WeekWeatherSource weekWeatherSource = new WeekWeatherSource(getResources()).build();
        RecyclerView recyclerView = findViewById(R.id.weekWeather);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        WeekWeatherAdapter adapter = new WeekWeatherAdapter(weekWeatherSource);
        recyclerView.setAdapter(adapter);
    }

    private void setMetrics() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(IS_METRIC_SETTINGS, false)) {
            mainTemperatureName.setText(sharedPreferences.getString(METRICS_TEMPERATURE_VALUE, mainTemperatureName.getText().toString()));
            mainWindPowerName.setText(sharedPreferences.getString(METRICS_WIND_POWER_VALUE, mainWindPowerName.getText().toString()));
            mainPressureName.setText(sharedPreferences.getString(METRICS_PRESSURE_VALUE, mainPressureName.getText().toString()));
        }
    }

    private void setTheme() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(IS_DARK_THEME_KEY, true)) {
            setTheme(R.style.AppDarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }
    }

    public void setMainInfoOnDisplay() {
        new DownloadWeatherData("", new DownloadWeatherData.Callback() {
            @Override
            public void getData(WeatherData weatherData) {
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
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.navigationHome:
                            if (getSupportFragmentManager().getFragments().size() != 0) {
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .hide(getSupportFragmentManager().findFragmentById(R.id.fragmentPart))
                                        .commit();
                                return true;
                            }
                        case R.id.navigationSearch:
                            setFragment(selectCityFragment);
                            return true;
                        case R.id.navigationSettings:
                            setFragment(settingsFragment);
                            return true;
                    }
                    return false;
                }
            };

    private SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            swipeRefreshLayout.setRefreshing(true);
            swipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(false);
                    setMainInfoOnDisplay();
                }
            }, 1000);
        }
    };
}
