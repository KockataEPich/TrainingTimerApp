package com.example.customisable_training_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapterForLineWithNameAndTimer extends BaseAdapter
{
    private static ArrayList<LineWithNameAndTimer> lineWithNameAndTimerArrayList;

    private LayoutInflater inflater;

    public CustomAdapterForLineWithNameAndTimer(Context context, ArrayList<LineWithNameAndTimer>
                                                                                           allLines)
    {
        lineWithNameAndTimerArrayList = allLines;
        inflater = LayoutInflater.from(context);
    }//Constructor

    public int getCount()
    {
        return lineWithNameAndTimerArrayList.size();
    }//getCount

    public Object getItem(int position)
    {
        return lineWithNameAndTimerArrayList.get(position);
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
            convertView = inflater.inflate(R.layout.line_with_name_and_timer, null);
            holder = new ViewHolder();
            holder.txtNameOfTimer = (TextView) convertView.findViewById(R.id.name_of_timer);
            holder.actualTimerSeconds = (TextView) convertView.findViewById(R.id.the_timer_itself_seconds);
            convertView.setTag(holder);
        }
        else
        {
            holder = (CustomAdapterForLineWithNameAndTimer.ViewHolder) convertView.getTag();
        }

        holder.txtNameOfTimer.setText(lineWithNameAndTimerArrayList.get(position).getNameOfTimer());

        String totalTime = "" + lineWithNameAndTimerArrayList.get(position).getMinutesOfTimer();
        totalTime +=":";
        if (lineWithNameAndTimerArrayList.get(position).getSecondsOfTimer() < 10)
            totalTime += "0";
        totalTime += lineWithNameAndTimerArrayList.get(position).getSecondsOfTimer();

        holder.actualTimerSeconds.setText(totalTime);

        return convertView;
    }

    static class ViewHolder
    {
        TextView txtNameOfTimer;
        TextView actualTimerSeconds;
    }//class ViewHolder
}
