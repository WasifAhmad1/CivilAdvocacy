package com.example.civiladvocacy;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ImageActivity extends AppCompatActivity {

    private TextView location;
    private TextView role;
    private TextView name;
    private TextView party;
    private ImageView official;
    private ImageView logo;
    private Picasso picasso;
    private String getLocation;
    private String getRole;
    private String getName;
    private String getParty;
    private String getPhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        location = findViewById(R.id.location2);
        role = findViewById(R.id.role2);
        name = findViewById(R.id.name2);
        party = findViewById(R.id.party2);
        official = findViewById(R.id.imageView2);
        logo = findViewById(R.id.logo2);
        View view = this.getWindow().getDecorView();
        picasso = Picasso.get();

        if(getIntent().hasExtra("location")) {
            getLocation = getIntent().getStringExtra("location");
            getRole = getIntent().getStringExtra("title");
            getName = getIntent().getStringExtra("name");
            getParty = getIntent().getStringExtra("party");
            getPhoto = getIntent().getStringExtra("photo");
        }

        if(getParty.equals("Democratic Party")) {
            location.setText(getLocation);
            role.setText(getRole);
            name.setText(getName);
            party.setText(getParty);
            view.setBackgroundColor(Color.BLUE);
            logo.setImageResource(R.drawable.dem_logo);
        }

        if(getParty.equals("Republican Party")) {
            location.setText(getLocation);
            role.setText(getRole);
            name.setText(getName);
            party.setText(getParty);
            view.setBackgroundColor(Color.RED);
            logo.setImageResource(R.drawable.rep_logo);
        }

        if(!getPhoto.isEmpty() && getPhoto.charAt(4)!='s') {
            StringBuilder stringBuilder = new StringBuilder(getPhoto);
            stringBuilder.insert(4, 's');
            getPhoto = stringBuilder.toString();
        }
        loadRemoteImage(getPhoto);

    }

    public void loadRemoteImage (String url) {

        if (!url.isEmpty()) {
            picasso.load(url)
                    .error(R.drawable.image_not_found)
                    .placeholder(R.drawable.placeholder)
                    .into(official);
        }
        else{
            official.setImageResource(R.drawable.image_not_found);

        }
    }
}