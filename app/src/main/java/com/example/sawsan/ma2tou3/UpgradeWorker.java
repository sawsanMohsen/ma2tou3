package com.example.sawsan.ma2tou3;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class UpgradeWorker extends AppCompatActivity {
    Spinner spinner;
    CheckBox check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_worker);

        check = (CheckBox) findViewById(R.id.checkBox);
        spinner = (Spinner) findViewById(R.id.spinner_upgrade);
        String[] specialties = {getResources().getString(R.string.Mechanic), getResources().getString(R.string.Electrician), getResources().getString(R.string.TireRepair), getResources().getString(R.string.TowTruck)};
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, specialties));


        Button upgrade = (Button) findViewById(R.id.sign_up_button_upgrade);
        upgrade.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });


    }

    public void chooseLocation(View v)
    {
        Intent i = new Intent(this,GetLocationMapsActivity.class);
        startActivity(i);


    }

    private void attemptLogin() {

        Boolean gotCurrentLoc = false,chosenLoc = false;
        try {
            BufferedReader bf = new BufferedReader(new FileReader(new File("sdcard/Android/data/com.Ma2tou3.app/loc.txt")));
            String temp = "";
            while ((temp=bf.readLine())!=null)
            {
                if (temp.contains("currentLocation"))
                    gotCurrentLoc = true;
                else if (temp.contains("chosenLocation"))
                    chosenLoc = true;
                else
                {
                    longitude = Double.parseDouble(bf.readLine());
                    latitude = Double.parseDouble(bf.readLine());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        if ( gotCurrentLoc||chosenLoc )
        {
            check.setChecked(true);
        }


        if (!check.isChecked())
            Toast.makeText(this,getResources().getString(R.string.selectLocation),Toast.LENGTH_LONG).show();
        else
        {
            File file = new File("sdcard/Android/data/com.Ma2tou3.app/Info.txt");


            String temp = "";
            String[] info = new String[3];
            int counter = 0;
            try {
                BufferedReader bf = new BufferedReader(new FileReader(file));
                while ((temp = bf.readLine()) != null) {
                    info[counter++] = temp;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {


                FileOutputStream fos = new FileOutputStream(file);
                OutputStreamWriter osw = new OutputStreamWriter(fos);
                osw.write("name:" + info[1] + "\n");
                osw.write(info[0] + "\n");

                osw.write(info[2] + "\n");
                osw.write(spinner.getSelectedItem().toString() + "\n");
                osw.write("Longitude"+String.valueOf(longitude) + "\n");
                osw.write("Latitude"+String.valueOf(latitude) + "\n");
                osw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Toast.makeText(this, getResources().getString(R.string.upgradedSuccessfully), Toast.LENGTH_SHORT).show();



            finish();
        }




    }

    double longitude;
    double latitude;
    }

