package com.example.drazoapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Login extends AppCompatActivity {

    Button callSignUp, login;
    ImageView logo;
    TextView logoText, message;
    TextInputLayout username, password;


    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        callSignUp = findViewById(R.id.signup_screen);
        logo = findViewById(R.id.logo);
        logoText = findViewById(R.id.greetings);
        message = findViewById(R.id.msg);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);


        callSignUp.setOnClickListener((v) -> {

            Intent intent = new Intent(Login.this, SignUp.class);

            Pair[] pairs = new Pair[7];
            pairs[0] = new Pair<View, String>(logo, "logo_image");
            pairs[1] = new Pair<View, String>(logoText, "logo_text");
            pairs[2] = new Pair<View, String>(message, "message");
            pairs[3] = new Pair<View, String>(username, "username_trans");
            pairs[4] = new Pair<View, String>(password, "password_trans");
            pairs[5] = new Pair<View, String>(login, "login_trans");
            pairs[6] = new Pair<View, String>(callSignUp, "back_to_singup_trans");

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
                startActivity(intent, options.toBundle());
            }

        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(v);
            }
        });
    }

    public Boolean isValideUsername() {
        String val = username.getEditText().getText().toString();

        if (val.isEmpty()) {
            username.setError("Entrez votre nom d'utilisateur");
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }

    }

    public Boolean isValidePassword() {
        String val = password.getEditText().getText().toString();

        if (val.isEmpty()) {
            password.setError("Entrez votre mot de passe");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }

    }

    public void loginUser(View view) {

        if (!isValidePassword() | !isValideUsername()) {
            return;
        } else {
            isUser();
        }

    }

    private void isUser() {

        final String userEnteredUsername = username.getEditText().getText().toString().trim();
        final String userEnteredPassword = password.getEditText().getText().toString().trim();

        reference = FirebaseDatabase.getInstance().getReference("Users");


        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                boolean userExist = false;
                boolean isPassword = false;

                boolean whileLoop = true;


                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


                    String pseudo = dataSnapshot.child("pseudo").getValue(String.class);
                    String passwordFromDB = dataSnapshot.child("password").getValue(String.class);

                    while (pseudo.equals(userEnteredUsername) && whileLoop) {
                        userExist = true;

                        if (passwordFromDB.equals(userEnteredPassword)) {
                            isPassword = true;

                            String emailFromDB = dataSnapshot.child("email").getValue(String.class);
                            String nameFromDB = dataSnapshot.child("fullName").getValue(String.class);
                            String imageUrlFromDB = dataSnapshot.child("imageURL").getValue(String.class);
                            String phoneNoFromDB = dataSnapshot.child("phoneNo").getValue(String.class);

                            Intent intent = new Intent(getApplicationContext(), FeedActivity.class);

                            intent.putExtra("email", emailFromDB);
                            intent.putExtra("fullName", nameFromDB);
                            intent.putExtra("imageURL", imageUrlFromDB);
                            intent.putExtra("password", passwordFromDB);
                            intent.putExtra("phoneNo", phoneNoFromDB);
                            intent.putExtra("pseudo", pseudo);

                            startActivity(intent);

                            Toast.makeText(Login.this, "Salut " + nameFromDB, Toast.LENGTH_LONG).show();

                        }

                        whileLoop = false;
                    }
                }

                if (isPassword) {

                } else if (!userExist) {
                    username.setError("Utilisateur introuvable");
                    username.requestFocus();
                } else {
                    password.setError("Mot de passe incorrect");
                    password.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}