package com.example.mentalhealth;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.mentalhealth.databinding.ActivityViewlocationBinding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Viewlocation extends FragmentActivity  implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityViewlocationBinding binding;

    TextView describe;

    String mood;

    Double lat;

    Double longitude;

    Toolbar backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityViewlocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);//set map.

        Bundle b = getIntent().getExtras();


        mood = b.getString("mood"); //recieve mood bundle to show user.

        lat = b.getDouble("lat");

        longitude = b.getDouble("long");

        backbutton=(Toolbar) findViewById(R.id.toolbar);



        backbutton.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(Viewlocation.this,MainActivity.class);
                back.putExtra("moodselect","Moodselectpage");
                startActivity(back);
            }
        });


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Geocoder geocoder;

        Log.d(TAG,"mood is "+mood);

        describe = (TextView) findViewById(R.id.Textview);

        List<Address> addresses;

        geocoder = new Geocoder(this, Locale.getDefault());





        // Add a marker in Sydney and move the camera
        LatLng userlocation = new LatLng(lat, longitude);

        try {
            addresses = geocoder.getFromLocation(lat, longitude, 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        String address = addresses.get(0).getAddressLine(0);
        String City = addresses.get(0).getLocality();
        mMap.addMarker(new MarkerOptions().position(userlocation).title(address));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userlocation, 11.5F));

        describe.setText("You felt "+ mood +" at"+"\n"+","+address); //set text field with the adress and mood





    }
}