package com.AliRZ.greendome;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.time.Year;
import java.util.Calendar;
import java.util.Scanner;
import java.util.TimeZone;

import com.AliRZ.greendome.DetermineSalahTime;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ManualSalahClockActivity extends AppCompatActivity   {

    TextView tv_fajrCalculated, tv_sunriseCalculated,tv_zuhrCalculated,tv_sunsetCalculated,tv_maghribCalculated, tv_user_feedback;
    Button button_adhan, button_getSalahInfo, button_back;
    DatePicker datePicker;
    MediaPlayer mediaPlayer;
    EditText et_locationSelection;
    Context context;
    DetermineSalahTime determineSalahTime = new DetermineSalahTime();
    Calendar userCalendar = Calendar.getInstance();
    String adhanStarted = "null";
    String APIkey;
    String dstTrue = "without daylight savings time";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_salah_clock);

        tv_fajrCalculated = findViewById(R.id.fajr_time_response);
        tv_sunriseCalculated = findViewById(R.id.sunrise_time_response);
        tv_zuhrCalculated = findViewById(R.id.zuhr_time_response);
        tv_sunsetCalculated = findViewById(R.id.sunset_time_response);
        tv_maghribCalculated = findViewById(R.id.maghrib_time_response);
        tv_user_feedback = findViewById(R.id.user_feedback);

        et_locationSelection = findViewById(R.id.editTextTextPostalAddress);

        button_adhan = findViewById(R.id.adhan_button);
        button_back = findViewById(R.id.back_button);
        button_getSalahInfo = findViewById(R.id.get_info_button);

        datePicker = findViewById(R.id.date_pick);

        button_getSalahInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    getSalahInfo();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });
    }

    private interface TimeZoneResponseListener {
        void onResponse (int TimeZoneOffset, String dstValue);
        void onError (String message);
    }

    private interface LocationResponseListener {
        void onResponse (double Latitude, double Longitude);
        void onError (String message);
    }

    private void locationJsonParse(String location, final LocationResponseListener locationResponseListener) {
        context = this;
        String url = "https://nominatim.openstreetmap.org/search?q="+location+"+&format=geocodejson&limit=1";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray features = response.getJSONArray("features");
                    JSONObject featuresJSONObject = features.getJSONObject(0);
                    JSONObject geometry = featuresJSONObject.getJSONObject("geometry");
                    JSONArray coordinates = geometry.getJSONArray("coordinates");
                    double Latitude = coordinates.getDouble(1);
                    double Longitude = coordinates.getDouble(0);
                    locationResponseListener.onResponse(Latitude, Longitude);
                } catch (Exception e) {
                    locationResponseListener.onError(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                locationResponseListener.onError(error.toString());
            }
        });
        RequestSingleton.getInstance(context).addToRequestQueue(request);
    }

    private void timeZoneJsonParse(double userLatitude, double userLongitude, final TimeZoneResponseListener timeZoneResponseListener) {
        context = this;
        String url = "https://api.timezonedb.com/v2.1/get-time-zone?key="+APIkey+"&format=json&by=position&lat="+userLatitude+"&lng="+userLongitude;
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int timeZoneOffset = response.getInt("gmtOffset")/3600;
                    String dstValue = response.getString("dst");
                    timeZoneResponseListener.onResponse(timeZoneOffset, dstValue);
                } catch (JSONException e) {
                    timeZoneResponseListener.onError(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                timeZoneResponseListener.onError(error.toString());
            }
        });
        RequestSingleton.getInstance(context).addToRequestQueue(request);
    }

    private void getSalahInfo() {
        String location = et_locationSelection.getText().toString();
        userCalendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
        locationJsonParse(location, new LocationResponseListener() {
            @Override
            public void onResponse(final double Latitude, final double Longitude) {
                timeZoneJsonParse(Latitude, Longitude, new TimeZoneResponseListener() {
                    @Override
                    public void onResponse(int TimeZoneOffset, String dstValue) {
                        tv_fajrCalculated.setText(determineSalahTime.fajrClock(Latitude,Longitude, TimeZoneOffset, userCalendar));
                        tv_sunriseCalculated.setText(determineSalahTime.sunriseClock(Latitude,Longitude, TimeZoneOffset, userCalendar));
                        tv_zuhrCalculated.setText(determineSalahTime.zuhrClock(Longitude, TimeZoneOffset, userCalendar));
                        tv_sunsetCalculated.setText(determineSalahTime.sunsetClock(Latitude,Longitude, TimeZoneOffset, userCalendar));
                        tv_maghribCalculated.setText(determineSalahTime.maghribCalculator(Latitude,Longitude, TimeZoneOffset, userCalendar));
                        if (dstValue.equals("1")){
                            dstTrue = "with daylight savings time";
                        }
                        tv_user_feedback.setText("The times shown are "+ dstTrue+". This may be invalid for the date in question. Adjust time figures accordingly");
                    }

                    @Override
                    public void onError(String message) {
                        Log.e(null, "onError: " + message);
                        Toast.makeText(ManualSalahClockActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onError(String message) {
                Log.e(null, "onError: " + message);
                Toast.makeText(ManualSalahClockActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void playAdhan(View view) {
        if (mediaPlayer == null){
            mediaPlayer = MediaPlayer.create(this, R.raw.adhan);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    stopAdhan();
                }
            });
            mediaPlayer.start();
        }else{
            if (adhanStarted.equals("null")){
                pauseAdhan();
            }else{
                mediaPlayer.start();
                adhanStarted = "null";
            }
        }

    }

    private void pauseAdhan(){
        if (mediaPlayer != null){
            mediaPlayer.pause();
            adhanStarted = "started";
        }
    }

    private void stopAdhan() {
        if (mediaPlayer != null){
            mediaPlayer.release();
            adhanStarted = "null";
            Toast.makeText(this, "Adhan has been stopped", Toast.LENGTH_SHORT).show();
        }
    }

    private void goBack() {
        if (mediaPlayer != null){
            stopAdhan();
        }
        Intent intent = new Intent(this, SalahClockActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopAdhan();
    }
}