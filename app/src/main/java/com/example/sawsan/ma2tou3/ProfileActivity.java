package com.example.sawsan.ma2tou3;

import android.Manifest;
import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {


    TextView mech_name;
    TextView mech_email;
    TextView mech_phone;
    TextView distance;
    double longi, lat;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        mech_name = (TextView) findViewById(R.id.textView_name);
        mech_email = (TextView) findViewById(R.id.textView_email);
        mech_email.setTextColor(Color.BLACK);
        mech_phone = (TextView) findViewById(R.id.textView_phone);
        distance = (TextView) findViewById(R.id.textView_distance);

        String  email, phone;
        double dist;
        Intent i = getIntent();
        name = i.getExtras().getString("Name");
        email = i.getExtras().getString("Email");

        if (email != null && email.equals("-")) {
            email = getResources().getString(R.string.noEmail);
            //mech_email.setTextAppearance();

            mech_email.setTextColor(Color.GRAY);
        }

        phone = i.getExtras().getString("Phone");
        dist = i.getDoubleExtra("Distance", -1);
        longi = i.getDoubleExtra("Longitude", 0);
        lat = i.getDoubleExtra("Latitude",0);

        mech_email.setText(email);
        mech_name.setText(name);
        mech_phone.setText(phone);
        distance.setText(String.format("%.2f", dist));

    }

    public void done(View v) {
        finish();
    }

    public void call(View v) {

        Intent c = getIntent();
        String ph = c.getExtras().getString("Phone");
        Intent phoneIntent = new Intent(Intent.ACTION_CALL);
        phoneIntent.setData(Uri.parse("tel:"+ph));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(phoneIntent);
    }

    public void showOnMap(View v)
    {
        Intent i = new Intent(this, MapsActivity.class);
        i.putExtra("Name", name).putExtra("Longitude", longi).putExtra("Latitude",lat);
        startActivity(i);

    }


}
