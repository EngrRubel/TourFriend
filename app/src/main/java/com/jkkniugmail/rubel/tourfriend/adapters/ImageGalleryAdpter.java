package com.jkkniugmail.rubel.tourfriend.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jkkniugmail.rubel.tourfriend.R;
import com.jkkniugmail.rubel.tourfriend.models.Moment;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by islan on 1/6/2017.
 */

public class ImageGalleryAdpter extends RecyclerView.Adapter<ImageGalleryAdpter.ViewHolder> {

    private Context context;
    private ArrayList<Moment> momentArrayList;
    // Define listener member variable
    private OnItemClickListener listener;

    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ImageGalleryAdpter(Context context, ArrayList<Moment> momentArrayList) {
        this.context = context;
        this.momentArrayList = momentArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View imageView = inflater.inflate(R.layout.row_list_gallery, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(imageView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Moment moment = momentArrayList.get(position);
        holder.imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        holder.captionTv.setText(moment.getCaption().toString());


        File imgFile = new File(moment.getImage_link());

        if(imgFile.exists()){

            holder.imageView.setImageDrawable(Drawable.createFromPath(moment.getImage_link()));
        }
        else {
            holder.imageView.setImageResource(R.drawable.not_found);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(holder.itemView, position);
                    }
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return momentArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView captionTv;
        private View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            imageView = (ImageView) itemView.findViewById(R.id.img_row);
            captionTv = (TextView) itemView.findViewById(R.id.caption_tv);
        }
    }
}
