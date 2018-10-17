package com.sourcedev.kryptoquiz.activities;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.sourcedev.kryptoquiz.R;
import com.sourcedev.kryptoquiz.UI.QuizFragment;
import com.sourcedev.kryptoquiz.models.QuestionResponse;
import com.sourcedev.kryptoquiz.models.submitQuizResponse;
import com.sourcedev.kryptoquiz.network.ApiCalls;
import com.sourcedev.kryptoquiz.util.AlertDialogUtils;
import com.sourcedev.kryptoquiz.util.Constants;
import com.sourcedev.kryptoquiz.util.CustomViewPager;
import com.sourcedev.kryptoquiz.util.SharedPrefrencesHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = QuizActivity.class.getSimpleName();
    CustomViewPager pager;
    private static int PAGES = 10;
    @BindViews({R.id.iv_1, R.id.iv_2, R.id.iv_3, R.id.iv_4, R.id.iv_5, R.id.iv_6, R.id.iv_7, R.id.iv8, R.id.iv9, R.id.iv10})
    List<ImageView> imageViewList;
    @BindViews({R.id.v1, R.id.v2, R.id.v3, R.id.v4, R.id.v5, R.id.v6, R.id.v7, R.id.v8, R.id.v9})
    List<View> viewList;
    @BindView(R.id.question_format)
    TextView question;
    @BindView(R.id.p_bar)
    ProgressBar pBar;
    String quizId;
    Adapter adapter;
    HashSet<Integer> hashSet;

    ArrayList<QuestionResponse> listQuestions;
    ArrayList<Fragment> fragmentList;
    HashMap<Integer, Integer> maps;

    ArrayList<Integer> correctAns;
    ArrayList<Integer> givenAnswers;

    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        ButterKnife.bind(this);
        pager = findViewById(R.id.view_pager);
        List<Fragment> fragmentList = new ArrayList<>();
        hashSet = new HashSet<>();
        listQuestions = new ArrayList<>();
        maps = new HashMap<>();
        correctAns = new ArrayList<>();
        givenAnswers = new ArrayList<>();
        quizId = getIntent().getExtras().getString("quizId");
//        for (int i = 0; i < PAGES; i++) {
        // Number of pages in a vertical Pager
//            new QuizFragment();
//            fragmentList.add(QuizFragment.newInstance(i));
//        }
//        getData(1);
        mRequestQueue = Volley.newRequestQueue(this);
        fetchJsonResponse();
    }

    private void fetchJsonResponse() {
        // Pass second argument as "null" for GET requests
        setVisibility(true);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, Constants.API_BASE_URL + "api/secure/getQuizQues?quizId=" + quizId, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        setVisibility(false);
                        try {
                            JSONArray jsonArray = response.getJSONArray("message");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                QuestionResponse ob = new QuestionResponse();
                                ob.setAns((String) jsonObject.getString("ans"));
                                correctAns.add(Integer.parseInt(jsonObject.getString("ans")));
                                ob.setOptionOne((String) jsonObject.getString("o_one"));
                                ob.setOptionTwo((String) jsonObject.getString("o_two"));
                                ob.setOptionThree((String) jsonObject.getString("o_three"));
                                ob.setOptionFour((String) jsonObject.getString("o_four"));
                                ob.setQues((String) jsonObject.getString("ques"));
                                listQuestions.add(ob);
                            }
                            callFragment();
                            Log.e(TAG, "onResponse: ");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setVisibility(false);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                final String token = SharedPrefrencesHelper.getInstance().getString(Constants.USER_TOKEN, null);
                params.put("Content-Type", "application/json");
                params.put("jwt-token", token);
                return params;
            }
        };

        /* Add your Requests to the RequestQueue to execute */
        mRequestQueue.add(req);
    }

    private void callFragment() {
        fragmentList = new ArrayList<>();
        for (int i = 0; i < listQuestions.size(); i++) {
            fragmentList.add(QuizFragment.newInstance(i, listQuestions.get(i)));
        }
        adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(fragmentList);
        pager.setAdapter(adapter);
        pager.setPagingEnabled(false);
        setView(1);
    }

    private void getData(final int qnum) {
        Log.e(TAG, "getData: " + qnum);
        setVisibility(true);
        ApiCalls.getQuestions(quizId).enqueue(new Callback<List<QuestionResponse>>() {
            @Override
            public void onResponse(Call<List<QuestionResponse>> call, Response<List<QuestionResponse>> response) {
                setVisibility(false);
                if (response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + qnum);
//                    setDataForResult(qnum, response.body());
                }
            }

            @Override
            public void onFailure(Call<List<QuestionResponse>> call, Throwable t) {
                setVisibility(false);
                Log.e("onResponse: ", "failure");
            }
        });
    }

    private void setDataForResult(int qnum, QuestionResponse body) {
        Log.e(TAG, "setDataForResult: " + qnum);
        adapter.addCustomFragment(new QuizFragment().newInstance(qnum, body));
        adapter.notifyDataSetChanged();
        pager.setCurrentItem(qnum - 1);
    }

    public void setView(int totalNumber) {
        question.setText(totalNumber + "  of  10");
        for (int i = 0; i < totalNumber; i++) {
            imageViewList.get(i).setColorFilter(ContextCompat.getColor(this, R.color.yellow), android.graphics.PorterDuff.Mode.SRC_IN);
            if (i != 0) {
                viewList.get(i - 1).setBackgroundColor(ContextCompat.getColor(this, R.color.yellow));
            }
        }
    }

    public void setViewPagerPosition(int i) {
        if (i < 11 && !hashSet.contains(i)) {
            setData(i);
            hashSet.add(i);
        } else if (i > 10) {
            callSubmitApi();
        }

//        pager.setCurrentItem(i);
    }

    private void callSubmitApi() {
        int correct = 0;
        for (int i = 0; i < correctAns.size(); i++) {
            if (correctAns.get(i) == maps.get(i-1))
                correct += 1;
            Log.e(TAG, "callSubmitApi: " + correctAns.get(i) + " "  +maps.get(i-1));
        }

        setVisibility(true);
        final int finalCorrect = correct;
        new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                callSubmitQuiz(finalCorrect);
                setVisibility(false);
                Toast.makeText(QuizActivity.this, "Submit Successfully", Toast.LENGTH_SHORT);
            }

        }.start();
    }


    private void callSubmitQuiz(final int correct) {
        ApiCalls.postQuizResponse(quizId, 9).enqueue(new Callback<submitQuizResponse>() {
            @Override
            public void onResponse(Call<submitQuizResponse> call, Response<submitQuizResponse> response) {
                if (response.isSuccessful()) {
                    AlertDialogUtils.showTextDialogForOkay(QuizActivity.this, "Result", "You have given " + correct + " Answers out of 10 questions", new AlertDialogUtils.OkayDialog() {
                        @Override
                        public void onOkayClick() {
                            Intent intent = new Intent(QuizActivity.this, BaseActivity.class);
//                            intent.putExtra("random_sample", 7);
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<submitQuizResponse> call, Throwable t) {

            }
        });
    }


    private void setData(final int pos) {
        setVisibility(true);
        new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                setVisibility(false);
                onUpcomingFragment(pos);
            }

        }.start();
    }

    private void onUpcomingFragment(int pos) {
        setView(pos);
        pager.setCurrentItem(pos - 1);
    }

    public void setAnswers(int qnum, String ans, int i) {
        Log.e(TAG, "setAnswers: " + i + "  " + qnum);
        if (qnum ==  0) {
            maps.put(qnum, 0);
            maps.put(qnum, i);
        } else {
            maps.put(qnum-1, 0);
            maps.put(qnum-1, i);
        }

    }


    static class Adapter extends FragmentPagerAdapter {
        private List<Fragment> mFragments = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addCustomFragment(Fragment fragment) {
            mFragments.add(fragment);
        }

        public void addFragment(List<Fragment> fragmentList) {
            mFragments = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }

    private void setVisibility(Boolean flag) {
        if (flag) {
            pBar.setVisibility(View.VISIBLE);
            pager.setVisibility(View.GONE);
            question.setVisibility(View.GONE);
        } else {
            pBar.setVisibility(View.GONE);
            pager.setVisibility(View.VISIBLE);
            question.setVisibility(View.VISIBLE);
        }
    }
}