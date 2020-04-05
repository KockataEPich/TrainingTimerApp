package com.example.customisable_training_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.security.cert.PKIXRevocationChecker;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class EditPage extends AppCompatActivity implements EditPageInformationExchangeListener {


    private Object o;
    private LineWithNameAndTimer fullObject;
    private ListView listOfTimers;
    private ArrayList<LineWithNameAndTimer> allLines;
    private CustomAdapterForLineWithNameAndTimer adapter;
    private AdView mAdView;
    private int savedIndex;
    private SharedPreferences saveFileForWorkouts;
    private SharedPreferences.Editor myEdit;

    boolean tts;
    boolean continueWhenLocked;
    boolean vibrate;

    private EditPageInformationExchangeListener listener;

    private int positionInList;

    private EditText editText;
    private EditText rounds;

    private int numberOfRounds;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_page);

        LinearLayout adLayout = findViewById(R.id.ad_layout);

        mAdView = new AdView(this);
        mAdView.setAdSize(AdSize.BANNER);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        mAdView.setLayoutParams(params);
        adLayout.addView(mAdView);

        mAdView.setAdUnitId("ca-app-pub-3935469427404511/2734014321");
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        saveFileForWorkouts = getSharedPreferences("saveFileForOfWorkouts", MODE_PRIVATE);
        // The edit for the save file
        myEdit = saveFileForWorkouts.edit();

        editText = findViewById(R.id.choose_name_of_program);

        Intent intent = getIntent();
        setTitle("Program: " + intent.getStringExtra("nameOfWholeProgram"));
        editText.setText(intent.getStringExtra("nameOfWholeProgram"));
        savedIndex = intent.getIntExtra("savedIndex", 0);
        numberOfRounds = intent.getIntExtra("numberOfRounds", 1);

        tts = intent.getBooleanExtra("isTTSOn", false);
        continueWhenLocked = intent.getBooleanExtra("IsContinueOn", false);
        vibrate = intent.getBooleanExtra("isVibrateOn", false);



        //numberOfRounds = findViewById(R.id.number_of_rounds);
        //numberOfRounds.setText("" + intent.getIntExtra("numberOfRounds", 0));

      /*  numberOfRounds.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(!hasFocus)
                {
                    String strEnteredVal = numberOfRounds.getText().toString();
                    if(strEnteredVal.equals("") )
                    {
                        numberOfRounds.setText("" + 1);
                        Toast.makeText(EditPage.this, "You need to have at least 1 round", Toast.LENGTH_SHORT).show();
                    }
                    else if(Integer.parseInt(strEnteredVal) == 0)
                    {
                        numberOfRounds.setText("" + 1);
                        Toast.makeText(EditPage.this, "You need to have at least 1 round", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });
        */


        allLines = getAllLines();
        listOfTimers = findViewById(R.id.list_of_timers);
        adapter = new CustomAdapterForLineWithNameAndTimer(this, allLines);
        listOfTimers.setAdapter(adapter);

        listOfTimers.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id)
            {

                positionInList = position;
                o = listOfTimers.getItemAtPosition(position);
                fullObject = (LineWithNameAndTimer) o;

                timeSetter(fullObject);
            }
        });


        editText.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (!hasFocus)
                {
                    updateSharedPreference();
                }
            }
        });


    }

    //TODO option to start the program
    @Override
    public void onBackPressed()
    {

        Intent resultIntent = new Intent();
        String message = editText.getText().toString();
        resultIntent.putExtra("numberOfRounds", numberOfRounds);

        resultIntent.putExtra("passedNameOfWorkout", message);
        resultIntent.putExtra("sizeOfList", allLines.size());

        resultIntent.putExtra("isTTSOn", tts);
        resultIntent.putExtra("isVibrateOn", vibrate);
        resultIntent.putExtra("isContinueOn", continueWhenLocked);

        for (int i = 0; i < allLines.size(); i++)
        {
            resultIntent.putExtra(("startSound2" + i), allLines.get(i).getSoundWhenStart());
            resultIntent.putExtra(("nameOfTimer2" + i), allLines.get(i).getNameOfTimer());
            resultIntent.putExtra(("minutesOfTimer2" + i), allLines.get(i).getMinutesOfTimer());
            resultIntent.putExtra(("secondsOfTimer2" + i), allLines.get(i).getSecondsOfTimer());

        }

        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    public void timeSetter(LineWithNameAndTimer timer)
    {
        PopUpForTimeSetter popUpForTimeSetter = new PopUpForTimeSetter();
        popUpForTimeSetter.givenName = timer.getNameOfTimer();
        popUpForTimeSetter.startSound = timer.getSoundWhenStart();
        popUpForTimeSetter.currentMinutes = timer.getMinutesOfTimer();
        popUpForTimeSetter.currentSeconds = timer.getSecondsOfTimer();
        popUpForTimeSetter.show(getSupportFragmentManager(), "time setter dialogue");
    }

    public void options(View view)
    {
        OptionsDialogue optionsDialogue = new OptionsDialogue();
        optionsDialogue.currentRounds = numberOfRounds;
        optionsDialogue.boolTTS = tts;
        optionsDialogue.boolContinueWhenLocked = continueWhenLocked;
        optionsDialogue.boolVibrate = vibrate;
        optionsDialogue.show(getSupportFragmentManager(), "options dialogue");
    }//options




    public void deleteProgram(View view)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Delete entry");
        alert.setMessage("Are you sure you want to delete this program?");
        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent resultIntent = new Intent();
                setResult(8, resultIntent);
                finish();
            }
        });

        alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });
        alert.show();

    }//deleteProgram


    public void startProgram(View view)
    {
        if (allLines.size() == 0)
        {
            Toast toast = Toast.makeText(this, "There are no timers", Toast.LENGTH_SHORT);
            toast.show();
        }
        else
        {
            Intent intent = new Intent(EditPage.this, CountDownRunning.class);
            intent.putExtra("sizeOfList", allLines.size());
            intent.putExtra("numberOfRounds", numberOfRounds);

            for (int i = 0; i < allLines.size(); i++)
            {
                intent.putExtra("startSound" + i, allLines.get(i).getSoundWhenStart());
                intent.putExtra(("nameOfTimer3" + i), allLines.get(i).getNameOfTimer());
                intent.putExtra(("minutesOfTimer3" + i), allLines.get(i).getMinutesOfTimer());
                intent.putExtra(("secondsOfTimer3" + i), allLines.get(i).getSecondsOfTimer());
            }
            startActivity(intent);
        }
    }

    public ArrayList<LineWithNameAndTimer> getAllLines()
    {

        ArrayList<LineWithNameAndTimer> getInitialLines = new ArrayList<LineWithNameAndTimer>();


        Intent intent = getIntent();
        int numberOfTimers = intent.getIntExtra("numberOfTimers", 0);

        for (int i = 0; i < numberOfTimers; i++)
        {
            LineWithNameAndTimer lineToAdd = new LineWithNameAndTimer();
            lineToAdd.setSoundWhenStart(intent.getIntExtra("sound" + i, 0));
            lineToAdd.setNameOfTimer(intent.getStringExtra(("nameOfTimer" + i)));
            lineToAdd.setMinutesOfTimer(intent.getIntExtra(("minutesOfTimer" + i), 0));
            lineToAdd.setSecondsOfTimer(intent.getIntExtra(("secondsOfTimer" + i), 0));
            getInitialLines.add(lineToAdd);
        }
        return getInitialLines;
    }


    public void addTimer(View view)
    {
        LineWithNameAndTimer lineToAdd = new LineWithNameAndTimer();

        lineToAdd.setNameOfTimer("Timer");
        lineToAdd.setMinutesOfTimer(0);
        lineToAdd.setSecondsOfTimer(30);
        allLines.add(lineToAdd);
        adapter.notifyDataSetChanged();
        updateSharedPreference();
    }//addTimer


    public void updateSharedPreference()
    {


        myEdit.putString(("nameOfWorkout" + savedIndex), editText.getText().toString());
        myEdit.putInt(("numberOfRounds" + savedIndex), numberOfRounds);
        myEdit.putInt(("howManyTimers" + savedIndex), allLines.size());
        myEdit.putBoolean(("isTTSOn" + savedIndex), tts);
        myEdit.putBoolean(("isContinueOn" + savedIndex), continueWhenLocked);
        myEdit.putBoolean(("isVibrateOn" + savedIndex), vibrate);

        for (int j = 0; j < allLines.size(); j++) {
            myEdit.putString(("currentTimerName" + savedIndex + "" + j), allLines.get(j).getNameOfTimer());
            myEdit.putInt(("currentSoundWhenStart" + savedIndex + "" + j), allLines.get(j).getSoundWhenStart());
            myEdit.putInt(("currentTimerMinutes" + savedIndex + "" + j), allLines.get(j).getMinutesOfTimer());
            myEdit.putInt(("currentTimerSeconds" + savedIndex + "" + j), allLines.get(j).getSecondsOfTimer());
        }//for

        myEdit.commit();
    }

    public void textFromOptionsToEditPage(int givenNumberOfRounds, boolean ttsIsOn,
                                          boolean continueWhenLockedIsOn, boolean vibrateIsOn)
    {
        numberOfRounds = givenNumberOfRounds;
        tts = ttsIsOn;
        continueWhenLocked = continueWhenLockedIsOn;
        vibrate = vibrateIsOn;
        updateSharedPreference();
    }//textFromOptionsToEditPage

    public void textFromTimerEditToEditPage(String name, int sound, int minutes, int seconds)
    {
        fullObject.setNameOfTimer(name);
        fullObject.setSoundWhenStart(sound);
        fullObject.setMinutesOfTimer(minutes);
        fullObject.setSecondsOfTimer(seconds);
        adapter.notifyDataSetChanged();
        updateSharedPreference();
    }
}

