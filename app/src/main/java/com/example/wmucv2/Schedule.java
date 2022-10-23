package com.example.wmucv2;

import static com.example.wmucv2.RadioSelection.DIGITAL;
import static com.example.wmucv2.RadioSelection.FM;
import static com.example.wmucv2.RadioSelection.FRI;
import static com.example.wmucv2.RadioSelection.MON;
import static com.example.wmucv2.RadioSelection.SAT;
import static com.example.wmucv2.RadioSelection.SUN;
import static com.example.wmucv2.RadioSelection.THURS;
import static com.example.wmucv2.RadioSelection.TUES;
import static com.example.wmucv2.RadioSelection.WED;
import static com.example.wmucv2.RadioSelection.currChannel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class Schedule extends AppCompatActivity {

    RecyclerView shows;
    ArrayList<Show> showList;
    ImageView back;
    int currDay;


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
        Calendar c = Calendar.getInstance();
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        if (dayOfWeek == Calendar.SUNDAY) {
            setShowList(currChannel,0);
            currDayText.setText("Sunday");
        } else if (dayOfWeek == Calendar.MONDAY) {
            setShowList(currChannel,1);
            currDayText.setText("Monday");
        } else if (dayOfWeek == Calendar.TUESDAY) {
            setShowList(currChannel,2);
            currDayText.setText("Tuesday");
        } else if (dayOfWeek == Calendar.WEDNESDAY) {
            setShowList(currChannel,3);
            currDayText.setText("Wednesday");
        } else if (dayOfWeek == Calendar.THURSDAY) {
            setShowList(currChannel,4);
            currDayText.setText("Thursday");
        } else if (dayOfWeek == Calendar.FRIDAY) {
            setShowList(currChannel,5);
            currDayText.setText("Friday");
        } else {
            setShowList(currChannel,6);
            currDayText.setText("Saturday");
        }
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
        Show currShow;

        int begTime = 0;
        for (int i=0; i<24; i++) {
            if (currChannel==FM) {
                currShow = MainActivity.fmSched[i][day];
            } else {
                currShow = MainActivity.digSched[i][day];
            }
            if (currShow==null) {
                break;
            } else {
                showList.add(currShow);
            }
        }
        Collections.sort(showList);
    }

}