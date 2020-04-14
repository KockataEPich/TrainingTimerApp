package com.example.customisable_training_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;


import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.gass.internal.Program;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

public class MainActivity extends Activity
{
    private ArrayList<Line> allLines;
    private ListView programList;
    private Object o;
    private Line fullObject;
    private SharedPreferences saveFileForWorkouts;
    private SharedPreferences.Editor myEdit;
    private CustomAdapter adapter;
    int positionInList;
    private InterstitialAd mInterstitialAd;
    private InterstitialAd wholeScreenAd2;
    private AdView mAdView;

    Animation fabOpen, fabClose;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //Initialise Mobile Ads
        MobileAds.initialize(this, "ca-app-pub-3935469427404511~1371124907");

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3935469427404511/4182827364");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        wholeScreenAd2 = new InterstitialAd(this);
        wholeScreenAd2.setAdUnitId("ca-app-pub-3935469427404511/7930500684");
        wholeScreenAd2.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener()
        {
            @Override
            public void onAdClosed()
            {
                super.onAdClosed();
                finish();
            }//onAdCloesd
        });

        // Get the ad layout
        LinearLayout adLayout = findViewById(R.id.ad_layout);

        // Place the banner
        mAdView = new AdView(this);
        mAdView.setAdSize(AdSize.BANNER);

        // Create the layout with the appropriate parameters
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        mAdView.setLayoutParams(params);
        adLayout.addView(mAdView);

        mAdView.setAdUnitId("ca-app-pub-3935469427404511/6673259334");
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);






        //Get the saved lines from the stored file
        allLines = getAllLines();

        programList = (ListView) findViewById(R.id.workout_names);

        // Set the header of the ListView
        TextView header = new TextView(this);
        header.setText("My programs");
        header.setTextSize(30);
        header.setPadding(0,0,0,5);
        header.setTextColor(Color.parseColor("#FFFFFF"));
        header.setTypeface(Typeface.DEFAULT_BOLD);
        header.setGravity(Gravity.CENTER);
        programList.addHeaderView(header);

        // Create the adapter for the lines
        adapter = new CustomAdapter(this, allLines);
        programList.setAdapter(adapter);

        // The saved file
        saveFileForWorkouts = getSharedPreferences("saveFileForOfWorkouts", MODE_PRIVATE);

        // The edit for the save file
        myEdit = saveFileForWorkouts.edit();

        // Tapping on one of the programes
        programList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id)
            {

                // Get the position and from there get which of the programs was selected
                positionInList = position;
                o = programList.getItemAtPosition(position);
                fullObject = (Line) o;


                shakeItem(v);

                // Put all of the needed information into the intent and start a new activity
                Intent intent = new Intent(MainActivity.this, EditPage.class);
                intent.putExtra("numberOfTimers", fullObject.getTimerList().size());
                intent.putExtra("nameOfWholeProgram", fullObject.getNameOfWorkout());
                intent.putExtra("numberOfRounds",fullObject.getNumberOfRounds());
                intent.putExtra("savedIndex", fullObject.getNumberInList());

                intent.putExtra("isTTSOn", fullObject.getTTS());
                intent.putExtra("IsContinueOn", fullObject.getContinueWhenLocked());
                intent.putExtra("isVibrateOn", fullObject.getVibrate());

                for(int forEveryElement = 0; forEveryElement < fullObject.getTimerList().size(); forEveryElement++)
                {
                        intent.putExtra("sound" + forEveryElement, fullObject.getElementFromListOfTimers(forEveryElement).getSoundWhenStart());
                        intent.putExtra("nameOfTimer" + forEveryElement, fullObject.getElementFromListOfTimers(forEveryElement).getNameOfTimer());
                        intent.putExtra("minutesOfTimer" + forEveryElement, fullObject.getElementFromListOfTimers(forEveryElement).getMinutesOfTimer());
                        intent.putExtra("secondsOfTimer" + forEveryElement, fullObject.getElementFromListOfTimers(forEveryElement).getSecondsOfTimer());
                }//for

                startActivityForResult(intent, 2);
            }
        });


        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close);


        programList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                positionInList = position;
                o = programList.getItemAtPosition(position);
                fullObject = (Line) o;
                final View lineInView = view;


                shakeItem(view);


                AlertDialog.Builder alert =  new AlertDialog.Builder(MainActivity.this);

                alert.setTitle("Delete entry");
                alert.setMessage("Are you sure you want to delete " + "\"" + fullObject.getNameOfWorkout() + "\"" +  " program?");
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        MainActivity.this.removeItem(lineInView, fullObject);
                    }//onClick
                });

                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.cancel();
                    }//onClick
                });
                alert.show();


                return true;
            }//onItemLongClick
        });


    }//OnCreate



    // When closing the app display and add
    public void showMyAdd()
    {
        if(mInterstitialAd.isLoaded())
            mInterstitialAd.show();
        else
            finish();
    }// ShowMyAdd



    // Show an add when the back is pressed
    public void onBackPressed()
    {
        showMyAdd();
    }//OnBackPressed


    public void shakeItem(View view)
    {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.react_on_click);
        view.startAnimation(shake);

    }//shakeItem


    // Method for adding a new program to the list
    // It is invoked when the "+" is pressed on the main activity
    public void addNewProgram(View view)
    {
        YoYo.with(Techniques.Tada)
                .duration(600)
                .playOn(view);

        Line programToAdd = new Line();
        programToAdd.setNameOfWorkout("Workout " + (allLines.size() + 1));
        programToAdd.setDateOfCreation(Calendar.getInstance().getTime());
        programToAdd.setList(new ArrayList<LineWithNameAndTimer>());
        programToAdd.setNumberOfRounds(1);
        this.addItem(programToAdd);

        Intent intent = new Intent(MainActivity.this, EditPage.class);
        intent.putExtra("numberOfTimers", allLines.get(allLines.size() - 1).getTimerList().size());
        intent.putExtra("nameOfWholeProgram", allLines.get(allLines.size() - 1).getNameOfWorkout());
        intent.putExtra("numberOfRounds",allLines.get(allLines.size() - 1).getNumberOfRounds());
        for(int forEveryElement = 0; forEveryElement < allLines.get(allLines.size() - 1).getTimerList().size(); forEveryElement++)
        {
            intent.putExtra("sound" + forEveryElement, allLines.get(allLines.size() - 1).getElementFromListOfTimers(forEveryElement).getSoundWhenStart());
            intent.putExtra("nameOfTimer" + forEveryElement, allLines.get(allLines.size() - 1).getElementFromListOfTimers(forEveryElement).getNameOfTimer());
            intent.putExtra("minutesOfTimer" + forEveryElement, allLines.get(allLines.size() - 1).getElementFromListOfTimers(forEveryElement).getMinutesOfTimer());
            intent.putExtra("secondsOfTimer" + forEveryElement, allLines.get(allLines.size() - 1).getElementFromListOfTimers(forEveryElement).getSecondsOfTimer());
        }//for

        fullObject = programToAdd;
        startActivityForResult(intent, 2);
    }//addNewProgram



    // Method for getting all the initial lines to be displayed
    private ArrayList<Line> getAllLines()
    {
        ArrayList<Line> getInitialLines = new ArrayList<Line>();
        saveFileForWorkouts = getSharedPreferences("saveFileForOfWorkouts", MODE_PRIVATE);

        // Use the file to get the workouts
        for (int i = 0; i < saveFileForWorkouts.getInt("numberOfWorkouts", 0); i++)
        {
            Line workoutToAdd = new Line();
            ArrayList<LineWithNameAndTimer> list = new ArrayList<LineWithNameAndTimer>();
            workoutToAdd.setNameOfWorkout(saveFileForWorkouts.getString(("nameOfWorkout" + i), ""));
            workoutToAdd.setNumberOfRounds(saveFileForWorkouts.getInt(("numberOfRounds" + i), 1));
            workoutToAdd.setNumberInList(saveFileForWorkouts.getInt(("savedIndex" + i), 0));

            workoutToAdd.setTTS(saveFileForWorkouts.getBoolean(("isTTSOn" + i), false));
            workoutToAdd.setContinueWhenLocked(saveFileForWorkouts.getBoolean(("isContinueOn" + i), false));
            workoutToAdd.setVibrate(saveFileForWorkouts.getBoolean(("isVibrateOn" + i), false));

            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            try
            {
                workoutToAdd.setDateOfCreation(df.parse(saveFileForWorkouts.getString(("dateOfCreationOfProgram" + i), "")));
            }//try
            catch (ParseException ex)
            {
                Log.v("Exception", ex.getLocalizedMessage());
            }//catch

            // For each workout get the list of timers in it and their specifics (Name,Sound,Time)
            for(int j = 0; j < saveFileForWorkouts.getInt(("howManyTimers" + i), 0); j++)
            {
                LineWithNameAndTimer timerToAdd = new LineWithNameAndTimer();

                timerToAdd.setNameOfTimer(saveFileForWorkouts.getString(("currentTimerName" + i + "" + j), ""));
                timerToAdd.setSoundWhenStart(saveFileForWorkouts.getInt(("currentSoundWhenStart" + i + "" + j), 0));
                timerToAdd.setMinutesOfTimer(saveFileForWorkouts.getInt(("currentTimerMinutes" + i + "" + j), 0));
                timerToAdd.setSecondsOfTimer(saveFileForWorkouts.getInt(("currentTimerSeconds" + i + "" + j), 0));
                list.add(timerToAdd);
            }//for

            // Add the workout to the list when done
            workoutToAdd.setList(list);
            getInitialLines.add(workoutToAdd);
        }//for

        return getInitialLines;
    }//getAllLines



    // Method for adding an item to the list
    public void addItem(Line lineToAdd)
    {
        allLines.add(lineToAdd);
        adapter.notifyDataSetChanged();
        this.updateSharePreference();
    }//addItem


    // Method that removes an item from the list and updates files
    public void removeItem(View theView, final Line lineToRemove)
    {
        final Animation animation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_out_right);
        theView.startAnimation(animation);
        Handler handle = new Handler();
        handle.postDelayed(new Runnable()
        {
            @Override
            public void run() {
                allLines.remove(lineToRemove);
                adapter.notifyDataSetChanged();
                MainActivity.this.updateSharePreference();
                animation.cancel();
            }
        },400);

    }//removeItem



    // Method for getting the results of activities
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // Second "if" - for handling the return from the edit page
        // Checks if save or delete is desired and does accordingly
        if(requestCode == 2)
        {

            if(resultCode == RESULT_OK)
            {
                    // If we want to save we set the new workout the desired options
                    String result = data.getStringExtra("passedNameOfWorkout");
                    fullObject.setNameOfWorkout(result);

                    fullObject.setNumberOfRounds(data.getIntExtra("numberOfRounds", 1));

                    fullObject.setTTS(data.getBooleanExtra("isTTSOn", false));
                    fullObject.setContinueWhenLocked(data.getBooleanExtra("isContinueOn", false));
                    fullObject.setVibrate(data.getBooleanExtra("isVibrateOn", false));

                    ArrayList<LineWithNameAndTimer> list = new ArrayList<LineWithNameAndTimer>();

                    for (int i = 0; i < data.getIntExtra("sizeOfList", 0); i++)
                    {
                        LineWithNameAndTimer lineToAdd = new LineWithNameAndTimer();
                        lineToAdd.setNameOfTimer(data.getStringExtra(("nameOfTimer2" + i)));
                        lineToAdd.setSoundWhenStart(data.getIntExtra(("startSound2" + i), 0));
                        lineToAdd.setMinutesOfTimer(data.getIntExtra(("minutesOfTimer2" + i), 0));
                        lineToAdd.setSecondsOfTimer(data.getIntExtra(("secondsOfTimer2" + i), 0));
                        list.add(lineToAdd);
                    }//for

                    fullObject.setList(list);


                    adapter.notifyDataSetChanged();
                    this.updateSharePreference();
            }//if

        }//if
    }//OnActivityResult


    //TODO Make the optioons and timer setter more aesthethic
    //TODO Increase the size of the timmer line to include the sound it gives
    //TODO Make the adding program button fill out the app instead of waiting for it to get out




    // Method for updating the file which contains the the information about the workouts
    // and its timers.

    public void updateSharePreference()
    {
        myEdit.putInt(("numberOfWorkouts"), allLines.size());
        for(int i = 0; i < allLines.size(); i++)
        {
            myEdit.putString(("nameOfWorkout" + i), allLines.get(i).getNameOfWorkout());
            myEdit.putInt(("numberOfRounds" + i), allLines.get(i).getNumberOfRounds());
            myEdit.putInt(("howManyTimers" + i), allLines.get(i).getTimerList().size());
            myEdit.putInt(("savedIndex" + i), i);

            myEdit.putBoolean(("isTTSOn" + i), allLines.get(i).getTTS());
            myEdit.putBoolean(("isContinueOn" + i), allLines.get(i).getContinueWhenLocked());
            myEdit.putBoolean(("isVibrateOn" + i), allLines.get(i).getVibrate());

            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = df.format(allLines.get(i).getDateOfCreation());
            myEdit.putString(("dateOfCreationOfProgram" + i), formattedDate);


            for(int j = 0; j < allLines.get(i).getTimerList().size(); j++)
            {
                myEdit.putString(("currentTimerName" + i + "" + j), allLines.get(i).getTimerList().get(j).getNameOfTimer());
                myEdit.putInt(("currentSoundWhenStart" + i + "" + j), allLines.get(i).getTimerList().get(j).getSoundWhenStart());
                myEdit.putInt(("currentTimerMinutes" + i + "" + j), allLines.get(i).getTimerList().get(j).getMinutesOfTimer());
                myEdit.putInt(("currentTimerSeconds" + i + "" + j), allLines.get(i).getTimerList().get(j).getSecondsOfTimer());
            }//for
        }//for
        myEdit.commit();
    }//updateSharedPreference
}//class MainActivity