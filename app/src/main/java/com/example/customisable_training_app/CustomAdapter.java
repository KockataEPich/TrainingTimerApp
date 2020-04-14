package com.example.customisable_training_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter
{
    private static ArrayList<Line> lineArrayList;

    private LayoutInflater inflater;

    public CustomAdapter(Context context, ArrayList<Line> allLines)
    {
        lineArrayList = allLines;
        inflater = LayoutInflater.from(context);
    }//Constructor

    public int getCount()
    {
        return lineArrayList.size();
    }//getCount

    public Object getItem(int position)
    {
        return lineArrayList.get(position);
    }//getItem

    public long getItemId(int position)
    {
        return position;
    }//getItemId

    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.custom_line, null);
            holder = new ViewHolder();
            holder.txtNameOfWorkout = (TextView) convertView.findViewById(R.id.name_of_program);
            holder.dateOfCreation = (TextView) convertView.findViewById(R.id.date_of_program);




            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.txtNameOfWorkout.setText(lineArrayList.get(position).getNameOfWorkout());

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(lineArrayList.get(position).getDateOfCreation());
        holder.dateOfCreation.setText(formattedDate);
        return convertView;
    }

    static class ViewHolder
    {
        TextView txtNameOfWorkout;
        TextView dateOfCreation;



    }//class ViewHolder
}


