package com.example.halflife;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private int seconds;
    private boolean running;
    private boolean wasRunning;
    int periods;
    double remaining;

    double halflife = 0;
    double mass = 0;

    boolean restared = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            seconds = savedInstanceState.getInt("seconds");
            running = savedInstanceState.getBoolean("running");
            wasRunning = savedInstanceState.getBoolean("wasRunning");
            halflife = savedInstanceState.getDouble("halflife");
            mass = savedInstanceState.getDouble("mass");


        }
        runTimer();
    }

    private void runTimer() {

        final TextView timeView = (TextView) findViewById(R.id.tvTime);

        final Handler handler = new Handler(Looper.getMainLooper());

        handler.post(new Runnable() {
                         @Override
                         public void run() {

                             int hours = seconds / 3600;
                             int minutes = (seconds % 3600) / 60;
                             int secs = seconds % 60;


                             String time = String.format(Locale.getDefault(),
                                     "%d:%02d:%02d", hours, minutes, secs);

                             if (mass == 0 || halflife == 0) {

                                timeView.setText(R.string.zero_error);
                                restared=true;

                             }else {
                                 periods = (int) (seconds / halflife);
                                 remaining = mass * Math.pow(0.5, periods);


                                 timeView.setText("t: " + time +
                                         "\n"+ "m: " + remaining+" g" +
                                         "\n"+ "p: " + periods);

                                 Log.d("licznik", String.valueOf(seconds));

                                 if (running) seconds++;
                             }
                             handler.postDelayed(this, 1000);


                         }
                     }


        );

    }

    public void onClickStart(View view) {

        if (restared) {

            EditText poleTime = (EditText) findViewById(R.id.inTime);
            EditText poleMass = (EditText) findViewById(R.id.inMass);

            if(poleTime.getText().toString().equals("")){
                halflife=0;
            }else {
                halflife = Double.parseDouble(poleTime.getText().toString());
            }

            if(poleMass.getText().toString().equals("")){
               mass=0;
            }else {
                mass = Double.parseDouble(poleTime.getText().toString());
            }

            restared = false;
        }


        running = true;
    }

    public void onClickStop(View view) {
        running = false;
    }

    public void onClickReset(View view) {
        restared = true;

        running = false;

        seconds = 0;
        mass = 0;
        halflife = 0;
        periods = 0;
        remaining = 0;

        EditText poleTime = (EditText) findViewById(R.id.inTime);
        EditText poleMass = (EditText) findViewById(R.id.inMass);

        poleTime.setText("");
        poleMass.setText("");
    }

    @Override
    protected void onStart() {
        super.onStart();
        wasRunning = running;
//        running=false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (wasRunning) running = true;
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("seconds", seconds);
        savedInstanceState.putBoolean("running", running);
        savedInstanceState.putBoolean("wasrunning", wasRunning);
        savedInstanceState.putDouble("halflife", halflife);
        savedInstanceState.putDouble("mass", mass);
    }
}