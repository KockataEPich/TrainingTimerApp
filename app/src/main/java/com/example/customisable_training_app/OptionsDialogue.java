package com.example.customisable_training_app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.zip.Inflater;

//Class that is involved with the options pop up
public class OptionsDialogue extends AppCompatDialogFragment implements EditPageInformationExchangeListener
{
    private EditText rounds;
    private CheckBox tts;
    private CheckBox continueWhenLocked;
    private CheckBox vibrate;
    private EditPageInformationExchangeListener listener;

    private Button addRoundButton;
    private Button subtractRoundButton;

    public int currentRounds;
    public boolean boolTTS;
    public boolean boolContinueWhenLocked;
    public boolean boolVibrate;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {

        // Create the dialogue builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.options_menu, null);

        // Set the options buttons and title
        builder.setView(view)
                .setTitle("Options")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {


                    }//onclick
                })
                .setPositiveButton("Set", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // Get the values from the view that we will pass down
                        int roundsNumber = Integer.parseInt(rounds.getText().toString());
                        boolean isTTS = tts.isChecked();
                        boolean isContinueWhenLocked = continueWhenLocked.isChecked();
                        boolean isVibrate = vibrate.isChecked();
                        listener.textFromOptionsToEditPage(roundsNumber, isTTS, isContinueWhenLocked, isVibrate);
                    }//onClick
                });


      rounds = view.findViewById(R.id.view_of_rounds);
      tts = view.findViewById(R.id.checkbox_TTS);
      continueWhenLocked = view.findViewById(R.id.checkbox_ContinueWhenLocked);
      vibrate = view.findViewById(R.id.checkbox_Vibrate);
      addRoundButton = view.findViewById(R.id.add_round_button);
      subtractRoundButton = view.findViewById(R.id.subtract_round_button);





      addRoundButton.setOnClickListener(new View.OnClickListener()
      {
          @Override
          public void onClick(View v)
          {
              currentRounds++;
              if(currentRounds <= 99)
              {
                  setTextOnView();
                  rounds.clearFocus();
              }
              else
              {
                  currentRounds--;
              }
          }//onClick
      });




       subtractRoundButton.setOnClickListener(new View.OnClickListener()
       {
           @Override
           public void onClick(View v)
           {
               currentRounds--;
               if(currentRounds >= 0)
               {
                   setTextOnView();
                   rounds.clearFocus();
               }//if
               else
               {
                   currentRounds++;
               }//else
           }//onClick
       });

       tts.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               boolean checked = ((CheckBox) v).isChecked();
               // Check which checkbox was clicked
               if (checked)
               {
                   rounds.clearFocus();
                   // Do your coding
               }
               else{
                   // Do your coding
               }
           }
       });

       continueWhenLocked.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               boolean checked = ((CheckBox) v).isChecked();
               // Check which checkbox was clicked
               if (checked)
               {
                   rounds.clearFocus();
                   // Do your coding
               }
               else
               {
                   // Do your coding
               }
           }
       });

        vibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                boolean checked = ((CheckBox) v).isChecked();
                // Check which checkbox was clicked
                if (checked)
                {
                    rounds.clearFocus();
                    // Do your coding
                }
                else{
                    // Do your coding
                }
            }
        });





        rounds.setOnFocusChangeListener(new View.OnFocusChangeListener()
      {
           @Override
           public void onFocusChange(View v, boolean hasFocus)
           {
               if(!hasFocus)
               {
                   String strEnteredVal = rounds.getText().toString();
                   if(strEnteredVal.equals("") || strEnteredVal.equals("") )
                   {
                       rounds.setText("00");
                   }

                   if(strEnteredVal.length() == 1)
                   {
                       rounds.setText("0" + strEnteredVal);
                   }

                   if(strEnteredVal.equals("") )
                   {
                       rounds.setText("0" + 1);
                   }
                   else if(Integer.parseInt(strEnteredVal) == 0)
                   {
                       rounds.setText("0" + 1);
                   }
               }
           }
        });

        rounds.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(!rounds.getText().toString().isEmpty())
                {
                    int number = Integer.parseInt(rounds.getText().toString());
                    if (number > 99)
                    {
                        rounds.setText("99");

                    }//if
                    else
                    {
                        currentRounds = number;

                    }//else
                }//if

            }//onTextChanged

            @Override
            public void afterTextChanged(Editable s)
            {

            }//afterTextChanged
        });


      setTextOnView();
      checkTTS(boolTTS);
      checkContinueWhenLocked(boolContinueWhenLocked);
      checkVibrate(boolVibrate);

      return builder.create();
    }//onCreateDialogue


    public void setTextOnView()
    {
        if(currentRounds > 0)
        {
            if (currentRounds < 10)
                rounds.setText("0" + currentRounds + ":");
            else
                rounds.setText("" + currentRounds + ":");
        }//if
    }//setText

    // Method which we use the interface to send values
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

    public void checkTTS(boolean givenTTS)
    {
        if(givenTTS)
        {
            tts.setChecked(true);
        }//if
        else
        {
            tts.setChecked(false);
        }//else
    }//setTTS

    public void checkContinueWhenLocked(boolean givenContinueWhenLocked)
    {
        if(givenContinueWhenLocked)
        {
            continueWhenLocked.setChecked(true);
        }//if
        else
        {
            continueWhenLocked.setChecked(false);
        }//else
    }//setContinueWhenLocked

    public void checkVibrate(boolean givenVibrate)
    {
        if(givenVibrate)
        {
            vibrate.setChecked(true);
        }
        else
        {
            vibrate.setChecked(false);
        }//else
    }//setVibrate



    public void textFromOptionsToEditPage(int givenNumberOfRounds, boolean ttsIsOn,
                                   boolean continueWhenLockedIsOn, boolean vibrateIsOn){}

    public void textFromTimerEditToEditPage(String name, int sound, int minutes, int seconds){}

}
