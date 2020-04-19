package ru.geekbrains.kozirfm.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import org.parceler.Parcels;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SelectCityActivity extends AppCompatActivity implements Constants {

    private RadioGroup cityList;
    private RadioButton selectedCity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);
        cityList = findViewById(R.id.checkedCity);
        Button sendSelectedCity = findViewById(R.id.sendSelectedCityButton);
        sendSelectedCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int id = cityList.getCheckedRadioButtonId();
                    selectedCity = findViewById(id);
                    WeatherCity weatherCity = new WeatherCity(selectedCity.getText().toString(), "+25", "755", "10");
                    Parcelable parcelable = Parcels.wrap(weatherCity);
                    Intent intentResult = new Intent();
                    intentResult.putExtra(SELECTED_CITY, parcelable);
                    setResult(RESULT_OK, intentResult);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } finally {
                    finish();
                }
            }
        });
    }
}
