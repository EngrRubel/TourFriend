package com.jkkniugmail.rubel.tourfriend.fragments.dialog_fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.jkkniugmail.rubel.tourfriend.Constants;
import com.jkkniugmail.rubel.tourfriend.R;
import com.jkkniugmail.rubel.tourfriend.databases.DatabaseManager;
import com.jkkniugmail.rubel.tourfriend.models.Moment;
import com.jkkniugmail.rubel.tourfriend.utils.MySharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by islan on 1/9/2017.
 */


public class MomentSaving extends AppCompatDialogFragment {

    private int event_id;
    private ImageView imageView;
    private EditText captionET;
    private Button cancelBTN;
    private Button okBTN;
    MySharedPreferences sharedPreferences;
    private String image_url;

    static final String DIRECTORY= "/tourFriendMoment/photo";

    public static MomentSaving newInstance(int event_id) {
        Bundle args = new Bundle();
        args.putInt(Constants.EVENT_ID, event_id);

        MomentSaving fragment = new MomentSaving();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
        sharedPreferences = new MySharedPreferences(getContext());
        image_url = sharedPreferences.getLastMoment();
        if(getArguments()!=null){
            event_id = getArguments().getInt(Constants.EVENT_ID);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_moment_save, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView = (ImageView) view.findViewById(R.id.captured_image);
        captionET = (EditText) view.findViewById(R.id.captured_image_caption);
        cancelBTN = (Button) view.findViewById(R.id.moment_cancel_btn);
        okBTN = (Button) view.findViewById(R.id.moment_ok_btn);


        imageView.setImageDrawable(Drawable.createFromPath(image_url));

        cancelBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        okBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMoment();
                dismiss();
            }
        });
    }


    public void saveMoment(){
        Moment moment;
        String caption = captionET.getText().toString();
        DatabaseManager manager = new DatabaseManager(getContext());



        moment = new Moment(caption, getCurrentDateFormat(),image_url);

        manager.addMoment(moment, event_id);

    }


    private String getCurrentDateFormat(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String  currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }

}
