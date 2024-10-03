package com.example.mentalhealth;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.app.PendingIntent.getActivity;




import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.ActionBar;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    private static final String TAG = "MainActivity";
    FirstFragment firstFragment = new FirstFragment(); //instances of fragments to load.

    SecondFragment secondFragment = new SecondFragment();

    SelectLocation selectLocation = new SelectLocation();

    Selectentry diary = new Selectentry();

    BottomNavigationView bottomNavigationView;

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>(); //permissions for location
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTracking locationTrack;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        FirebaseMessaging.getInstance().subscribeToTopic("all");
        getFirebaseCloudMessagingToken();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (Build.VERSION.SDK_INT >= 33) {
                if
                (ContextCompat.checkSelfPermission(MainActivity.this,
                        android.Manifest.permission.POST_NOTIFICATIONS) !=
                        PackageManager.PERMISSION_GRANTED) {                        //check permissions granted.

                    ActivityCompat.requestPermissions(MainActivity.this, new
                            String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
                }
            }

            NotificationChannel channel = new
                    NotificationChannel("firebase", "Firebase channel",
                    NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager =
                    getSystemService(NotificationManager.class); //create channel for notifications to be shown on.
            manager.createNotificationChannel(channel);

        }




        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }








        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        bottomNavigationView.setSelectedItemId(R.id.home);//set bottom nav bar to select home fragment


        Intent getintent = getIntent(); //get intents from firebase instance service and View location.

        String diarypage = getintent.getStringExtra("diaryfragment");

        String Moodselectpage = getintent.getStringExtra("moodselect");



        Log.d(TAG,"fragment name is first "+diarypage);


        if("diary".equals(diarypage)){
            Log.d(TAG,"Fragment name is " +diarypage);
            bottomNavigationView.setSelectedItemId(R.id.Diary);

        }
                //use intents to navigate.

        if("Moodselectpage".equals(Moodselectpage)){
            bottomNavigationView.setSelectedItemId(R.id.Mood);
        }







    }





    public boolean
    onNavigationItemSelected(@NonNull MenuItem item){ //menu to respond to fragments clicked.

        int itemId = item.getItemId();

        {
            if(itemId == R.id.person){
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment,firstFragment)
                        .commit();
                return true;
            }

            else if (itemId == R.id.home){
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment,secondFragment)
                        .commit();
                return true;
            }

            else if (itemId == R.id.Mood){
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment,selectLocation)
                        .commit();
                return true;
            }

            else if(itemId == R.id.Diary){
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment,diary)
                        .commit();
                return true;

            }
        }

        return false;

    }


    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {//checks unasked permissions.
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }
        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    private void getFirebaseCloudMessagingToken() { //gives token to use for notification.
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        String token = task.getResult();
                        Log.d("FCM Token", "Token: " + token);
                        // Save the token for future use (e.g., se messages from the server)
                    } else {
                        Log.e("FCM Token", "Failed to get token");
                    }
                });
    }


}