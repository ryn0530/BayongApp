package com.example.bayongapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bayongapp.R;

public class CodOrDeliveryActivity extends AppCompatActivity {

    Button buttonA,buttonB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cod_or_delivery);
        buttonA = findViewById(R.id.buttonA);
        buttonB = findViewById(R.id.buttonB);


        ////pickup
        buttonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PlacedOrderActivity.class);
                startActivity(intent);
            }
        });
        ////delivered
        buttonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
    }
}