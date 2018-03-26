package com.example.ashwani.complaintbox;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.hello);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.settings:
                        tv.setText("settings");
                        break;

                    case R.id.home:
                        tv.setText("Home");
                        Toast.makeText(MainActivity.this, "home clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.complaint:
                        tv.setText("complaint");
                        Toast.makeText(MainActivity.this, "complaint clicked", Toast.LENGTH_SHORT).show();
                        break;

                }
//                if(item.getItemId()==R.id.settings){
//                    tv.setText("settings");
//                }
//                else if(item.getItemId()==R.id.home)
//                {
//                    tv.setText("Home");
//                }else if(item.getItemId()==R.id.complaint){
//                    tv.setText("complaint");
//                }
                return true;
            }
        });
    }
}
