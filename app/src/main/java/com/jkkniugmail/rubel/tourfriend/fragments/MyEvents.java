package com.jkkniugmail.rubel.tourfriend.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jkkniugmail.rubel.tourfriend.Constants;
import com.jkkniugmail.rubel.tourfriend.R;
import com.jkkniugmail.rubel.tourfriend.fragments.dialog_fragments.BudgetAdd;
import com.jkkniugmail.rubel.tourfriend.fragments.dialog_fragments.ExpenseAdd;
import com.jkkniugmail.rubel.tourfriend.fragments.my_events_child.EventAdd;
import com.jkkniugmail.rubel.tourfriend.fragments.my_events_child.EventDetail;
import com.jkkniugmail.rubel.tourfriend.fragments.my_events_child.EventList;
import com.jkkniugmail.rubel.tourfriend.fragments.my_events_child.EventUpdate;
import com.jkkniugmail.rubel.tourfriend.fragments.my_events_child.ImageGallery;

public class MyEvents extends Fragment implements EventList.OnEventListInteractionListener,
        EventAdd.OnAddEventResult, EventDetail.OnEventDetailInteractionListener,
        ExpenseAdd.OnAddExpenseListener, BudgetAdd.OnExtraBudgetListener,
        EventUpdate.OnUpdateEventResultInteractionListener{

    private int user_id;
    private FragmentManager fragmentManager;
    private Context context;
    Fragment fragment;
    TextView headerTV;

    private OnMyEventInteractionListener interactionListener;

    public MyEvents() {
        // Required empty public constructor
    }


    public static MyEvents newInstance(int user_id) {
        MyEvents fragment = new MyEvents();
        Bundle args = new Bundle();
        args.putInt(Constants.USER_ID, user_id);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_events, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        headerTV = (TextView) view.findViewById(R.id.my_events_head);
        fragment = EventList.newInstance(user_id);
        fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction().add(R.id.my_event_child, fragment).commit();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (interactionListener != null) {
            interactionListener.onMyEventInteraction();
        }
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
        if (context instanceof OnMyEventInteractionListener) {
            interactionListener = (OnMyEventInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMyEventInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        interactionListener = null;
    }

    @Override
    public void onAddEventButtonClick() {
        fragment = EventAdd.newInstance(user_id);
        fragmentManager.beginTransaction().replace(R.id.my_event_child, fragment).commit();
    }

    @Override
    public void onEventSearch(int result) {
        if (result == Constants.EVENT_FOUND) {
            headerTV.setVisibility(View.GONE);
            headerTV.setText("My Events");
        } else if (result == Constants.EVENT_NOT_FOUND) {
            headerTV.setVisibility(View.VISIBLE);
            headerTV.setText("No event inserted yet");
        }
    }

    @Override
    public void onItemClick(int event_id) {
        fragment = EventDetail.newInstance(event_id);
        fragmentManager.beginTransaction().replace(R.id.my_event_child, fragment).commit();
    }

    @Override
    public void onAddEventResult(int result) {

        String message;
        if (result == Constants.RESULT_SUCCESS) {
            message = "Event is created successfully";
        }
        else if (result == Constants.RESULT_FAIL) {
            message = "Event creation has failed";
        }
        else {
            message = "Event creation has canceled";
        }

        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        fragment = EventList.newInstance(user_id);
        fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.my_event_child, fragment).commit();
    }

    @Override
    public void onEventDetailRefreshListener(int event_id) {

        fragment = EventDetail.newInstance(event_id);
        fragmentManager.beginTransaction().replace(R.id.my_event_child, fragment).commit();
    }

    @Override
    public void onViewAllImage(int event_id) {
        fragment = ImageGallery.newInstance(event_id);
        fragmentManager.beginTransaction().replace(R.id.my_event_child, fragment).commit();
    }

    @Override
    public void onEventDelete(int event_id) {
        fragment = EventList.newInstance(user_id);
        fragmentManager.beginTransaction().replace(R.id.my_event_child, fragment).commit();
    }

    @Override
    public void onEventUpdateRequest(int event_id) {
        fragment = EventUpdate.newInstance(event_id);
        fragmentManager.beginTransaction().replace(R.id.my_event_child, fragment).commit();
    }

    @Override
    public void onMomentCapturedChild(int event_id) {
        interactionListener.onMomentCapturedParent(event_id);
    }


    @Override
    public void onUpdateEventResult(int event_id) {
        fragment = EventDetail.newInstance(event_id);
        fragmentManager.beginTransaction().replace(R.id.my_event_child, fragment).commit();
    }

    //send parameter to container activity
    public interface OnMyEventInteractionListener {

        void onMyEventInteraction();
        void onMomentCapturedParent(int event_id);
    }
}