package com.AliRZ.greendome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class linksPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_links_page);

        ImageButton QuranButton, DuasButton, MoreButton;

        QuranButton = findViewById(R.id.QuranImageButton);
        DuasButton = findViewById(R.id.DuasImageButton);
        MoreButton = findViewById(R.id.MoreImageButton);

        QuranButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openQuranLink();
            }
        });

        DuasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDuasLink();
            }
        });
        MoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMoreLink();
            }
        });
    }

    private void openMoreLink() {
        String url = "http://m.hussainiat.com";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    private void openDuasLink() {
        String url = "https://www.duas.org";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    private void openQuranLink() {
        String url = "http://www.tanzil.net";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}