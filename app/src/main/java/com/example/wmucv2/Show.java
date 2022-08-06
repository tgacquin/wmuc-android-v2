package com.example.wmucv2;

public class Show {
    String showName;
    String showHost;
    String startTime;
    String endTime;

    public Show(String showName, String showHost) {
        this.showName = showName;
        this.showHost = showHost;
        startTime=null;
        endTime=null;
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
}
