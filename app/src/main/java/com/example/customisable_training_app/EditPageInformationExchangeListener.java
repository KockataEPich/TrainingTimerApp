package com.example.customisable_training_app;

public interface EditPageInformationExchangeListener
{

    void textFromOptionsToEditPage(int givenNumberOfRounds, boolean ttsIsOn,
                                               boolean continueWhenLockedIsOn, boolean vibrateIsOn);

    void textFromTimerEditToEditPage(String name, int sound, int minutes, int seconds);


}