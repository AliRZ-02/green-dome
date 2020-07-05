package com.AliRZ.goldendome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button openSalahClock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton simpleImageButton = (ImageButton)findViewById(R.id.openSalahClock);

        simpleImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSalahClockActivity();
            }
        });
        /*
        openSalahClock = findViewById(R.id.openSalahClock);
        openSalahClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSalahClockActivity();
            }
        });
         */
    }
    public void openSalahClockActivity (){
        Intent intent = new Intent (this, SalahClockActivity.class);
        startActivity(intent);
    }
}