package com.singularitycoder.yesbank;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    View indicator1;
    View indicator2;
    View indicator3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.viewpager_auth_intro);
        indicator1 = findViewById(R.id.indicator1);
        indicator2 = findViewById(R.id.indicator2);
        indicator3 = findViewById(R.id.indicator3);

        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new WizardPageChangeListener());
        viewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.minus_seventy_six));
        viewPager.setOffscreenPageLimit(3);
        updateIndicators(0);
    }

    public void login(View view) {
        startActivity(new Intent(MainActivity.this, LogInActivity.class));
    }

    private static class ViewPagerAdapter extends FragmentPagerAdapter {

        private final int WIZARD_PAGES_COUNT = 3;

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new FragmentAuthIntro(position);
        }

        @Override
        public int getCount() {
            return WIZARD_PAGES_COUNT;
        }
    }

    private class WizardPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int position) {

        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            updateIndicators(position);
        }
    }

    private void updateIndicators(int position) {
        switch (position) {
            case 0:
                indicator1.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_dot));
                indicator2.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_dot_light_green));
                indicator3.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_dot_light_green));
                break;
            case 1:
                indicator1.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_dot_light_green));
                indicator2.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_dot));
                indicator3.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_dot_light_green));
                break;
            case 2:
                indicator1.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_dot_light_green));
                indicator2.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_dot_light_green));
                indicator3.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_dot));
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public static class FragmentAuthIntro extends Fragment {
        private final int wizard_page_position;
        ImageView ivIntroImage;
        TextView tvIntroText;

        private final String TAG = FragmentAuthIntro.class.getSimpleName();

        public FragmentAuthIntro(int position) {
            this.wizard_page_position = position;
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_auth_intro, container, false);
            ivIntroImage = view.findViewById(R.id.iv_intro_image);
            tvIntroText = view.findViewById(R.id.tv_auth_tour_text);
            return view;

        }

        @Override
        public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            switch (wizard_page_position) {
                case 0:
                    ivIntroImage.setImageResource(R.drawable.login_bg);
                    tvIntroText.setText("Assured Returns for a Secured Future");
                    break;
                case 1:
                    ivIntroImage.setImageResource(R.drawable.login_bg2);
                    tvIntroText.setText("Your 24 hour Personal Banking Assistant");
                    break;
                case 2:
                    ivIntroImage.setImageResource(R.drawable.login_bg);
                    tvIntroText.setText("Find nearest branch with just a 'Hi'");
                    break;
            }
        }
    }
}
