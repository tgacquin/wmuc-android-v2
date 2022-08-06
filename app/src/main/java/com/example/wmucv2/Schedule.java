package com.example.wmucv2;

import static com.example.wmucv2.RadioService.DIGITAL;
import static com.example.wmucv2.RadioService.FM;
import static com.example.wmucv2.RadioService.FRI;
import static com.example.wmucv2.RadioService.MON;
import static com.example.wmucv2.RadioService.SAT;
import static com.example.wmucv2.RadioService.SUN;
import static com.example.wmucv2.RadioService.THURS;
import static com.example.wmucv2.RadioService.TUES;
import static com.example.wmucv2.RadioService.WED;
import static com.example.wmucv2.RadioService.currChannel;
import static com.example.wmucv2.RadioService.currDay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wmucv2.databinding.ActivityMainBinding;
import com.example.wmucv2.databinding.ActivityScheduleBinding;

import java.util.ArrayList;

public class Schedule extends AppCompatActivity {

    RecyclerView shows;
    ArrayList<Show> showList;
    ImageView back;


    private int channel;
    private TextView mon, tues, wed, thurs, fri, sat, sun,fm,dig,currDayText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        back = findViewById(R.id.backbtn);
        sun = findViewById(R.id.Sun);
        mon = findViewById(R.id.Mon);
        tues = findViewById(R.id.Tues);
        wed = findViewById(R.id.Wed);
        thurs = findViewById(R.id.Thurs);
        fri = findViewById(R.id.Fri);
        sat = findViewById(R.id.Sat);
        fm = findViewById(R.id.fm_channel);
        dig = findViewById(R.id.dig_channel);
        shows = findViewById(R.id.show_recycler);
        showList = new ArrayList<>();
        currDayText = findViewById(R.id.currDay);
        setShowList(0,0);
        setAdapter();
        //Set buttons for changing day
        sun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setShowList(currChannel, SUN);
                currDayText.setText("Sunday");
                setAdapter();
            }
        });

        mon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currDay=MON;
                setShowList(currChannel, currDay);
                currDayText.setText("Monday");
                setAdapter();
            }
        });

        tues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currDay=TUES;
                setShowList(currChannel, currDay);
                currDayText.setText("Tuesday");
                setAdapter();
            }
        });

        wed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currDay=WED;
                setShowList(currChannel, currDay);
                currDayText.setText("Wednesday");
                setAdapter();
            }
        });

        thurs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currDay=THURS;
                setShowList(currChannel, currDay);
                currDayText.setText("Thursday");
                setAdapter();
            }
        });

        fri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currDay=FRI;
                setShowList(currChannel, currDay);
                currDayText.setText("Friday");
                setAdapter();
            }
        });

        sat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currDay=SAT;
                setShowList(currChannel, currDay);
                currDayText.setText("Saturday");
                setAdapter();
            }
        });

        fm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currChannel=FM;
                setShowList(currChannel, currDay);
                setAdapter();
            }
        });

        dig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currChannel=DIGITAL;
                setShowList(currChannel, currDay);
                setAdapter();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

    private void setAdapter() {
        ScheduleAdapter adapter = new ScheduleAdapter(showList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        shows.setLayoutManager(layoutManager);
        shows.setItemAnimator(new DefaultItemAnimator());
        shows.setAdapter(adapter);
    }

    private void setShowList(int channel, int day) {
        showList.clear();
        Show showToAdd = null;
        Show currShow=null;

        int begTime = 0;
        for (int i=0; i<24; i++) {
            if (currChannel==FM) {
                currShow = MainActivity.fmSched[i][day];
            } else {
                currShow = MainActivity.digSched[i][day];
            }
            if (showToAdd==null) {
                showToAdd = currShow;
            } else if (!currShow.showName.equals(showToAdd.showName)){
                showToAdd.startTime=(begTime<10) ?  begTime + "0:00" :  begTime + ":00";
                showToAdd.endTime=(i<10) ?  i + "0:00" :  i + ":00";
                showList.add(showToAdd);
                begTime=i;
                showToAdd=currShow;
            }
        }
        return;
    }

}