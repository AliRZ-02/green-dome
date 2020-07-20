package com.AliRZ.goldendome;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.location.LocationCallback;
import java.time.Year;
import java.util.Calendar;
import java.util.TimeZone;

@RequiresApi(api = Build.VERSION_CODES.O)
public class SalahClockActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;

    static TimeZone timezone = TimeZone.getDefault();
    static Calendar userCalendar = Calendar.getInstance(timezone);
    final static int  dayOfYear = userCalendar.get(Calendar.DAY_OF_YEAR);
    final static int currentYear = Year.now().getValue();
    final static int yearLength = Year.of(currentYear).length();
    static int userTimezoneOffset = timezone.getOffset(userCalendar.getTimeInMillis()) / 3600000;
    final static float equationOfTimePeriodRadians= 6.283F;
    final static double daysPerRadian = yearLength / equationOfTimePeriodRadians;
    final static double currentRadians = dayOfYear / daysPerRadian;
    static double equationOfTimeFunction = (-7.655 *(Math.sin(currentRadians))) + (9.873 * (Math.sin((2*currentRadians)+3.588)));
    static double equationOfTimeHour = equationOfTimeFunction/60; // Divide the Value given by the equation of time function by 60, to get hours of offset
    static double horizonAngle;
    static double timeDifference=0;
    static double declinationAngle;
    static int sunTimingCounter;

    public static final int DEFAULT_UPDATE_INTERVAL = 30;
    public static final int FAST_UPDATE_INTERVAL = 5;
    public static final int LOCATION_REQUEST_CODE = 100;
    TextView tv_fajrCalculated, tv_sunriseCalculated,tv_zuhrCalculated,tv_sunsetCalculated,tv_maghribCalculated;
    Button button_adhan, button_back, button_manual;
    ImageButton imageButton_adhan, imageButton_back, imageButton_manual;

    FusedLocationProviderClient fusedLocationProviderClient;

    LocationRequest locationRequest;

    LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salah_clock);

        tv_fajrCalculated = findViewById(R.id.fajr_time_response);
        tv_sunriseCalculated = findViewById(R.id.sunrise_time_response);
        tv_zuhrCalculated = findViewById(R.id.zuhr_time_response);
        tv_sunsetCalculated = findViewById(R.id.sunset_time_response);
        tv_maghribCalculated = findViewById(R.id.maghrib_time_response);

        button_manual = findViewById(R.id.manual_button);
        button_adhan = findViewById(R.id.adhan_button);
        button_back = findViewById(R.id.back_button);
        imageButton_manual = findViewById(R.id.manual_image_button);
        imageButton_adhan = findViewById(R.id.adhan_image_button);
        imageButton_back = findViewById(R.id.back_image_button);

        locationRequest = new LocationRequest();
        locationRequest.setInterval(DEFAULT_UPDATE_INTERVAL * 1000);
        locationRequest.setFastestInterval(FAST_UPDATE_INTERVAL * 1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        button_manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openComingSoon();
            }
        });

        imageButton_manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openComingSoon();
            }
        });

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

        imageButton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

        locationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult){
                super.onLocationResult(locationResult);

                Location location = locationResult.getLastLocation();
                try {
                    localSalahClock(location);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        updateGPS();
    } // End of OnCreate();

    public void playAdhan(View v){
        if (mediaPlayer == null){
            mediaPlayer = MediaPlayer.create(this, R.raw.adhan);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    stopAdhan();
                }
            });
        }
        mediaPlayer.start();
    }

    public void pauseAdhan(View v) {
        if (mediaPlayer != null){
            mediaPlayer.pause();
        }
    }

    private void stopAdhan (){
        if (mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
            Toast.makeText(this, "Adhan has been stopped", Toast.LENGTH_SHORT).show();
        }
    }

    private void openComingSoon() {
            Toast toast = Toast.makeText(this, "Coming Soon!", Toast.LENGTH_SHORT);
            toast.show();
    }

    private void goBack() {
        if (mediaPlayer != null){
            stopAdhan();
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case LOCATION_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    updateGPS();
                }else{
                    Toast.makeText(this, "Location permissions are required", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void updateGPS() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    try {
                        localSalahClock(location);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void localSalahClock (Location location) throws Exception{
        final double userLatitude = location.getLatitude();
        final double userLongitude = location.getLongitude();

        // Salah timings are based on the position of the sun, which follow predictable functions, which can be used to determine the timings for any day
        declinationAngle = Declination(yearLength, dayOfYear);

        // Zuhr Time must be calculated here as it is called upon by several different Salah times
        // Initializing Zuhr Calculation Variables
        double zuhrTimeTotal = (12 + userTimezoneOffset - (userLongitude / 15) - equationOfTimeHour);
        double zuhrTimeHour = Math.floor(zuhrTimeTotal);
        double zuhrTimeMinute = (zuhrTimeTotal - zuhrTimeHour) *60;
        zuhrTimeMinute = Math.ceil(zuhrTimeMinute);
        int zuhrTimeMinuteInteger = (int) Math.round(zuhrTimeMinute);
        int zuhrTimeHourInteger = (int) Math.round(zuhrTimeHour);
        String zuhrTimeConfirmation = "am";
        String zuhrTimeMinuteString;

        // Fajr Calculations
        horizonAngle = Math.toRadians(16);
        fajrClock (horizonAngle, timeDifference, userLatitude, declinationAngle, zuhrTimeHourInteger, zuhrTimeMinuteInteger);

        // Sunrise Calculations
        horizonAngle = Math.toRadians(0.833333);
        sunTimingCounter = 0;
        riseAndSetClock(sunTimingCounter,horizonAngle, timeDifference, userLatitude, declinationAngle, zuhrTimeHourInteger, zuhrTimeMinuteInteger);

        // Zuhr Output
        if (zuhrTimeHourInteger > 12){
            zuhrTimeHourInteger = zuhrTimeHourInteger-12;
            zuhrTimeConfirmation = "pm";
        }

        if (zuhrTimeMinuteInteger <=9){
            zuhrTimeMinuteString = "0"+zuhrTimeMinuteInteger;
        }else{
            zuhrTimeMinuteString = String.valueOf(zuhrTimeMinuteInteger);
        }
        String zuhr = zuhrTimeHourInteger + " : " + zuhrTimeMinuteString + " " + zuhrTimeConfirmation;
        tv_zuhrCalculated.setText(zuhr);

        if (zuhrTimeConfirmation == "pm"){
            zuhrTimeHourInteger = zuhrTimeHourInteger+12;
        }

        // Sunset Calculations
        horizonAngle = Math.toRadians(0.833333);
        sunTimingCounter = 1;
        riseAndSetClock(sunTimingCounter,horizonAngle, timeDifference, userLatitude, declinationAngle, zuhrTimeHourInteger, zuhrTimeMinuteInteger);

        // Maghrib Calculator
        maghribCalculator(horizonAngle, timeDifference, userLatitude, declinationAngle, zuhrTimeHourInteger, zuhrTimeMinuteInteger);
    }

    public void riseAndSetClock (int sunTimingCounter, double horizonAngle, double timeDifference, double userLatitude, double declinationAngle, int zuhrTimeHourInteger, int zuhrTimeMinuteInteger) {
        timeDifference = timeDifferenceMethod(horizonAngle, declinationAngle, userLatitude);
        double timeDifferenceTotal = Math.toDegrees(timeDifference);
        double timeDifferenceHour = Math.floor(timeDifferenceTotal);
        double timeDifferenceMinute = (timeDifferenceTotal - timeDifferenceHour) * 60;
        timeDifferenceMinute = Math.ceil (timeDifferenceMinute);
        int timeDifferenceHourInteger = (int) Math.round(timeDifferenceHour);
        int timeDifferenceMinuteInteger = (int) Math.round(timeDifferenceMinute);
        int sunriseHour = (zuhrTimeHourInteger - timeDifferenceHourInteger);
        int sunriseMin = (zuhrTimeMinuteInteger - timeDifferenceMinuteInteger);
        int sunsetHour = (zuhrTimeHourInteger + timeDifferenceHourInteger);
        int sunsetMin = (zuhrTimeMinuteInteger + timeDifferenceMinuteInteger);
        String sunriseMinString ="null";
        String sunsetMinString = "null";
        if (sunriseMin < 0){
            sunriseHour = sunriseHour - 1;
            sunriseMin = sunriseMin + 60;
        }
        if (sunsetMin > 60){
            sunsetHour = sunsetHour +1;
            sunsetMin = sunsetMin - 60;
        }
        if (sunsetHour > 12){
            sunsetHour = sunsetHour - 12;
        }
        if (sunTimingCounter == 0){
            if (sunriseMin < 10){
                sunriseMinString = "0"+sunriseMin;
            }else{
                sunriseMinString = String.valueOf(sunriseMin);
            }
            tv_sunriseCalculated.setText(sunriseHour + " : " + sunriseMinString + " am");
        } else {
            if (sunsetMin < 10){
                sunsetMinString = "0"+sunsetMin;
            }else{
                sunsetMinString = String.valueOf(sunsetMin);
            }
            tv_sunsetCalculated.setText(sunsetHour + " : " + sunsetMinString + " pm");
        }
    }

    public void fajrClock (double horizonAngle, double timeDifference, double userLatitude, double declinationAngle, int zuhrTimeHourInteger, int zuhrTimeMinuteInteger) {
        timeDifference = timeDifferenceMethod(horizonAngle, declinationAngle, userLatitude);
        double timeDifferenceTotal = Math.toDegrees(timeDifference);
        double timeDifferenceHour = Math.floor(timeDifferenceTotal);
        double timeDifferenceMinute = (timeDifferenceTotal - timeDifferenceHour) * 60;
        timeDifferenceMinute = Math.ceil (timeDifferenceMinute);
        int timeDifferenceHourInteger = (int) Math.round(timeDifferenceHour);
        int timeDifferenceMinuteInteger = (int) Math.round(timeDifferenceMinute);
        int fajrHour = (zuhrTimeHourInteger - timeDifferenceHourInteger);
        int fajrMin = (zuhrTimeMinuteInteger - timeDifferenceMinuteInteger);
        String fajrMinString;

        if (fajrMin < 0){
            fajrHour = fajrHour - 1;
            fajrMin = fajrMin + 60;
        }
        if (fajrMin > 60){
            fajrHour = fajrHour +1;
            fajrMin = fajrMin - 60;
        }
        if ((fajrMin - 5) >= 0){
            System.out.println("The Imsak Time is : "+ fajrHour + " : " + (fajrMin - 5) + " am");
        }else{
            System.out.println("The Imsak Time is : "+ (fajrHour-1) + " : " + (fajrMin + 55) + " am");
        }

        if (fajrMin <= 9) {
            fajrMinString = "0"+fajrMin;
        }else{
            fajrMinString = String.valueOf(fajrMin);
        }
        tv_fajrCalculated.setText(fajrHour + " : " + fajrMinString + " am");
    }

    public void maghribCalculator (double horizonAngle, double timeDifference, double userLatitude, double declinationAngle, int zuhrTimeHourInteger, int zuhrTimeMinuteInteger) {
        horizonAngle = Math.toRadians(3);
        timeDifference = timeDifferenceMethod(horizonAngle, declinationAngle, userLatitude);
        double timeDifferenceTotal = Math.toDegrees(timeDifference);
        double timeDifferenceHour = Math.floor(timeDifferenceTotal);
        double timeDifferenceMinute = (timeDifferenceTotal - timeDifferenceHour) * 60;
        timeDifferenceMinute = Math.ceil (timeDifferenceMinute);
        int timeDifferenceHourInteger = (int) Math.round(timeDifferenceHour);
        int timeDifferenceMinuteInteger = (int) Math.round(timeDifferenceMinute);
        int maghribHour = (zuhrTimeHourInteger + timeDifferenceHourInteger);
        int maghribMin = (zuhrTimeMinuteInteger + timeDifferenceMinuteInteger);
        String maghribMinString;
        if (maghribMin < 0){
            maghribHour = maghribHour - 1;
            maghribMin = maghribMin + 60;
        }
        if (maghribMin > 60){
            maghribHour = maghribHour +1;
            maghribMin = maghribMin - 60;
        }
        if (maghribHour > 12) {
            maghribHour = maghribHour - 12;
        }

        if (maghribMin <= 9){
            maghribMinString = "0"+maghribMin;
        }else{
            maghribMinString = String.valueOf(maghribMin);
        }
        tv_maghribCalculated.setText(maghribHour + " : " + maghribMinString + " pm");
    }

    public double Declination (double yearLength, int dayOfYear){
        // The Declination Angle of the sun at any day can be calculated by determining the number of degrees that the earth has rotated since the last winter solstice
        // and multiplying the cosine of said value by the tilt of the Earth, at -23.45 degrees
        double earthTilt = Math.toRadians(-23.45); // Converts to radians as the Cosine function returns a Radian value
        double declinationPartB = Math.toRadians((360/yearLength) * (dayOfYear+10)); // Days of the year are converted into degrees and multiplied by the current day of the year to find the angle of the earth since the last winter solstice
        double declinationAngle = (earthTilt * Math.cos(declinationPartB));
        return declinationAngle;
    }

    public double timeDifferenceMethod (double horizonAngle, double declinationAngle, double userLatitude){
        double userLatitudeRadians = Math.toRadians(userLatitude);
        double timeDifference = (Math.acos (((-Math.sin(horizonAngle))-(Math.sin(userLatitudeRadians)*Math.sin(declinationAngle)))/ (Math.cos(userLatitudeRadians)*Math.cos(declinationAngle)))/15);
        return timeDifference;
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopAdhan();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopAdhan();
    }
}