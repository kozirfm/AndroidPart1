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

import android.os.Handler;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

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
    private DownloadWeatherData downloadWeatherData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme();
        setContentView(R.layout.activity_main);
        initView();
        if (savedInstanceState == null) {
            setMainInfoOnDisplay();
        }
        setMetrics();
        initWeekWeather();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
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
        mainCity.setText(R.string.city_moscow);
        mainTemperature.setText(R.string.main_temperature);
        mainWindPower.setText(R.string.main_wind_power);
        mainPressure.setText(R.string.main_pressure);
        mainTemperatureName.setText(R.string.celsius);
        mainWindPowerName.setText(R.string.wind_power_ms);
        mainPressureName.setText(R.string.pressure_button_mmHg);
        settingsFragment = new SettingsFragment();
        selectCityFragment = new SelectCityFragment();
        downloadWeatherData = new DownloadWeatherData();
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

    private void setMainInfoOnDisplay() {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!downloadWeatherData.downloadWeather()) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), R.string.downloadError, Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                downloadWeatherData.downloadWeather();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mainCity.setText(downloadWeatherData.getCityName());
                        mainTemperature.setText(Integer.toString(Math.round(downloadWeatherData.getTemperature())));
                        mainPressure.setText(Integer.toString(Math.round(downloadWeatherData.getPressure())));
                        mainWindPower.setText(Integer.toString(Math.round(downloadWeatherData.getWindPower())));
                        Toast.makeText(getApplicationContext(), R.string.dataUpdated, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
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
                            return false;
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
