package com.jkkniugmail.rubel.tourfriend.fragments.dialog_fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.jkkniugmail.rubel.tourfriend.Constants;
import com.jkkniugmail.rubel.tourfriend.R;
import com.jkkniugmail.rubel.tourfriend.databases.DatabaseManager;
import com.jkkniugmail.rubel.tourfriend.models.Expense;
import com.jkkniugmail.rubel.tourfriend.utils.MyDate;

/**
 * Created by islan on 1/3/2017.
 */

public class ExpenseAdd extends AppCompatDialogFragment {
    private int event_id;
    private long budget;
    private long total_expense;
    private EditText detail_et;
    private EditText amount_et;
    private Button ok_btn;
    private Button cancel_btn;
    private OnAddExpenseListener expenseListener;

    public static ExpenseAdd newInstance(int event_id) {

        Bundle args = new Bundle();
        args.putInt(Constants.EVENT_ID, event_id);
        ExpenseAdd fragment = new ExpenseAdd();
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

    public void onAttachToParentFragment(Fragment parentFragment) {
        if (parentFragment instanceof OnAddExpenseListener) {
            expenseListener = (OnAddExpenseListener) parentFragment;
        } else
            throw new RuntimeException(parentFragment.toString() + " must implement OnAddExpenseListener");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_view_add_expense, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        detail_et = (EditText) view.findViewById(R.id.expense_detail_et);
        amount_et = (EditText) view.findViewById(R.id.expense_amount_et);
        ok_btn = (Button) view.findViewById(R.id.expense_add_ok_btn);
        cancel_btn = (Button) view.findViewById(R.id.expense_add_cancel_btn);
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseManager manager = new DatabaseManager(getContext());
                float budget = manager.getBudget(event_id);
                float total_expense = manager.getTotalExpense(event_id);
                String expense_detail = detail_et.getText().toString();
                String expense_amount_string = amount_et.getText().toString();
                if (TextUtils.isEmpty(expense_amount_string)) {
                    amount_et.setError("please insert amount");
                }
                if (TextUtils.isEmpty(expense_detail)) {
                    detail_et.setError("please insert a detail");
                }
                if (!TextUtils.isEmpty(expense_amount_string) && !TextUtils.isEmpty(expense_detail)) {
                    float expense_amount = Float.parseFloat(expense_amount_string);
                    if (expense_amount > budget - total_expense) {


                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        alert.setTitle("");
                        alert.setMessage("You have not sufficient balance\nPlease add more budget");
                        alert.show();
                        dismiss();
                    }
                    else if (expense_amount<=0){
                        amount_et.setError("?????");
                    }

                    else {
                        String date = new MyDate().getTodayDateString();
                        Expense expense = new Expense(expense_detail, expense_amount, date);
                        manager.addExpense(expense, event_id);
                        expenseListener.onEventDetailRefreshListener(event_id);
                        dismiss();

                    }
                }

            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }


    public interface OnAddExpenseListener{
        public void onEventDetailRefreshListener(int event_id);
    }

}
