package com.singularitycoder.yesbank;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.singularitycoder.yesbank.More.MoreAdapter;
import com.singularitycoder.yesbank.More.MoreItem;
import com.singularitycoder.yesbank.Notifications.NotificationItem;
import com.singularitycoder.yesbank.Notifications.NotificationsAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

        RecyclerView recyclerView;
        HorizontalAdapter horizontalAdapter;
        RecyclerView.LayoutManager layoutManager;
        ArrayList<HorizontalItem> list;

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
            recyclerView = view.findViewById(R.id.recycler_favourites);

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

            setUpRecyclerView();
            return view;
        }

        private void setUpRecyclerView() {
            list = new ArrayList<>();
            list.add(new HorizontalItem(R.drawable.ic_account, "Scan", R.color.colorPrimary, R.color.colorBlack, R.color.colorWhite));
            list.add(new HorizontalItem(R.drawable.ic_account, "Scan", R.color.colorPrimary, R.color.colorBlack, R.color.colorWhite));
            list.add(new HorizontalItem(R.drawable.ic_account, "Scan", R.color.colorPrimary, R.color.colorBlack, R.color.colorWhite));
            list.add(new HorizontalItem(R.drawable.ic_account, "Scan", R.color.colorPrimary, R.color.colorBlack, R.color.colorWhite));
            list.add(new HorizontalItem(R.drawable.ic_account, "Scan", R.color.colorPrimary, R.color.colorBlack, R.color.colorWhite));
            list.add(new HorizontalItem(R.drawable.ic_account, "Scan", R.color.colorPrimary, R.color.colorBlack, R.color.colorWhite));
            list.add(new HorizontalItem(R.drawable.ic_account, "Scan", R.color.colorPrimary, R.color.colorBlack, R.color.colorWhite));

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));
            horizontalAdapter = new HorizontalAdapter(list, getActivity());
            horizontalAdapter.setHasStableIds(true);
            recyclerView.setAdapter(horizontalAdapter);
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
        private Toolbar toolbar;

        RecyclerView recyclerView1;
        RecyclerView recyclerView2;
        RecyclerView recyclerView3;
        HorizontalAdapter horizontalAdapter;
        RecyclerView.LayoutManager layoutManager;
        ArrayList<HorizontalItem> list1;
        ArrayList<HorizontalItem> list2;
        ArrayList<HorizontalItem> list3;

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
            recyclerView1 = view.findViewById(R.id.recycler_pay_bills);
            recyclerView2 = view.findViewById(R.id.recycler_recharge);
            recyclerView3 = view.findViewById(R.id.recycler_travel);
            initToolBar(view);
            setUpRecyclerView1();
            setUpRecyclerView2();
            setUpRecyclerView3();
            return view;
        }

        private void setUpRecyclerView1() {
            list1 = new ArrayList<>();
            list1.add(new HorizontalItem(R.drawable.ic_qr_code, "Scan QR Code", R.color.colorPrimaryLightest, R.color.colorPrimaryLightest, R.color.colorPrimary));
            list1.add(new HorizontalItem(R.drawable.ic_attach_money_24px, "UPI Payment", R.color.colorPrimaryLightest, R.color.colorPrimaryLightest, R.color.colorPrimary));
            list1.add(new HorizontalItem(R.drawable.ic_money_24px, "Transfer Money", R.color.colorPrimaryLightest, R.color.colorPrimaryLightest, R.color.colorPrimary));
            list1.add(new HorizontalItem(R.drawable.ic_payment_24px, "Card Payment", R.color.colorPrimaryLightest, R.color.colorPrimaryLightest, R.color.colorPrimary));
            list1.add(new HorizontalItem(R.drawable.ic_account, "Scan", R.color.colorPrimaryLightest, R.color.colorPrimaryLightest, R.color.colorPrimary));
            list1.add(new HorizontalItem(R.drawable.ic_account, "Scan", R.color.colorPrimaryLightest, R.color.colorPrimaryLightest, R.color.colorPrimary));

            recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            horizontalAdapter = new HorizontalAdapter(list1, getActivity());
            horizontalAdapter.setHasStableIds(true);
            recyclerView1.setAdapter(horizontalAdapter);
        }

        private void setUpRecyclerView2() {
            list2 = new ArrayList<>();
            list2.add(new HorizontalItem(R.drawable.ic_mobile_friendly_24px, "Mobile Prepaid", R.color.colorPrimary, R.color.colorBlack, R.color.colorWhite));
            list2.add(new HorizontalItem(R.drawable.ic_settings_input_antenna_24px, "DTH", R.color.colorPrimary, R.color.colorBlack, R.color.colorWhite));
            list2.add(new HorizontalItem(R.drawable.ic_local_taxi_24px, "FasTag", R.color.colorPrimary, R.color.colorBlack, R.color.colorWhite));
            list2.add(new HorizontalItem(R.drawable.ic_tram_24px, "Metro", R.color.colorPrimary, R.color.colorBlack, R.color.colorWhite));
            list2.add(new HorizontalItem(R.drawable.ic_account, "Scan", R.color.colorPrimary, R.color.colorBlack, R.color.colorWhite));
            list2.add(new HorizontalItem(R.drawable.ic_account, "Scan", R.color.colorPrimary, R.color.colorBlack, R.color.colorWhite));
            list2.add(new HorizontalItem(R.drawable.ic_account, "Scan", R.color.colorPrimary, R.color.colorBlack, R.color.colorWhite));

            recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            horizontalAdapter = new HorizontalAdapter(list2, getActivity());
            horizontalAdapter.setHasStableIds(true);
            recyclerView2.setAdapter(horizontalAdapter);
        }

        private void setUpRecyclerView3() {
            list3 = new ArrayList<>();
            list3.add(new HorizontalItem(R.drawable.ic_flight_24px, "Flight Tickets", R.color.colorPrimary, R.color.colorBlack, R.color.colorWhite));
            list3.add(new HorizontalItem(R.drawable.ic_location_city_24px, "Hotels", R.color.colorPrimary, R.color.colorBlack, R.color.colorWhite));
            list3.add(new HorizontalItem(R.drawable.ic_train_24px, "Train Tickets", R.color.colorPrimary, R.color.colorBlack, R.color.colorWhite));
            list3.add(new HorizontalItem(R.drawable.ic_directions_bus_24px, "Bus Tickets", R.color.colorPrimary, R.color.colorBlack, R.color.colorWhite));
            list3.add(new HorizontalItem(R.drawable.ic_account, "Scan", R.color.colorPrimary, R.color.colorBlack, R.color.colorWhite));
            list3.add(new HorizontalItem(R.drawable.ic_account, "Scan", R.color.colorPrimary, R.color.colorBlack, R.color.colorWhite));
            list3.add(new HorizontalItem(R.drawable.ic_account, "Scan", R.color.colorPrimary, R.color.colorBlack, R.color.colorWhite));

            recyclerView3.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            horizontalAdapter = new HorizontalAdapter(list3, getActivity());
            horizontalAdapter.setHasStableIds(true);
            recyclerView3.setAdapter(horizontalAdapter);
        }

        private void initToolBar(View view) {
            toolbar = view.findViewById(R.id.toolbar);
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            if (((AppCompatActivity) getActivity()) != null) {
                // For back navigation button use this
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                ((AppCompatActivity) getActivity()).setTitle("Pay");
//            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
            }
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

        private Toolbar toolbar;

        RecyclerView recyclerMore;
        MoreAdapter moreAdapter;
        RecyclerView.LayoutManager layoutManager;
        ArrayList<MoreItem> moreList;

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
            initToolBar(view);

            recyclerMore = view.findViewById(R.id.recycler_more);

            initResources();
            return view;
        }

        private void initResources() {
            setUpRecyclerView();
        }

        private void setUpRecyclerView() {
            moreList = new ArrayList<>();
            moreList.add(new MoreItem("", "Bean Man", "Acc: 8349832823893000", "Cust ID: 82993828982"));
            moreList.add(new MoreItem("Banking Services", R.drawable.ic_account, R.color.colorBlack, R.color.colorPrimary));
            moreList.add(new MoreItem("Banking Tools", R.drawable.ic_emoji_objects_24px, R.color.colorBlack, R.color.colorPrimary));
            moreList.add(new MoreItem("Locate Us", R.drawable.ic_my_location_24px, R.color.colorBlack, R.color.colorPrimary));
            moreList.add(new MoreItem("Customer Support", R.drawable.ic_call_24px, R.color.colorBlack, R.color.colorPrimary));
            moreList.add(new MoreItem("Settings", R.drawable.ic_settings_black_24dp, R.color.colorBlack, R.color.colorPrimary));
            moreList.add(new MoreItem("Share", R.drawable.ic_share_24px, R.color.colorBlack, R.color.colorPrimary));
            moreList.add(new MoreItem("Logout", R.drawable.ic_power_settings_new_24px, R.color.colorRed, R.color.colorRed));

            recyclerMore.setLayoutManager(new LinearLayoutManager(getActivity()));
            moreAdapter = new MoreAdapter(moreList, getActivity());
            moreAdapter.setHasStableIds(true);
            moreAdapter.setOnItemClickListener((view, position, imageView) -> {
                if (position == 1) {
                    dialogCustomerSupport();
                }

                if (position == 2) {
                    shareAppLink(getResources().getDrawable(R.drawable.yesbanklogo), imageView, "Smart School", "Get the Smart School App from Playstore!");
                }

                if (position == 3) {

                }

                if (position == 4) {
                    AlertDialog.Builder builderLogOut = new AlertDialog.Builder(getActivity());
                    builderLogOut.setMessage("Do you want to Log Out?");

                    builderLogOut.setPositiveButton("Yes", (dialog, which) -> {
                        // Api
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        Objects.requireNonNull(getActivity()).finish();
                        dialog.dismiss();
                    });

                    builderLogOut.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

                    AlertDialog alertDialogLogOut = builderLogOut.create();
                    alertDialogLogOut.show();
                }
            });
            recyclerMore.setAdapter(moreAdapter);
        }

        private void dialogCustomerSupport() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Help Lines");
            String[] selectArray = {"Email", "Call", "SMS", "WhatsApp"};
            builder.setItems(selectArray, (dialog, which) -> {
                switch (which) {
                    case 0:
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "hithesh@immerpact.com", null));
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Login Help");
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello Operator");
                        getActivity().startActivity(Intent.createChooser(emailIntent, "Send email..."));
                        break;
                    case 1:
                        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "9535509155", null));
                        callIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                        getActivity().startActivity(callIntent);
                        break;
                    case 2:
                        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                        smsIntent.setType("vnd.android-dir/mms-sms");
                        smsIntent.putExtra("address", "9535509155");
                        smsIntent.putExtra("sms_body", "Login Issue: ");
                        smsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                        if (smsIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                            getActivity().startActivity(smsIntent);
                        }
                        break;
                    case 3:
                        PackageManager packageManager = getActivity().getApplicationContext().getPackageManager();
                        try {
                            // checks if such an app exists or not
                            packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                            String phone = "9535509155";
                            Uri uri = Uri.parse("smsto:" + phone);
                            Intent whatsAppIntent = new Intent(Intent.ACTION_SENDTO, uri);
                            whatsAppIntent.setPackage("com.whatsapp");
                            getActivity().startActivity(Intent.createChooser(whatsAppIntent, "Dummy Title"));
                        } catch (PackageManager.NameNotFoundException e) {
                            Toast.makeText(getActivity(), "WhatsApp not found. Install from playstore.", Toast.LENGTH_SHORT).show();
                            Uri uri = Uri.parse("market://details?id=com.whatsapp");
                            Intent openPlayStore = new Intent(Intent.ACTION_VIEW, uri);
                            openPlayStore.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                            getActivity().startActivity(openPlayStore);
                        }
                        break;
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        private void initToolBar(View view) {
            toolbar = view.findViewById(R.id.toolbar);
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            if (((AppCompatActivity) getActivity()) != null) {
                // For back navigation button use this
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                ((AppCompatActivity) getActivity()).setTitle("More...");
//            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
            }
        }

        private void shareAppLink(Drawable imageDrawable, ImageView imageView, String title, String subtitle) {
            if ((null) != imageDrawable) {
                Dexter.withActivity(getActivity())
                        .withPermissions(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                if (report.areAllPermissionsGranted()) {

                                    Glide.with(getActivity())
                                            .asBitmap()
                                            .load(imageDrawable)
                                            .into(new CustomTarget<Bitmap>() {
                                                @Override
                                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                    imageView.setImageBitmap(resource);
                                                }

                                                @Override
                                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                                }
                                            });

                                    Uri bmpUri = getLocalBitmapUri(imageView);
                                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                                    sharingIntent.setType("image/.*");
                                    sharingIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, title);
                                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, subtitle);
                                    sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                                    startActivity(Intent.createChooser(sharingIntent, "Share Image Using"));
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            } else {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_SUBJECT, title);
                share.putExtra(Intent.EXTRA_TEXT, subtitle);
//                share.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                startActivity(Intent.createChooser(share, "Share to"));
            }
        }

        private Uri getLocalBitmapUri(ImageView imageView) {
            // Extract Bitmap from ImageView drawable
            Drawable drawable = imageView.getDrawable();
            Bitmap bmp;
            if (drawable instanceof BitmapDrawable) {
                bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            } else {
                return null;
            }
            // Store image to default external storage directory
            Uri bmpUri = null;
            try {
                // Use methods on Context to access package-specific directories on external storage. This way, you don't need to request external read/write permission.
                File file = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
                FileOutputStream out = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.close();
                // Warning: This will fail for API >= 24, use a FileProvider as shown below instead.
                bmpUri = Uri.fromFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmpUri;
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
