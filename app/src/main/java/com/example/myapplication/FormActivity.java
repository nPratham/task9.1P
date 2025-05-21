package com.example.myapplication;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.*;
import android.os.Bundle;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

public class FormActivity extends AppCompatActivity {

    RadioButton rbLost, rbFound;
    EditText nameInput, phoneInput, descriptionInput, dateInput;
    TextView locationInput;
    Button saveButton, getLocationButton;

    DatabaseHelper dbHelper;
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lostitemandfound);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyDZJ7yPJkJfhie0LjH931lPhG8G1LekocA");
        }
        PlacesClient placesClient = Places.createClient(this);

        rbLost = findViewById(R.id.found);
        rbFound = findViewById(R.id.rbFound);
        nameInput = findViewById(R.id.Name);
        phoneInput = findViewById(R.id.Phone);
        descriptionInput = findViewById(R.id.Description);
        dateInput = findViewById(R.id.Date);
        locationInput = findViewById(R.id.textView11);
        saveButton = findViewById(R.id.Savebutton);
        getLocationButton = findViewById(R.id.GetCurrentlocation);

        dbHelper = new DatabaseHelper(this);

        saveButton.setOnClickListener(v -> {
            String type = rbLost.isChecked() ? "Lost" : "Found";
            boolean saved = dbHelper.insertData(
                    type,
                    nameInput.getText().toString(),
                    phoneInput.getText().toString(),
                    descriptionInput.getText().toString(),
                    dateInput.getText().toString(),
                    locationInput.getText().toString()
            );
            Toast.makeText(this, saved ? "Saved!" : "Failed!", Toast.LENGTH_SHORT).show();
            if (saved) finish();
        });

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                locationInput.setText("Lat: " + latitude + ", Lon: " + longitude);
            }
        };

        getLocationButton.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        });

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        if (autocompleteFragment != null) {
            autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS));

            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(@NonNull Place place) {
                    locationInput.setText(place.getName() + " - " + place.getAddress());
                    Toast.makeText(FormActivity.this, "Selected: " + place.getName(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(@NonNull Status status) {
                    Toast.makeText(FormActivity.this, "Error: " + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Autocomplete fragment not found", Toast.LENGTH_SHORT).show();
        }
    }
}



