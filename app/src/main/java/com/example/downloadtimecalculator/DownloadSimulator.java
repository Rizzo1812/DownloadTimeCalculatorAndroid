package com.example.downloadtimecalculator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class DownloadSimulator {
    private final Activity mainActivity;
    private final ProgressBar progressBar;
    private final EditText editTextBandwidth;
    private final TextView textSpeed;
    private final TextView textTime;
    private final TextView textDownloaded;
    private final TextView textPercentage;
    private final ImageView deleteImgBtn;
    private long millisecondsToDownload;
    private double fileSize;
    private double bandwidth;
    private CountDownTimer clock;
    public boolean ready;
    NumberFormat numberFormat = new DecimalFormat("##.###");

    public DownloadSimulator(View view,Activity mainActivity) {
        this.mainActivity = mainActivity;
        this.progressBar = view.findViewById(R.id.progressBar);
        this.editTextBandwidth = view.findViewById(R.id.editTextBandwidth);
        this.textSpeed = view.findViewById(R.id.textSpeed);
        this.textTime = view.findViewById(R.id.textTime);
        this.textDownloaded = view.findViewById(R.id.textDownloaded);
        this.textPercentage = view.findViewById(R.id.textPercentage);
        this.deleteImgBtn = view.findViewById(R.id.deleteImgBtn);
        this.reset();
    }

    public void reset() {
        ready = false;
        if(clock != null) clock.cancel();
        progressBar.setProgress(0);
        progressBar.setProgressTintList(ColorStateList.valueOf(Color.rgb(0,127,255)));
        textSpeed.setText("");
        textTime.setText("");
        textDownloaded.setText("");
        textPercentage.setText("");
        textPercentage.setTypeface(null, Typeface.NORMAL);
        deleteImgBtn.setVisibility(View.VISIBLE);
        millisecondsToDownload = -1;
        fileSize = 0;
        bandwidth = 0;
    }

    public void calcDownloadTime() {
        if(!isSpeedSet() || editTextBandwidth.getText().toString().equals(".")){
            reset();
            return;
        }
        fileSize = Double.parseDouble(((EditText) (mainActivity.findViewById(R.id.editTxtFileSize))).getText().toString());
        int selectedRadioID = ((RadioGroup) (mainActivity.findViewById(R.id.radioGroupSize))).getCheckedRadioButtonId();
        fileSize = convertToB(fileSize, ((RadioButton) (mainActivity.findViewById(selectedRadioID))).getText().toString());
        bandwidth = Double.parseDouble(editTextBandwidth.getText().toString());
        selectedRadioID = ((RadioGroup) (mainActivity.findViewById(R.id.radioGroupBandwidth))).getCheckedRadioButtonId();
        bandwidth = convertToBps(bandwidth, ((RadioButton) (mainActivity.findViewById(selectedRadioID))).getText().toString());
        textSpeed.setText(speedFormat(bandwidth));

        if(bandwidth <= 1){
            editTextBandwidth.setText("0");
            textSpeed.setText("~0 B/s");
            bandwidth = 0;
        }
        if (bandwidth != 0) {
            millisecondsToDownload = (long) (Math.floor(fileSize / bandwidth *1000));
            textTime.setText(timeFormat(millisecondsToDownload));
        } else {
            if(fileSize == 0){
                millisecondsToDownload = 0;
                textTime.setText(timeFormat(0));
            }else {
                millisecondsToDownload = -1;
                textTime.setText("∞");
            }
        }
        ready = true;
    }

    public void simulate(){
        if(millisecondsToDownload == -1) return;

        deleteImgBtn.setVisibility(View.INVISIBLE);

        clock = new CountDownTimer(millisecondsToDownload, 50) {
            public void onTick(long millisUntilFinished) {
                long finishedMilliseconds = millisecondsToDownload - millisUntilFinished;
                double percentage = (((double)finishedMilliseconds / (double) millisecondsToDownload) * 100.0);
                textPercentage.setText(String.format(Locale.ENGLISH,"%.2f%%",percentage));
                textDownloaded.setText(fileSizeFormat(bandwidth * finishedMilliseconds / 1000));
                progressBar.setProgress((int)percentage);
            }

            @SuppressLint("SetTextI18n")
            public void onFinish() {
                progressBar.setProgress(100);
                textPercentage.setText("100%");
                textPercentage.setTypeface(null, Typeface.BOLD);
                progressBar.setProgressTintList(ColorStateList.valueOf(Color.rgb(0,192,0)));
                textDownloaded.setText(fileSizeFormat(fileSize));
            }
        };

        clock.start();
    }

    private double convertToB(double fileSize, String unitOfMeasure) {
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

    private String speedFormat(double Bps) {
        if(Bps >= Math.pow(10,9)){
            return String.format(Locale.ENGLISH,"%s GB/s",numberFormat.format(Bps / (Math.pow(10, 9))));
        }
        if(Bps >= Math.pow(10,6)){
            return String.format(Locale.ENGLISH,"%s MB/s",numberFormat.format(Bps / (Math.pow(10, 6))));
        }
        if(Bps >= Math.pow(10,3)){
            return String.format(Locale.ENGLISH,"%s KB/s",numberFormat.format(Bps / (Math.pow(10, 3))));
        }
        return String.format(Locale.ENGLISH,"%s B/s",numberFormat.format(Bps));
    }

    private String timeFormat(long milliseconds) {
        if(milliseconds == 0) return "Instant";
        long seconds = milliseconds / 1000L;
        if(seconds >= 31536000000L) {//1000 years
            return "> 1000 years";
        }
        if(seconds >= 31536000L) { //1 year
            long years = seconds / 31536000L;
            long months = (seconds % 31536000L) / 2592000L;
            long days = ((seconds % 31536000L) % 2592000L) / 86400L;
            return String.format(Locale.ENGLISH,"%03dy %02dm %02dd", years, months, days);
        }
        if(seconds >= 172800L) { //2 days
            long months = seconds / 2592000L;
            long days = (seconds % 2592000L) / 86400L;
            long hours = ((seconds % 2592000L) % 86400L) / 3600L;
            return String.format(Locale.ENGLISH,"%02dm %02dd %02dh", months, days, hours);
        }
        if(seconds >= 60) { //1 minute
            long hours = seconds / 3600;
            long minutes = (seconds % 3600) / 60;
            seconds = seconds % 60;
            return String.format(Locale.ENGLISH,"%02dh %02dm %02ds", hours, minutes, seconds);
        }
        return  String.format(Locale.ENGLISH,"%02ds %03dms", seconds, milliseconds % 1000L);
    }

    private String fileSizeFormat(double fileSizeBytes) {
       if(fileSizeBytes >= Math.pow(1024, 3)){
           return String.format(Locale.ENGLISH,"%.2f GB",fileSizeBytes / (Math.pow(1024, 3)));
       }
       if(fileSizeBytes >= Math.pow(1024, 2)){
           return String.format(Locale.ENGLISH,"%.2f MB",fileSizeBytes / (Math.pow(1024, 2)));
       }
        if(fileSizeBytes >= 1024){
            return String.format(Locale.ENGLISH,"%.2f KB",fileSizeBytes / 1024);
        }
        return String.format(Locale.ENGLISH,"%.2f B",fileSizeBytes);
    }

    private boolean isSpeedSet() {
        return !editTextBandwidth.getText().toString().equals("");
    }
}
