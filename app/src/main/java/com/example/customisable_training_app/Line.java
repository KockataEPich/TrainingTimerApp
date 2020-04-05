package com.example.customisable_training_app;


import java.util.ArrayList;
import java.util.Date;

public class Line
{
    private String nameOfWorkout;
    private ArrayList<LineWithNameAndTimer> theTimerListForTheWorkout;
    private int numberOfRounds = 1;
    private Date dateOfCreation;
    private boolean tts = false;
    private boolean continueWhenLocked = false;
    private boolean vibrate = false;
    private int numberInList;

    public void setNameOfWorkout(String name)
    {
        this.nameOfWorkout = name;
    }//setNameOfWorkout

    public String getNameOfWorkout()
    {
        return nameOfWorkout;
    }//getNameOfWorkout

    public void addTimerToTheWorkout(LineWithNameAndTimer currentTimer)
    {
        this.theTimerListForTheWorkout.add(currentTimer);
    }//addTimerToTheWorkout

    public ArrayList<LineWithNameAndTimer> getTimerList()
    {
        return this.theTimerListForTheWorkout;
    }//getTimerList

    public LineWithNameAndTimer getElementFromListOfTimers(int position)
    {
        return theTimerListForTheWorkout.get(position);
    }//LineWithNameAndTimer

    public void setList(ArrayList<LineWithNameAndTimer> currentList)
    {
        theTimerListForTheWorkout = currentList;
    }//setList

    public void setNumberOfRounds(int rounds)
    {
        numberOfRounds = rounds;
    }

    public int getNumberOfRounds()
    {
        return numberOfRounds;
    }

    public void setDateOfCreation(Date newDate)
    {
        dateOfCreation = newDate;
    }//SetDateOfCreation

    public Date getDateOfCreation()
    {
        return dateOfCreation;
    }

    public void setTTS(boolean givenTTS)
    {
        tts = givenTTS;
    }//setTTS

    public boolean getTTS()
    {
        return tts;
    }//getTTS

    public void setContinueWhenLocked(boolean givenContinueWhenLocked)
    {
        continueWhenLocked = givenContinueWhenLocked;
    }//setContinueWhenLocked

    public boolean getContinueWhenLocked()
    {
        return continueWhenLocked;
    }//getContinueWhenLocked

    public void setVibrate(boolean givenVibrate)
    {
        vibrate = givenVibrate;
    }//setVibrate

    public boolean getVibrate()
    {
        return vibrate;
    }//getVibrate

    public void setNumberInList(int givenNumberInList)
    {
        numberInList = givenNumberInList;
    }//setNumberInList

    public int getNumberInList()
    {
        return numberInList;
    }//getNumberInList


}//Line
