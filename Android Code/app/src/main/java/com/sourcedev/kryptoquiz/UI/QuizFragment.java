package com.sourcedev.kryptoquiz.UI;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sourcedev.kryptoquiz.R;
import com.sourcedev.kryptoquiz.activities.QuizActivity;
import com.sourcedev.kryptoquiz.models.QuestionResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuizFragment extends Fragment {
    @BindView(R.id.question)
    TextView question;
    @BindView(R.id.tag1)
    TextView tag1;
    @BindView(R.id.tag2)
    TextView tag2;
    @BindView(R.id.tag3)
    TextView tag3;
    @BindView(R.id.tag4)
    TextView tag4;
    private View view;
    private static final int blurFactor = 200;

    boolean _areLecturesLoaded = false;

    private static final String NUM = "NUMBER";
    @BindViews({R.id.option1, R.id.option2, R.id.option3, R.id.option4})
    List<ConstraintLayout> clLayouts;
    @BindViews({R.id.tag1, R.id.tag2, R.id.tag3, R.id.tag4})
    List<TextView> textViewList;
    @BindView(R.id.tv_seconds)
    TextView secondsRemaining;
    int qnum;
    QuestionResponse object;

//    public static QuizFragment newInstance(int number) {
//        QuizFragment quizFragment = new QuizFragment();
//        Bundle args = new Bundle();
//        args.putInt(NUM, number);
//        quizFragment.setArguments(args);
//        return quizFragment;
//    }

    public static QuizFragment newInstance(int number, QuestionResponse body) {
        QuizFragment quizFragment = new QuizFragment();
        Bundle args = new Bundle();
        Gson gson = new Gson();
        args.putInt("qnum", number);
        args.putString("object", gson.toJson(body));
        quizFragment.setArguments(args);
        return quizFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.quiz_fragment, container, false);
        ButterKnife.bind(this, view);
        qnum = getArguments().getInt("qnum");
        object = new Gson().fromJson(getArguments().getString("object"), QuestionResponse.class);
        setDataInView(object);
        initViews();
//        totalNumber = getArguments().getInt(NUM, 0);
//        if (totalNumber == 0) {
//            initViews();
//        }
        return view;
    }

    private void setDataInView(QuestionResponse object) {
        question.setText(object.getQues().trim());
        tag1.setText(object.getOptionOne());
        tag2.setText(object.getOptionTwo());
        tag3.setText(object.getOptionThree());
        tag4.setText(object.getOptionFour());
    }

    @Nullable
    @OnClick({R.id.option1, R.id.option2, R.id.option3, R.id.option4})
    public void onOptionClick(View view) {
        for (int i = 0; i < 4; i++) {
            if (view.getId() != clLayouts.get(i).getId()) {
                clLayouts.get(i).setSelected(false);
                textViewList.get(i).setTextColor(getResources().getColor(R.color.white));
            } else {
                if (clLayouts.get(i).getId() == R.id.option1) {
                    ((QuizActivity) getActivity()).setAnswers(qnum, object.getAns(), 1);
                } else if (clLayouts.get(i).getId() == R.id.option2) {
                    ((QuizActivity) getActivity()).setAnswers(qnum, object.getAns(), 2);
                } else if (clLayouts.get(i).getId() == R.id.option3) {
                    ((QuizActivity) getActivity()).setAnswers(qnum, object.getAns(), 3);
                } else if (clLayouts.get(i).getId() == R.id.option4) {
                    ((QuizActivity) getActivity()).setAnswers(qnum, object.getAns(), 4);
                }
                clLayouts.get(i).setSelected(true);
                textViewList.get(i).setTextColor(getResources().getColor(R.color.black));
            }
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !_areLecturesLoaded && qnum != 0) {
            initViews();
            _areLecturesLoaded = true;
        }
    }

    private void initViews() {
        secondsRemaining.setText(11 + "");
        new CountDownTimer(11000, 1000) {
            public void onTick(long millisUntilFinished) {
                int a = (int) (millisUntilFinished / 1000);
                if (a <= 5) {
                    Animation hyperspaceJump = AnimationUtils.loadAnimation(getContext(), R.anim.jump);
                    secondsRemaining.startAnimation(hyperspaceJump);
                }
                secondsRemaining.setText("" + ((millisUntilFinished / 1000)-1));
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                Log.e("onFinish: ", "Finish " + qnum);
                ((QuizActivity) getActivity()).setViewPagerPosition(++qnum);
            }

        }.start();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}