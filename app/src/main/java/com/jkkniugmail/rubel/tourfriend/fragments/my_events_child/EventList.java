package com.jkkniugmail.rubel.tourfriend.fragments.my_events_child;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jkkniugmail.rubel.tourfriend.Constants;
import com.jkkniugmail.rubel.tourfriend.R;
import com.jkkniugmail.rubel.tourfriend.databases.DatabaseManager;
import com.jkkniugmail.rubel.tourfriend.models.Event;
import com.jkkniugmail.rubel.tourfriend.adapters.EventListAdapter;

import java.util.ArrayList;

/**
 * Created by islan on 12/27/2016.
 */

public class EventList extends Fragment {
    private RecyclerView eventListView;
    private FloatingActionButton addBtn;
    private OnEventListInteractionListener childInteractionListener;
    int user_id;

    public static EventList newInstance(int user_id) {

        Bundle args = new Bundle();
        args.putInt(Constants.USER_ID, user_id);
        EventList fragment = new EventList();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user_id = getArguments().getInt(Constants.USER_ID);
        } else {
            //error
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        onAttachToParentFragment(getParentFragment());
        return inflater.inflate(R.layout.event_child_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventListView = (RecyclerView) view.findViewById(R.id.my_events_list);
        addBtn = (FloatingActionButton) view.findViewById(R.id.add_event_Button);

        ArrayList<Event> eventArrayList = new ArrayList<>();
        DatabaseManager manager = new DatabaseManager(getContext());
        eventArrayList = manager.getAllEvent(user_id);

        if (eventArrayList.size() > 0) {
            EventListAdapter adapter = new EventListAdapter(getContext(), eventArrayList);
            final ArrayList<Event> finalEventArrayList = eventArrayList;
            adapter.setOnItemClickListener(new EventListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View itemView, int position) {
                    int event_id = finalEventArrayList.get(position).getId();
                    childInteractionListener.onItemClick(event_id);
                }
            });
            eventListView.setAdapter(adapter);
            eventListView.setLayoutManager(new LinearLayoutManager(getContext()));
            childInteractionListener.onEventSearch(Constants.EVENT_FOUND);
        } else {
            childInteractionListener.onEventSearch(Constants.EVENT_NOT_FOUND);
        }

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                childInteractionListener.onAddEventButtonClick();
            }
        });
    }

    public void onAttachToParentFragment(Fragment parentFragment) {
        if (parentFragment instanceof OnEventListInteractionListener) {
            childInteractionListener = (OnEventListInteractionListener) parentFragment;
        } else
            throw new RuntimeException(parentFragment.toString() + " must implement OnChildInteractionListener");

    }

    public interface OnEventListInteractionListener {
        public void onAddEventButtonClick();

        public void onEventSearch(int result);

        public void onItemClick(int event_id);
    }
}