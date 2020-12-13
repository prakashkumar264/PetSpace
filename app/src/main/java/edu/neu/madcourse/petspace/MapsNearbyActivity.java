package edu.neu.madcourse.petspace;

// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.inappmessaging.model.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

/**
 * An activity that displays a map showing the place at the device's current location.
 */
public class MapsNearbyActivity extends AppCompatActivity {
    //Initialize varible
    Spinner spType;
    Button btFind;
    SupportMapFragment supportMapFragment;
    GoogleMap map;

    FusedLocationProviderClient fusedLocationProviderClient;
    double currentLat = 0, currentLong = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapsnearby);

        //Assign variable
        spType = findViewById(R.id.sp_type);




        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);

        //Initialize array of place type
        String[] placeTypeList = {"vet_clinic", "pet_store", "pet_restaurant", "public_park"};
        //Initialize array of place name
        String[] placeNameList = {"Vet Clinic", "Pet Store", "Pet Restaurant", "Public Park"};

        //Set adapter on spinner
        spType.setAdapter(new ArrayAdapter<>(MapsNearbyActivity.this,
                android.R.layout.simple_spinner_dropdown_item, placeNameList));

        //Initialize fused Location provider client
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //Check permission
        if (ActivityCompat.checkSelfPermission(MapsNearbyActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //When permission granted
            //Call method
            getCurrentLocation();
        } else{
            //When permission denied
            //Request permission
            ActivityCompat.requestPermissions(MapsNearbyActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);

        }





    }

    private void getCurrentLocation() {
        //Initialize task Location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }else{
            //When permission denied
            //Request permission
            ActivityCompat.requestPermissions(MapsNearbyActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                //When success
                if(location != null) {
                    //When location is not equal to null
                    //Get current Latitude
                    currentLat = location.getLatitude();
                    //Get current Longitude
                    currentLong = location.getLongitude();
                    //Sync map
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            //when map is ready
                            map = googleMap;
                            //Zoom current location on map
                            map.animateCamera((CameraUpdateFactory.newLatLngZoom(new LatLng(currentLat, currentLong), 10)));
                        }
                    });
                }

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 44) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //When permission granted
                //Call method
                getCurrentLocation();
            }
        }
    }

    private class PlaceTask extends AsyncTask <String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            String data = null;

            try {
                //Initialize data
                data = downloadUrl(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Execute parser task
            new ParserTask().execute(s);
        }
    }

    private String downloadUrl(String string) throws IOException {
        //Initialize url
        URL url = new URL(string);
        //Initialize connection
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        //Connect connection
        connection.connect();
        //Initialize input stream
        InputStream stream = connection.getInputStream();
        //Initialize buffer reader
        BufferedReader reader = new BufferedReader((new InputStreamReader(stream)));
        //Initialize string builder
        StringBuilder builder = new StringBuilder();
        //Initialize string variable
        String line = "";
        //Use while loop
        while ((line = reader.readLine()) != null) {
            //Append line
            builder.append(line);
        }
        //Get append data
        String data = builder.toString();
        //Close reader
        reader.close();
        //Retuen data
        return data;
    }

    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>>  {
        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {
            //Create json parser class
            JasonParser jasonParser = new JasonParser();
            //Initialize hash map list
            List<HashMap<String, String>> mapList = null;
            JSONObject object = null;
            try {
                //Initialize jason object
                object = new JSONObject(strings[0]);
                //Parse json object
                mapList = jasonParser.parseResult(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Return map list
            return mapList;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
            //Clear map
            map.clear();
            //Use for loop
            for(int i = 0; i <hashMaps.size(); i++){
                //Initialize hash map
                HashMap<String, String> hashMapList = hashMaps.get(i);
                //Get latitude
                double lat = Double.parseDouble(hashMapList.get("lat"));
                //Get longitude
                double lng = Double.parseDouble(hashMapList.get("lng"));
                //Fet name
                String name = hashMapList.get("name");
                //Concat latitude and longtitude
                LatLng latLng = new LatLng(lat, lng);
                //Initialize marker options
                MarkerOptions options = new MarkerOptions();
                //Set position
                options.position(latLng);
                //Set title
                options.title(name);
                //Add marker on map
                map.addMarker(options);
            }
        }
    }

    /**
     * Called when the user taps the 'Return to PetSpace' button.
     * @param view the View object that was clicked
     */
    public void onClickCancel(View view) {
        //Sending User back to MainActivity.
        Intent loginIntent = new Intent(MapsNearbyActivity.this, MainActivity.class);
        startActivity(loginIntent);
    }

    public void onClickFind(View view) {
        //Get selected position of spinner

        //Initialize array of place type
        String[] placeTypeList = {"vet_clinic", "pet_store", "pet_restaurant", "public_park"};
        //Initialize array of place name
        String[] placeNameList = {"Vet Clinic", "Pet Store", "Pet Restaurant", "Public Park"};

        int i = spType.getSelectedItemPosition();
        //Initialize url
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" //url
                + "?location=" + currentLat + "," + currentLong //Location latitude and logitude
                + "&radius=5000" //Nearby radius
                + "&types=" + placeTypeList[i] //Place type
                + "&sensor=true" //Sensor
                + "&key" + getResources().getString(R.string.google_map_key); //Google map key

        //Execute place task method to download json date
        new PlaceTask().execute(url);
    }

}