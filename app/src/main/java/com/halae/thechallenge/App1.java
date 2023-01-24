package com.halae.thechallenge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;

public class App1 extends AppCompatActivity {

    int secondsPassed = 0;
    int rightAnswers = 0;
    int seconds = 120000;
    private ImageButton last_btn;
    public static final String total_score = "com.halae.project1.total_score";
    boolean isStartCLicked;
    CountDownTimer countDownTimer;
    TextView time, countryName, score, textview2;
    ArrayList<String> countries = new ArrayList<>(17);
    ArrayList<ImageButton> buttons = new ArrayList<>(17);
    ImageButton UK, ireland, iceland, norway, sweden, finland, spain, portugal,
            france, germany, poland, belarus, russia, ukraine, romania, greece, italy;
    Button start, giveup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app1);
        getSupportActionBar().hide();

        UK = findViewById(R.id.UK);
        ireland = findViewById(R.id.ireland);
        france = findViewById(R.id.france);
        germany = findViewById(R.id.germany);
        poland = findViewById(R.id.poland);
        belarus = findViewById(R.id.belarus);
        russia = findViewById(R.id.russia);
        ukraine = findViewById(R.id.ukraine);
        romania = findViewById(R.id.romania);
        greece = findViewById(R.id.greece);
        italy = findViewById(R.id.italy);
        iceland = findViewById(R.id.iceland);
        norway = findViewById(R.id.norway);
        sweden = findViewById(R.id.sweden);
        finland = findViewById(R.id.finland);
        spain = findViewById(R.id.spain);
        portugal = findViewById(R.id.portugal);
        start = findViewById(R.id.startButton);
        time = findViewById(R.id.time);
        countryName = findViewById(R.id.countryName);
        score = findViewById(R.id.correct);
        textview2 = findViewById(R.id.textView2);
        giveup = findViewById(R.id.giveUpButton);

        ImageButton[] buttonsArray = {UK, ireland, france, germany, poland, belarus,
                russia, ukraine, romania, greece, italy, iceland, norway, sweden,
                finland, spain, portugal};

        String[] countriesArray = {"Portugal", "Romania", "France", "Germany", "UK",
                "Iceland", "Ireland", "Sweden", "Norway", "Finland", "Belarus",
                "Greece", "Spain", "Poland", "Ukraine", "Italy", "Russia"};

        fillArrayLists(countries, countriesArray, buttons, buttonsArray);

        startGame();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("time", time.getText().toString());
        outState.putString("score", score.getText().toString());
        outState.putInt("scoreColor", score.getCurrentTextColor());
        outState.putInt("timeVis", time.getVisibility());
        outState.putInt("startVisibility", start.getVisibility());
        outState.putInt("giveUpVisibility", giveup.getVisibility());
        outState.putInt("textviewVisibility", textview2.getVisibility());
        outState.putString("countryName", countryName.getText().toString());
        outState.putInt("countryNameVis", countryName.getVisibility());
        outState.putStringArrayList("countryNameList", countries);
        outState.putBoolean("isStartCLicked", isStartCLicked);
        for (int i = 0; i < buttons.size(); i++) {
            outState.putString(buttons.get(i).getId() + "checked", buttons.get(i).getContentDescription().toString());
            outState.putInt(buttons.get(i).getId() + "visible", buttons.get(i).getVisibility());
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        time.setText(savedInstanceState.getString("time"));
        score.setText(savedInstanceState.getString("score"));
        score.setTextColor(savedInstanceState.getInt("scoreColor"));
        time.setVisibility(savedInstanceState.getInt("timeVis"));
        start.setVisibility(savedInstanceState.getInt("startVisibility"));
        giveup.setVisibility(savedInstanceState.getInt("giveUpVisibility"));
        textview2.setVisibility(savedInstanceState.getInt("textviewVisibility"));
        countryName.setText(savedInstanceState.getString("countryName"));
        countryName.setVisibility(savedInstanceState.getInt("countryNameVis"));
        countries = savedInstanceState.getStringArrayList("countryNameList");
        isStartCLicked = savedInstanceState.getBoolean("isStartCLicked");
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setVisibility(savedInstanceState.getInt(buttons.get(i).getId() + "visible"));
            buttons.get(i).setClickable(true);
            buttons.get(i).setContentDescription(savedInstanceState.getString(buttons.get(i).getId() + "checked"));
            if (buttons.get(i).getContentDescription().toString().equals("checked")){
                buttons.get(i).setClickable(false);
                buttons.get(i).setBackground(getDrawable(R.drawable.checked_button));
            }
            if (buttons.get(i).getContentDescription().toString().equals("unchecked")){
                buttons.get(i).setBackground(getDrawable(R.drawable.clicked_wrong));
            }
        }
        if(isStartCLicked){ //checks if start has been clicked, otherwise works without clicking start
            countSeconds();
            checkClickedButton(buttons, score);
            giveup.setOnClickListener(view1 -> {
                startGame();
                timeFinish();
            });
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void checkAnswer(ImageButton button, String country,
                            TextView answers){
        if(button.getContentDescription().toString().equals(country)){
            button.setBackground(getDrawable(R.drawable.checked_button));
            button.setContentDescription("checked");
            button.setClickable(false);
            setRightAnswers(answers);
            randomizeCountry(countryName, countries);
        }
        else{
            button.setContentDescription("unchecked");
            System.out.println(button.getContentDescription().toString());
            button.setBackground(getDrawable(R.drawable.clicked_wrong));
            secondsPassed+=10;
        }
    }

    public void randomizeCountry(TextView country, ArrayList<String> countryNames){
        finishGame(country, countryNames);

    }

    public void checkClickedButton(ArrayList<ImageButton> buttons, TextView answers){
        for (int i = 0; i < buttons.size(); i++) {
            int index = i;
            buttons.get(i).setOnClickListener(view -> checkAnswer(buttons.get(index), countryName.getText().toString(), answers));
        }
    }

    public void finishGame(TextView country, ArrayList<String> countryNames){
        if (countryNames.isEmpty()){
            Snackbar mySnackBar = Snackbar.make(findViewById(R.id.myLayout), R.string.congrats, 2000);
            mySnackBar.show();
            giveup.setVisibility(View.INVISIBLE);
            score.setTextColor(R.drawable.checked_button);
            time.setVisibility(View.INVISIBLE);

            Intent resultIntent = new Intent(App1.this, MainActivity.class);
            resultIntent.putExtra(total_score, rightAnswers);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
        else{
            int index = (int)(Math.random() * countryNames.size());
            country.setText(countryNames.get(index));
            countryNames.remove(index);
        }
    }

    @SuppressLint("SetTextI18n")
    public void setRightAnswers(TextView answers){
        rightAnswers++;
        answers.setText(rightAnswers + "/17");
    }

    public void fillArrayLists(ArrayList<String> countries, String[] countriesArray,
                               ArrayList<ImageButton> buttons, ImageButton[] buttonsArray){
        countries.addAll(Arrays.asList(countriesArray));
        buttons.addAll(Arrays.asList(buttonsArray));
    }

    @SuppressLint("SetTextI18n")
    public void setTimeText(int secondsPassed, TextView time,
                            String minutes, int start, int end){
        if(secondsPassed<=120) {
            if (secondsPassed > start && secondsPassed < end) {
                time.setText(minutes + (end - 1 - secondsPassed));
                if (secondsPassed > 50 && secondsPassed < 61 || secondsPassed > 110) {
                    time.setText(minutes + "0" + (end - 1 - secondsPassed));
                }
            }
        }
        else{
            time.setText("0:00");
        }
    }

    public void countSeconds() {

        countDownTimer = new CountDownTimer(seconds, 1000) {
            @Override
            public void onTick(long seconds) {
                secondsPassed++;
                setTimeText(secondsPassed, time, "1:", 0, 61);
                setTimeText(secondsPassed, time, "0:", 60, 121);
                if(secondsPassed>120){
                    timeFinish();
                    Intent resultIntent = new Intent(App1.this, MainActivity.class);
                    resultIntent.putExtra(total_score, rightAnswers);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
            public void onFinish() {
                timeFinish();
                Intent resultIntent = new Intent(App1.this, MainActivity.class);
                resultIntent.putExtra(total_score, rightAnswers);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        }.start();
    }

    public void timeFinish(){
        time.setVisibility(View.INVISIBLE);
        score.setTextColor(getColor(R.color.clicked_wrong));
        giveup.setVisibility(View.INVISIBLE);
        countryName.setVisibility(View.INVISIBLE);
        textview2.setVisibility(View.INVISIBLE);
        secondsPassed = 121;
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setVisibility(View.INVISIBLE);
        }
    }

    public void startGame(){
        start.setOnClickListener(view -> {
            isStartCLicked = true;
            countSeconds();
            textview2.setVisibility(View.VISIBLE);
            randomizeCountry(countryName, countries);
            start.setVisibility(View.INVISIBLE);
            giveup.setVisibility(View.VISIBLE);

            checkClickedButton(buttons, score);

            giveup.setOnClickListener(view1 -> {
                startGame();
                timeFinish();
            });
        });
    }

}