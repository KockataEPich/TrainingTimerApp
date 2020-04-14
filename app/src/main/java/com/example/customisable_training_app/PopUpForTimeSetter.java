package com.example.customisable_training_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class PopUpForTimeSetter extends AppCompatDialogFragment implements EditPageInformationExchangeListener
{

   public int currentMinutes;
   public int currentSeconds;

   public int startSound;
   public String givenName;

   EditText minutes;
   EditText seconds;
   EditText name;

   boolean atTheEnd = false;
   boolean atTheBeginning = false;

   private EditPageInformationExchangeListener listener;

    TextView addMinButton;
    TextView addSecondsButton;
    TextView removeMinuteButton;
    TextView removeSecondButton;
    TextView setButton;


    Spinner dropdown;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {

        // Create the dialogue builder
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_pop_up_for_time_setter, null);

        // Set the options buttons and title
        builder.setView(view);





        addMinButton = view.findViewById(R.id.add_min_button);
        addSecondsButton = view.findViewById(R.id.add_seconds_button);
        removeMinuteButton = view.findViewById(R.id.subtract_minute_button);
        removeSecondButton = view.findViewById(R.id.subtract_second_button);
        setButton = view.findViewById(R.id.set_button);

        setButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v)
            {
                YoYo.with(Techniques.Tada)
                        .duration(400)
                        .playOn(v);


                String nameOfTimer = name.getText().toString();
                listener.textFromTimerEditToEditPage(nameOfTimer, startSound, currentMinutes, currentSeconds);
                dismiss();
            }
        });

        addMinButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                YoYo.with(Techniques.Tada)
                        .duration(400)
                        .playOn(v);

                YoYo.with(Techniques.Tada)
                        .duration(400)
                        .playOn(minutes);

                if(currentMinutes != 59)
                {
                    currentMinutes++;
                    if(currentMinutes < 10)
                        minutes.setText("0" + currentMinutes + ":");
                    else
                        minutes.setText("" + currentMinutes + ":");

                    atTheEnd = false;
                }
                else
                    atTheEnd = true;
            }//onClick
        });

        addSecondsButton.setOnClickListener(new View.OnClickListener()
        {
           @Override
           public void onClick(View v)
           {

               YoYo.with(Techniques.Tada)
                       .duration(400)
                       .playOn(v);

               YoYo.with(Techniques.Tada)
                       .duration(400)
                       .playOn(seconds);

            if(currentSeconds != 59)
            {
                currentSeconds++;
                if(currentSeconds < 10)
                    seconds.setText("0" + currentSeconds);
                else
                    seconds.setText("" + currentSeconds);
            }//if
            else if(!atTheEnd)
            {
                addMinButton.callOnClick();
                currentSeconds = 0;
                seconds.setText("0" + currentSeconds);
            }//if
           }//onClick
       });

        removeMinuteButton.setOnClickListener(new View.OnClickListener()
        {
        @Override
        public void onClick(View v)
        {

            YoYo.with(Techniques.Tada)
                    .duration(400)
                    .playOn(v);

            YoYo.with(Techniques.Tada)
                    .duration(400)
                    .playOn(minutes);

            if(currentMinutes != 0)
            {
            currentMinutes--;
            if(currentMinutes < 10)
                minutes.setText("0" + currentMinutes + ":");
            else
                minutes.setText("" + currentMinutes + ":");
            }
            else
               atTheBeginning = true;
        }//onClick
       });

        removeSecondButton.setOnClickListener(new View.OnClickListener()
        {
        @Override
        public void onClick(View v)
        {
            YoYo.with(Techniques.Tada)
                    .duration(400)
                    .playOn(v);

            YoYo.with(Techniques.Tada)
                    .duration(400)
                    .playOn(seconds);

            if(currentSeconds != 0)
            {
                currentSeconds--;
                if (currentSeconds < 10)
                    seconds.setText("0" + currentSeconds);
                else
                    seconds.setText("" + currentSeconds);
            }//if
            else if (!atTheEnd)
            {
                removeMinuteButton.callOnClick();
                currentSeconds = 59;
                seconds.setText("" + currentSeconds);
            }
        }//onClick
        });




        name = view.findViewById(R.id.timer_name);



        minutes =  view.findViewById(R.id.text_view_of_minutes);
        seconds =  view.findViewById(R.id.text_view_of_seconds);


        minutes.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(!hasFocus)
                {
                    String strEnteredVal = minutes.getText().toString();
                    if(strEnteredVal.equals("") || strEnteredVal.equals("") )
                    {
                        minutes.setText("00");
                    }

                    if(strEnteredVal.length() == 1)
                    {
                        minutes.setText("0" + strEnteredVal);
                    }
                }


            }
        });

        minutes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(!minutes.getText().toString().isEmpty())
                {
                    int number = Integer.parseInt(minutes.getText().toString());
                    currentMinutes = number;
                    if (number > 60)
                    {

                        if ((number - 60) < 10)
                        {
                            minutes.setText("0" + (number - 60));
                            currentMinutes = number - 60;
                            minutes.setSelection(seconds.getText().length());
                        }
                        else
                        {
                            currentMinutes = number - 60;
                            minutes.setText("" + (number - 60));
                            minutes.setSelection(seconds.getText().length());
                        }
                    }

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




        seconds.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(!hasFocus)
                {
                    String strEnteredVal = seconds.getText().toString();
                    if(strEnteredVal.equals("") )
                    {
                        seconds.setText("00");
                    }

                    if(strEnteredVal.length() == 1)
                    {
                        seconds.setText("0" + strEnteredVal);
                    }
                }


            }
        });

        seconds.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (!seconds.getText().toString().isEmpty())
                {
                    int number = Integer.parseInt(seconds.getText().toString());
                    currentSeconds = number;
                    if (number > 60)
                    {
                        if ((number - 60) < 10)
                        {
                            currentSeconds = number - 60;
                            seconds.setText("0" + (number - 60));
                            seconds.setSelection(seconds.getText().length());
                        }
                        else
                        {
                            currentSeconds = number - 60;
                            seconds.setText("" + (number - 60));
                            seconds.setSelection(seconds.getText().length());
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        name.setText(givenName);

        if(currentMinutes < 10)
            minutes.setText("0" + currentMinutes + ":");
        else
            minutes.setText("" + currentMinutes + ":");

        if(currentSeconds < 10)
            seconds.setText("0" + currentSeconds);
        else
            seconds.setText("" + currentSeconds);

        setDropdown(view);
        return builder.create();
    }

    public void setDropdown(View view)
    {
        dropdown = view.findViewById(R.id.spinner);
        String[] items = new String[]{"Box Bell 1", "Box Bell 2", "Air Horn", "Drums", "Epic", "Fare", "Gong 1", "Gong 2", "Gong 3", "Gong 4"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(R.layout.spinner_background);
        dropdown.setAdapter(adapter);

        dropdown.setSelection(startSound);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);

                if(position == 0)
                    startSound = 0;
                if(position == 1)
                    startSound = 1;
                if(position == 2)
                    startSound = 2;
                if(position == 3)
                    startSound = 3;
                if(position == 4)
                    startSound = 4;
                if(position == 5)
                    startSound = 5;
                if(position == 6)
                    startSound = 6;
                if(position == 7)
                    startSound = 7;
                if(position == 8)
                    startSound = 8;
                if(position == 9)
                    startSound = 9;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }

    public void onAttach(Context context)
    {
        super.onAttach(context);
        try
        {
            listener = (EditPageInformationExchangeListener) context;
        }//try
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString() +
                    "must implement EditPageInformationListener");
        }//catch
    }//onAttach

    public void textFromOptionsToEditPage(int givenNumberOfRounds, boolean ttsIsOn,
                                          boolean continueWhenLockedIsOn, boolean vibrateIsOn){}

    public void textFromTimerEditToEditPage(String name, int sound, int minutes, int seconds){}


}
