package com.AliRZ.greendome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class QiblaFinder extends AppCompatActivity {

    FusedLocationProviderClient fusedLocationProviderClient;

    LocationRequest locationRequest;

    LocationCallback locationCallback;

    private ImageView iv_compass, iv_pointer;

    private TextView tv_qiblaAngle;

    private Button bt_back, bt_instructions;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetic_field;

    private float currentCompass;
    private float oldCompass =0;
    private float currentPointerCompass;
    private float oldPointerCompass =0;

    private float[] floatGravity = new float[3];
    private float[] floatgeoMagnetic = new float[3];
    private float[] floatOrientation = new float[3];
    private float[] floatRotationMatrix= new float[9];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qibla_finder);

        iv_compass = findViewById(R.id.compass_imageView);
        iv_pointer = findViewById(R.id.pointer_imageView);
        tv_qiblaAngle = findViewById(R.id.qiblaAngle);
        bt_back = findViewById(R.id.back_button);
        bt_instructions = findViewById(R.id.how_to_button);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetic_field = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        locationRequest = new LocationRequest();
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);

        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

        bt_instructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getInstructions();
            }
        });

        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
                getQiblaDirection(location);
            }
        };
        updateGPS();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case 1000:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    updateGPS();
                }else{
                    Toast.makeText(this, "Please Update Location Permissions", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void updateGPS() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    getQiblaDirection(location);
                }
            });
        } else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String [] {Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
            }
        }
    }

    private float getMagneticDeclination(GeomagneticField geomagneticField){
        float declination = geomagneticField.getDeclination();
        return declination;
    }

    private float getCompassDirection(SensorEvent sensorEvent) {
        floatGravity = sensorEvent.values;
        SensorManager.getRotationMatrix(floatRotationMatrix, null, floatGravity, floatgeoMagnetic);
        SensorManager.getOrientation(floatRotationMatrix, floatOrientation);
        currentCompass = (float) (-floatOrientation[0] * (180/Math.PI));
        return currentCompass;
    }

    private float getUnadjustedQiblaDirection (Location location){
        float qiblaDirection;
        float currentLatitude = (float) Math.toRadians(location.getLatitude());
        float currentLongitude = (float) Math.toRadians(location.getLongitude());
        float kaabaLatitude = (float) Math.toRadians(21.423333F);
        float kaabaLongitude = (float) Math.toRadians(39.823333F);
        float qiblaNumerator = (float) Math.sin(kaabaLongitude - currentLongitude);
        float qiblaDenominatorStart = (float) (Math.cos(currentLatitude) * Math.tan(kaabaLatitude));
        float qiblaDenominatorEnd = (float) (Math.sin(currentLatitude) * Math.cos(kaabaLongitude-currentLongitude));
        qiblaDirection = (float) Math.atan((qiblaNumerator)/(qiblaDenominatorStart-qiblaDenominatorEnd));
        qiblaDirection = (float) Math.toDegrees(qiblaDirection);
        return qiblaDirection;
    }

    private void getQiblaDirection(Location location) {
        GeomagneticField geomagneticField = new GeomagneticField((float) location.getLatitude(), (float) location.getLongitude(), (float) location.getAltitude(), System.currentTimeMillis());
        final float declination = getMagneticDeclination(geomagneticField);
        final float qibla = getUnadjustedQiblaDirection(location) - declination;
        tv_qiblaAngle.setText("Qibla Bearing: "+Math.round(qibla) +"°");
        SensorEventListener sensorEventListenerAccelerometer = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                currentCompass = getCompassDirection(sensorEvent);
                currentPointerCompass = currentCompass +(qibla/2);
                int compassDifferenceLoop = (int) Math.floor(currentCompass - oldCompass);
                if (compassDifferenceLoop >= 3 || compassDifferenceLoop <=-3){
                    RotateAnimation rotateAnimation = getRotateAnimation(oldCompass, currentCompass);
                    iv_compass.startAnimation(rotateAnimation);
                    RotateAnimation rotatePointerAnimation = getRotateAnimation(oldPointerCompass, currentPointerCompass);
                    iv_pointer.startAnimation(rotatePointerAnimation);
                    oldCompass = currentCompass;
                    oldPointerCompass = currentPointerCompass;
                }else{
                    iv_compass.setRotation(oldCompass);
                    iv_pointer.setRotation(oldPointerCompass);
                }
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
        SensorEventListener sensorEventListenerMagneticField = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                floatgeoMagnetic = sensorEvent.values;
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
        sensorManager.registerListener(sensorEventListenerAccelerometer, accelerometer, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(sensorEventListenerMagneticField, magnetic_field, SensorManager.SENSOR_DELAY_UI);
    }

    private RotateAnimation getRotateAnimation(float oldCompass, float currentCompass){
        RotateAnimation rotateAnimation = new RotateAnimation(oldCompass, currentCompass, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setDuration(300);
        return rotateAnimation;
    }

    private void goBack() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void getInstructions(){
        Intent intent = new Intent(this, QiblaInstructionsActivity.class);
        startActivity(intent);
    }

}