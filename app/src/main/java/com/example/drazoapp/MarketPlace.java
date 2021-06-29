package com.example.drazoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MarketPlace extends AppCompatActivity {

    String name, pseudo, imageUrl, email, phoneNo, password;


    String carCategory, electronicCategory, clothingCategory, housingCategory, furnitureCategory;


    RelativeLayout sellVehicles, sellElctronics, sellClothing, sellRentHouse, sellFurniture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_place);

        sellVehicles = findViewById(R.id.sell_vehicle);
        sellElctronics = findViewById(R.id.sell_electronics);
        sellClothing = findViewById(R.id.sell_clothing);
        sellRentHouse = findViewById(R.id.sell_rent_house);
        sellFurniture = findViewById(R.id.sell_furniture);

        transferUserDataHere();

        carCategory = "Vehicules";
        electronicCategory = "Electroniques";
        clothingCategory = "Habillements";
        housingCategory = "Maisons";
        furnitureCategory = "Meubles";


        Toast.makeText(MarketPlace.this, carCategory, Toast.LENGTH_LONG).show();


        sellVehicles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PostItemForSale.class);


                intent.putExtra("category", carCategory);
                intent.putExtra("email", email);
                intent.putExtra("fullName", name);
                intent.putExtra("imageURL", imageUrl);
                intent.putExtra("password", password);
                intent.putExtra("phoneNo", phoneNo);
                intent.putExtra("pseudo", pseudo);

                startActivity(intent);
            }
        });
        sellElctronics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PostItemForSale.class);

                intent.putExtra("category", electronicCategory);
                intent.putExtra("email", email);
                intent.putExtra("fullName", name);
                intent.putExtra("imageURL", imageUrl);
                intent.putExtra("password", password);
                intent.putExtra("phoneNo", phoneNo);
                intent.putExtra("pseudo", pseudo);

                startActivity(intent);
            }
        });
        sellClothing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PostItemForSale.class);

                intent.putExtra("category", clothingCategory);
                intent.putExtra("email", email);
                intent.putExtra("fullName", name);
                intent.putExtra("imageURL", imageUrl);
                intent.putExtra("password", password);
                intent.putExtra("phoneNo", phoneNo);
                intent.putExtra("pseudo", pseudo);

                startActivity(intent);
            }
        });
        sellRentHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PostItemForSale.class);

                intent.putExtra("category", housingCategory);
                intent.putExtra("email", email);
                intent.putExtra("fullName", name);
                intent.putExtra("imageURL", imageUrl);
                intent.putExtra("password", password);
                intent.putExtra("phoneNo", phoneNo);
                intent.putExtra("pseudo", pseudo);

                startActivity(intent);
            }
        });
        sellFurniture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PostItemForSale.class);

                intent.putExtra("category", furnitureCategory);
                intent.putExtra("email", email);
                intent.putExtra("fullName", name);
                intent.putExtra("imageURL", imageUrl);
                intent.putExtra("password", password);
                intent.putExtra("phoneNo", phoneNo);
                intent.putExtra("pseudo", pseudo);

                startActivity(intent);
            }
        });

    }

    public void transferUserDataHere() {

        Intent intent = getIntent();

        name = intent.getStringExtra("fullName");
        pseudo = intent.getStringExtra("pseudo");
        email = intent.getStringExtra("email");
        phoneNo = intent.getStringExtra("phoneNo");
        password = intent.getStringExtra("password");
        imageUrl = intent.getStringExtra("imageURL");

    }

}