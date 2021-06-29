package com.example.drazoapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PostItemForSale extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private static final  int MY_PERMISSION_REQUEST_GET_LOCATION = 1;
    private int STORAGE_PERMISSION_CODE = 1;

    String name, pseudo, imageUrl, email, phoneNo, password;

    String category;

    ImageSlider slider;
    ProgressBar progressBar2;
    TextInputLayout itemName, itemDescription, itemPrice, itemState;
    TextView localisation;
    ImageView imageView;
    Button button;

    ArrayList<Uri> imageList = new ArrayList<Uri>();

    private Uri imageUri;

    StorageReference usersMarketPostImages = FirebaseStorage.getInstance().getReference("UsersMarketPostFolder");
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UsersMarketPostLinks");
    DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("FeedActivityStorage");

    private FusedLocationProviderClient mFusedLocationClient;

    String longitude;
    String latidude;
    String countryName;
    String locality;
    String subLocality;
    String itemLocation;
    String userAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_item_for_sale);

        slider = findViewById(R.id.slider_show_item);

        progressBar2 = findViewById(R.id.progressBar2_show_item);
        progressBar2.setVisibility(View.INVISIBLE);

        imageView = findViewById(R.id.image_of_item);
        button = findViewById(R.id.btn_post_item);

        itemName = findViewById(R.id.post_name);
        itemDescription = findViewById(R.id.post_description);
        itemPrice = findViewById(R.id.post_price);
        itemState = findViewById(R.id.item_state);
        localisation = findViewById(R.id.item_localisation);

        transferUserDataHere();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        Toast.makeText(PostItemForSale.this, category, Toast.LENGTH_LONG).show();

        slider.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(PostItemForSale.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    startActivityForResult(intent, PICK_IMAGE);
                } else {
                    requestStoragePermission();
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String nodeKey = String.valueOf(System.currentTimeMillis());

                getLocation();

                String itemLocationBis = itemLocation;

                String itemName2 = itemName.getEditText().getText().toString();
                String itemPrice2 = itemPrice.getEditText().getText().toString();
                String itemDescription2 = itemDescription.getEditText().getText().toString();
                String itemState2 = itemState.getEditText().getText().toString();


                UserMarketHelperClass helperClass = new UserMarketHelperClass(itemLocationBis, itemName2, itemPrice2, itemDescription2, itemState2);

                databaseReference.child(phoneNo).child(category).child(nodeKey).child("Item Info").setValue(helperClass);


                for (int i = 0; i < imageList.size(); i++) {
                    Uri imageUri = imageList.get(i);
                    StorageReference imageName = usersMarketPostImages.child("Image" + System.currentTimeMillis());

                    String imageNo = String.valueOf(i + 1);
                    int finalI = i + 1;
                    imageName.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            //todo progressbar here

                            imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    String imageLink = String.valueOf(uri);

                                    StoreLink(imageLink, itemLocation, nodeKey, imageNo);

                                    //TODO Localisation here
                                    appendInfo(databaseReference2, itemDescription2, nodeKey, itemName2, itemPrice2, itemLocation, longitude, latidude, userAddress);
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    private void getLocation() {

        if (ContextCompat.checkSelfPermission(PostItemForSale.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(PostItemForSale.this, Manifest.permission.ACCESS_COARSE_LOCATION))
            {

                new AlertDialog.Builder(this)
                        .setTitle("Acces a votre localisation")
                        .setMessage("Drazo a besoin de cette permission pour acceder votre localisation")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                ActivityCompat.requestPermissions(PostItemForSale.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_REQUEST_GET_LOCATION);

                            }
                        })
                        .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create().show();

            }
            else{
                ActivityCompat.requestPermissions(PostItemForSale.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_REQUEST_GET_LOCATION);
            }

        }
        else
        {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    if(location != null)
                    {
                        try {
                            Geocoder geocoder = new Geocoder(PostItemForSale.this, Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                            longitude = String.valueOf(addresses.get(0).getLongitude());
                            latidude = String.valueOf(addresses.get(0).getLatitude());
                            countryName = addresses.get(0).getCountryName();
                            locality = addresses.get(0).getAdminArea();
                            subLocality = addresses.get(0).getSubLocality();
                            userAddress = addresses.get(0).getAddressLine(0);

                            itemLocation = subLocality + ", " + locality + ", " + countryName;

                            //NY, BX, USA



                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }
                }
            });
        }
    }



    private void StoreLink(String imageLink, String localisation, String nodeKey, String imageNo) {

        transferUserDataHere();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("ImageLink", imageLink);


        databaseReference.child(phoneNo).child(category).child(nodeKey).child("Item Pictures").child(imageNo).setValue(hashMap);

        databaseReference2.child(nodeKey).setValue(hashMap);

        progressBar2.setVisibility(View.GONE);

    }

    public void appendInfo(DatabaseReference databaseReference7, String desc, String nodeKey, String itemName, String itemPrice, String localisation, String longi, String latid, String userAddr){

        Map<String, Object> hashMap = new HashMap<>();

        hashMap.put("itemName", itemName);
        hashMap.put("itemPrice", itemPrice);
        hashMap.put("localisation", localisation);
        hashMap.put("itemDescription", desc);
        hashMap.put("longitude", longi);
        hashMap.put("latidude", latid);
        hashMap.put("userAddress", userAddr);


        databaseReference7.child(nodeKey).updateChildren(hashMap);
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale( this, Manifest.permission.READ_EXTERNAL_STORAGE))
        {
            new AlertDialog.Builder(this)
                    .setTitle("Permission d'access")
                    .setMessage("Drazo a besoin de cette permission pour acceder vos donnees")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            ActivityCompat.requestPermissions(PostItemForSale.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

                        }
                    })
                    .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        }
        else{
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == STORAGE_PERMISSION_CODE || requestCode == MY_PERMISSION_REQUEST_GET_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "Permission donnee", Toast.LENGTH_LONG).show();
            }
            else {

                Toast.makeText(this, "Permission refusee", Toast.LENGTH_LONG).show();
            }
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getClipData() != null)
        {
            int countClipData = data.getClipData().getItemCount();

            int currentImageSelected = 0;

            List<SlideModel> slideModels = new ArrayList<>();

            while (currentImageSelected < countClipData)
            {

                imageUri = data.getClipData().getItemAt(currentImageSelected).getUri();

                imageList.add(imageUri);
                slideModels.add(new SlideModel(imageUri.toString(),ScaleTypes.FIT));

                currentImageSelected ++;
            }

            slider.setImageList(slideModels, ScaleTypes.FIT);

            imageView.setVisibility(View.GONE);

            progressBar2.setVisibility(View.VISIBLE);

            Toast.makeText(this, "Vous avez selectionnez "+imageList.size()+ " images", Toast.LENGTH_LONG).show();

        }
        else {
            Toast.makeText(this, "Postez au moins deux image SVP", Toast.LENGTH_LONG).show();

        }
    }

    public void transferUserDataHere() {

        Intent intent = getIntent();

        name = intent.getStringExtra("fullName");
        category = intent.getStringExtra("category");
        pseudo = intent.getStringExtra("pseudo");
        email = intent.getStringExtra("email");
        phoneNo = intent.getStringExtra("phoneNo");
        password = intent.getStringExtra("password");
        imageUrl = intent.getStringExtra("imageURL");

    }
}