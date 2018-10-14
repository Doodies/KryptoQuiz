package com.sourcedev.kryptoquiz.activities;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sourcedev.kryptoquiz.R;
import com.sourcedev.kryptoquiz.UI.BottomSheetFragment;
import com.sourcedev.kryptoquiz.models.User;
import com.sourcedev.kryptoquiz.network.ApiCalls;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseActivity extends AppCompatActivity {

    @BindView(R.id.tv_next_game)
    TextView tvNextGame;
    @BindView(R.id.cl_next_game)
    ConstraintLayout clNextGame;
    @BindView(R.id.options)
    ImageView options;
    @BindView(R.id.tv_full_name)
    TextView tvFullName;
    @BindView(R.id.tv_no_of_games)
    TextView tvNoOfGames;
    @BindView(R.id.tv_games)
    TextView tvGames;
    @BindView(R.id.tv_qcoin_left)
    TextView tvQcoinLeft;
    @BindView(R.id.tv_left_qcoin)
    TextView tvLeftQcoin;
    @BindView(R.id.tv_qcoin_won)
    TextView tvQcoinWon;
    @BindView(R.id.tv_won_qcoin)
    TextView tvWonQcoin;
    @BindView(R.id.cl_details)
    ConstraintLayout clDetails;
    @BindView(R.id.tv_share)
    TextView tvShare;
    @BindView(R.id.iv_facebook)
    ImageView ivFacebook;
    @BindView(R.id.iv_twitter)
    ImageView ivTwitter;
    @BindView(R.id.cl_profile)
    ConstraintLayout clProfile;
    @BindView(R.id.imgProfile)
    CircleImageView imgProfile;
    @BindView(R.id.cameraIcon)
    ImageView cameraIcon;
    @BindView(R.id.rlProfile)
    RelativeLayout rlProfile;
    @BindView(R.id.tv_leaderboard)
    TextView tvLeaderboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
        ButterKnife.bind(this);
        getData();
    }

    @OnClick(R.id.options)
    public void onOptionsClick() {
        Log.e("onOptionsClick: ", "clicked");
        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
        bottomSheetFragment.show(getSupportFragmentManager(), "");
    }

    private void getData() {
        ApiCalls.getUserDetail().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    setDataInView(response.body());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private void setDataInView(User user) {
        tvFullName.setText(user.getFirstName() + " " + user.getLastName());
        tvGames.setText(user.getGamesPlayed());
        tvLeftQcoin.setText(user.getBalance());
        tvWonQcoin.setText(user.getAmountWon());
    }
}
