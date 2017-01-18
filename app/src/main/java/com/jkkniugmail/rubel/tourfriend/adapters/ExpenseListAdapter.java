package com.jkkniugmail.rubel.tourfriend.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jkkniugmail.rubel.tourfriend.R;
import com.jkkniugmail.rubel.tourfriend.models.Expense;

import java.util.ArrayList;

/**
 * Created by Bdjobs on 4/1/2017.
 */

public class ExpenseListAdapter extends ArrayAdapter {
    Context context;
    ArrayList<Expense> expenseArrayList;
    public ExpenseListAdapter(Context context, ArrayList<Expense> expenseArrayList) {
        super(context, R.layout.row_expense_list, expenseArrayList);
        this.context = context;
        this.expenseArrayList = expenseArrayList;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {


        ViewHolder viewHolder;
        Expense expense;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_expense_list, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);

        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }
        expense = expenseArrayList.get(position);
        viewHolder.detailTV.setText(expense.getDetail());
        viewHolder.amountTV.setText(String.valueOf(expense.getCost()));


        return view;
    }

    private class ViewHolder{
        TextView detailTV;
        TextView amountTV;
        //TextView timeTV;
        public ViewHolder(View view){
            detailTV = (TextView) view.findViewById(R.id.row_expense_detail);
            amountTV = (TextView) view.findViewById(R.id.row_expense_amount);
        }
    }
}
