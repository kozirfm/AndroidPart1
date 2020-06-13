package ru.geekbrains.kozirfm.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity implements Constants {

    private SwipeRefreshLayout swipeRefreshLayout;
    private SharedPreferences sharedPreferences;
    private MainDisplayFragment mainDisplayFragment;
    private SelectCityFragment selectCityFragment;
    private SettingsFragment settingsFragment;
    private BottomNavigationView navigationView;
    private BroadcastReceiver lowBatteryModeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isBatteryLow();
        setTheme();
        setContentView(R.layout.activity_main);
        initView();
        initToken();
        setFragment(mainDisplayFragment, MAIN_DISPLAY_FRAGMENT);
        getCurrentLocationWeather();
    }

    private void requestPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            requestLocation();
        }else{
            requestLocationPermission();
        }
    }

    private void requestLocationPermission() {
        if(!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_REQUEST_CODE){
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                requestLocation();
            }
        }
    }

    private void getCurrentLocationWeather(){
        floatingActionButton.setOnClickListener(v -> requestPermission());
    }

    private void requestLocation() {
            if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                return;
            }
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();
                    setFragment(new MainDisplayFragment(lat, lon), MAIN_DISPLAY_FRAGMENT);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });

    }


    private void initToken(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener((task -> {
                    if (task.isSuccessful()){
                        String token = task.getResult().getToken();
                    }
                }));
    }

    private void isBatteryLow() {
        lowBatteryModeReceiver = new LowBatteryModeReceiver();
        registerReceiver(lowBatteryModeReceiver, new IntentFilter(Intent.ACTION_BATTERY_LOW));
    }

    private void initToken(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener((task -> {
                    if (task.isSuccessful()){
                        String token = task.getResult().getToken();
                        Log.d("TOKEN", token);
                    }
                }));
    }

    private void isBatteryLow() {
        lowBatteryModeReceiver = new LowBatteryModeReceiver();
        registerReceiver(lowBatteryModeReceiver, new IntentFilter(Intent.ACTION_BATTERY_LOW));
    }

    private void setFragment(Fragment fragment, String s) {
        if (s.equals(MAIN_DISPLAY_FRAGMENT)) {
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
        selectCityFragment = new SelectCityFragment(string -> {
            setMainCity(string);
            navigationView.setSelectedItemId(R.id.navigationHome);
        });
        floatingActionButton = findViewById(R.id.currentLocationButton);
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
            swipeRefreshLayout.postDelayed(() -> {
                setFragment(new MainDisplayFragment(getMainCity()), MAIN_DISPLAY_FRAGMENT);
                swipeRefreshLayout.setRefreshing(false);

            }, 1000);
        }
    };

    private void initNotificationChannel() {
        startService(new Intent(MainActivity.this, DownloadWeatherDataService.class));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel("1", "name", NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(lowBatteryModeReceiver != null){
            unregisterReceiver(lowBatteryModeReceiver);
        }
    }

}
