package com.example.civiladvocacy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private FusedLocationProviderClient mFusedLocationClient;
    private static final int LOCATION_REQUEST = 111;
    private TextView location;
    private RecyclerView recyclerView;
    private ArrayList<Official> officialList = new ArrayList<>();
    private OfficialAdapter officialAdapter;
    private static String locationString = "Unspecified Location";
    private boolean isConnected;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler);
        officialAdapter = new OfficialAdapter(officialList, this);
        recyclerView.setAdapter(officialAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //isConnected = doNetCheck(isConnected);


        location = findViewById(R.id.location);
        mFusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this);
        determineLocation();
        isConnected = doNetCheck(isConnected);


        //once we determine the location we should send the location information to a thread
    }

    private void determineLocation () {
        if (checkPermission()) {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location2) {
                            // Got last known location. In some situations this can be null.
                            if (location != null) {
                                locationString = MainActivity.this.getPlace(location2);
                                location.setText(locationString);
                                executeThread(locationString);
                            }
                        }
                    })
                    .addOnFailureListener(this, e -> Toast.makeText(MainActivity.this,
                            e.getMessage(), Toast.LENGTH_LONG).show());
        }


    }

    private boolean checkPermission () {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, LOCATION_REQUEST);
            return false;
        }
        return true;

    }

    private String getPlace(Location loc) {

        StringBuilder sb = new StringBuilder();

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String zip = addresses.get(0).getPostalCode();
            sb.append(String.format(
                    Locale.getDefault(),
                    "%s, %s  %s",
                    city, state, zip));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    determineLocation();
                } else {
                    location.setText(R.string.deniedText);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Generates the menu layout for this activity
        getMenuInflater().inflate(R.menu.advocacy_menu, menu);
        return true;
    }


    public void noLocation() {
        officialList.clear();
        recyclerView = findViewById(R.id.recycler);
        officialAdapter = new OfficialAdapter(officialList, this);
        recyclerView.setAdapter(officialAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //next we create an alert dialog that pops up
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("There is no data for this location");
        builder.setTitle("Please enter in another location");

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void hello (ArrayList <Official> listCopy){
        officialList.clear();
        officialList.addAll(listCopy);
        System.out.println("What do we got here?" + listCopy.get(0).getAddress());

        recyclerView = findViewById(R.id.recycler);
        officialAdapter = new OfficialAdapter(officialList, this);
        recyclerView.setAdapter(officialAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //System.out.println(officialList.get(0).getName());

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.add){
            LayoutInflater inflater = LayoutInflater.from(this);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            @SuppressLint("InflateParams") final View view = inflater.inflate(R.layout.official_dialog, null);
            builder.setView(view);
            TextView t1 = view.findViewById(R.id.addressSelect);
            t1.setText("Enter a City, State or a zip code");
            EditText et1 = view.findViewById(R.id.textS);
            System.out.println(et1.getText().toString());
            //OfficialNames officialNames = new OfficialNames(this, et1.getText().toString());


            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    location.setText(et1.getText());
                    executeThread(et1.getText().toString());


                }
            });


            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Toast.makeText(MainActivity.this, "You selected no", Toast.LENGTH_SHORT).show();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();




        }

        if(id==R.id.info) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;

        }
        return super.onOptionsItemSelected(item);



    }

    public void executeThread(String text){
        OfficialNames officialNames = new OfficialNames(this, text);
        new Thread(officialNames).start();


    }

    @Override
    public void onClick(View view) {
        int pos = recyclerView.getChildLayoutPosition(view);
        Official o = officialList.get(pos);
        Intent intent = new Intent(this, OfficialActivity.class);
        String title = o.getTitle();
        String name = o.getName();
        String party = o.getParty();
        String address = o.getAddress();
        String phone = o.getPhone();
        String site = o.getWebsite();
        String photo = o.getPhotoURL();
        String fb = o.getFaceURL();
        String tw = o.getTwitURL();
        String yt = o.getYtURL();
        String sendLocation = location.getText().toString();

        intent.putExtra("title", title);
        intent.putExtra("name", name);
        intent.putExtra("party", party);
        intent.putExtra("address", address);
        intent.putExtra("phone", phone);
        intent.putExtra("site", site);
        intent.putExtra("photo", photo);
        intent.putExtra("fb", fb);
        intent.putExtra("tw", tw);
        intent.putExtra("yt", yt);
        intent.putExtra("location", sendLocation);

        startActivity(intent);

    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }


    private boolean doNetCheck(boolean connect) {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null) {
            Toast.makeText(this, "Cannot access ConnectivityManager", Toast.LENGTH_SHORT).show();
            return false;
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected==true) {
            return true;

        } else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("There is no internet connection currently");
            builder.setTitle("Please connect to the internet if you want to use this app");
            AlertDialog dialog = builder.create();
            dialog.show();
            //location.setText("No Data For Location");
            return false;
        }

    }
}
