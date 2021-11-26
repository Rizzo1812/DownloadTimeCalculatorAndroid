package com.app.downloadtimecalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    LinearLayout layout;
    NestedScrollView scrollView;
    EditText editTxtFileSize;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch switchRunning;
    Button buttonCalc;
    FloatingActionButton floatingAddSimulator;
    LinkedList<DownloadSimulator> listOfDownloadSimulators = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout =  findViewById(R.id.main_activity_layout);
        scrollView = findViewById(R.id.scrollView);
        editTxtFileSize = findViewById(R.id.editTxtFileSize);
        buttonCalc = findViewById(R.id.buttonCalc);
        floatingAddSimulator = findViewById(R.id.floatingAddSimulator);
        addNewDownloader();

        switchRunning = (Switch) findViewById(R.id.switchRunning);
        switchRunning.setOnCheckedChangeListener((buttonView, isChecked) -> {
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
        });

        scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, previousScrollX, previousScrollY) -> {
            if (scrollY > previousScrollY) {
                floatingAddSimulator.hide();
            } else {
                if(!switchRunning.isEnabled())
                    floatingAddSimulator.show();
            }
        });
    }

    public void addNewDownloader(){
        @SuppressLint("InflateParams") View newDownloaderView = getLayoutInflater().inflate(R.layout.download_simulator, null);
        layout.addView(newDownloaderView);
        DownloadSimulator newDownloaderSimulator = new DownloadSimulator(newDownloaderView,this);
        listOfDownloadSimulators.add(newDownloaderSimulator);

        ImageView deleteImgBtn = (ImageView)newDownloaderView.findViewById(R.id.deleteImgBtn);
        deleteImgBtn.setOnClickListener(v -> {
            listOfDownloadSimulators.remove(newDownloaderSimulator);
            layout.removeView(newDownloaderView);
            for (DownloadSimulator simulator : listOfDownloadSimulators) {
                if(simulator.ready) {
                    switchRunning.setEnabled(true);
                    return;
                }
            }
            switchRunning.setEnabled(false);
        });
    }

    public void onBtnAddClick(View view) {
        addNewDownloader();
    }

    public void onBtnCalcClick(View view) {
        View current = getCurrentFocus();
        hideKeyboard(view);
        if (current != null) current.clearFocus();

        if(editTxtFileSize.getText().toString().isEmpty() || editTxtFileSize.getText().toString().equals(".")){
            Toast.makeText(this, "Insert file size!",
                    Toast.LENGTH_LONG).show();
        }else{
            if(listOfDownloadSimulators.size() > 0){
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(),0);

                for (DownloadSimulator simulator : listOfDownloadSimulators) {
                    simulator.calcDownloadTime();
                    if(simulator.ready) switchRunning.setEnabled(true);
                }
            }else{
                Toast.makeText(this, "Add at least one download simulator!",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public void hideKeyboard(View view){
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(),0);
    }
}