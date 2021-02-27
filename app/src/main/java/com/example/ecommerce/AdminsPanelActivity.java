package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class AdminsPanelActivity extends AppCompatActivity {
    ImageView glasses,hats,bags,headPhones,lapTops,dresses;
    Button shipBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admins_panel);
        glasses = findViewById(R.id.admin_panel_glasses);
        hats = findViewById(R.id.admin_panel_hats);
        bags = findViewById(R.id.admin_panel_bags);
        headPhones = findViewById(R.id.admin_panel_headpgones);
        lapTops = findViewById(R.id.admin_panel_laptops);
        dresses = findViewById(R.id.admin_panel_dresses);
        shipBtn = findViewById(R.id.admin_panel_ship);

        glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminsPanelActivity.this,AddNewProductActivity.class);
                intent.putExtra("category","glasses");
                startActivity(intent);
            }
        });

        hats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminsPanelActivity.this,AddNewProductActivity.class);
                intent.putExtra("category","hats");
                startActivity(intent);
            }
        });

        bags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminsPanelActivity.this,AddNewProductActivity.class);
                intent.putExtra("category","bags");
                startActivity(intent);
            }
        });

        headPhones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminsPanelActivity.this,AddNewProductActivity.class);
                intent.putExtra("category","headphones");
                startActivity(intent);
            }
        });

        lapTops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminsPanelActivity.this,AddNewProductActivity.class);
                intent.putExtra("category","laptops");
                startActivity(intent);
            }
        });


        dresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminsPanelActivity.this,AddNewProductActivity.class);
                intent.putExtra("category","dresses");
                startActivity(intent);
            }
        });

        shipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             startActivity(new Intent(AdminsPanelActivity.this,AdminAcceptOrdersActivity.class));
            }
        });
    }
}