package com.example.customisable_training_app;

public class LineWithNameAndTimer
{
    private String nameOfTimer;
    private int minutes;
    private int seconds;

    private int soundWhenStart = 0;

    public String getNameOfTimer()
    {
        return nameOfTimer;
    }

    public void setNameOfTimer(String name)
    {
        nameOfTimer = name;
    }//setNameOfTimer

    public void setMinutesOfTimer(int minutesToSet)
    {
        minutes = minutesToSet;
    }//setMinutesOfTimer

    public int getMinutesOfTimer()
    {
        return minutes;
    }//getMinutesOfTimer

    public void setSecondsOfTimer(int secondsToSet)
    {
        seconds = secondsToSet;
    }//setSecondsOfTimer

    public int getSecondsOfTimer()
    {
        return seconds;
    }//getSecondsOfTimer

    public void setSoundWhenStart(int sound)
    {
        soundWhenStart = sound;
    }

    public int getSoundWhenStart()
    {
        return soundWhenStart;
    }
}
