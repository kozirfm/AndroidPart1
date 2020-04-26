package ru.geekbrains.kozirfm.weatherapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class SelectCityFragment extends Fragment {

    private RadioGroup cityList;
    private RadioButton selectedCity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_select_city, container, false);
        cityList = view.findViewById(R.id.checkedCity);
        Button button = view.findViewById(R.id.sendSelectedCityButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int id = cityList.getCheckedRadioButtonId();
                    selectedCity = view.findViewById(id);
                    TextView textView = getActivity().findViewById(R.id.mainCity);
                    textView.setText(selectedCity.getText().toString());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } finally {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });
        return view;
    }
}
