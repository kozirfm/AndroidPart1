package ru.geekbrains.kozirfm.weatherapp;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WeekWeatherAdapter extends RecyclerView.Adapter<WeekWeatherAdapter.WeekWeatherViewHolder> {
    private WeekWeatherSource dataSource;

    public WeekWeatherAdapter(WeekWeatherSource dataSource) {
        this.dataSource = dataSource;
    }

    @NonNull
    @Override
    public WeekWeatherAdapter.WeekWeatherViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.week_weather_card_view, viewGroup, false);
        return new WeekWeatherAdapter.WeekWeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeekWeatherAdapter.WeekWeatherViewHolder weekWeatherViewHolder, int i) {
        WeekWeather weekWeather = dataSource.getWeekWeather(i);
        weekWeatherViewHolder.setData(weekWeather.getWeekDay(),weekWeather.getWeekTemp());
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    class WeekWeatherViewHolder extends RecyclerView.ViewHolder {

        private TextView weekDay;
        private TextView weekTemp;

        public WeekWeatherViewHolder(@NonNull View itemView) {
            super(itemView);

            weekDay = itemView.findViewById(R.id.weekDay);
            weekTemp = itemView.findViewById(R.id.weekTemp);

        }

        public void setData(String weekDay, String weekTemp){
            this.weekDay.setText(weekDay);
            this.weekTemp.setText(weekTemp);
        }
    }
}
