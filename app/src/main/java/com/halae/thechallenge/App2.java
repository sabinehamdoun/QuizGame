package com.halae.thechallenge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class App2 extends Activity {
    private TextView timer;
    private Button ans1, ans2, ans3, ans4;
    private Button next;
    private TextView correct;
    private ImageView image;
    private ArrayList<String> animalNames = new ArrayList<>();
    private HashMap<String, Integer> map = new HashMap<>();

    private static final String KEY_INDEX="index";
    private static int mCurrentIndex;
    private static final String TAG = "App2";

    private int i;
    private TextView Score;
    public int score;
    private int points;
    private CountDownTimer countDownTimer;
    private long seconds;
    public static final String total_score = "com.halae.project1.total_score";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app2);

        if(savedInstanceState!=null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }
        Log.d(TAG, "this is the first log");//for testing

        timer = findViewById(R.id.timer);
        correct = findViewById(R.id.correct);
        image = findViewById(R.id.image);
        ans1 = findViewById(R.id.ans1);
        ans2 = findViewById(R.id.ans2);
        ans3 = findViewById(R.id.ans3);
        ans4 = findViewById(R.id.ans4);
        Score = findViewById(R.id.Score);
        next = findViewById(R.id.next_button);
        i = 0;




        animalNames.add(getString(R.string.bluefish));
        animalNames.add(getString(R.string.FinWhale));
        animalNames.add(getString(R.string.SeaGull));
        animalNames.add(getString(R.string.SiameseFightingFish));
        animalNames.add(getString(R.string.Squid));
        animalNames.add(getString(R.string.KillerWhale));
        animalNames.add(getString(R.string.Octopus));
        animalNames.add(getString(R.string.BottleNoseDolphin));


        map.put(animalNames.get(0), R.drawable.bluefish);
        map.put(animalNames.get(1), R.drawable.finwhale);
        map.put(animalNames.get(2), R.drawable.seagull);
        map.put(animalNames.get(3), R.drawable.siamese_fighting);
        map.put(animalNames.get(4), R.drawable.squid);
        map.put(animalNames.get(5), R.drawable.killerwhale);
        map.put(animalNames.get(6), R.drawable.octopus);
        map.put(animalNames.get(7), R.drawable.bottlenose_dolphin);



        Collections.shuffle(animalNames);
        seconds = 10000;
        points = 0;
        if(i>animalNames.size()-2){
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent resultIntent = new Intent(App2.this, MainActivity.class);
                    if(score<0)
                        score=0;
                    resultIntent.putExtra(total_score, score);
                    setResult(RESULT_OK, resultIntent);
                    finish();

                }
            });
        }
        startGame();

    }

    protected void startGame() {
        seconds = 10000;
        timer.setText("" + (seconds / 1000) + "s");
        Score.setText("Score: " + points);
        generateQuestions(i);
        countDownTimer = new CountDownTimer(seconds, 1000) {
            @Override
            public void onTick(long seconds) {
                timer.setText("" + (seconds/1000) + "s");
            }

            @Override
            public void onFinish() {
                i++;
                if (i> animalNames.size() - 1){
                    image.setVisibility(View.GONE);
                    ans1.setVisibility(View.GONE);
                    ans2.setVisibility(View.GONE);
                    ans3.setVisibility(View.GONE);
                    ans4.setVisibility(View.GONE);
                    Intent intent = new Intent(App2.this, MainActivity.class);
                    intent.putExtra("points", points);
                    startActivity(intent);
                    setResult(RESULT_OK, intent);
                    finish();

                } else {
                    countDownTimer = null;
                    startGame();
                }
            }
        }.start();
    }

    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("Timer", timer.getText().toString());
        outState.putString("Score", Score.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        timer.setText(savedInstanceState.getString("Timer"));
        Score.setText(savedInstanceState.getString("Score"));

    }

    private void generateQuestions(int i) {
        ArrayList<String> animalNamesTemp = (ArrayList<String>) animalNames.clone();
        String correctAnswer = animalNames.get(App2.this.i);
        animalNamesTemp.remove(correctAnswer);
        Collections.shuffle(animalNamesTemp);
        ArrayList<String> newList = new ArrayList<>();
        newList.add(animalNamesTemp.get(0));
        newList.add(animalNamesTemp.get(1));
        newList.add(animalNamesTemp.get(2));
        newList.add(correctAnswer);
        Collections.shuffle(newList);
        ans1.setText(newList.get(0));
        ans2.setText(newList.get(1));
        ans3.setText(newList.get(2));
        ans4.setText(newList.get(3));
        image.setImageResource(map.get(animalNames.get(App2.this.i)));
    }

    public void nextQuestion(View view) {
        ans1.setEnabled(true);
        ans2.setEnabled(true);
        ans3.setEnabled(true);
        ans4.setEnabled(true);

        countDownTimer.cancel();
        i++;
        if (i> animalNames.size() - 1) {
            image.setVisibility(View.GONE);
            ans1.setVisibility(View.GONE);
            ans2.setVisibility(View.GONE);
            ans3.setVisibility(View.GONE);
            ans4.setVisibility(View.GONE);

            
            Intent resultIntent = new Intent(App2.this, MainActivity.class);
            if(score<0)
                score=0;
            resultIntent.putExtra(total_score, score);
            //if back button was pressed, then setResult Result_cancelled.
            setResult(RESULT_OK, resultIntent);
            finish();

        } else {
            countDownTimer = null;
            startGame();
        }
    }

    public void answerSelected(View view){
        ans1.setEnabled(false);
        ans2.setEnabled(false);
        ans3.setEnabled(false);
        ans4.setEnabled(false);

        countDownTimer.cancel();
        String answer = ((Button) view).getText().toString().trim();
        String correctAnswer = animalNames.get(i);
        if (answer.equals(correctAnswer)) {
            points = points + 2;
            Score.setText("Score: " + points);
            score+=2;
            correct.setText("Correct");

        } else {
            points = points - 1;
            Score.setText("Score: " + points);
            score-=1;
            correct.setText("Incorrect");
        }

    }
}