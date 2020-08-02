package com.AliRZ.greendome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TasbeehCounter extends AppCompatActivity {

    private TextView tv_counter_response;

    private Button bt_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasbeeh_counter);

        tv_counter_response = findViewById(R.id.counter_response);
    }

    private int currentCounterValue (){
        String currentCounter = tv_counter_response.getText().toString();
        int currentCount = Integer.parseInt(currentCounter);
        return currentCount;
    }

    public void addCounter (View view) {
        int currentCount = currentCounterValue();
        tv_counter_response.setText(String.valueOf(currentCount + 1));
    }

    public void subtractCounter (View view) {
        int currentCount = currentCounterValue();
        if (currentCount != 0){
            tv_counter_response.setText(String.valueOf(currentCount - 1));
        }else{
            tv_counter_response.setText(String.valueOf(currentCount));
        }

    }

    public void resetCounter (View view) {
        tv_counter_response.setText("0");
    }

    public void goBack(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}