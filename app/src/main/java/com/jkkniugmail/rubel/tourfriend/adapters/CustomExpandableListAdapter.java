package com.jkkniugmail.rubel.tourfriend.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.jkkniugmail.rubel.tourfriend.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Bdjobs on 3/1/2017.
 */

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> groupItems;
    private HashMap<String, List<String>> childItems;

    public CustomExpandableListAdapter(Context context, List<String> groupItems, HashMap<String, List<String>> childItems) {
        this.context = context;
        this.groupItems = groupItems;
        this.childItems = childItems;
    }

    @Override
    public int getGroupCount() {
        return groupItems.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return childItems.get(groupItems.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return groupItems.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return childItems.get(groupItems.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.row_group_items, null);
        TextView groupName = (TextView) view.findViewById(R.id.group_name_tv);
        groupName.setText(groupItems.get(i));
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.row_child_items, null);
        TextView groupName = (TextView) view.findViewById(R.id.child_name_tv);
        groupName.setText(childItems.get(groupItems.get(i)).get(i1));
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

}
