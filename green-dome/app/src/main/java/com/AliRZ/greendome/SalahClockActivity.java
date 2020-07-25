package com.AliRZ.greendome;

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
import java.util.Calendar;
import java.util.TimeZone;

import com.AliRZ.greendome.DetermineSalahTime;

@RequiresApi(api = Build.VERSION_CODES.O)
public class SalahClockActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;

    static TimeZone timezone = TimeZone.getDefault();
    static Calendar userCalendar = Calendar.getInstance(timezone);

    public static final int DEFAULT_UPDATE_INTERVAL = 30;
    public static final int FAST_UPDATE_INTERVAL = 5;
    public static final int LOCATION_REQUEST_CODE = 100;

    TextView tv_fajrCalculated, tv_sunriseCalculated,tv_zuhrCalculated,tv_sunsetCalculated,tv_maghribCalculated;
    Button button_adhan, button_back, button_manual;
    ImageButton imageButton_adhan, imageButton_back, imageButton_manual;

    FusedLocationProviderClient fusedLocationProviderClient;

    LocationRequest locationRequest;

    LocationCallback locationCallback;

    DetermineSalahTime determineSalahTime = new DetermineSalahTime();

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

        button_manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openManualSalahClock();
            }
        });

        imageButton_manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openManualSalahClock();
            }
        });

        updateGPS();
    }

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

    private void openManualSalahClock() {
        if (mediaPlayer != null){
            stopAdhan();
        }
        Intent intent = new Intent(this, ManualSalahClockActivity.class);
        startActivity(intent);
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
    public void localSalahClock (Location location) {
        final double userLatitude = location.getLatitude();
        final double userLongitude = location.getLongitude();
        int userTimezoneOffset = timezone.getOffset(userCalendar.getTimeInMillis()) / 3600000;

        tv_fajrCalculated.setText(determineSalahTime.fajrClock(userLatitude,userLongitude, userTimezoneOffset, userCalendar));
        tv_sunriseCalculated.setText(determineSalahTime.sunriseClock(userLatitude,userLongitude, userTimezoneOffset, userCalendar));
        tv_zuhrCalculated.setText(determineSalahTime.zuhrClock(userLongitude, userTimezoneOffset, userCalendar));
        tv_sunsetCalculated.setText(determineSalahTime.sunsetClock(userLatitude,userLongitude, userTimezoneOffset, userCalendar));
        tv_maghribCalculated.setText(determineSalahTime.maghribCalculator(userLatitude,userLongitude, userTimezoneOffset, userCalendar));
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