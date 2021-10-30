package com.example.downloadtimecalculator;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DownloadSimulator{
    View view;
    Context ctx;
    ProgressBar progressBar;
    EditText editTextBandwidth;
    TextView textSpeed;
    TextView textTime;
    TextView textDownloaded;
    TextView textPercentage;

    public DownloadSimulator(View view,Activity mainActivity){
        this.view = view;
        this.progressBar = view.findViewById(R.id.progressBar);
        this.editTextBandwidth = view.findViewById(R.id.editTextBandwidth);
        this.textSpeed = view.findViewById(R.id.textSpeed);
        this.textTime = view.findViewById(R.id.textTime);
        this.textDownloaded = view.findViewById(R.id.textDownloaded);
        this.textPercentage = view.findViewById(R.id.textPercentage);
        this.reset();
    }

    public void reset(){
        progressBar.setProgress(0);
        editTextBandwidth.setText("");
        textSpeed.setText("");
        textTime.setText("");
        textDownloaded.setText("");
        textPercentage.setText("");
    }

    public float calcDownloadTime(){
        //TODO
        return 0;
    }

    public void simulate(){
        //TODO
    }


}
