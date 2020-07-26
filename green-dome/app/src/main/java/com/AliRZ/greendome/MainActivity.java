package com.AliRZ.greendome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
                openComingSoonPage();
            }
        });

    }

    private void openTasbeehCounter() {
        Intent intent = new Intent(this, TasbeehCounter.class);
        startActivity(intent);
    }

    public void openSalahClockActivity() {
        Intent intent = new Intent(this, SalahClockActivity.class);
        startActivity(intent);
    }

    public void openComingSoonPage (){
        Toast toast = Toast.makeText(this, "Coming Soon!", Toast.LENGTH_SHORT);
        toast.show();
    }
}