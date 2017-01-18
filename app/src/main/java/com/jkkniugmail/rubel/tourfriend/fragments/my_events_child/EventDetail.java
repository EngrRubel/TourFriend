package com.jkkniugmail.rubel.tourfriend.fragments.my_events_child;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jkkniugmail.rubel.tourfriend.Constants;
import com.jkkniugmail.rubel.tourfriend.R;
import com.jkkniugmail.rubel.tourfriend.adapters.CustomExpandableListAdapter;
import com.jkkniugmail.rubel.tourfriend.databases.DatabaseManager;
import com.jkkniugmail.rubel.tourfriend.fragments.dialog_fragments.AllExpense;
import com.jkkniugmail.rubel.tourfriend.fragments.dialog_fragments.BudgetAdd;
import com.jkkniugmail.rubel.tourfriend.fragments.dialog_fragments.ExpenseAdd;
import com.jkkniugmail.rubel.tourfriend.models.Event;
import com.jkkniugmail.rubel.tourfriend.utils.MyDate;
import com.jkkniugmail.rubel.tourfriend.utils.MySharedPreferences;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by islan on 1/2/2017.
 */

public class EventDetail extends Fragment {
    private int event_id;
    private DatabaseManager manager;
    private List<String> groupItems;
    private HashMap<String, List<String>> childItems;
    private ViewHolder holder;
    private OnEventDetailInteractionListener interactionListener;
    private Event event;
    private float budget;
    private float totalExpense;
    private CustomExpandableListAdapter adapter;
    public static final String DIRECTORY= "/tourFriendMoment/photo";
    private String last_moment;

    public static EventDetail newInstance(int event_id) {
        Bundle args = new Bundle();
        args.putInt(Constants.EVENT_ID, event_id);
        EventDetail fragment = new EventDetail();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onAttachToParentFragment(getParentFragment());
        if (getArguments() != null) {
            event_id = getArguments().getInt(Constants.EVENT_ID);
        } else {
            //error
        }
        prepareExpandableListData();
        adapter = new CustomExpandableListAdapter(getContext(), groupItems, childItems);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.event_child_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        holder = new ViewHolder(view);
        manager = new DatabaseManager(getContext());
        getViewData();
        setViewData();




    }

    //to access data from database
    public void getViewData() {
        event = manager.getEvent(event_id);
        budget = manager.getBudget(event_id);
        totalExpense = manager.getTotalExpense(event_id);
    }

    //to set database data to view. must use after getViewData()
    public void setViewData() {
        holder.titleTV.setText(event.getTitle());
        holder.descriptionTV.setText(event.getDescription());
        holder.budgetTV.setText("Buget "+event.getBudget() + " tk");
        holder.locationTV.setText(event.getLocation());
        holder.start_timeTV.setText(event.getStarting_date());
        String count;
        count = new MyDate().getCountDown(event.getStarting_date());
        holder.time_countTV.setText(count);
        drawBudgetChart(holder.budgetChart);
        holder.expandableListView.setAdapter(adapter);
        holder.expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                if (i == 0) {
                    if (i1 == 0) {
                        DialogFragment expenseAdd = ExpenseAdd.newInstance(event_id);
                        expenseAdd.show(getFragmentManager(), "addExpense");

                    }
                    else if (i1 == 1) {
                        DialogFragment allExpense = AllExpense.newInstance(event_id);
                        allExpense.show(getFragmentManager(), "viewExpense");

                    }
                    else if (i1 == 2) {
                        DialogFragment extraBudget = BudgetAdd.newInstance(event_id);
                        extraBudget.show(getFragmentManager(), "extraBudget");
                    }

                    else {
                    }
                }
                else if (i == 1) {
                    if (i1 == 0) {
                        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        Uri file = Uri.fromFile(getFile());
                        camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
                        startActivityForResult(camera_intent,Constants.REQUEST_CAMERA);
                    }
                    else if (i1 == 1) {
                        interactionListener.onViewAllImage(event_id);
                    }
                    else {

                    }
                }
                else if (i == 2) {
                    if (i1 == 0) {
                        //edit event
                        interactionListener.onEventUpdateRequest(event_id);

                    }
                    else if (i1 == 1) {
                        //delete event
                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        alert.setTitle("Delete Event?");
                        alert.setMessage("Are you sure to delete the event");
                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(manager.deleteEvent(event_id)){
                                    interactionListener.onEventDelete(event_id);
                                }
                                else {
                                    Toast.makeText(getContext(), "please try again", Toast.LENGTH_LONG).show();
                                }
                                dialog.dismiss();
                            }
                        });
                        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alert.show();
                    }
                    else {

                    }
                }
                else {

                }
                return false;
            }
        });
    }

    public void onAttachToParentFragment(Fragment parentFragment) {
        if (parentFragment instanceof OnEventDetailInteractionListener) {
            interactionListener = (OnEventDetailInteractionListener) parentFragment;
        } else
            throw new RuntimeException(parentFragment.toString() + " must implement OnAddEventResult");

    }

    private void prepareExpandableListData() {
        groupItems = new ArrayList<>();
        childItems = new HashMap<>();

        groupItems.add(0, "Expendature");
        groupItems.add(1, "Moment");
        groupItems.add(2, "More");

        List<String> expendature = new ArrayList<>();
        expendature.add(0, "Add New Expense");
        expendature.add(1, "View All Expense ");
        expendature.add(2, "Add More Budget");

        List<String> moment = new ArrayList<>();
        moment.add(0, "Create A Moment");
        moment.add(1, "View All Moments");

        List<String> more = new ArrayList<>();
        more.add(0, "edit event");
        more.add(1, "delete event");

        childItems.put(groupItems.get(0), expendature);
        childItems.put(groupItems.get(1), moment);
        childItems.put(groupItems.get(2), more);

    }


    public void drawBudgetChart(ProgressBar budgetChart) {
        int progress;
        budgetChart.setVisibility(View.VISIBLE);
        holder.remainingTkTv.setText("Remaining "+(event.getBudget()-totalExpense)+" tk");
        holder.expenseTkTv.setText("Total expense "+totalExpense+" tk");

        if (budget == 0) {
            progress = 0;
            holder.expensePercentTv.setText("0 %");
            holder.remainingPercentTv.setText("100 %");
        }
        else {
            progress = (int) ((totalExpense / budget) * 100);
            holder.expensePercentTv.setText(progress+" %");
            holder.remainingPercentTv.setText((100-progress)+" %");
        }

        budgetChart.setProgress(progress);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Constants.REQUEST_CAMERA&&resultCode==RESULT_OK) {

            interactionListener.onMomentCapturedChild(event_id);
        }
    }


    //New Folder in External Storage +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    private File getFile(){
        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+DIRECTORY);

        if(!folder.exists()){
            folder.mkdir();
        }
        //Saving picture with the name

        String date = getCurrentDateFormat();
        String imageName = date+".jpg";
        File image_file = new File (folder,imageName);

        last_moment = String.valueOf(image_file);
        MySharedPreferences sharedPreferences = new MySharedPreferences(getContext());
        sharedPreferences.saveLastMoment(last_moment);

        Log.e("file", String.valueOf(image_file));

        return image_file;
    }


    private String getCurrentDateFormat(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String  currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }

    public interface OnEventDetailInteractionListener {
        void onViewAllImage(int event_id);
        void onEventDelete(int event_id);
        void onEventUpdateRequest(int event_id);
        void onMomentCapturedChild(int event_id);

    }

    class ViewHolder {

        TextView titleTV;
        TextView descriptionTV;
        TextView locationTV;
        TextView start_timeTV;
        TextView time_countTV;
        TextView budgetTV;
        TextView expensePercentTv;
        TextView expenseTkTv;
        TextView remainingPercentTv;
        TextView remainingTkTv;
        ProgressBar budgetChart;
        ExpandableListView expandableListView;

        ViewHolder(View view) {
            titleTV = (TextView) view.findViewById(R.id.event_title);
            descriptionTV = (TextView) view.findViewById(R.id.event_description);
            locationTV = (TextView) view.findViewById(R.id.event_location);
            start_timeTV = (TextView) view.findViewById(R.id.event_start_time);
            time_countTV = (TextView) view.findViewById(R.id.event_count_time);
            budgetTV = (TextView) view.findViewById(R.id.event_budget);
            expensePercentTv = (TextView) view.findViewById(R.id.expense_percent);
            expenseTkTv = (TextView) view.findViewById(R.id.expense_tk);
            remainingPercentTv = (TextView) view.findViewById(R.id.remaining_percent);
            remainingTkTv = (TextView) view.findViewById(R.id.remaining_tk);
            budgetChart = (ProgressBar) view.findViewById(R.id.buget_diagram);
            expandableListView = (ExpandableListView) view.findViewById(R.id.exapandable_list_view);

        }
    }
}
