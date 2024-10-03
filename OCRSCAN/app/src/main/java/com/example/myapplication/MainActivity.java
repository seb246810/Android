package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;



import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    Reciepts reciepts = new Reciepts();

    Report report = new Report();

    Scanning scanning = new Scanning();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.Home);




    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        {
            if (itemId == R.id.Home) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, scanning)
                        .commit();
                return true;
            } else if (itemId == R.id.Reciepts) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, reciepts)
                        .commit();
                return true;
            } else if (itemId == R.id.Report) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, report)
                        .commit();
                return true;
            }
            return false;
        }
    }
}