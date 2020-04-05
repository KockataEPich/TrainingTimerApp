package com.example.customisable_training_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;

public class CountDownRunning extends AppCompatActivity
{

    private TextView nameOfTimer;
    private TextView countdownText;
    private TextView displayedRounds;
    private RelativeLayout countdownLayout;
    private TextView pauseTextView;

    private long timeLeftMiliseconds;
    private boolean isRunning = true;


    private MediaPlayer tickForLast3Seconds;
    private MediaPlayer currentSoundWhenStart;

    int position = 0;
    int currentSound;
    int numberOfRounds;


    private ArrayList<LineWithNameAndTimer> allLines;

    private CountDownTimer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_down_running);

        tickForLast3Seconds = MediaPlayer.create(this, R.raw.tick_for_last_seconds);
        allLines = getAllLines();

        countdownLayout = (RelativeLayout) findViewById(R.id.countdown_text);
        nameOfTimer = findViewById(R.id.timer_name);
        countdownText = findViewById(R.id.time_left_on_timer);
        pauseTextView = findViewById(R.id.textView9);

        timeLeftMiliseconds = (allLines.get(position).getMinutesOfTimer() * 60 + allLines.get(position).getSecondsOfTimer()) * 1000;

        displayedRounds = findViewById(R.id.rounds);
        actualStart();

        countdownLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                startStop();
            }

        });

        onUserInteraction();
    }



    public void startStop()
    {
        if(isRunning)
        {
            stopTimer();
        }
        else
        {
            startTimer();
        }
    }


    public void actualStart()
    {
        returnCurrentSound(allLines.get(position).getSoundWhenStart());
        currentSoundWhenStart.start();
        startTimer();
    }

    public void returnCurrentSound(int sound)
    {
        if(sound == 0)
            currentSoundWhenStart = MediaPlayer.create(this, R.raw.boxing_buzzer_sound);
        if(sound == 1)
            currentSoundWhenStart = MediaPlayer.create(this, R.raw.boxbell);
        if(sound == 2)
            currentSoundWhenStart = MediaPlayer.create(this, R.raw.air_horn_sound);
        if(sound == 3)
            currentSoundWhenStart = MediaPlayer.create(this, R.raw.drums);
        if(sound == 4)
            currentSoundWhenStart = MediaPlayer.create(this, R.raw.epic);
        if(sound == 5)
            currentSoundWhenStart = MediaPlayer.create(this, R.raw.fanfare);
        if(sound == 6)
            currentSoundWhenStart = MediaPlayer.create(this, R.raw.gong1);
        if(sound == 7)
            currentSoundWhenStart = MediaPlayer.create(this, R.raw.gong2);
        if(sound == 8)
            currentSoundWhenStart = MediaPlayer.create(this, R.raw.gong3);
        if(sound == 9)
            currentSoundWhenStart = MediaPlayer.create(this, R.raw.gong4);
        if(sound == 10)
            currentSoundWhenStart = MediaPlayer.create(this, R.raw.lego_yoda_death_sound);


    }

    public void startTimer()
    {

        displayedRounds.setText("" + (numberOfRounds - 1));
        nameOfTimer.setText(allLines.get(position).getNameOfTimer());
        timer = new CountDownTimer(timeLeftMiliseconds, 1000)
        {
            @Override
            public void onTick(long i)
            {
                timeLeftMiliseconds = i;
                if(timeLeftMiliseconds <= 3000)
                    tickForLast3Seconds.start();
                updateTimer();

            }

            @Override
            public void onFinish()
            {
                if(position < allLines.size() - 1)
                {
                    position++;
                    returnCurrentSound(allLines.get(position).getSoundWhenStart());
                    timeLeftMiliseconds = (allLines.get(position).getMinutesOfTimer() * 60 + allLines.get(position).getSecondsOfTimer()) * 1000;
                    currentSoundWhenStart.start();
                    timer.cancel();
                    startTimer();
                }
                else if(numberOfRounds > 1)
                    {
                        numberOfRounds--;
                        position = 0;
                        timeLeftMiliseconds = (allLines.get(position).getMinutesOfTimer() * 60 + allLines.get(position).getSecondsOfTimer()) * 1000;
                         returnCurrentSound(allLines.get(position).getSoundWhenStart());
                        currentSoundWhenStart.start();
                        startTimer();
                    }
                else
                 finish();

            }
        }.start();

        pauseTextView.setText("TAP ANYWHERE TO PAUSE");
        isRunning = true;
    }

    public void stopTimer()
    {
        pauseTextView.setText("TAP ANYWHERE TO RESUME");
        timer.cancel();
        isRunning = false;
    }//stopTimer

     public void updateTimer()
    {
        int minute = (int) timeLeftMiliseconds / 60000;
        int seconds = (int) timeLeftMiliseconds % 60000 / 1000;

        String timeLeftText;

        timeLeftText = "" + minute;
        timeLeftText +=":";
        if (seconds < 10)
            timeLeftText += "0";
        timeLeftText += seconds;

        countdownText.setText(timeLeftText);
    }

    public ArrayList<LineWithNameAndTimer> getAllLines()
    {
        ArrayList<LineWithNameAndTimer> getAllLines = new ArrayList<LineWithNameAndTimer>();
        Intent intent = getIntent();
        numberOfRounds = intent.getIntExtra("numberOfRounds", 1);
        for(int i = 0; i < intent.getIntExtra("sizeOfList", 0); i++)
        {
            LineWithNameAndTimer lineToAdd = new LineWithNameAndTimer();
            lineToAdd.setSoundWhenStart(intent.getIntExtra(("startSound" + i), 0));
            lineToAdd.setNameOfTimer(intent.getStringExtra(("nameOfTimer3" + i)));
            lineToAdd.setMinutesOfTimer(intent.getIntExtra(("minutesOfTimer3" + i), 0));
            lineToAdd.setSecondsOfTimer(intent.getIntExtra(("secondsOfTimer3" + i), 0));

            getAllLines.add(lineToAdd);
        }

        return getAllLines;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isRunning)
        {
            stopTimer();
        }
    }


    @Override
    public void onBackPressed() {
        stopTimer();
        finish();
    }
}
