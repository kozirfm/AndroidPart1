package ru.geekbrains.kozirfm.weatherapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ListCitiesAdapter extends RecyclerView.Adapter<ListCitiesAdapter.ListCitiesViewHolder> {

    private String[] dataSource;
    private OnItemClickListener itemClickListener;

    public ListCitiesAdapter(String[] dataSource) {
        this.dataSource = dataSource;
    }

    @NonNull
    @Override
    public ListCitiesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_cities_recycler_view, viewGroup, false);
        return new ListCitiesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListCitiesViewHolder listCitiesViewHolder, int i) {
        listCitiesViewHolder.getTextView().setText(dataSource[i]);

    }

    @Override
    public int getItemCount() {
        return dataSource.length;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    class ListCitiesViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        public TextView getTextView() {
            return textView;
        }

        public ListCitiesViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView;

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(v, getAdapterPosition());
                    }
                }
            });
        }
    }
}
