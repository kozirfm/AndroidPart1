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
import android.widget.Switch;

public class SettingsFragment extends Fragment implements Constants {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_settings, container, false);
        Button button = view.findViewById(R.id.selectSettingsButton);
        Switch setDarkTheme = view.findViewById(R.id.darkLightSwitch);
        setDarkTheme.setChecked(isDarkTheme());
        setDarkTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setDarkTheme(isChecked);
                getActivity().recreate();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        return view;
    }

    private boolean isDarkTheme() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(IS_DARK_THEME_KEY, true);
    }

    private void setDarkTheme(boolean isDarkTheme) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_DARK_THEME_KEY, isDarkTheme);
        editor.apply();
    }
}
