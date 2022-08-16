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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.text.SimpleDateFormat;

import java.util.*;

import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    //Load the schedule data on bootup
    public static Show[][] digSched = new Show[24][7];
    public static Show[][] fmSched = new Show[24][7];
    String digUrl = "https://wmucmusic.com/api/schedule/DIG";
    String fmUrl = "https://wmucmusic.com/api/schedule/FM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initCrawler();
        startActivity(new Intent(this, RadioService.class));
    }



    private static void scheduleReceiver(Show[][] sched, String stationRequestURL) {

        try {
            URL url = new URL(stationRequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            //Getting the response code
            int responsecode = conn.getResponseCode();
            if (responsecode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responsecode);
            } else {
                String inline = "";
                Scanner scanner = new Scanner(url.openStream());
                //Write all the JSON data into a string using a scanner
                while (scanner.hasNext()) {
                    inline += scanner.nextLine();
                }
                //Close the scanner
                scanner.close();
                //Using the JSON simple library parse the string into a json object
                JSONParser parse = new JSONParser();
                JSONArray data = (JSONArray) parse.parse(inline);
                SimpleDateFormat dateFormatter = new SimpleDateFormat("h:mm a"); //Convert 0-23 to PM or AM
                for (int i = 0; i<data.size(); i++) {
                    JSONArray dayData = (JSONArray) parse.parse(data.get(i).toString());
                    for (int j = 0; j<dayData.size(); j++) {
                        JSONObject currShow = (JSONObject) dayData.get(j);

                        //Get start and end times as integers
                        Integer startTimeInt = ((Long) currShow.get("start_hour")).intValue();
                        Integer endTimeInt = startTimeInt + ((Long) currShow.get("duration")).intValue();

                        //Get hosts in a String
                        String showHosts = "";
                        JSONArray hosts = (JSONArray) currShow.get("dj");
                        for (int k = 0; k<hosts.size(); k++) {
                            showHosts += (String) hosts.get(k);
                            if (k<hosts.size()-1) {
                                showHosts += " & ";
                            }
                        }

                        if (showHosts==null) {
                            showHosts="";
                        }

                        //Get show name
                        String name = (String) currShow.get("name");
                        if (name==null) {
                            name="";
                        }


                        String startTime = dateFormatter.format(new Date(0,0,0,startTimeInt,0));
                        String endTime = dateFormatter.format(new Date(0,0,0,endTimeInt,0));
                        sched[j][i] = new Show(name,showHosts,
                                startTime,endTime,startTimeInt,endTimeInt);
                    }
                }
                conn.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void initCrawler() {
        Thread digThread = new Thread(new Runnable() {
            @Override
            public void run() {
                scheduleReceiver(digSched, digUrl);
            }
        });
        digThread.start();

        Thread fmThread = new Thread(new Runnable() {
            @Override
            public void run() {
                scheduleReceiver(fmSched, fmUrl);
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