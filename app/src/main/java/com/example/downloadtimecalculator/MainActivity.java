package com.example.downloadtimecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    LinearLayout layout;
    ArrayList<View> listOfViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        layout =  findViewById(R.id.main_activity_layout);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar1);

        final int timeNeeded = 10000;

        CountDownTimer cd1 = new CountDownTimer(timeNeeded, 50) {
            public void onTick(long millisUntilFinished) {

                long finishedSeconds = timeNeeded - millisUntilFinished;
                int total = (int) (((float)finishedSeconds / (float)timeNeeded) * 100.0);
                progressBar.setProgress(total);

            }

            public void onFinish() {
                progressBar.setProgress(100);
                ProgressBar pb = listOfViews.get(0).findViewById(R.id.progressBar);
                pb.setProgress(0);
                pb = listOfViews.get(1).findViewById(R.id.progressBar);
                pb.setProgress(0);
            }
        };

        cd1.start();

    }



    public void addNewDownloader(){
        View view = getLayoutInflater().inflate(R.layout.download_simulator, null);
        layout.addView(view);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setProgress(100);
        listOfViews.add(view);
    }

    public void onBtnAddClick(View view) {
        addNewDownloader();
    }
}