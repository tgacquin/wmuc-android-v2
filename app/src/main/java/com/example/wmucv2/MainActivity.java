package com.example.wmucv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    //Load the schedule data on bootup
    public static Show[][] digSched = new Show[24][7];
    public static Show[][] fmSched = new Show[24][7];
    String digUrl = "http://wmuc.umd.edu/station/schedule/0/2";
    String fmUrl = "http://wmuc.umd.edu/station/schedule";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initCrawler();
        startActivity(new Intent(this, RadioService.class));
    }



    private static void tableToArray(Show[][] sched, String url) {
        Show none = new Show("Off The Air", "");
        int currRow=-1;
        //Indexes for up until what point of each day we've copied data
        ArrayList<Integer> currLoc= new ArrayList<Integer>();
        currLoc.add(0);currLoc.add(0);currLoc.add(0);
        currLoc.add(0);currLoc.add(0);currLoc.add(0);
        currLoc.add(0);
        Document docFM = null;
        try {
            docFM = Jsoup.connect(url).timeout(5000).get();
        } catch (IOException e) {
            System.out.println("Failed to connect to webpage.");
            return;
        }


        for (Element e : docFM.getElementById("main").getElementsByTag("tbody").select("tr")) {
            if (currRow%2==0) {
                for (Element x : e.select("td")) {
                    //Initiate 2d array of first row
                    if (!x.text().contains(":00") && !x.text().contains(":30")) {
                        int rowSpan = Integer.parseInt(x.attr("rowspan"));
                        int updateIndex=currLoc.indexOf(currRow);
                        String showName = x.text();
                        Show show = null;

                        if (showName.equals("Off The Air")) {
                            show = none;
                        } else {
                            String[] curr = showName.split("\\*\\*\\*");
                            show = new Show(curr[0].trim(), curr[1].trim());
                        }
                        currLoc.set(updateIndex, currRow+rowSpan);
                        for (int i=currRow; i<currRow+rowSpan; i++) {
                            sched[i/2][updateIndex]=show;
                        }
                    }
                }
            }
            currRow++;
        }
    }

    private void initCrawler() {
        Thread digThread = new Thread(new Runnable() {
            @Override
            public void run() {
                tableToArray(digSched, digUrl);
            }
        });
        digThread.start();

        Thread fmThread = new Thread(new Runnable() {
            @Override
            public void run() {
                tableToArray(fmSched, fmUrl);
            }
        });
        fmThread.start();

        try {
            digThread.join();
            fmThread.join();
        } catch (java.lang.InterruptedException e) {
            System.out.println("Uh oh.");
        }
    }
}