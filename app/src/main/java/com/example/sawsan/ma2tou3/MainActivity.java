package com.example.sawsan.ma2tou3;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager locationManager;
    private LocationRequest mLocationRequest;
    private GoogleMap mMap;


    Bundle temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        temp = savedInstanceState;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.START);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        File folder = new File("sdcard/Android/data/com.Ma2tou3.app");
        if (!folder.exists())
            folder.mkdirs();

        createElectriciansFile();
        createMechanicFile();
        createTireRepair();
        createTruckTowFile();


        String username = "", email = "";
        File file = new File("sdcard/Android/data/com.Ma2tou3.app/Info.txt");
        if (file.exists()) {
            try {
                BufferedReader bf = new BufferedReader(new FileReader(file));
                username = bf.readLine();
                email = bf.readLine();

            } catch (Exception e) {
                e.printStackTrace();
            }

            StringTokenizer st = new StringTokenizer(username, ":");
            st.nextToken();

            username = st.nextToken();
            if (email.length() == 1 && email.contains("@"))
                username = "";
        } else {
            username = "Ma2tou3";
            email = "";
        }


        View hView = navigationView.getHeaderView(0);

        TextView user;
        TextView mail;
        mail = (TextView) hView.findViewById(R.id.txt_UserName);
        user = (TextView) hView.findViewById(R.id.txt_email);


        user.setText(email);
        mail.setText(username);


    }

    public void createMechanicFile() {
        File file = new File("sdcard/Android/data/com.Ma2tou3.app/Mechanics.txt");
        String[] mechs = getResources().getStringArray(R.array.Mechanics);
        if (!file.exists()) {
            try {
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file);
                OutputStreamWriter osw = new OutputStreamWriter(fos);

                for (int i = 0; i < mechs.length; i++) {
                    osw.write(mechs[i] + "\n");
                }
                osw.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createElectriciansFile() {
        File file = new File("sdcard/Android/data/com.Ma2tou3.app/Electricians.txt");
        String[] mechs = getResources().getStringArray(R.array.Electricians);
        if (!file.exists()) {
            try {
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file);
                OutputStreamWriter osw = new OutputStreamWriter(fos);

                for (int i = 0; i < mechs.length; i++) {
                    osw.write(mechs[i] + "\n");
                }
                osw.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createTireRepair() {
        File file = new File("sdcard/Android/data/com.Ma2tou3.app/TireRepairs.txt");
        String[] mechs = getResources().getStringArray(R.array.TireRepairs);

        if (!file.exists()) {
            try {
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file);
                OutputStreamWriter osw = new OutputStreamWriter(fos);

                for (int i = 0; i < mechs.length; i++) {
                    osw.write(mechs[i] + "\n");
                }
                osw.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createTruckTowFile() {
        File file = new File("sdcard/Android/data/com.Ma2tou3.app/TowTrucks.txt");
        String[] mechs = getResources().getStringArray(R.array.TowTrucker);
        if (!file.exists()) {
            try {
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file);
                OutputStreamWriter osw = new OutputStreamWriter(fos);

                for (int i = 0; i < mechs.length; i++) {
                    osw.write(mechs[i] + "\n");
                }
                osw.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

            Toast.makeText(this, getResources().getString(R.string.BackButtonAgain), Toast.LENGTH_SHORT).show();

            this.doubleBackToExitPressedOnce = true;


            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);

    }

    int lineCount = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        File file = new File("sdcard/Android/data/com.ma2tou3.app/Info.txt");
        int counter = 0;
        String temp = "";

        try {
            BufferedReader bf = new BufferedReader(new FileReader(file));
            while ((temp = bf.readLine()) != null) {
                counter++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (counter == 3)
            numLines = 3;
        else
            numLines = 0;


        if (!file.exists()) {

            getMenuInflater().inflate(R.menu.main, menu);
        } else if (counter == 3) {
            getMenuInflater().inflate(R.menu.main_user, menu);

        } else {
            getMenuInflater().inflate(R.menu.delete_main, menu);


        }
        return true;
    }

    int numLines = 0;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();



        File file = new File("sdcard/Android/data/com.ma2tou3.app/Info.txt");

        if (id == R.id.action_signUpUser) {

            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);


            if (file.exists())
                onRestart();
        } else if (id == R.id.action_signUpAsWorker) {
            Intent i = new Intent(this, WorkerLogin.class);
            startActivity(i);


            if (file.exists())
                onRestart();

        } else if (id == R.id.action_upgradeToWorker) {
            Intent i = new Intent(this, UpgradeWorker.class);
            startActivity(i);

        } else if (id == R.id.action_deleteAccount) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder
                    .setTitle(getResources().getString(R.string.Delete_account))
                    .setMessage(getResources().getString(R.string.AreYouSure))
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //Yes button clicked, do something
                            File file = new File("sdcard/Android/data/com.ma2tou3.app/Info.txt");
                            File f = new File("sdcard/Android/data/com.Ma2tou3.app/loc.txt");
                            f.delete();
                            file.delete();
                            onRestart();
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.No), null)                        //Do nothing on no
                    .show();
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onRestart() {

        // TODO Auto-generated method stub
        super.onRestart();
        Intent i = new Intent(MainActivity.this, MainActivity.class);  //your class
        startActivity(i);
        finish();

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Mechanic) {

            Intent i = new Intent(this, MechanicActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_Electrician) {

            Intent i = new Intent(this, ElectriciansActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_TireRepair) {

            Intent i = new Intent(this, TireRepairActivity.class);
            startActivity(i);
        }
        else if (id == R.id.nav_TowTruck)
        {
            Intent i = new Intent(this, TowTruckActivity.class);
            startActivity(i);
        }
        else if (id == R.id.nav_aboutUs)
        {
            Intent i = new Intent (this, AboutUsActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    double longitude = 0, latitude = 0;


    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(3000)
                .setFastestInterval(16);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
    }

    int MY_PERMISSIONS_REQUEST = 0;

    @Override
    public void onConnected(@Nullable Bundle bundle) {
       /*
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        else
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST);
        }
*/
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLocation == null) {
            startLocationUpdates();
        }
        if (mLocation != null) {
            latitude = mLocation.getLatitude();
            longitude = mLocation.getLongitude();
            SharedPreferences prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = prefs.edit();

            edit.putString("currentLongitude", String.valueOf(longitude));
            edit.putString("currentLatitude", String.valueOf(latitude));


            edit.commit();
        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS_REQUEST) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (mLocation == null) {
                    startLocationUpdates();
                }
                if (mLocation != null) {
                    latitude = mLocation.getLatitude();
                    longitude = mLocation.getLongitude();
                    SharedPreferences prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putString("currentLongitude", String.valueOf(longitude));
                    edit.putString("currentLatitude", String.valueOf(latitude));
                    edit.commit();

                }
            } else {
                Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
            }
        }


    }


    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        //UiSettings.setMyLocationButtonEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
           return;
        }


        LatLng position = new LatLng(latitude, longitude) ;
        mMap.setMyLocationEnabled(true);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        mMap.setPadding(10,250,10,10);

        putMarkersOnMap();


    }

    public void putMarkersOnMap()
    {
        createList(new File ("sdcard/Android/data/com.Ma2tou3.app/Mechanics.txt")); //put mechanics on the maps
        for (int i= 0; i<counter2; i++)
        {
            mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).position(new LatLng(Latitudes[i], Longitudes[i])).title(name[i])).setSnippet(getResources().getString(R.string.Mechanic));
        }
        counter2 = 0;

        createList(new File ("sdcard/Android/data/com.Ma2tou3.app/Electricians.txt")); //put Electricians on the maps
        for (int i= 0; i<counter2; i++)
        {
            mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).position(new LatLng(Latitudes[i], Longitudes[i])).title(name[i])).setSnippet(getResources().getString(R.string.Electrician));
        }
        counter2 = 0;

        createList(new File ("sdcard/Android/data/com.Ma2tou3.app/TireRepairs.txt")); //put Tire Repairs on the maps
        for (int i= 0; i<counter2; i++)
        {
            mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).position(new LatLng(Latitudes[i], Longitudes[i])).title(name[i])).setSnippet(getResources().getString(R.string.TireRepair));
        }
        counter2 = 0;

        createList(new File ("sdcard/Android/data/com.Ma2tou3.app/TowTrucks.txt")); //put Tow Truckers on the maps
        for (int i= 0; i<counter2; i++)
        {
            mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)).position(new LatLng(Latitudes[i], Longitudes[i])).title(name[i])).setSnippet(getResources().getString(R.string.TowTruck));
        }
        counter2 = 0;
    }


    String [] name = null;
    Double Longitudes [] = null, Latitudes[]  =null;
    int counter=0;
    int counter2 = 0;
    public void createList(File file)
    {



        BufferedReader bf = null;
        int counter = 0;

        String temp = "";
        int longCount = 0, latCount = 0;
        try {
            bf = new BufferedReader(new FileReader(file));
            while ((temp = bf.readLine()) != null) {
                if (temp.contains("name")) {

                    counter++;
                    counter2++;
                }
            }


            name = new String [counter];
            Longitudes = new Double [counter];
            Latitudes = new Double[counter];
            bf = new BufferedReader(new FileReader(file));

            counter = 0;
            while ((temp = bf.readLine()) != null) {
                if (temp.contains("name")) {

                    StringTokenizer st = new StringTokenizer(temp, ":");
                    st.nextToken();
                    name[counter++] = st.nextToken();
                }
                else if (temp.contains("Longitude"))
                {
                    StringTokenizer st = new StringTokenizer(temp,":");
                    st.nextToken();
                    Longitudes[longCount++] = Double.parseDouble(st.nextToken());
                }
                else if (temp.contains("Latitude"))
                {
                    StringTokenizer st = new StringTokenizer(temp,":");
                    st.nextToken();
                    Latitudes[latCount++] = Double.parseDouble(st.nextToken());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }













    }
}
