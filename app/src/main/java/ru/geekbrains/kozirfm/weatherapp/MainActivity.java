package ru.geekbrains.kozirfm.weatherapp;

import android.content.Intent;
import android.os.Parcelable;

import org.parceler.Parcels;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements Constants {


    private TextView mainCity;
    private TextView mainTemperature;
    private TextView mainWindPower;
    private TextView mainPressure;


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
        Button selectCityButton = findViewById(R.id.selectCityButton);
        selectCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SelectCityActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SELECTED_CITY_ACTIVITY);
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode != REQUEST_CODE_SELECTED_CITY_ACTIVITY){
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        try {
            if (resultCode == RESULT_OK) {
                Parcelable parcelable = data.getParcelableExtra(SELECTED_CITY);
                WeatherCity weatherCity = Parcels.unwrap(parcelable);
                mainCity.setText(weatherCity.city);
                mainTemperature.setText(weatherCity.temperature);
                mainWindPower.setText(weatherCity.temperature);
                mainPressure.setText(weatherCity.pressure);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
