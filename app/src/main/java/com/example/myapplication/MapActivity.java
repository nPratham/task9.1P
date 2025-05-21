package com.example.myapplication;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import java.util.List;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        dbHelper = new DatabaseHelper(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Cursor cursor = dbHelper.getAllData();
        boolean first = true;
        Geocoder geocoder = new Geocoder(this);

        while (cursor.moveToNext()) {
            String postType = cursor.getString(cursor.getColumnIndexOrThrow("POST_TYPE"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("DESCRIPTION"));
            String location = cursor.getString(cursor.getColumnIndexOrThrow("LOCATION"));

            try {
                LatLng latLng = null;
                if (location.contains("Lat:") && location.contains("Lon:")) {
                    String[] parts = location.split(",");
                    double lat = Double.parseDouble(parts[0].replace("Lat:", "").trim());
                    double lon = Double.parseDouble(parts[1].replace("Lon:", "").trim());
                    latLng = new LatLng(lat, lon);
                } else {
                    // Try to geocode address string
                    List<Address> addresses = geocoder.getFromLocationName(location, 1);
                    if (addresses != null && !addresses.isEmpty()) {
                        Address address = addresses.get(0);
                        latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    }
                }

                if (latLng != null) {
                    mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(postType)
                            .snippet(description));

                    if (first) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f));
                        first = false;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        cursor.close();
    }

}
