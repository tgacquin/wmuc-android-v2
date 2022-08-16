package com.example.wmucv2;

public class Show implements Comparable<Show> {
    String showName;
    String showHost;
    String startTime;
    String endTime;
    int startTimeInt; //For sorting
    int endTimeInt;

    public Show(String showName, String showHost) {
        this.showName = showName;
        this.showHost = showHost;
        startTime=null;
        endTime=null;
    }

    public Show(String showName, String showHost, String startTime, String endTime, int startTimeInt, int endTimeInt) {
        this.showName = showName;
        this.showHost = showHost;
        this.startTime=startTime;
        this.endTime=endTime;
        this.startTimeInt=startTimeInt;
        this.endTimeInt = endTimeInt;
    }


    public void setShowHost(String showHost) {
        this.showHost = showHost;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String toString() {
        System.out.println(showName);
        System.out.println(startTime);
        System.out.println(endTime);
        return "Name:" + showName + " beg: " + startTime + " end: " + endTime;
    }

    @Override
    public int compareTo(Show o) {
        return this.startTimeInt-o.startTimeInt;
    }
}
