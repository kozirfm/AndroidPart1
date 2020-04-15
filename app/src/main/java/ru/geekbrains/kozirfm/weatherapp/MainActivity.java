package ru.geekbrains.kozirfm.weatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city_activity);
        makeToast("onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        makeToast("onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        makeToast("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        makeToast("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        makeToast("onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        makeToast("onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        makeToast("onDestroy");
    }

    private void makeToast(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}

