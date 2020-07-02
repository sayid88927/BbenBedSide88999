package com.hosmart.ebaby.bean;

public class AlarmSettingBean {


    private int id;
    private  int hours;
    private  int minutes;
    private int[] colors;
    private  int music;
    private int type;                    //1代表单次闹,2代表重复闹
    private StringBuilder week;
    private boolean  TurnOn;
    private boolean TurnOff;
    private boolean alarmOnly;


    public boolean isAlarmOnly() {
        return alarmOnly;
    }

    public void setAlarmOnly(boolean alarmOnly) {
        this.alarmOnly = alarmOnly;
    }

    public boolean isTurnOn() {
        return TurnOn;
    }

    public void setTurnOn(boolean turnOn) {
        TurnOn = turnOn;
    }

    public boolean isTurnOff() {
        return TurnOff;
    }

    public void setTurnOff(boolean turnOff) {
        TurnOff = turnOff;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int[] getColors() {
        return colors;
    }

    public void setColors(int[] colors) {
        this.colors = colors;
    }

    public int getMusic() {
        return music;
    }

    public void setMusic(int music) {
        this.music = music;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public StringBuilder getWeek() {
        return week;
    }

    public void setWeek(StringBuilder week) {
        this.week = week;
    }
}