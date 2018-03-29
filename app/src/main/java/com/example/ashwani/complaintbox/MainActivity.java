package com.example.ashwani.complaintbox;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TextView tv;
    FragmentTransaction transaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationBar);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.settings:
                        Toast.makeText(MainActivity.this, "settigns", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.home:
                        transaction = getSupportFragmentManager().beginTransaction();
                        HomeFragment Hfragment=new HomeFragment();
                        transaction.replace(R.id.frame_layout, Hfragment);
                        transaction.commit();
                        Toast.makeText(MainActivity.this, "home", Toast.LENGTH_SHORT).show();

                        break;
                    case R.id.complaint:
                        Toast.makeText(MainActivity.this, "Complaint", Toast.LENGTH_SHORT).show();
                        transaction = getSupportFragmentManager().beginTransaction();
                        ComplaintRVListFragment fragment = new ComplaintRVListFragment();
                        transaction.replace(R.id.frame_layout, fragment);
                        transaction.commit();
                        break;

                }
                return true;
            }
        });
    }
}
