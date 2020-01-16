package com.singularitycoder.yesbank;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class HelperGeneral {
    private final static int FADE_DURATION = 550;
    private static final String TAG = HelperGeneral.class.getSimpleName();

    public static void progressDialog(Context context) {
        ProgressDialog dialog;
        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading...");
    }

    public static void dismissDialog(Context context) {
        ProgressDialog dialog;
        dialog = new ProgressDialog(context);
        dialog.dismiss();
    }

    public static boolean hasInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        return cm.getActiveNetworkInfo() != null;
    }

    public static boolean hasValidEmail(final String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";

        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);

        return matcher.matches();
    }

    public static boolean hasValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z]).{8,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }

    public static void showSnack(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

    public static void glideImage(Context context, String imgUrl, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.color.bg_gray)
                .error(R.mipmap.ic_launcher) //in case of any glide exception or not able to download then this image will be appear . if you won't mention this error() then nothing to worry placeHolder image would be remain as it is.
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC); //using to load into cache then second time it will load fast.

        Glide.with(context)
                .load(imgUrl)
                .apply(requestOptions)
                .into(imageView);
    }

    public static String getCurrentTime() {
        Date currentTime = Calendar.getInstance().getTime();
//        String formatTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String formattedTime = sdf.format(currentTime);
        return formattedTime;
    }

    public static boolean isThisTimeGreater(String currentTime, String EndTime) {
        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            Date time1 = sdf.parse(currentTime);
            Date time2 = sdf.parse(EndTime);
            if (time1 != null && time1.after(time2)) {
                System.out.println("time1 is after time2");
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c);
        return formattedDate;
    }

    public static boolean isThisDateGreater(String currentDate, String EndDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date1 = sdf.parse(currentDate);
            Date date2 = sdf.parse(EndDate);
            if (date1 != null && date1.after(date2)) {
                System.out.println("Date1 is after Date2");
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String convertDateFormat(String inputDate) {
        if (inputDate != null) {
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
            Date date = null;
            try {
                date = inputFormat.parse(inputDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return outputFormat.format(Objects.requireNonNull(date));
        } else {
            return "";
        }
    }


    public static void glideImage(Context context, String imgUrl, ImageView imageView, String empty1) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.color.bg_gray)
                .error(R.drawable.face1)  //in case of any glide exception or not able to download then this image will be appear . if you won't mention this error() then nothing to worry placeHolder image would be remain as it is.
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC); //using to load into cache then second time it will load fast.

        Glide.with(context).load(imgUrl)
                .apply(requestOptions)
                .into(imageView);
    }

    public static void glideSmallImageWithErrHandle(Context context, String imgUrl, ImageView imageView) {
//        RequestOptions requestOptions = new RequestOptions()
//                .placeholder(R.color.gray)
//                .error(R.drawable.ic_user) //in case of any glide exception or not able to download then this image will be appear . if you won't mention this error() then nothing to worry placeHolder image would be remain as it is.
//                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC); //using to load into cache then second time it will load fast.

        Glide.with(context)
                .load(imgUrl)
                .apply(
                        new RequestOptions()
                                .error(R.mipmap.ic_launcher)
                                .placeholder(R.color.bg_gray)
                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                .centerCrop()
                )
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        //on load failed
                        Toast.makeText(context, "Bad Image! Loading default!", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        //on load success
                        return false;
                    }
                })
                .transition(withCrossFade())
                .into(imageView);
    }

    // Glide Big with error handling
    public static void glideImageWithErrHandle(Context context, String imgUrl, ImageView imageView, String empty1) {
//        RequestOptions requestOptions = new RequestOptions()
//                .placeholder(R.color.gray)
//                .error(R.drawable.header) //in case of any glide exception or not able to download then this image will be appear . if you won't mention this error() then nothing to worry placeHolder image would be remain as it is.
//                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC); //using to load into cache then second time it will load fast.

        Glide.with(context)
                .load(imgUrl)
                .apply(
                        new RequestOptions()
                                .placeholder(R.color.bg_gray)
                                .error(R.mipmap.ic_launcher)
                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                .centerCrop()
                )
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        //on load failed
                        Toast.makeText(context, "Bad Image! Loading default!", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        //on load success
                        return false;
                    }
                })
                .transition(withCrossFade())
                .into(imageView);
    }


    public static void glideImage(Context context, ImageView imageView, String imgUrl) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.color.bg_gray)
                .error(R.mipmap.ic_launcher) //in case of any glide exception or not able to download then this image will be appear . if you won't mention this error() then nothing to worry placeHolder image would be remain as it is.
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC); //using to load into cache then second time it will load fast.

        Glide.with(context).load(imgUrl)
                .apply(requestOptions)
                .into(imageView);
    }

    public static void glideImageGroupIcon(Context context, String imgUrl, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.color.bg_gray)
                .error(R.mipmap.ic_launcher)
//                .error(R.color.colorPrimaryDark) //in case of any glide exception or not able to download then this image will be appear . if you won't mention this error() then nothing to worry placeHolder image would be remain as it is.
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC); //using to load into cache then second time it will load fast.

        Glide.with(context).load(imgUrl)
                .apply(requestOptions)
                .into(imageView);
    }

   /* public static void alertDialog(String message, String title){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Mail id not registered")
                .setTitle("Invalid email id");
        builder.setPositiveButton("Ok", (dialog, id) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
    }*/


    public static void alertDialog(Context mContext) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Atleast 1 attachment is required")
                .setTitle("");
        builder.setPositiveButton("Ok", (dialog, id) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void toast(Context context, String message, int length) {
        switch (length) {
            case 0:
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                break;
        }
    }

    public static void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }


    // Convert file to byte array
    public static void fileToByte(String filePath) {
        File file = new File(filePath);

        byte[] b = new byte[(int) file.length()];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            int inStream = fileInputStream.read(b);
            for (byte value : b) {
                System.out.print((char) value);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found.");
            e.printStackTrace();
        } catch (IOException e1) {
            System.out.println("Error Reading The File.");
            e1.printStackTrace();
        }
    }

    // Convert bytes to file
    public void byteToFile(String filePath) {

        String strFilePath = filePath;
        try {
            FileOutputStream fos = new FileOutputStream(strFilePath);
            String strContent = "Write File using Java ";

            fos.write(strContent.getBytes());
            fos.close();
        } catch (FileNotFoundException ex) {
            System.out.println("FileNotFoundException : " + ex);
        } catch (IOException ioe) {
            System.out.println("IOException : " + ioe);
        }

    }

//    public interface ESLThreadListener {
//        public List onBackground();
//        public void onPostExecute(List list);
//    }
//
//    public class ESLThread extends AsyncTask<Void, Void, List> {
//        private ESLThreadListener mListener;
//        public ESLThread() {
//            if (mListener != null) {
//                mListener.onBackground();
//            }
//        }
//
//        @Override
//        protected List doInBackground(Void... params) {
//            if (mListener != null) {
//                List list = mListener.onBackground();
//                return list;
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(List t) {
//            if (mListener != null) {
//                if (t != null) {
//                    mListener.onPostExecute(t);
//                }
//            }
//        }
//
//        public void setListener(ESLThreadListener mListener) {
//            this.mListener = mListener;
//        }
//    }
//
//    private void test() {
//        ESLThread eslThread = new ESLThread();
//        eslThread.setListener(new ESLThreadListener() {
//            @Override
//            public List onBackground() {
//                return null;
//            }
//
//            @Override
//            public void onPostExecute(List list) {
//
//            }
//        });
//    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.interrupted();
                }
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
//            TextView txt = (TextView) findViewById(R.id.output);
//            txt.setText("Executed"); // txt.setText(result);
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class LongBackOp extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return null;
        }

        public LongBackOp() {
            super();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

    }


}
