package ru.geekbrains.kozirfm.weatherapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class SettingsFragment extends Fragment implements Constants {

    private RadioGroup temperature;
    private RadioGroup windPower;
    private RadioGroup pressure;
    private RadioButton temperatureValue;
    private RadioButton windPowerValue;
    private RadioButton pressureValue;
    private SharedPreferences sharedPreferences;
    private Button setMetricsSetting;
    private Switch setDarkTheme;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_settings, container, false);
        init(view);

        if (sharedPreferences.getBoolean(IS_METRIC_SETTINGS, false)) {
            temperature.check(sharedPreferences.getInt(METRICS_TEMPERATURE_ID, temperature.getCheckedRadioButtonId()));
            windPower.check(sharedPreferences.getInt(METRICS_WIND_POWER_ID, windPower.getCheckedRadioButtonId()));
            pressure.check(sharedPreferences.getInt(METRICS_PRESSURE_ID, pressure.getCheckedRadioButtonId()));
        }

        setDarkTheme.setChecked(isDarkTheme());

        setDarkTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setDarkTheme(isChecked);
                getActivity().recreate();
            }
        });

        setMetricsSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMetricsSetting(temperature.getCheckedRadioButtonId(), windPower.getCheckedRadioButtonId(), pressure.getCheckedRadioButtonId());
                TextView temperatureName = getActivity().findViewById(R.id.mainTemperatureName);
                TextView windPowerName = getActivity().findViewById(R.id.mainWindPowerName);
                TextView pressureName = getActivity().findViewById(R.id.mainPressureName);
                temperatureName.setText(temperatureValue.getText());
                windPowerName.setText(windPowerValue.getText());
                pressureName.setText(pressureValue.getText());
                Snackbar.make(view, "Настройки успешно сохранены", Snackbar.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private void init(View view) {
        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        temperature = view.findViewById(R.id.temperatureSetting);
        windPower = view.findViewById(R.id.windPowerSetting);
        pressure = view.findViewById(R.id.pressureSetting);
        setMetricsSetting = view.findViewById(R.id.selectSettingsButton);
        setDarkTheme = view.findViewById(R.id.darkLightSwitch);
    }

    private void setMetricsSetting(int temperature, int windPower, int pressure) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        temperatureValue = getView().findViewById(this.temperature.getCheckedRadioButtonId());
        windPowerValue = getView().findViewById(this.windPower.getCheckedRadioButtonId());
        pressureValue = getView().findViewById(this.pressure.getCheckedRadioButtonId());
        editor.putBoolean(IS_METRIC_SETTINGS, true);
        editor.putInt(METRICS_TEMPERATURE_ID, temperature);
        editor.putInt(METRICS_WIND_POWER_ID, windPower);
        editor.putInt(METRICS_PRESSURE_ID, pressure);
        editor.putString(METRICS_TEMPERATURE_VALUE, temperatureValue.getText().toString());
        editor.putString(METRICS_WIND_POWER_VALUE, windPowerValue.getText().toString());
        editor.putString(METRICS_PRESSURE_VALUE, pressureValue.getText().toString());
        editor.apply();
    }

    private boolean isDarkTheme() {
        return sharedPreferences.getBoolean(IS_DARK_THEME_KEY, true);
    }

    private void setDarkTheme(boolean isDarkTheme) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_DARK_THEME_KEY, isDarkTheme);
        editor.apply();
    }

}
