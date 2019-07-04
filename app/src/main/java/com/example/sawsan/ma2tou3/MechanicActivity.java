package com.example.sawsan.ma2tou3;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

public class MechanicActivity extends AppCompatActivity {

    File file = new File("sdcard/Android/data/com.Ma2tou3.app/Mechanics.txt");
    String [] name = null;
    Double distance[] = null;
    String phone [] = null;
    String email [] = null;
    ListView list;
    Double Longitudes [] = null, Latitudes[]  =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic);
        list = (ListView) findViewById(R.id.listView_mech);
        createList();


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, name);
        list.setAdapter(adapter);





        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int ii, long l) {
                Intent i = new Intent(MechanicActivity.this, ProfileActivity.class);
                i.putExtra("Name", name[ii]);
                i.putExtra("Email", email[ii]);
                i.putExtra("Phone", phone[ii]);
                i.putExtra("Distance", distance[ii]);
                i.putExtra("Longitude",Longitudes[ii]);
                i.putExtra("Latitude", Latitudes[ii]);
                startActivity(i);

            }
        });

    }
    public void createList()
    {



        int counter = 0;
        String temp = "";

        BufferedReader bf = null;

        int longCount = 0, latCount = 0;

        int counter2 = 0;




        try {
            bf = new BufferedReader(new FileReader(file));
            while ((temp = bf.readLine()) != null) {
                if (temp.contains("name")) {
                    counter2 = 0;
                    counter++;
                }
                counter2++;
                if (counter2==4)
                    longCount++;
                if (counter2 == 5)
                    latCount++;
            }

            Longitudes = new Double [longCount];
            Latitudes = new Double[latCount];
            distance = new Double [latCount];
            phone = new String [counter];
            email = new String [counter];


            latCount = longCount = 0;
            bf = new BufferedReader(new FileReader(file));
            name = new String [counter];
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
                else if (!temp.contains("@") && temp.length()>=8)
                    phone[counter-1] = temp;
                else
                    email[counter-1] = temp;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        SharedPreferences preferences = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        double currentLong = Double.parseDouble(preferences.getString("currentLongitude","0"));
        double currentLat = Double.parseDouble(preferences.getString("currentLatitude","0"));


        for (int i =0; i<Longitudes.length; i++)
        {
            distance[i] = distance(currentLong,currentLat,Longitudes[i],Latitudes[i]);
        }


        double tempdis;
        String tempname;
        String tempEmail;
        String tempPhone;
        Double templongi, templati;
        for (int i =0; i<distance.length; i++)
        {
            for (int j =0; j<distance.length-1; j++)
            {
                if (distance[j]>distance[j+1])
                {
                    tempdis = distance[j];
                    distance[j] = distance[j+1];
                    distance[j+1] =  tempdis;

                    tempname = name[j];
                    name[j] = name[j+1];
                    name[j+1] = tempname;

                    tempEmail = email[j];
                    email[j] = email [j+1];
                    email[j+1] = tempEmail;

                    tempPhone = phone[j];
                    phone[j] = phone[j+1];
                    phone[j+1] = tempPhone;

                    templongi = Longitudes[j];
                    Longitudes[j] = Longitudes[j+1];
                    Longitudes[j+1] = templongi;

                    templati = Latitudes[j];
                    Latitudes[j] = Latitudes[j+1];
                    Latitudes[j+1] = templati;

                }
            }
        }






    }

    public Double distance(double lat1, double lon1, double  lat2, double lon2) {
        double dist;



        final int R = 6371; // Radious of the earth

        Double latDistance = Math.toRadians(lat2-lat1);
        Double lonDistance = Math.toRadians(lon2-lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        Double distance = R * c;
        return distance;
    }

}
