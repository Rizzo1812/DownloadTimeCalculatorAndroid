package com.example.downloadtimecalculator;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Locale;

public class DownloadSimulator{
    private final Activity mainActivity;
    private final ProgressBar progressBar;
    private final EditText editTextBandwidth;
    private final TextView textSpeed;
    private final TextView textTime;
    private final TextView textDownloaded;
    private final TextView textPercentage;
    private int secondsToDownload;
    public boolean ready;

    public DownloadSimulator(View view,Activity mainActivity){
        this.mainActivity = mainActivity;
        this.progressBar = view.findViewById(R.id.progressBar);
        this.editTextBandwidth = view.findViewById(R.id.editTextBandwidth);
        this.textSpeed = view.findViewById(R.id.textSpeed);
        this.textTime = view.findViewById(R.id.textTime);
        this.textDownloaded = view.findViewById(R.id.textDownloaded);
        this.textPercentage = view.findViewById(R.id.textPercentage);
        this.reset();
    }

    public void reset(){
        ready = false;
        progressBar.setProgress(0);
        editTextBandwidth.setText("");
        textSpeed.setText("");
        textTime.setText("");
        textDownloaded.setText("");
        textPercentage.setText("");
        secondsToDownload = 0;
    }

    public void calcDownloadTime(){
        if(!isSpeedSet()){
            reset();
            return;
        }
        double fileSize = Double.parseDouble(((EditText) (mainActivity.findViewById(R.id.editTxtFileSize))).getText().toString());
        int selectedRadioID = ((RadioGroup) (mainActivity.findViewById(R.id.radioGroupSize))).getCheckedRadioButtonId();
        fileSize = convertToB(fileSize, ((RadioButton) (mainActivity.findViewById(selectedRadioID))).getText().toString());
        double bandwidth = Double.parseDouble(editTextBandwidth.getText().toString());
        selectedRadioID = ((RadioGroup) (mainActivity.findViewById(R.id.radioGroupBandwidth))).getCheckedRadioButtonId();
        textSpeed.setText(String.format(Locale.getDefault(),"%.3f %s",bandwidth/8, ((RadioButton) (mainActivity.findViewById(selectedRadioID))).getText().toString().toUpperCase()));
        bandwidth = convertToBps(bandwidth, ((RadioButton) (mainActivity.findViewById(selectedRadioID))).getText().toString());
        if (bandwidth != 0) {
            secondsToDownload = (int) Math.floor(fileSize / bandwidth);
            textTime.setText(timeFormat(secondsToDownload));
        } else {
            secondsToDownload = -1;
            textTime.setText("∞");
        }
        ready = true;
    }

    public void simulate(){
        //TODO
    }

    private double convertToB(double fileSize, String unitOfMeasure){
        switch (unitOfMeasure){
            case "GB":
                return fileSize * (Math.pow(1024,3));
            case "MB":
                return fileSize * (Math.pow(1024,2));
            case "KB":
                return fileSize * 1024;
        }
        return -1;
    }

    private double convertToBps(double bandwidth, String unitOfMeasure) {
        switch (unitOfMeasure) {
            case "Gb/s":
                return bandwidth * (Math.pow(1000, 3)) / 8;
            case "Mb/s":
                return bandwidth * (Math.pow(1000, 2)) / 8;
            case "Kb/s":
                return bandwidth * 1000 / 8;
        }
        return -1;
    }

    private String timeFormat(int seconds){
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;
        return String.format(Locale.getDefault(),"%02d:%02d:%02d", hours, minutes, seconds);
    }

    private boolean isSpeedSet(){
        return !editTextBandwidth.getText().toString().equals("");
    }
}
