package com.jkkniugmail.rubel.tourfriend.fragments.dialog_fragments;

import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.jkkniugmail.rubel.tourfriend.Constants;
import com.jkkniugmail.rubel.tourfriend.R;
import com.jkkniugmail.rubel.tourfriend.databases.DatabaseManager;
import com.jkkniugmail.rubel.tourfriend.models.Expense;
import com.jkkniugmail.rubel.tourfriend.adapters.ExpenseListAdapter;

import java.util.ArrayList;

/**
 * Created by Bdjobs on 4/1/2017.
 */

public class AllExpense extends AppCompatDialogFragment {

    private int event_id;
    private float totalExpense;
    private ArrayList<Expense> expenseArrayList;
    private ExpenseListAdapter adapter;
    private DatabaseManager manager;
    private ListView listView;
    private TextView totalExpenseTV;

    public static AllExpense newInstance(int event_id) {

        Bundle args = new Bundle();
        args.putInt(Constants.EVENT_ID, event_id);
        AllExpense fragment = new AllExpense();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
        if (getArguments() != null) {
            event_id = getArguments().getInt(Constants.EVENT_ID);
        } else {
            //error
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_view_all_expense, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        manager = new DatabaseManager(getContext());
        listView = (ListView) view.findViewById(R.id.expense_list_view);
        totalExpenseTV = (TextView) view.findViewById(R.id.total_expense);
        expenseArrayList = manager.getAllExpense(event_id);
        totalExpense = manager.getTotalExpense(event_id);
        adapter = new ExpenseListAdapter(getContext(), expenseArrayList);
        listView.setAdapter(adapter);
        totalExpenseTV.setText(String.valueOf(totalExpense)+ " tk");

    }
}







