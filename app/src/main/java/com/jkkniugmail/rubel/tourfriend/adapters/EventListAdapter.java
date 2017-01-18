package com.jkkniugmail.rubel.tourfriend.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jkkniugmail.rubel.tourfriend.R;
import com.jkkniugmail.rubel.tourfriend.models.Event;
import com.jkkniugmail.rubel.tourfriend.utils.MyDate;

import java.util.ArrayList;

/**
 * Created by islan on 12/26/2016.
 */

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Event> eventArrayList;

    // Define listener member variable
    private OnItemClickListener listener;

    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public EventListAdapter(Context context, ArrayList<Event> eventArrayList) {
        this.context = context;
        this.eventArrayList = eventArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View eventView = inflater.inflate(R.layout.row_event_list, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(eventView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Event event = eventArrayList.get(position);
        holder.titleTV.setText(event.getTitle());
        holder.locationTV.setText(event.getLocation());
        holder.start_timeTV.setText(event.getStarting_date());
        String count;
        count = new MyDate().getCountDown(event.getStarting_date());

        holder.time_countTV.setText(count);

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(holder.itemView, position);
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return eventArrayList.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView locationTV;
        private TextView titleTV;
        private TextView start_timeTV;
        private TextView time_countTV;
        private View item;

        protected ViewHolder(View itemView) {
            super(itemView);
            item = itemView;
            locationTV = (TextView) itemView.findViewById(R.id.row_event_location);
            titleTV = (TextView) itemView.findViewById(R.id.row_event_title);
            start_timeTV = (TextView) itemView.findViewById(R.id.row_start_time);
            time_countTV = (TextView) itemView.findViewById(R.id.row_time_count);


        }
    }

}
