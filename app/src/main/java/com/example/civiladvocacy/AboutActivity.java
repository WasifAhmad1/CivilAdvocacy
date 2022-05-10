package com.example.civiladvocacy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AboutActivity extends AppCompatActivity {
    private TextView google;
    private String gURL = "https://developers.google.com/civic-information/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        google = findViewById(R.id.google);

    }

    public void clickAPI(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(gURL));

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "This does not appear to be working", Toast.LENGTH_SHORT).show();
        }



    }
}