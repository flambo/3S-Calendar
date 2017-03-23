package kr.ac.hansung.a3scalendar;

import java.util.ArrayList;

/**
 * Created by Owner on 2017-01-31.
 */

public class CalData {



    int dayofweek;//요일
    //int images;//날씨 정보 이미지
    private int year;
    private int month;
    int day;//날짜

    private  boolean isFreezen;//날짜 동결 여부 체크 변수

    private int hour;
    private int min;
    private String txt;
    private ArrayList<String> scheduleArrList;

    public CalData(){ init(); }
    public CalData(int Y, int M, int D, int h){
        //MonthCalendar  용

        year = Y;
        month = M;
        day = D;

        dayofweek = h;
        init();

    }

    public CalData(int Y, int M, int D) {

        //WeekCalendar 용
        year = Y;
        month = M;
        day = D;
        init();

    }
   /*
    public CalData(int d, int h) {
        //MonthCalendar  용
        day = d;
        dayofweek = h;
        init();
    }

    public CalData(int d){
        //WeekCalendar 용
        day = d;
        init();
    }
*/

    public void init(){
        hour = 0;
        min = 0;
        txt = "";
        isFreezen=false;
    }

    public boolean isFreezen() {
        return isFreezen;
    }

    public void setFreezen(boolean freezen) {
        isFreezen = freezen;
    }

    public ArrayList<String> getScheduleArrList() {
        return scheduleArrList;
    }

    public void setScheduleArrList(ArrayList<String> scheduleArrList) {
        this.scheduleArrList = scheduleArrList;
    }

    public void setYear(int Y){
        year = Y;
    }

    public void setMonth(int M){
        month = M;
    }

    public void setDay(int D){
        day = D;
    }

    public int getYear(){
        return year;
    }

    public int getMonth(){
        return month;
    }

    public int getDay(){
        return day;
    }

    public void setString(String T){
        txt = T;
    }

    public void setTime(int H, int M){
        hour = H;
        min = M;
    }

    public String getString(){
        return txt;
    }

    public String getTime(){
        return ""+hour+" : "+min;

    }

    public String getSchedule(){
        return ""+hour+" : " + min + "  " +txt;
    }





    public int getDayofweek() {
        return dayofweek;
    }




/*
    public int getImages() {
        return images;
    }

    public void setImages(int images) {
        this.images = images;
    }*/


}
