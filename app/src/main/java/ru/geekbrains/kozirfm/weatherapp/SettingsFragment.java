package ru.geekbrains.kozirfm.weatherapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;


public class SettingsFragment extends Fragment implements Constants {

    private RadioGroup temperature;
    private RadioGroup windPower;
    private RadioGroup pressure;
    private SharedPreferences sharedPreferences;
    private Button setMetricsSetting;
    private Switch setDarkTheme;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_settings, container, false);
        init(view);

        if (sharedPreferences.getBoolean(IS_METRIC_SETTINGS, false)) {
            temperature.check(sharedPreferences.getInt(METRICS_TEMPERATURE_ID, temperature.getCheckedRadioButtonId()));
            windPower.check(sharedPreferences.getInt(METRICS_WIND_POWER_ID, windPower.getCheckedRadioButtonId()));
            pressure.check(sharedPreferences.getInt(METRICS_PRESSURE_ID, pressure.getCheckedRadioButtonId()));
        }

        setDarkTheme.setChecked(isDarkTheme());

        setDarkTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            setDarkTheme(isChecked);
            requireActivity().recreate();
        });

        setMetricsSetting.setOnClickListener(v -> {
            setMetricsSetting(temperature.getCheckedRadioButtonId(), windPower.getCheckedRadioButtonId(), pressure.getCheckedRadioButtonId());
            Toast.makeText(getContext(), R.string.settingSaved, Toast.LENGTH_SHORT).show();
        });
        return view;
    }

    private void init(View view) {
        sharedPreferences = this.requireActivity().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        temperature = view.findViewById(R.id.temperatureSetting);
        windPower = view.findViewById(R.id.windPowerSetting);
        pressure = view.findViewById(R.id.pressureSetting);
        setMetricsSetting = view.findViewById(R.id.selectSettingsButton);
        setDarkTheme = view.findViewById(R.id.darkLightSwitch);
    }

    private void setMetricsSetting(int temperature, int windPower, int pressure) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        RadioButton temperatureValue = requireView().findViewById(this.temperature.getCheckedRadioButtonId());
        RadioButton windPowerValue = requireView().findViewById(this.windPower.getCheckedRadioButtonId());
        RadioButton pressureValue = requireView().findViewById(this.pressure.getCheckedRadioButtonId());
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
