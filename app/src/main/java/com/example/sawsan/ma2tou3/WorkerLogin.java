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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class WorkerLogin extends AppCompatActivity {


    Spinner spinner;


    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_login);


        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);



        check = (CheckBox) findViewById(R.id.checkBox);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        spinner = (Spinner) findViewById(R.id.spinner);
        String[] specialties = {getResources().getString(R.string.Mechanic), getResources().getString(R.string.Electrician), getResources().getString(R.string.TireRepair), getResources().getString(R.string.TowTruck)};
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, specialties));
    }








    public void chooseLocation(View v)
    {
        Intent i = new Intent(this,GetLocationMapsActivity.class);
        startActivity(i);


    }
    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        boolean isCool = true;
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

                    longitude = Double.parseDouble(temp);
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



        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        EditText phone = (EditText) findViewById(R.id.phoneNumber);

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
            isCool = false;
        }

        // Check for a valid email address.
        if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;

        }

        if (!isNumberValid(phone.getText().toString()))
        {
            isCool = false;
            phone.setError(getString(R.string.error_phoneNumber_tooShort));
        } else  {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.


            if (isCool)
                LoginInfo();
        }
    }

    CheckBox check;
    public void LoginInfo() {
        File file = new File("sdcard/Android/data/com.Ma2tou3.app/Info.txt");
        EditText username = (EditText) findViewById(R.id.userName);
        EditText phone = (EditText) findViewById(R.id.phoneNumber);
        EditText email = (EditText) findViewById(R.id.email);








        if (username.getText().toString().isEmpty() || phone.getText().toString().isEmpty() || !check.isChecked())
            Toast.makeText(this,getResources().getString(R.string.fields), Toast.LENGTH_LONG).show();
        else if (!file.exists()) {
            try {
                file.createNewFile();

                FileOutputStream fos = new FileOutputStream(file);
                OutputStreamWriter osw = new OutputStreamWriter(fos);

                osw.write("name:" +username.getText().toString() + "\n");

                if (email.getText().toString().isEmpty())
                    osw.write("-" + "\n");
                else
                    osw.write(email.getText().toString() + "\n");

                osw.write(phone.getText().toString() + "\n");
                osw.write(spinner.getSelectedItem().toString()+"\n");
                osw.write("Longitude:"+String.valueOf(longitude)+"\n");
                osw.write("Latitude:"+String.valueOf(latitude)+"\n");
                osw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }



            Toast.makeText(this, getResources().getString(R.string.createdSuccessfully), Toast.LENGTH_SHORT).show();

            finish();
        }
    }

    double longitude, latitude;

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return (email.contains("@") && !email.contains(" ")) || email.isEmpty();
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private boolean isNumberValid (String number)
    {
        return number.length() >= 8;
    }


}

