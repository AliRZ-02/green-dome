package com.AliRZ.greendome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.text.LineBreaker;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QiblaInstructionsActivity extends AppCompatActivity {

    private final String disclaimer = "Due to several factors such as calibration errors, external influences, among others, the reading from the app cannot be guaranteed to be completey accurate. However, All-h is all merciful and is willing to accept our best efforts.";
    private final String calibration = "Rotate your device through a figure 8 pattern to calibrate your compass. Make sure to calibrate away from any significant metallic objects or electrical fields, as these may impact the readings from the compass.";
    private final String how_to = "After calibration, place the device on a flat surface; The green arrow points towards the location of Qibla.";

    private int currentScreen;

    TextView tv_instructions, tv_instructionsTitle;

    Button bt_next, bt_back, bt_backToQibla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qibla_instructions);

        tv_instructions = findViewById(R.id.instructions_textView);
        tv_instructionsTitle = findViewById(R.id.instructions_title);

        bt_next = findViewById(R.id.bt_next);
        bt_back = findViewById(R.id.bt_back);
        bt_backToQibla = findViewById(R.id.bt_go_to_Qibla_Finder);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tv_instructions.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
        }

        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToNext();
            }
        });

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBackInstructions();
            }
        });

        bt_backToQibla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBackToQibla();
            }
        });

        setDisclaimerText();
    }

    private void setDisclaimerText() {
        tv_instructionsTitle.setText("Disclaimer");
        tv_instructions.setText(disclaimer);
    }

    private void setHowToText() {
        tv_instructionsTitle.setText("Instructions");
        tv_instructions.setText(how_to);
    }

    private void setCalibrationText() {
        tv_instructionsTitle.setText("Calibration");
        tv_instructions.setText(calibration);
    }

    private void checkCurrentInstructions() {
        String currentText = tv_instructions.getText().toString();
        switch (currentText){
            case disclaimer:
                currentScreen = 1;
                break;
            case calibration:
                currentScreen = 2;
                break;
            case how_to:
                currentScreen = 3;
                break;
        }
    }

    private void goToNext() {
        checkCurrentInstructions();
        switch (currentScreen){
            case 1:
                setCalibrationText();
                break;
            case 2:
                setHowToText();
                break;
            case 3:
                Toast.makeText(this, "Last Page",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void goBackInstructions() {
        checkCurrentInstructions();
        switch (currentScreen){
            case 1:
                Toast.makeText(this, "First Page",Toast.LENGTH_SHORT).show();
                break;
            case 2:
                setDisclaimerText();
                break;
            case 3:
                setCalibrationText();
                break;
        }
    }

    private void goBackToQibla() {
        Intent intent = new Intent(this, QiblaFinder.class);
        startActivity(intent);
    }
}