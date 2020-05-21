package ru.geekbrains.kozirfm.weatherapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SelectCityFragment extends Fragment implements Constants {

    private String[] cities;
    private Callback callback;

    public SelectCityFragment(){

    }

    public SelectCityFragment(Callback callback){
        this.callback = callback;
    }

    interface Callback {
        void click(String string);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_select_city, container, false);
        initListCities(view);
        return view;
    }

    private void initListCities(View view) {
        String[] data = getResources().getStringArray(R.array.items);
        cities = getResources().getStringArray(R.array.cities);
        RecyclerView recyclerView = view.findViewById(R.id.listCities);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        ListCitiesAdapter adapter = new ListCitiesAdapter(data);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ListCitiesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                callback.click(cities[position]);
            }
        });
    }
    
}
