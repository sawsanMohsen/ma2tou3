package com.example.sawsan.ma2tou3;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {


    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);


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

        Button mEmailSignInButton = (Button) findViewById(R.id.sign_up_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });


        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    private void attemptLogin() {


        boolean isCool = true; // to check if all the fields are filled. It becomes false when one field is empty


        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        EditText phone = (EditText) findViewById(R.id.phoneNumber);
        EditText username = (EditText) findViewById(R.id.userName);

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!isPasswordValid(password)) {
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
            isCool = false;
        }

        if (phone.getText().toString().isEmpty()) {
            isCool = false;
            phone.setError(getString(R.string.error_field_required));

        }
        else if (!isNumberValid(phone.getText().toString()))
        {
            isCool = false;
            phone.setError(getString(R.string.error_phoneNumber_tooShort));
        } else if (username.getText().toString().isEmpty()) {
            username.setError(getString(R.string.error_field_required));
            isCool = false;
        }

        if (isCool)
            LoginInfo();
    }

    private boolean isEmailValid(String email) {
        File file = new File("sdcard/Android/data/com.Ma2tou3.app/Mechanics.txt");


        return (email.contains("@") && !email.contains(" "))||email.isEmpty();
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private boolean isNumberValid (String number)
    {
        return number.length() >= 8;
    }


    public void LoginInfo () {
        File file = new File("sdcard/Android/data/com.Ma2tou3.app/Info.txt");
        EditText username = (EditText) findViewById(R.id.userName);
        EditText phone = (EditText) findViewById(R.id.phoneNumber);
        EditText email = (EditText) findViewById(R.id.email);

        if (username.getText().toString().isEmpty() || phone.getText().toString().isEmpty())
            Toast.makeText(this,getResources().getString(R.string.fields), Toast.LENGTH_LONG).show();

        else if (!file.exists())
        {
            try {
                file.createNewFile();

                FileOutputStream fos = new FileOutputStream(file);
                OutputStreamWriter osw = new OutputStreamWriter(fos);
                osw.write("name:" + username.getText().toString() + "\n");
                if (email.getText().toString().isEmpty())
                    osw.write("-\n");
                else
                    osw.write(email.getText().toString()+ "\n");

                osw.write(phone.getText().toString() + "\n");
                osw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Toast.makeText(this, getResources().getString(R.string.createdSuccessfully), Toast.LENGTH_SHORT).show();

            finish();
        }

    }
}

