package com.AliRZ.greendome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton salahClockImageButton = (ImageButton) findViewById(R.id.openSalahClock);
        ImageButton tasbeehCounterImageButton = findViewById(R.id.counter_button);
        ImageButton comingSoonImageButton = findViewById(R.id.comingSoonButton);
        ImageButton qiblaFinderImageButton = findViewById(R.id.qibla_button);
        ImageButton remindersButton = findViewById(R.id.reminderButton);
        ImageButton linksButton = findViewById(R.id.links_button);

        salahClockImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSalahClockActivity();
            }
        });

        tasbeehCounterImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTasbeehCounter();
            }
        });

        comingSoonImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAboutSection();
            }
        });

        qiblaFinderImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openQiblaFinder();
            }
        });

        remindersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openReminders();
            }
        });

        linksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openResourcesActivity();
            }
        });
    }

    private void openResourcesActivity() {
        Intent intent = new Intent(this, linksPage.class);
        startActivity(intent);
    }

    private void openReminders() {
        Intent intent = new Intent(this, SalahClockActivity.class);
        startActivity(intent);
    }

    private void openTasbeehCounter() {
        Intent intent = new Intent(this, TasbeehCounter.class);
        startActivity(intent);
    }

    public void openSalahClockActivity() {
        Intent intent = new Intent(this, SalahClockActivity.class);
        startActivity(intent);
    }

    public void openAboutSection(){
        String url = "https://github.com/AliRZ-02/green-dome";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    public void openQiblaFinder(){
        Intent intent = new Intent(this, QiblaFinder.class);
        startActivity(intent);
    }
}