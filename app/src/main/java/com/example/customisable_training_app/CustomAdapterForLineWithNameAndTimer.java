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
            holder.sound = (TextView) convertView.findViewById(R.id.textView2);
            convertView.setTag(holder);
        }
        else
        {
            holder = (CustomAdapterForLineWithNameAndTimer.ViewHolder) convertView.getTag();
        }

        holder.txtNameOfTimer.setText(lineWithNameAndTimerArrayList.get(position).getNameOfTimer());

        int startSound = lineWithNameAndTimerArrayList.get(position).getSoundWhenStart();
        if(startSound == 0)
            holder.sound.setText("Box Bell 1");
        if(startSound == 1)
            holder.sound.setText("Box Bell 2");
        if(startSound == 2)
            holder.sound.setText("Air Horn");;
        if(startSound == 3)
            holder.sound.setText("Drums");;
        if(startSound == 4)
            holder.sound.setText("Epic");;
        if(startSound == 5)
            holder.sound.setText("Fare");;
        if(startSound == 6)
            holder.sound.setText("Gong 1");;
        if(startSound == 7)
            holder.sound.setText("Gong 2");;
        if(startSound == 8)
            holder.sound.setText("Gong 3");;
        if(startSound == 9)
            holder.sound.setText("Gong 4");;
        if(startSound == 10)
            holder.sound.setText("Lego Yoda");;

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
        TextView sound;
    }//class ViewHolder
}
