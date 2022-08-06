package com.example.wmucv2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.MyViewHolder> {

    private ArrayList<Show> showList;

    public ScheduleAdapter(ArrayList<Show> showList) {
        this.showList = showList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView showName;
        private TextView showHost;
        private TextView startTime;
        private TextView endTime;

        public MyViewHolder(final View view) {
            super(view);
            showName = view.findViewById(R.id.show_name);
            showHost = view.findViewById(R.id.show_host);
            startTime = view.findViewById(R.id.start_time);
            endTime = view.findViewById(R.id.end_time);

        }
    }

    @NonNull
    @Override
    public ScheduleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleAdapter.MyViewHolder holder, int position) {
        String showHost = showList.get(position).showHost;
        String showName = showList.get(position).showName;
        String startTime = showList.get(position).startTime;
        String endTime = showList.get(position).endTime;
        holder.showName.setText(showName);
        holder.showHost.setText(showHost);
        holder.startTime.setText(startTime);
        holder.endTime.setText(endTime);
    }

    @Override
    public int getItemCount() {
        return showList.size();
    }
}
