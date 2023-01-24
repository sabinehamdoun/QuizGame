package com.halae.thechallenge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends Activity {

    public static final int RESULT_CANCELED =0;
    public static final int RESULT_OK =-1;
    public static int result;
    private static final String KEY_INDEX="index";
    private static int mCurrentIndex;
    private static final String TAG = "MainActivity";
    public static final int max_score1=17;
    public static final int max_score2=8;
    public static ArrayList<String> a1 = new ArrayList<String>(); ///to track previous scores of App1
    public static ArrayList<String> a2 = new ArrayList<String>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState!=null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }
        Log.d(TAG, "this is the first log");//for testing

        Button start1 = findViewById(R.id.start_btn);  //Start button for Guess Country App
        Button start2 = findViewById(R.id.start2_btn);  //Start button for Guess Animal App
        TextView tv1 = (TextView) findViewById(R.id.result);  //This is used to display the current score

        //app1
        start1.setOnClickListener(new View.OnClickListener() {   //when clicking the 1st game
            @Override
            public void onClick(View v) {
                openApp1();
            }
        });

        Spinner spinner1 = findViewById(R.id.spinner1);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, a1);
        spinner1.setAdapter(spinnerAdapter);

        //app2
        start2.setOnClickListener(new View.OnClickListener() {  //when clicking the 2nd game
            @Override
            public void onClick(View v) { openApp2(); }
        });

        Spinner spinner2 = findViewById(R.id.spinner2);
        ArrayAdapter<String> spinnerAdapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, a2);
        spinner2.setAdapter(spinnerAdapter2);

    }


    public void openApp1(){

        Intent intent = new Intent(this, App1.class);
        startActivityForResult(intent, 1);   //we are starting the other activity but waiting for a result from it.

    }

    public void openApp2(){
        Intent intent = new Intent(this, App2.class);
        startActivityForResult(intent, 2);   //we are starting the other activity but waiting for a result from it.
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //our requestCode=1, resultCode=RESULT_OK
        super.onActivityResult(requestCode, resultCode, data);
        TextView tv1 = (TextView) findViewById(R.id.result);

        ////Countries Game
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                result = data.getIntExtra(App1.total_score, 0);
                tv1.setText("Your Current Score is: " + result);
                a1.add(String.valueOf(result));  ///add the result to the list of previous scores
            }
            if (resultCode == RESULT_CANCELED) {  //if quiz was not completed and home button was pressed, reset score to 0.
                tv1.setText("" + 0);
            }
        }


        ////Marine Animals Game
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                result = data.getIntExtra(App1.total_score, 0);
                tv1.setText("" + result);
                a2.add(String.valueOf(result));  ///add the result to the list of previous scores
            }
            if (resultCode == RESULT_CANCELED) {  //if quiz was not completed and home button was pressed, reset score to 0.
                tv1.setText("" + 0);
            }
        }
    }
    }