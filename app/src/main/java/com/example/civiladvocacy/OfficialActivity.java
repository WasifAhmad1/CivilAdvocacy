package com.example.civiladvocacy;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.regex.Pattern;

public class OfficialActivity extends AppCompatActivity {

    private TextView title;
    private TextView name;
    private TextView party;
    private TextView address;
    private TextView phone;
    private TextView site;
    private ImageView imageView;
    private Picasso picasso;
    private ImageView logo;
    private ImageView youtube;
    private ImageView twitter;
    private ImageView facebook;
    private String getFB;
    private String getTW;
    private String getYT;
    private String getParty;
    private String getPhoto;
    private TextView location;
    private String repub = "https://www.gop.com/";
    private String dem = "https://democrats.org/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);

        title = findViewById(R.id.role);
        name = findViewById(R.id.name);
        party = findViewById(R.id.party);
        address = findViewById(R.id.address);
        phone = findViewById(R.id.phone);
        site = findViewById(R.id.site);
        imageView = findViewById(R.id.imageView);
        logo = findViewById(R.id.logo);
        youtube = findViewById(R.id.youtube);
        twitter = findViewById(R.id.twitter);
        facebook = findViewById(R.id.facebook);
        location = findViewById(R.id.locationOfficial);
        View view = this.getWindow().getDecorView();
        picasso = Picasso.get();



        if(getIntent().hasExtra("title")) {
            String getTitle = getIntent().getStringExtra("title");
            String getName = getIntent().getStringExtra("name");
            getParty = getIntent().getStringExtra("party");
            String getAddress = getIntent().getStringExtra("address");
            String getPhone = getIntent().getStringExtra("phone");
            String getSite = getIntent().getStringExtra("site");
            getPhoto = getIntent().getStringExtra("photo");
            getFB = getIntent().getStringExtra("fb");
            getYT = getIntent().getStringExtra("yt");
            getTW = getIntent().getStringExtra("tw");
            String getLocation = getIntent().getStringExtra("location");

            //System.out.println("The string at the 4th character is " + getPhoto.charAt(4));
            System.out.println("the phote is empty? " + getPhoto.isEmpty());
            if(!getPhoto.isEmpty() && getPhoto.charAt(4)!='s') {
                StringBuilder stringBuilder = new StringBuilder(getPhoto);
                stringBuilder.insert(4, 's');
                getPhoto = stringBuilder.toString();
            }
            loadRemoteImage(getPhoto);

            if(getParty.equals("Democratic Party")) {
                view.setBackgroundColor(Color.BLUE);
                logo.setImageResource(R.drawable.dem_logo);
                //logo.setImageURI(Uri.parse(dem));

            }

            if(getParty.equals("Republican Party")) {
                view.setBackgroundColor(Color.RED);
                logo.setImageResource(R.drawable.rep_logo);
            }

            if(!getFB.isEmpty()) {
                facebook.setImageResource(R.drawable.facebook);
            }

            if(!getYT.isEmpty()) {
                youtube.setImageResource(R.drawable.youtube);
            }

            if(!getTW.isEmpty()) {
                twitter.setImageResource(R.drawable.twitter);
            }

            title.setText(getTitle);
            name.setText(getName);
            party.setText(getParty);
            address.setText(getAddress);
            phone.setText(getPhone);
            site.setText(getSite);
            location.setText(getLocation);
            Pattern pattern = Pattern.compile(".*", Pattern.DOTALL);

            Linkify.addLinks(site, Linkify.ALL);
            Linkify.addLinks(phone, Linkify.ALL);
            Linkify.addLinks(address, pattern, "geo:0,0?q=");


            //logo.setImageResource(R.drawable.dem_logo);



            /*
            Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(address.getText().toString()));
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(mapUri);
            if(intent.resolveActivity(getPackageManager()) !=null) {
                startActivity(intent);
            }   */


        }
    }


    public void openPhoto (View v) {
        if(!getPhoto.isEmpty()){
            Intent intent = new Intent(this, ImageActivity.class);
            String sendLocation = location.getText().toString();
            String sendRole = title.getText().toString();
            String sendName = name.getText().toString();
            String sendParty = party.getText().toString();
            String sendPhoto = getPhoto.toString();

            intent.putExtra("location", sendLocation);
            intent.putExtra("title", sendRole);
            intent.putExtra("name", sendName);
            intent.putExtra("party", sendParty);
            intent.putExtra("photo", sendPhoto);

            startActivity(intent);

        }

        else if(getPhoto.isEmpty()) {
            Toast.makeText(this, "This cannot be clicked", Toast.LENGTH_SHORT).show();

        }
    }

    public void genPartyURL (View v) {
        if(getParty.equals("Democratic Party")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(dem));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, "This does not appear to be working", Toast.LENGTH_SHORT).show();
            }
        }

        else if (getParty.equals("Republican Party")){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(repub));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, "This does not appear to be working", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "This official has no party!", Toast.LENGTH_SHORT).show();
        }
    }

    public void loadRemoteImage (String url) {

        if (!url.isEmpty()) {
            picasso.load(url)
                    .error(R.drawable.missing)
                    .placeholder(R.drawable.placeholder)
                    .into(imageView);
        }
        else{
            imageView.setImageResource(R.drawable.image_not_found);

        }
    }

    public void clickFacebook (View v) {
        String FACEBOOK_URL = "https://www.facebook.com/".concat(getFB.trim());

        Intent intent;

        // Check if FB is installed, if not we'll use the browser
        if (isPackageInstalled("com.facebook.katana")) {
            String urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlToUse));
        } else {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(FACEBOOK_URL));
        }

        // Check if there is an app that can handle fb or https intents
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            makeErrorAlert("No Application found that handles ACTION_VIEW (fb/https) intents");
        }



    }

    public void clickTwitter (View v) {
        String twitterAppUrl = "twitter://user?screen_name=".concat(getTW.trim());
        String twitterWebUrl = "https://twitter.com/".concat(getTW.trim());

        Intent intent;
        // Check if Twitter is installed, if not we'll use the browser
        if (isPackageInstalled("com.twitter.android")) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterAppUrl));
        } else {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterWebUrl));
        }

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            makeErrorAlert("No Application found that handles ACTION_VIEW (twitter/https) intents");
        }


    }

    public void clickYouTube(View v) {
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/".concat(getYT.trim())));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/".concat(getYT.trim()))));
        }

    }

    public boolean isPackageInstalled(String packageName) {
        try {
            return getPackageManager().getApplicationInfo(packageName, 0).enabled;
        }
        catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    private void makeErrorAlert(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(msg);
        builder.setTitle("No App Found");

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}