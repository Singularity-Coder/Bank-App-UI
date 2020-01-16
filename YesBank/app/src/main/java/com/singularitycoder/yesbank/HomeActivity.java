package com.singularitycoder.yesbank;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.singularitycoder.yesbank.Notifications.NotificationItem;
import com.singularitycoder.yesbank.Notifications.NotificationsAdapter;

import java.util.ArrayList;

import static com.singularitycoder.yesbank.HelperGeneral.hasInternet;
import static java.lang.String.valueOf;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "HomeActivity";
    public static ConstraintLayout conLayHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpStatusBar();
        setContentView(R.layout.activity_home);

        conLayHome = findViewById(R.id.container);
        //loading the default fragment
        loadFragment(new HomeFragment());

        //getting bottom navigation view and attaching the listener
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setItemIconTintList(null);
    }

    private void setUpStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);  // clear FLAG_TRANSLUCENT_STATUS flag:
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);  // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));   // change the color
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    // Home or Base fragments should not contain addToBackStack. But if u want to navigate to home frag then add HomeFrag
//                        .addToBackStack(null)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;

        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                fragment = new HomeFragment();
                break;
            case R.id.nav_pay:
                fragment = new PayFragment();
                break;
            case R.id.nav_save:
                fragment = new SaveFragment();
                break;
            case R.id.nav_invest:
                fragment = new InvestFragment();
                break;
            case R.id.nav_more:
                fragment = new MoreFragment();
                break;
        }

        return loadFragment(fragment);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        conLayHome.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack(); //Pops one of the added fragments
        }
    }

    public static class HomeFragment extends Fragment {

        private static final String TAG = "HomeFragment";

        ViewPager viewPager;
        View indicator1;
        View indicator2;
        View indicator3;

        ImageView imgProfile;
        ImageView imgNotifications;

        public HomeFragment() {
        }

        @Override
        public void onAttach(@NonNull Context context) {
            super.onAttach(context);
        }

        @Override
        public void onAttachFragment(@NonNull Fragment childFragment) {
            super.onAttachFragment(childFragment);
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
            // Prepare data, containers n adapters
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_home, container, false);

            viewPager = view.findViewById(R.id.viewpager);
            imgProfile = view.findViewById(R.id.img_profile);
            imgNotifications = view.findViewById(R.id.img_notifications);

            indicator1 = view.findViewById(R.id.indicator1);
            indicator2 = view.findViewById(R.id.indicator2);
            indicator3 = view.findViewById(R.id.indicator3);

            viewPager.setAdapter(new ViewPagerAdapter(getActivity().getSupportFragmentManager()));
            viewPager.addOnPageChangeListener(new WizardPageChangeListener());
            viewPager.setOffscreenPageLimit(3);
            updateIndicators(0);

            imgNotifications.setOnClickListener(view1 -> HomeFragment.this.getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new NotificationsFragment())
                    .addToBackStack("HomeDriverFragment")
                    .commit());

            imgProfile.setOnClickListener(view13 -> HomeFragment.this.getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new ProfileFragment())
                    .addToBackStack("HomeDriverFragment")
                    .commit());
            return view;
        }

        private static class ViewPagerAdapter extends FragmentPagerAdapter {

            private final int WIZARD_PAGES_COUNT = 3;

            ViewPagerAdapter(FragmentManager fm) {
                super(fm);
            }

            @Override
            public Fragment getItem(int position) {
                return new FragmentAds(position);
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
                    indicator1.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ic_dot));
                    indicator2.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ic_dot_light_blue));
                    indicator3.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ic_dot_light_blue));
                    break;
                case 1:
                    indicator1.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ic_dot_light_blue));
                    indicator2.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ic_dot));
                    indicator3.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ic_dot_light_blue));
                    break;
                case 2:
                    indicator1.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ic_dot_light_blue));
                    indicator2.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ic_dot_light_blue));
                    indicator3.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ic_dot));
                    break;
            }
        }

        public static class FragmentAds extends Fragment {
            private final int wizard_page_position;
            ImageView ivIntroImage;

            private static final String TAG = "FragmentAds";

            public FragmentAds(int position) {
                this.wizard_page_position = position;
            }

            @Override
            public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
                View view = inflater.inflate(R.layout.fragment_ad, container, false);
                ivIntroImage = view.findViewById(R.id.iv_intro_image);
                return view;

            }

            @Override
            public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
                super.onViewCreated(view, savedInstanceState);
                switch (wizard_page_position) {
                    case 0:
                        ivIntroImage.setImageResource(R.drawable.ad2);
                        break;
                    case 1:
                        ivIntroImage.setImageResource(R.drawable.ad1);
                        break;
                    case 2:
                        ivIntroImage.setImageResource(R.drawable.ad3);
                        break;
                }
            }
        }


    }

    public static class PayFragment extends Fragment {

        private static final String TAG = "PayFragment";

        public PayFragment() {
        }

        @Override
        public void onAttach(@NonNull Context context) {
            super.onAttach(context);
        }

        @Override
        public void onAttachFragment(@NonNull Fragment childFragment) {
            super.onAttachFragment(childFragment);
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
            // Prepare data, containers n adapters
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_pay, container, false);
            return view;
        }
    }

    public static class SaveFragment extends Fragment {

        private static final String TAG = "SaveFragment";

        public SaveFragment() {
        }

        @Override
        public void onAttach(@NonNull Context context) {
            super.onAttach(context);
        }

        @Override
        public void onAttachFragment(@NonNull Fragment childFragment) {
            super.onAttachFragment(childFragment);
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
            // Prepare data, containers n adapters
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_save, container, false);
            return view;
        }
    }

    public static class InvestFragment extends Fragment {

        private static final String TAG = "InvestFragment";

        public InvestFragment() {
        }

        @Override
        public void onAttach(@NonNull Context context) {
            super.onAttach(context);
        }

        @Override
        public void onAttachFragment(@NonNull Fragment childFragment) {
            super.onAttachFragment(childFragment);
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
            // Prepare data, containers n adapters
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_invest, container, false);
            return view;
        }
    }

    public static class MoreFragment extends Fragment {

        private static final String TAG = "MoreFragment";

        public MoreFragment() {
        }

        @Override
        public void onAttach(@NonNull Context context) {
            super.onAttach(context);
        }

        @Override
        public void onAttachFragment(@NonNull Fragment childFragment) {
            super.onAttachFragment(childFragment);
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
            // Prepare data, containers n adapters
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_more, container, false);
            return view;
        }
    }

    public static class ProfileFragment extends Fragment {

        private static final String TAG = "ProfileFragment";

        ScrollView scrollViewPorfile;
        Toolbar toolbar;

        public ProfileFragment() {
        }

        @Override
        public void onAttach(@NonNull Context context) {
            super.onAttach(context);
        }

        @Override
        public void onAttachFragment(@NonNull Fragment childFragment) {
            super.onAttachFragment(childFragment);
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
            // Prepare data, containers n adapters
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_profile, container, false);
            initToolBar(view);
            inits(view);
            return view;
        }

        private void initToolBar(View view) {
            toolbar = view.findViewById(R.id.toolbar);
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            if (((AppCompatActivity) getActivity()) != null) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                ((AppCompatActivity) getActivity()).setTitle("");
                ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_24px);
            }

            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24px);
            toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        }

        private void onBackButtonPressed() {
            getView().setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        getActivity().getSupportFragmentManager().popBackStackImmediate();
                        return true;
                    }
                    return false;
                }
            });
        }

        private void inits(View view) {
            scrollViewPorfile = view.findViewById(R.id.scrollview_profile);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                scrollViewPorfile.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                    @Override
                    public void onScrollChange(View view, int x, int y, int oldx, int oldy) {

//                    System.out.println("--x: " + x);
//                    System.out.println("--y: " + y);
//                    System.out.println("--oldx: " + oldx);
//                    System.out.println("--oldy: " + oldy);

                        if (y > 400) {
//                            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            toolbar.setBackground(getResources().getDrawable(R.drawable.lay_round_toolbar));
                            toolbar.setTitle("Driver Name");
                            toolbar.setSubtitle("Email");
                            toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryLighter));
                            toolbar.setSubtitleTextColor(getResources().getColor(R.color.colorPrimaryLighter));
                        }

                        if (y < 400) {
                            toolbar.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                            toolbar.setTitle("");
                            toolbar.setSubtitle("");
                        }
                    }
                });
            }
        }
    }

    public static class NotificationsFragment extends Fragment {

        private Toolbar toolbar;

        RecyclerView recyclerNotif;
        NotificationsAdapter notifAdapter;
        RecyclerView.LayoutManager layoutManager;
        ArrayList<NotificationItem> notifList;

        private SwipeRefreshLayout swipeRefreshLayout;
        private TextView noFeedText;
        ProgressDialog dialog;

        public NotificationsFragment() {
        }

        @Override
        public void onAttach(@NonNull Context context) {
            super.onAttach(context);
        }

        @Override
        public void onAttachFragment(@NonNull Fragment childFragment) {
            super.onAttachFragment(childFragment);
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
            // Prepare data, containers n adapters
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_notifications, container, false);
            initToolBar(view);

            dialog = new ProgressDialog(getActivity());
            recyclerNotif = view.findViewById(R.id.recycler_notifications);
            noFeedText = view.findViewById(R.id.nothing_text);
            swipeRefreshLayout = view.findViewById(R.id.swipe_layout);

            initResources();
            swipeRefreshLayout.setOnRefreshListener(this::initResources);
            return view;
        }

        private void initResources() {
            if (hasInternet(getActivity())) {
                setUpRecyclerView();
//            api call();
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            } else {
                Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();
            }
        }

        private void setUpRecyclerView() {
            notifList = new ArrayList<>();
            notifList.add(new NotificationItem("https://cdn.pixabay.com/photo/2019/08/02/14/20/eagle-4379798_960_720.jpg", "Shiddhant Mishra", "Hello. Please Pay your dues before this date to avoid fines.", "12 Jan, 2039"));
            notifList.add(new NotificationItem("https://cdn.pixabay.com/photo/2019/08/02/14/20/eagle-4379798_960_720.jpg", "Shiddhant Mishra", "Hello. Please Pay your dues before this date to avoid fines.", "12 Jan, 2039"));
            notifList.add(new NotificationItem("https://cdn.pixabay.com/photo/2019/08/02/14/20/eagle-4379798_960_720.jpg", "Shiddhant Mishra", "Hello. Please Pay your dues before this date to avoid fines.", "12 Jan, 2039"));
            notifList.add(new NotificationItem("https://cdn.pixabay.com/photo/2019/08/02/14/20/eagle-4379798_960_720.jpg", "Shiddhant Mishra", "Hello. Please Pay your dues before this date to avoid fines.", "12 Jan, 2039"));
            notifList.add(new NotificationItem("https://cdn.pixabay.com/photo/2019/08/02/14/20/eagle-4379798_960_720.jpg", "Shiddhant Mishra", "Hello. Please Pay your dues before this date to avoid fines.", "12 Jan, 2039"));
            notifList.add(new NotificationItem("https://cdn.pixabay.com/photo/2019/08/02/14/20/eagle-4379798_960_720.jpg", "Shiddhant Mishra", "Hello. Please Pay your dues before this date to avoid fines.", "12 Jan, 2039"));
            notifList.add(new NotificationItem("https://cdn.pixabay.com/photo/2019/08/02/14/20/eagle-4379798_960_720.jpg", "Shiddhant Mishra", "Hello. Please Pay your dues before this date to avoid fines.", "12 Jan, 2039"));
            notifList.add(new NotificationItem("https://cdn.pixabay.com/photo/2019/08/02/14/20/eagle-4379798_960_720.jpg", "Shiddhant Mishra", "Hello. Please Pay your dues before this date to avoid fines.", "12 Jan, 2039"));
            notifList.add(new NotificationItem("https://cdn.pixabay.com/photo/2019/08/02/14/20/eagle-4379798_960_720.jpg", "Shiddhant Mishra", "Hello. Please Pay your dues before this date to avoid fines.", "12 Jan, 2039"));
            notifList.add(new NotificationItem("https://cdn.pixabay.com/photo/2019/08/02/14/20/eagle-4379798_960_720.jpg", "Shiddhant Mishra", "Hello. Please Pay your dues before this date to avoid fines.", "12 Jan, 2039"));
            notifList.add(new NotificationItem("https://cdn.pixabay.com/photo/2019/08/02/14/20/eagle-4379798_960_720.jpg", "Shiddhant Mishra", "Hello. Please Pay your dues before this date to avoid fines.", "12 Jan, 2039"));
            notifList.add(new NotificationItem("https://cdn.pixabay.com/photo/2019/08/02/14/20/eagle-4379798_960_720.jpg", "Shiddhant Mishra", "Hello. Please Pay your dues before this date to avoid fines.", "12 Jan, 2039"));
            notifList.add(new NotificationItem("https://cdn.pixabay.com/photo/2019/08/02/14/20/eagle-4379798_960_720.jpg", "Shiddhant Mishra", "Hello. Please Pay your dues before this date to avoid fines.", "12 Jan, 2039"));
            notifList.add(new NotificationItem("https://cdn.pixabay.com/photo/2019/08/02/14/20/eagle-4379798_960_720.jpg", "Shiddhant Mishra", "Hello. Please Pay your dues before this date to avoid fines.", "12 Jan, 2039"));
            notifList.add(new NotificationItem("https://cdn.pixabay.com/photo/2019/08/02/14/20/eagle-4379798_960_720.jpg", "Shiddhant Mishra", "Hello. Please Pay your dues before this date to avoid fines.", "12 Jan, 2039"));
            notifList.add(new NotificationItem("https://cdn.pixabay.com/photo/2019/08/02/14/20/eagle-4379798_960_720.jpg", "Shiddhant Mishra", "Hello. Please Pay your dues before this date to avoid fines.", "12 Jan, 2039"));
            notifList.add(new NotificationItem("https://cdn.pixabay.com/photo/2019/08/02/14/20/eagle-4379798_960_720.jpg", "Shiddhant Mishra", "Hello. Please Pay your dues before this date to avoid fines.", "12 Jan, 2039"));
            notifList.add(new NotificationItem("https://cdn.pixabay.com/photo/2019/08/02/14/20/eagle-4379798_960_720.jpg", "Shiddhant Mishra", "Hello. Please Pay your dues before this date to avoid fines.", "12 Jan, 2039"));
            notifList.add(new NotificationItem("https://cdn.pixabay.com/photo/2019/08/02/14/20/eagle-4379798_960_720.jpg", "Shiddhant Mishra", "Hello. Please Pay your dues before this date to avoid fines.", "12 Jan, 2039"));
            notifList.add(new NotificationItem("https://cdn.pixabay.com/photo/2019/08/02/14/20/eagle-4379798_960_720.jpg", "Shiddhant Mishra", "Hello. Please Pay your dues before this date to avoid fines.", "12 Jan, 2039"));


            notifAdapter = new NotificationsAdapter(notifList, getActivity());
            recyclerNotif.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerNotif.setAdapter(notifAdapter);
        }

        private void initToolBar(View view) {
            toolbar = view.findViewById(R.id.toolbar);
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            if (((AppCompatActivity) getActivity()) != null) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                ((AppCompatActivity) getActivity()).setTitle("Notifications");
                ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_24px);
            }
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24px);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().onBackPressed();
                }
            });
        }
    }


}
