package ru.geekbrains.kozirfm.weatherapp;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements Constants {

    private TextView mainCity;
    private TextView mainTemperature;
    private TextView mainWindPower;
    private TextView mainPressure;
    private SettingsFragment settingsFragment;
    private SelectCityFragment selectCityFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainCity = findViewById(R.id.mainCity);
        mainTemperature = findViewById(R.id.mainTemperature);
        mainWindPower = findViewById(R.id.mainWindPower);
        mainPressure = findViewById(R.id.mainPressure);
        mainCity.setText(R.string.city_moscow);
        mainTemperature.setText(R.string.main_temperature);
        mainWindPower.setText(R.string.main_wind_power);
        mainPressure.setText(R.string.main_pressure);
        settingsFragment = new SettingsFragment();
        selectCityFragment = new SelectCityFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (item.getItemId()) {
            case R.id.menuSelectCity:
                if (fragmentManager.getBackStackEntryCount() != 0) {
                    fragmentManager.popBackStack();
                }
                if (fragmentManager.findFragmentById(R.id.fragmentPart) != selectCityFragment) {
                    fragmentTransaction.replace(R.id.fragmentPart, selectCityFragment).
                            addToBackStack(null).
                            commit();
                }
                break;
            case R.id.menuSettings:
                if (fragmentManager.getBackStackEntryCount() != 0) {
                    fragmentManager.popBackStack();
                }
                if (fragmentManager.findFragmentById(R.id.fragmentPart) != settingsFragment) {
                    fragmentTransaction.replace(R.id.fragmentPart, settingsFragment).
                            addToBackStack(null).
                            commit();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
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
}
