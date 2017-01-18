
package com.jkkniugmail.rubel.tourfriend.fragments.my_events_child;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jkkniugmail.rubel.tourfriend.Constants;
import com.jkkniugmail.rubel.tourfriend.R;
import com.jkkniugmail.rubel.tourfriend.adapters.ImageGalleryAdpter;
import com.jkkniugmail.rubel.tourfriend.databases.DatabaseManager;
import com.jkkniugmail.rubel.tourfriend.models.Moment;

import java.util.ArrayList;


public class ImageGallery extends Fragment {

    private int event_id;
    private DatabaseManager manager;
    private ArrayList<Moment> momentArrayList;
    private ImageGalleryAdpter adapter;
    private RecyclerView imageListView;

    public ImageGallery() {
        // Required empty public constructor
    }

    public static ImageGallery newInstance(int event_id) {

        Bundle args = new Bundle();
        args.putInt(Constants.EVENT_ID, event_id);
        ImageGallery fragment = new ImageGallery();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            event_id = getArguments().getInt(Constants.EVENT_ID);
        }
        else {
            //error
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        manager = new DatabaseManager(getContext());
        momentArrayList = new ArrayList<>();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.child_fragment_image_gallery, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageListView = (RecyclerView) view.findViewById(R.id.image_list);
        setImageAdaptere();

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setImageAdaptere(){
        momentArrayList = manager.getAllMoment(event_id);

        final StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        final StaggeredGridLayoutManager fullScreenLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(imageListView);
        adapter = new ImageGalleryAdpter(getContext(), momentArrayList);
        adapter.setOnItemClickListener(new ImageGalleryAdpter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                if(imageListView.getLayoutManager() == gridLayoutManager){
                    fullScreenLayoutManager.scrollToPosition(position);
                    imageListView.setLayoutManager(fullScreenLayoutManager);

                }
                else if (imageListView.getLayoutManager() == fullScreenLayoutManager){
                    imageListView.setLayoutManager(gridLayoutManager);

                }

            }
        });
        imageListView.setAdapter(adapter);
        imageListView.setLayoutManager(gridLayoutManager);
        //imageListView.setItemAnimator(new S );



    }

}
