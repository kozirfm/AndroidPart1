package ru.geekbrains.kozirfm.weatherapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import ru.geekbrains.kozirfm.weatherapp.data.WeatherRequest;

public class MainActivity extends AppCompatActivity implements Constants{

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
        if(savedInstanceState == null){
            downloadWeatherInfo();
        }
        setTheme();
        setContentView(R.layout.activity_main);
        initView();
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

    private void downloadWeatherInfo() {
        try {
            final URL uri = new URL(WEATHER_URL + WEATHER_API_KEY);
            final Handler handler = new Handler();
            new Thread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void run() {
                    HttpsURLConnection urlConnection = null;
                    try {
                        urlConnection = (HttpsURLConnection) uri.openConnection();
                        urlConnection.setRequestMethod("GET");
                        urlConnection.setReadTimeout(10000);
                        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        String result = result(in);
                        Gson gson = new Gson();
                        final WeatherRequest weatherRequest = gson.fromJson(result, WeatherRequest.class);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                setMainInfoOnDisplay(weatherRequest);
                                Toast.makeText(MainActivity.this, R.string.dataUpdated, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                private String result(BufferedReader in) throws IOException {
                    String line;
                    StringBuilder lines = new StringBuilder();
                    while ((line = in.readLine()) != null) {
                        lines.append(line);
                    }
                    return lines.toString();
                }

            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setMainInfoOnDisplay(WeatherRequest weatherRequest){
        mainCity.setText(weatherRequest.getName());
        mainTemperature.setText(Integer.toString(Math.round(weatherRequest.getMain().getTemp())));
        mainPressure.setText(Integer.toString(Math.round(weatherRequest.getMain().getPressure())));
        mainWindPower.setText(Integer.toString(Math.round(weatherRequest.getWind().getSpeed())));
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
                    downloadWeatherInfo();
                }
            }, 5000);
        }
    };

}
