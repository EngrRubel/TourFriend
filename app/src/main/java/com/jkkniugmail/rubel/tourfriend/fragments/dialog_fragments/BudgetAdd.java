package com.jkkniugmail.rubel.tourfriend.fragments.dialog_fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.jkkniugmail.rubel.tourfriend.Constants;
import com.jkkniugmail.rubel.tourfriend.R;
import com.jkkniugmail.rubel.tourfriend.databases.DatabaseManager;

/**
 * Created by islan on 1/4/2017.
 */

public class BudgetAdd extends DialogFragment {

    public int event_id;
    EditText amountET;
    Button okBTN;
    OnExtraBudgetListener extraBudgetListener;

    public static BudgetAdd newInstance(int event_id) {

        Bundle args = new Bundle();
        args.putInt(Constants.EVENT_ID, event_id);
        BudgetAdd fragment = new BudgetAdd();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
        onAttachToParentFragment(getParentFragment());

        if (getArguments() != null) {
            event_id = getArguments().getInt(Constants.EVENT_ID);
        } else {
            //error
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_view_add_budget, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        amountET = (EditText) view.findViewById(R.id.extra_budget);
        okBTN = (Button) view.findViewById(R.id.extra_budget_ok_btn);
        okBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amountS = amountET.getText().toString();
                if (TextUtils.isEmpty(amountS)){
                    amountET.setError("Please add amount");
                }
                else {
                    float amount = Float.parseFloat(amountS);
                    if(amount<=0){
                        amountET.setError("?????");
                    }
                    else {
                        DatabaseManager manager = new DatabaseManager(getContext());
                        manager.addMoreBudget(event_id, amount);
                        extraBudgetListener.onEventDetailRefreshListener(event_id);
                        dismiss();
                    }
                }
            }
        });
    }

    public void onAttachToParentFragment(Fragment parentFragment) {
        if (parentFragment instanceof OnExtraBudgetListener) {
            extraBudgetListener = (OnExtraBudgetListener) parentFragment;
        } else
            throw new RuntimeException(parentFragment.toString() + " must implement OnExtraBudgetListener");

    }

    public interface OnExtraBudgetListener{
        public void onEventDetailRefreshListener(int event_id);
    }
}
