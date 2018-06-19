import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utility {
    public static final int MILLISECONDS_PER_SECOND = 1000;
    public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
    public static final int FAST_CEILING_IN_SECONDS = 1;
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    public static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS = MILLISECONDS_PER_SECOND * FAST_CEILING_IN_SECONDS;
    public static final String DISPLAY_MESSAGE_ACTION = "pushnotifications.DISPLAY_MESSAGE";
    public static final String EXTRA_MESSAGE = "MESSAGE";
    private static final String TAG = "Agilies Lib";
    public static String INOTA = "Internet not available";
    public static String TIMEOUT = "Network / Server error";
    public static DateFormat dateFormatMMDDYYYY = new SimpleDateFormat("dd MMM,yyyy hh:mm aa", Locale.getDefault());
    public static double distance = 0.0;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;


    public static void showTost(final Context context, final String message) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MyApplication.getInstance(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void showTost(final String message) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MyApplication.getInstance(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    public static boolean isValidEmail(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void goNext(Activity mActivity) {
        mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public static void goPrevious(Activity mActivity) {
        mActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public static String getToken() {
        if (SharedPreferenceUtil.contains(Constant.DEVICE_TOKEN)
                && SharedPreferenceUtil.getString(Constant.DEVICE_TOKEN, null) != null) {
            Log.e("~ModelToken", "" + SharedPreferenceUtil.getString(Constant.DEVICE_TOKEN, ""));
            return SharedPreferenceUtil.getString(Constant.DEVICE_TOKEN, "Not Found");
        }
        return "";
    }

    public static void showProgress(Context mActivity) {
        dialog = new AppCompatDialog(mActivity);
        dialog.setContentView(R.layout.layout_progress);
        dialog.show();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progressBar);
            if (progressBar != null) {
                progressBar.getIndeterminateDrawable().setColorFilter(
                        mActivity.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                dialog.getWindow().setStatusBarColor(mActivity.getResources().getColor(R.color.colorPrimary));
            }
        }
    }

    public static AppCompatDialog dialog;

    public static void hideProgess() {
        if (dialog != null)
            dialog.dismiss();
    }



    public static void doLog(Object str) {
        Log.e("Pollia::", "::::" + str);

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Activity context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if ((ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) &&
                    (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.CAMERA)
                        || ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                        ) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Need Multiple Permissions");
                    builder.setMessage("This app needs Camera and External Storage permissions.");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }

    }

    public static String dateConvertUTC(String StringDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = df.parse(StringDate);
            df.setTimeZone(TimeZone.getDefault());
            return df.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void reduceMarginsInTabs(TabLayout tabLayout, int marginOffset) {
        View tabStrip = tabLayout.getChildAt(0);
        if (tabStrip instanceof ViewGroup) {
            ViewGroup tabStripGroup = (ViewGroup) tabStrip;
            for (int i = 0; i < ((ViewGroup) tabStrip).getChildCount(); i++) {
                View tabView = tabStripGroup.getChildAt(i);
                if (tabView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                    ((ViewGroup.MarginLayoutParams) tabView.getLayoutParams()).leftMargin = marginOffset;
                    ((ViewGroup.MarginLayoutParams) tabView.getLayoutParams()).rightMargin = marginOffset;
                }
            }
            tabLayout.requestLayout();
        }
    }

    public static DisplayMetrics getDeviceMetrics(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(metrics);
        return metrics;
    }

    public static Bitmap getScreenShot(View view) {
        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public static boolean isNotNullOrEmpty(String string) {
        return string != null && string.length() > 0;
    }

    public static String getCertificateSHA1Fingerprint(Context mContext) {
        PackageManager pm = mContext.getPackageManager();
        String packageName = mContext.getPackageName();
        int flags = PackageManager.GET_SIGNATURES;
        PackageInfo packageInfo = null;
        try {
            packageInfo = pm.getPackageInfo(packageName, flags);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Signature[] signatures = packageInfo.signatures;
        byte[] cert = signatures[0].toByteArray();
        InputStream input = new ByteArrayInputStream(cert);
        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X509");
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        X509Certificate c = null;
        try {
            c = (X509Certificate) cf.generateCertificate(input);
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        String hexString = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(c.getEncoded());
            hexString = byte2HexFormatted(publicKey);
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        }
        return hexString;
    }

    public static String byte2HexFormatted(byte[] arr) {
        StringBuilder str = new StringBuilder(arr.length * 2);
        for (int i = 0; i < arr.length; i++) {
            String h = Integer.toHexString(arr[i]);
            int l = h.length();
            if (l == 1) h = "0" + h;
            if (l > 2) h = h.substring(l - 2, l);
            str.append(h.toUpperCase());
            if (i < (arr.length - 1)) str.append(':');
        }
        return str.toString();
    }

    public static int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    public static void showAlert(Activity mActivity, String mTitle,
                                 String mMessage, boolean isCancelable,
                                 String posButtonTitle, String negButtonTitle,
                                 DialogInterface.OnClickListener posButtonClickListener,
                                 DialogInterface.OnClickListener negButtonClickListener) {
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(
                mActivity);
        alertDialogBuilder.setTitle(mTitle);
        alertDialogBuilder.setCancelable(isCancelable);
        alertDialogBuilder.setMessage(mMessage);
        if (posButtonTitle != null) {
            alertDialogBuilder.setPositiveButton(posButtonTitle, posButtonClickListener);
        }
        if (negButtonTitle != null) {
            alertDialogBuilder.setNegativeButton(negButtonTitle, negButtonClickListener);
        }
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static void hideActionBar(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // activity.getWindow().setFlags(
        // WindowManager.LayoutParams.FLAG_FULLSCREEN,
        // WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static void log(String message) {
        if (message != null) {
            Log.e("TAG", message);
        } else {
            Log.e("MESSAGE", "null");
        }
    }

    public static void fullScreenDecorate(Activity mActivity, View... containerPad1) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window w = mActivity.getWindow();
            w.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            w.setStatusBarColor(Color.parseColor("#00000000"));
            int height = getStatusbarHeight(mActivity);

            for (View containerPad :
                    containerPad1) {
                containerPad.setPadding(containerPad.getPaddingLeft(), containerPad.getPaddingTop() + height, containerPad.getPaddingRight(), containerPad.getPaddingBottom());
            }
        }
    }

    public static void fullScreenDecorate(Activity mActivity, List<View> containerForTopPadding, List<View> containerForTopMargin) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window w = mActivity.getWindow();
            w.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            w.setStatusBarColor(Color.parseColor("#00000000"));
            for (View container :
                    containerForTopPadding) {
                container.setPadding(container.getPaddingLeft(), container.getPaddingTop() + getStatusbarHeight(mActivity), container.getPaddingRight(), container.getPaddingBottom());
            }

            for (View container : containerForTopMargin) {
                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) container.getLayoutParams();
                p.topMargin += getStatusbarHeight(mActivity);
                container.setLayoutParams(p);
            }

        }
    }

    public static void fullScreenDecorate(Activity mActivity, View container) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window w = mActivity.getWindow();
            w.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            w.setStatusBarColor(Color.parseColor("#00000000"));
//            container.setPadding(container.getPaddingLeft(), container.getPaddingTop() + getStatusbarHeight(mActivity), container.getPaddingRight(), container.getPaddingBottom());
        }
    }

    public static int getStatusbarHeight(Activity mActivity) {
        int result = 0;
        int resourceId = mActivity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = mActivity.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static void hideKeyBoardFromView(Context mContext) {
        InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = ((Activity) mContext).getCurrentFocus();
        if (view == null) {
            view = new View(mContext);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showSnackBar(final View view, final String message) {
        Snackbar.make(view, String.valueOf(message), Snackbar.LENGTH_LONG).show();
    }

    public static String getTimezoneName() {
        TimeZone tz = TimeZone.getDefault();
        return tz.getDisplayName();

    }

    public static String getTimezoneId() {
        TimeZone tz = TimeZone.getDefault();
        return tz.getID();
    }

    public static boolean checkIsLollipopOrHigher() {
        return Build.VERSION.SDK_INT >= 21;
    }

    public static boolean checkIsMarshMallow23OrHigher() {
        return Build.VERSION.SDK_INT >= 23;
    }

    public static boolean checkIsKitKatOrHigher() {
        return Build.VERSION.SDK_INT >= 19;
    }

    private static boolean isOnline(Activity mActivity) {
        ConnectivityManager cm = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (cm != null) {
            netInfo = cm.getActiveNetworkInfo();
        }
        return netInfo != null && netInfo.isConnected();
    }

    public static boolean isOnline(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (cm != null) {
            netInfo = cm.getActiveNetworkInfo();
        }
        return netInfo != null && netInfo.isConnected();
    }

    public static void createDirectoryAndSaveFileCerti(Bitmap imageToSave, String fileName) {
        File file = new File(fileName);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap rotateBitmap(Bitmap b, float degrees) { // Rotate Image
        Matrix m = new Matrix();
        if (degrees != 0) {
            // clockwise
            m.postRotate(degrees, (float) b.getWidth() / 2,
                    (float) b.getHeight() / 2);
        }
        try {
            Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
                    b.getHeight(), m, true);
            if (b != b2) {
                b.recycle();
                b = b2;
            }
        } catch (OutOfMemoryError ex) {
            // We have no memory to rotate. Return the original bitmap.
        }
        return b;
    }

    public static Intent nextIntent(Context startIntent, Class<?> endIntent) {
        Intent intent = new Intent(startIntent, endIntent);
        return intent;
    }

    public static boolean isContainEmail(String argEditText) {
        try {
            Pattern pattern = Pattern
                    .compile("\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
            Matcher matcher = pattern.matcher(argEditText);
            return matcher.matches();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int getRendoNo() {
        Random r = new Random();
        int i1 = r.nextInt(1000 - 65) + 65;
        return i1;
    }

    public static String convertDateFormat(String dateString,
                                           DateFormat initDateFormat, DateFormat endDateFormat) {
        Date date = null;
        try {
            date = initDateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String format;
        if (date != null) {
            format = endDateFormat.format(date);
        } else {
            format = "";
        }

        return format;
    }

    public static Date getCurrentDate() {
        Date currentDate = null;
        try {
            currentDate = Utility.dateFormatMMDDYYYY
                    .parse(Utility.dateFormatMMDDYYYY.format(Calendar
                            .getInstance().getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return currentDate;
    }

    public static String getDateFromMILI(long milliSeconds,
                                         DateFormat dateFormat) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return dateFormat.format(calendar.getTime());
    }

    public static String milisecondsToHHmm(long millis) {
        long seconds = millis / 1000;
        @SuppressWarnings("unused")
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        long h = (seconds / (60 * 60)) % 24;
        Utility.log("h " + h + " m " + m);
        return h + ":" + m;
    }

    public static Date getDateFromString(String dateString) {
        Date convertedDate = new Date();
        try {
            convertedDate = Utility.dateFormatMMDDYYYY.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }

    public static void hideSoftKeyboard(Activity mActivity) {

        InputMethodManager inputMethodManager = (InputMethodManager) mActivity
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = mActivity.getCurrentFocus();
        if (view == null) {
            view = new View(mActivity);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);


    }

    public static void showNetworkAlert(String mTitle, String mMessage) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MyApplication.getInstance());
        alertDialogBuilder.setTitle(mTitle);
        alertDialogBuilder.setMessage(mMessage).setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }

    public static ProgressDialog initProgress(Object mActivity) {

        ProgressDialog mpDialog = null;
        if (mActivity instanceof Activity) {
            mpDialog = new ProgressDialog((Activity) mActivity, android.R.style.Theme_DeviceDefault_Light_Dialog);
        } else if (mActivity instanceof Context) {
            mpDialog = new ProgressDialog((Context) mActivity, android.R.style.Theme_DeviceDefault_Light_Dialog);
        }
        mpDialog.setMessage("Please wait a moment.");
        mpDialog.setCanceledOnTouchOutside(false);
        mpDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        return mpDialog;
    }

    public static void dialogShow(final ProgressDialog mpDialog) {
        if (mpDialog != null && !mpDialog.isShowing()) {
            try {
                mpDialog.show();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    public static void dialogDismiss(final ProgressDialog mpDialog) {
        if (mpDialog != null && mpDialog.isShowing()) {
            try {
                mpDialog.dismiss();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    public static void showNetworkAlert(final Activity mActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setCancelable(false);
        builder.setTitle("Network Error");
        builder.setMessage(INOTA);
        builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (Utility.isOnline(mActivity)) {

                } else {
                    showNetworkAlert(mActivity);
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public static boolean checkLocationPermissionEnabled(Activity mActivity) {
        if (ContextCompat.checkSelfPermission(mActivity,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    public static String dateFormatChange(String inputPattern, String outputPattern, String date) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date dates = null;
        String str = null;
        try {
            dates = inputFormat.parse(date);
            str = outputFormat.format(dates);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }



    public static boolean isProgressVisible() {
        return dialog != null && dialog.isShowing();
    }

    public static boolean isLocationEnabled() {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(MyApplication.getInstance().getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(MyApplication.getInstance().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }


 /*   public static void startActivity(Activity mActivity, Intent i) {
        mActivity.startActivity(i);
        goNext(mActivity);
    }*/


 /*   public static void showNetworkError() {
        showTost(INOTA);
    }

    public static void showTimeOutError(int statuscode) {
        showTost(TIMEOUT + " " + statuscode);
    }*/


    /*public static void showErrorSnackBar(final String message) {
        Toast.makeText(App.getInstance(), message, Toast.LENGTH_SHORT).show();
    }*/

   /* public static void showProgess(Context mActivity) {
        dialog = new AppCompatDialog(mActivity);
        dialog.setContentView(R.layout.layout_progress);
        dialog.show();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

//            ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progressBar);
//            if (progressBar != null) {
//                progressBar.getIndeterminateDrawable().setColorFilter(
//                        mActivity.getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY);
//            }

            AVLoadingIndicatorView indicator = (AVLoadingIndicatorView) dialog.findViewById(R.id.indicator);
            if (indicator != null) {
                indicator.setIndicator("TriangleSkewSpinIndicator");
            }


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                dialog.getWindow().setStatusBarColor(mActivity.getResources().getColor(R.color.white));
            }
        }
    }*/


   /* public static void showProgess(Activity mActivity, boolean cancelable) {
        dialog = new AppCompatDialog(mActivity, android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_progress);
        dialog.setCancelable(cancelable);
        dialog.show();
    }*/



    /*public static void facebookLogout(final Activity mActivity) {
        if (AccessToken.getCurrentAccessToken() == null) {
            Logout(mActivity);
            return;
        }
        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                LoginManager.getInstance().logOut();
                AccessToken.refreshCurrentAccessTokenAsync();
                AccessToken.setCurrentAccessToken(null);
                Logout(mActivity);
            }
        }).executeAsync();

    }*/

    /*private static void Logout(Activity mActivity) {
        String token = K.getDeviceToken();
        SharedPreferenceUtil.clear();
        SharedPreferenceUtil.putValue(K.token, token);

        Intent inte = new Intent(mActivity, SignInActivity.class);
        inte.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        mActivity.startActivity(inte);
        Utility.goPrevious(mActivity);
    }*/
}
