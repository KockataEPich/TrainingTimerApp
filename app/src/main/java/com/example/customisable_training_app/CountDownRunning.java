package com.example.customisable_training_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import android.os.Vibrator;
import com.daimajia.androidanimations.library.specials.in.LandingAnimator;

import java.util.ArrayList;
import java.util.Locale;

public class CountDownRunning extends AppCompatActivity
{

    private TextView nameOfTimer;
    private TextView countdownText;
    private TextView displayedRounds;
    private ConstraintLayout countdownLayout;
    private TextView pauseTextView;
    private TextView next_name;
    private LinearLayout  nextLayout;
    private Vibrator v;

    private long timeLeftMiliseconds;
    private boolean isRunning = true;


    private MediaPlayer tickForLast3Seconds;
    private MediaPlayer currentSoundWhenStart;

    int position = 0;
    int nextPosition = 1;
    int currentSound;
    int numberOfRounds;

    int currentRound = 1;

    boolean tts;
    boolean canUseTTS = false;

    boolean continueWhenLocked;
    boolean vibrate;

    private TextToSpeech mTTS;


    private ArrayList<LineWithNameAndTimer> allLines;

    private CountDownTimer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_down_running);

        tickForLast3Seconds = MediaPlayer.create(this, R.raw.tick_for_last_seconds);
        allLines = getAllLines();

        countdownLayout = findViewById(R.id.countdown_text);
        nameOfTimer = findViewById(R.id.timer_name);
        countdownText = findViewById(R.id.time_left_on_timer);
        pauseTextView = findViewById(R.id.textView9);
        next_name = findViewById(R.id.next_timer_name);
        nextLayout = findViewById(R.id.important_layout);

        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status)
            {
                if(status == TextToSpeech.SUCCESS)
                {
                    int result = mTTS.setLanguage(Locale.US);

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
                    {
                        Log.e("TTS", "Language not supported");
                    }
                    else
                    {
                        if(tts)
                           mTTS.speak(allLines.get(position).getNameOfTimer(), TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
                else
                {
                    Log.e("TTS", "Initialisation failed");
                }
            }
        });
        mTTS.setPitch(1);
        mTTS.setSpeechRate(1);
        mTTS.speak("keeek", TextToSpeech.QUEUE_FLUSH, null);





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


    private void speak(String theLineToSay)
    {
        mTTS.speak(theLineToSay, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    protected void onDestroy() {
        if(mTTS != null)
        {
            mTTS.stop();
            mTTS.shutdown();
        }
        super.onDestroy();
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

        if(!tts)
            currentSoundWhenStart.start();

        if(vibrate)
        {
            v.vibrate(600);
        }//if
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

        displayedRounds.setText("round   " + currentRound + "/" + numberOfRounds);

        nameOfTimer.setText(allLines.get(position).getNameOfTimer());

        if(nextPosition < allLines.size())
         next_name.setText(allLines.get(nextPosition).getNameOfTimer());
        else if (currentRound < numberOfRounds)
        {
            nextPosition = 0;
            next_name.setText(allLines.get(nextPosition).getNameOfTimer());
        }//if
        else
        {
            next_name.setText("End of workout");
        }//else

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
                    nextPosition++;
                    returnCurrentSound(allLines.get(position).getSoundWhenStart());
                    timeLeftMiliseconds = (allLines.get(position).getMinutesOfTimer() * 60 + allLines.get(position).getSecondsOfTimer()) * 1000;

                    if(tts)
                        speak(allLines.get(position).getNameOfTimer());
                    else
                        currentSoundWhenStart.start();

                    YoYo.with(Techniques.Landing)
                            .duration(200)
                            .playOn(nameOfTimer);

                    YoYo.with(Techniques.Landing)
                            .duration(200)
                            .playOn(next_name);

                    if(vibrate)
                    {
                        v.vibrate(600);
                    }//if


                    timer.cancel();
                    startTimer();
                }
                else if(currentRound < numberOfRounds)
                    {
                        currentRound++;

                        YoYo.with(Techniques.Landing)
                                .duration(200)
                                .playOn(nameOfTimer);

                        YoYo.with(Techniques.Landing)
                                .duration(200)
                                .playOn(next_name);

                        if(vibrate)
                        {
                            v.vibrate(600);
                        }//if

                        position = 0;
                        nextPosition = 1;
                        timeLeftMiliseconds = (allLines.get(position).getMinutesOfTimer() * 60 + allLines.get(position).getSecondsOfTimer()) * 1000;
                         returnCurrentSound(allLines.get(position).getSoundWhenStart());

                        if(tts)
                            speak(allLines.get(position).getNameOfTimer());
                        else
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

        tts = intent.getBooleanExtra("TTS", false);
        continueWhenLocked = intent.getBooleanExtra("continue", false);
        vibrate = intent.getBooleanExtra("vibrate", false);


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
        if(isRunning && !continueWhenLocked)
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
