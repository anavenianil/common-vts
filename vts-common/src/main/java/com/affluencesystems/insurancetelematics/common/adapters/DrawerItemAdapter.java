package com.affluencesystems.insurancetelematics.common.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.affluencesystems.insurancetelematics.common.Models.DataModel;
import com.affluencesystems.insurancetelematics.R;

import java.util.ArrayList;

public class DrawerItemAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<DataModel> items;
    private LayoutInflater layoutInflater;
    private Holder holder;

    /*
    *       constructor for adapter.
    * */
    public DrawerItemAdapter(Context context, ArrayList<DataModel> items){
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.items = items;
    }

    /*
    *       need to update adapter after any visible change in settings screen.
    * */
    public void updateAdapter(ArrayList<DataModel> items){
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return items.get(i).getId();
    }

    /*
    *       initialize views and setting the values
    * */
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        holder = new Holder();
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_view_item_row, null);
            holder.imageViewIcon = (ImageView) convertView.findViewById(R.id.imageViewIcon);
            holder.textViewName = (TextView) convertView.findViewById(R.id.textViewName);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.imageViewIcon.setImageResource(items.get(i).icon);
        holder.textViewName.setText(items.get(i).name);


        return convertView;
    }

    public static class Holder{
        ImageView imageViewIcon;
        TextView textViewName;
    }

}
