package com.example.downloadtimecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    LinearLayout layout;
    EditText editTxtFileSize;
    Switch switchRunning;
    Button buttonCalc;
    FloatingActionButton floatingAddSimulator;
    LinkedList<DownloadSimulator> listOfDownloadSimulators = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout =  findViewById(R.id.main_activity_layout);
        editTxtFileSize = findViewById(R.id.editTxtFileSize);
        buttonCalc = findViewById(R.id.buttonCalc);
        floatingAddSimulator = findViewById(R.id.floatingAddSimulator);
        addNewDownloader();

        switchRunning = (Switch)  findViewById(R.id.switchRunning);
        switchRunning.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    buttonCalc.setEnabled(false);
                    floatingAddSimulator.setVisibility(View.INVISIBLE);
                    for (DownloadSimulator simulator : listOfDownloadSimulators) {
                        simulator.simulate();
                    }
                }else{
                    for (DownloadSimulator simulator : listOfDownloadSimulators) {
                        simulator.reset();
                    }
                    switchRunning.setEnabled(false);
                    floatingAddSimulator.setVisibility(View.VISIBLE);
                    buttonCalc.setEnabled(true);
                }
            }
        });
    }

    public void addNewDownloader(){
        View newDownloaderView = getLayoutInflater().inflate(R.layout.download_simulator, null);
        layout.addView(newDownloaderView);
        listOfDownloadSimulators.add(new DownloadSimulator(newDownloaderView,this));
    }

    public void onBtnAddClick(View view) {
        addNewDownloader();
    }

    public void onBtnCalcClick(View view) {
        if(editTxtFileSize.getText().toString().equals("")){
            Toast.makeText(this, "Insert file size!",
                    Toast.LENGTH_LONG).show();
        }else{
            for (DownloadSimulator simulator : listOfDownloadSimulators) {
                simulator.calcDownloadTime();
                if(simulator.ready) switchRunning.setEnabled(true);
            }
        }
    }
}