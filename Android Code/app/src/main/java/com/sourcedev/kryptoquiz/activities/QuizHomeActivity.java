package com.sourcedev.kryptoquiz.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.sourcedev.kryptoquiz.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuizHomeActivity extends AppCompatActivity {

    String quizId;
    String quizStartsAt;
    @BindView(R.id.time)
    TextView time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_home);
        ButterKnife.bind(this);
        quizId = getIntent().getExtras().getString("quizId");
        quizStartsAt = getIntent().getExtras().getString("quizStartDT");
        long milliSeconds = Long.parseLong(quizStartsAt);
        setTimer(milliSeconds);
    }

    private void setTimer(long milliSeconds) {
        Log.e("setTimer: ", String.valueOf(milliSeconds));
        new CountDownTimer(milliSeconds, 1000) {

            public void onTick(long millisUntilFinished) {
                int a = (int) (millisUntilFinished / 1000);
                if (a <= 5) {
                    Animation hyperspaceJump = AnimationUtils.loadAnimation(QuizHomeActivity.this, R.anim.jump);
                    time.startAnimation(hyperspaceJump);
                }
                if (millisUntilFinished / 1000 < 10) {
                    time.setText("00:0" + (millisUntilFinished / 1000));
                } else {
                    time.setText("00:" + (millisUntilFinished / 1000));
                }
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                Intent intent = new Intent(QuizHomeActivity.this, QuizActivity.class);
                intent.putExtra("quizId", quizId);
                startActivity(intent);
                finish();
            }

        }.start();
    }
}
