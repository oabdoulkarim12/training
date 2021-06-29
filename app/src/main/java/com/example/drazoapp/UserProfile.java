package com.example.drazoapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import java.util.HashMap;

public class UserProfile extends AppCompatActivity {

    TextInputLayout name, email, password, pseudo;
    TextView fullNameLabel, phoneNumberLabel;
    Button update;
    ImageView imageView;

    String t_name, t_pseudo, t_imageUrl, t_email, t_phoneNo, t_password;


    String updatedName, updatedUsername, updatedEmail, updatedPhoneNo, updatedPassword;

    ProgressBar progressBar;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
    DatabaseReference databaseReference1;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference("UsersProfilePictures");

    Uri imageUri;

    // TODO Prevent the user information from being an empty string.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        imageView = findViewById(R.id.upload_profile_image);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        pseudo = findViewById(R.id.pseudo);
        password = findViewById(R.id.password);
        fullNameLabel = findViewById(R.id.full_name_label);
        phoneNumberLabel = findViewById(R.id.phone_number_label);
        update = findViewById(R.id.update);

        ShowAllDate();

        databaseReference1 = FirebaseDatabase.getInstance().getReference("Users").child(t_phoneNo).child("imageURL");

        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                String link = snapshot.getValue(String.class);

                if (!link.equals("NO PROFILE PICTURE"))
                {
                    Picasso.get().load(link).into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 2);

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updatedName = name.getEditText().getText().toString();
                updatedUsername = pseudo.getEditText().getText().toString();
                updatedEmail = email.getEditText().getText().toString();
                updatedPassword = password.getEditText().getText().toString();

                if(imageUri != null)
                {
                    UpdateFireBase(imageUri, updatedPhoneNo);
                }
                else{

                    updateInfo("Informations mises à jour");
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data !=null){

            imageUri = data.getData();
            imageView.setImageURI(imageUri);
            Picasso.get().load(imageUri).into(imageView);

        }
    }


    public void ShowAllDate() {

        Intent intent = getIntent();

        t_name = intent.getStringExtra("fullName");
        t_pseudo = intent.getStringExtra("pseudo");
        t_email = intent.getStringExtra("email");
        t_phoneNo = intent.getStringExtra("phoneNo");
        t_password = intent.getStringExtra("password");
        t_imageUrl = intent.getStringExtra("imageURL");


        fullNameLabel.setText(t_name);
        phoneNumberLabel.setText(t_phoneNo);
        name.getEditText().setText(t_name);
        pseudo.getEditText().setText(t_pseudo);
        password.getEditText().setText(t_password);
        email.getEditText().setText(t_email);

    }

    private void UpdateFireBase(Uri uri, String profileImageName){

        StorageReference fileRef = storageReference.child(profileImageName + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setProgress(0);
                    }
                },500);


                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        UserHelperClass userHelperClass = new UserHelperClass(updatedName, updatedUsername, updatedEmail, updatedPhoneNo, updatedPassword, uri.toString());
                        databaseReference.child(t_phoneNo).setValue(userHelperClass);

                        updateInfo("Photo de profile et informations actualisées");
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
                double progress = (100.0 * snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                progressBar.setProgress((int)progress);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(UserProfile.this, "Chargement de la photo échoué", Toast.LENGTH_LONG).show();
            }
        });

    }

    // get the file extension
    private String getFileExtension(Uri mUri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(mUri));
    }

    public void updateInfo(String msg){

        HashMap hashMap = new HashMap();

        hashMap.put("fullName", updatedName);
        hashMap.put("pseudo", updatedUsername);
        hashMap.put("password", updatedPassword);
        hashMap.put("email", updatedEmail);

        databaseReference.child(t_phoneNo).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(UserProfile.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

}