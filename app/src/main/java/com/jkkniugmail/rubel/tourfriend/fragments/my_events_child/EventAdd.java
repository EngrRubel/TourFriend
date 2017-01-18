package com.jkkniugmail.rubel.tourfriend.fragments.my_events_child;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.jkkniugmail.rubel.tourfriend.Constants;
import com.jkkniugmail.rubel.tourfriend.R;
import com.jkkniugmail.rubel.tourfriend.databases.DatabaseManager;
import com.jkkniugmail.rubel.tourfriend.models.Event;

import java.util.Calendar;

/**
 * Created by islan on 12/28/2016.
 */

public class EventAdd extends Fragment {
    private Viewholder viewholder;
    private int user_id;
    private int result;
    private OnAddEventResult onAddEventResult;

    public static EventAdd newInstance(int user_id) {
        EventAdd fragment = new EventAdd();
        Bundle args = new Bundle();
        args.putInt(Constants.USER_ID, user_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onAttachToParentFragment(getParentFragment());
        if (getArguments() != null) {
            user_id = getArguments().getInt(Constants.USER_ID);
        } else {
            //error
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.event_child_add_event, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewholder = new Viewholder(view);
        viewholder.start_date_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
//        viewholder.start_date_et.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                showDatePicker();
//                return false;
//            }
//        });

        viewholder.ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = viewholder.title_et.getText().toString();
                String description = viewholder.description_et.getText().toString();
                String location = viewholder.location_et.getText().toString();
                String start_date = viewholder.start_date_et.getText().toString();
                String budgetString = viewholder.budget_et.getText().toString();
                if (TextUtils.isEmpty(budgetString)) {
                    budgetString = "0";
                }
                float budget = Float.parseFloat(budgetString);
                if (TextUtils.isEmpty(title)) {
                    viewholder.title_et.setError("Must have a title");
                }
                else if(TextUtils.isEmpty(start_date)){
                    viewholder.start_date_et.setError("Must have a starting date");
                }
                else {
                    DatabaseManager manager = new DatabaseManager(getContext());
                    Event event = new Event(title, description, location, start_date, budget);
                    boolean rs = manager.addNewEvent(event, user_id);
                    if (rs) {
                        result = Constants.RESULT_SUCCESS;
                    } else {
                        result = Constants.RESULT_FAIL;
                    }
                    onAddEventResult.onAddEventResult(result);

                }

            }
        });

        viewholder.cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddEventResult.onAddEventResult(Constants.RESULT_CANCEL);
            }
        });
    }

    public void onAttachToParentFragment(Fragment parentFragment) {
        if (parentFragment instanceof OnAddEventResult) {
            onAddEventResult = (OnAddEventResult) parentFragment;
        } else
            throw new RuntimeException(parentFragment.toString() + " must implement OnAddEventResult");

    }

    public void showDatePicker() {
        Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                String date = String.valueOf(selectedyear) + "-" + String.valueOf(selectedmonth + 1) + "-" + String.valueOf(selectedday);
                viewholder.start_date_et.setText(date);
            }
        }, mYear, mMonth, mDay);
        mDatePicker.getDatePicker().setCalendarViewShown(false);
        mDatePicker.setTitle("Select date");
        mDatePicker.getDatePicker().setMinDate(mcurrentDate.getTimeInMillis());


        mDatePicker.show();

    }


    private class Viewholder {
        private EditText title_et;
        private EditText description_et;
        private EditText location_et;
        private EditText start_date_et;
        private EditText budget_et;
        private Button ok_btn;
        private Button cancel_btn;

        private Viewholder(View v) {
            title_et = (EditText) v.findViewById(R.id.event_title_et);
            description_et = (EditText) v.findViewById(R.id.event_desctiption_et);
            location_et = (EditText) v.findViewById(R.id.event_location_et);
            start_date_et = (EditText) v.findViewById(R.id.event_date_et);
            budget_et = (EditText) v.findViewById(R.id.event_budget_et);
            ok_btn = (Button) v.findViewById(R.id.event_ok_btn);
            cancel_btn = (Button) v.findViewById(R.id.event_cancel_btn);

        }
    }

    public interface OnAddEventResult {
        public void onAddEventResult(int result);
    }
}