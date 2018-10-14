package com.sourcedev.kryptoquiz.activities;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sourcedev.kryptoquiz.R;
import com.sourcedev.kryptoquiz.util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OnBoardingActivity extends AppCompatActivity {

    static ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        pager = findViewById(R.id.viewPagerOnBoarding);
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        pager.setAdapter(new MyPagerAdapter(fm));
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                if (position == 0)
                    Utils.darkenStatusBar(OnBoardingActivity.this, R.color.pinkish_tan);
                else
                    Utils.darkenStatusBar(OnBoardingActivity.this, R.color.pinkish_tan2);

            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        // Will be called once
        @Override
        public Fragment getItem(int pos) {
            OnBoardingFragment frag1 = new OnBoardingFragment();
            OnBoardingFragment frag2 = new OnBoardingFragment();

            frag1.setPosition(2);
            frag2.setPosition(1);
            if (pos == 1)
                return frag1;
            else
                return frag2;
        }


        @Override
        public int getCount() {
            return 2;
        }
    }

    public static class OnBoardingFragment extends Fragment {
        @BindView(R.id.textViewOnBoardingBold)
        TextView textViewBold;
        @BindView(R.id.textViewOnBoarding)
        TextView textView;
        @BindView(R.id.descriptionOnBoarding)
        TextView description;
        @BindView(R.id.ButtonOnBoarding)
        Button button;
        @BindView(R.id.imageOnBoarding)
        ImageView image;
        @BindView(R.id.onBoardingLayout)
        ConstraintLayout layout;

        int position;

        public OnBoardingFragment() {
        }

        void setPosition(int p) {
            this.position = p;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        // Will be called only once
        // Read the docs for FragmentPager or Ref MainActivity
        // So don't place status bar color here
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_on_boarding, container, false);
            ButterKnife.bind(this, rootView);

            if (position == 1) {
                textViewBold.setText("Welcome To");
                textView.setText("Krypto Quiz");
                button.setText("  CONTINUE  ");
                Utils.darkenStatusBar(getActivity(), R.color.pinkish_tan);
                image.setVisibility(View.GONE);
                layout.setBackgroundResource(R.drawable.onboarding);
            } else {
                description.setText("Earn Money with your Knowledge");
                textViewBold.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                button.setText("  GET STARTED  ");
                image.setVisibility(View.VISIBLE);
                layout.setBackgroundResource(R.drawable.onboarding2);
            }

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (position == 1) {
                        // switch to new pager
                        Utils.darkenStatusBar(getActivity(), R.color.pinkish_tan2);
                        pager.setCurrentItem(1);
                    } else {
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                    }

                }
            });

            return rootView;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}