package ru.geekbrains.kozirfm.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements Constants {

    private SwipeRefreshLayout swipeRefreshLayout;
    private SharedPreferences sharedPreferences;
    private MainDisplayFragment mainDisplayFragment;
    private SelectCityFragment selectCityFragment;
    private SettingsFragment settingsFragment;
    private BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme();
        setContentView(R.layout.activity_main);
        initView();
        setFragment(mainDisplayFragment, MAIN_DISPLAY_FRAGMENT);
    }

    private void setFragment(Fragment fragment, String s) {
        if (s.equals(MAIN_DISPLAY_FRAGMENT)){
            isNetworkAvailable(this);
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentPart, fragment, s)
                .commit();
        initNotificationChannel();
    }

    private void initView() {
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(refreshListener);
        mainDisplayFragment = new MainDisplayFragment(getMainCity());
        navigationView = findViewById(R.id.navigationMenu);
        settingsFragment = new SettingsFragment();
        navigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        selectCityFragment = new SelectCityFragment(new SelectCityFragment.Callback() {
            @Override
            public void click(String string) {
                setMainCity(string);
                navigationView.setSelectedItemId(R.id.navigationHome);
            }
        });
    }

    public void isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(R.string.Error)
                    .setMessage(R.string.ConnectionFalse);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    private void setMainCity(String city) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CURRENT_CITY, city);
        editor.apply();
    }

    private String getMainCity() {
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(CURRENT_CITY, "");
    }

    private void setTheme() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(IS_DARK_THEME_KEY, true)) {
            setTheme(R.style.AppDarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }
    }

    public BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.navigationHome:
                            setFragment(new MainDisplayFragment(getMainCity()), MAIN_DISPLAY_FRAGMENT);
                            return true;
                        case R.id.navigationSearch:
                            setFragment(selectCityFragment, SELECT_CITY_FRAGMENT);
                            return true;
                        case R.id.navigationSettings:
                            setFragment(settingsFragment, SETTINGS_FRAGMENT);
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
                    setFragment(new MainDisplayFragment(getMainCity()), MAIN_DISPLAY_FRAGMENT);
                    swipeRefreshLayout.setRefreshing(false);

                }
            }, 1000);
        }
    };

    private void initNotificationChannel(){
        startService(new Intent(MainActivity.this, DownloadWeatherDataService.class));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel("1", "name", NotificationManager.IMPORTANCE_LOW);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

}
