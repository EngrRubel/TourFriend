package com.jkkniugmail.rubel.tourfriend.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jkkniugmail.rubel.tourfriend.R;
import com.jkkniugmail.rubel.tourfriend.models.pojo.NearbyPlaces;

import java.util.List;

/**
 * Created by islan on 1/6/2017.
 */

public class NearbyListAdapter extends ArrayAdapter{
    private Context context;
    private List<NearbyPlaces.Result> resultList;
    public NearbyListAdapter(Context context, List<NearbyPlaces.Result> resultList) {
        super(context, R.layout.row_nearby_list, resultList);
        this.context = context;
        this.resultList = resultList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        NearbyPlaces.Result result;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_nearby_list, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        result = resultList.get(position);
        holder.name_tv.setText(result.getName());
        holder.address_tv.setText(result.getVicinity());
        String open;


        return convertView;
    }

    public class ViewHolder{
        TextView name_tv;
        TextView address_tv;

        public ViewHolder(View view){
            name_tv = (TextView) view.findViewById(R.id.nearby_name);
            address_tv = (TextView) view.findViewById(R.id.nearby_address);
        }

    }
}
