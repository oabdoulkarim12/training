package com.example.drazoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    TextInputLayout regName, regPseudo, regEmail, regPhoneNo, regPassword, regComfirmPassword;
    Button regButton, backToLogin;
    ImageView logo;
    TextView logoText, message;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        logo = findViewById(R.id.logo);
        logoText = findViewById(R.id.salutation);
        message = findViewById(R.id.msg);

        regName = findViewById(R.id.name);
        regPseudo = findViewById(R.id.reg_username);
        regEmail = findViewById(R.id.email);
        regPhoneNo = findViewById(R.id.phone_number);
        regPassword = findViewById(R.id.reg_password);
        regComfirmPassword = findViewById(R.id.comfirm_password);
        regButton = findViewById(R.id.new_account);
        backToLogin = findViewById(R.id.sign_in);


        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("Users");


                isValideUser(v);

                String name = regName.getEditText().getText().toString();
                String username = regPseudo.getEditText().getText().toString();
                String email = regEmail.getEditText().getText().toString();
                String phoneNo = regPhoneNo.getEditText().getText().toString();
                String password = regPassword.getEditText().getText().toString();
                String comfirmedPassword = regComfirmPassword.getEditText().getText().toString();

                UserHelperClass helperClass = new UserHelperClass(name, username, email, phoneNo, password);

                if (password.equals(comfirmedPassword)){

                    if (isValideName(name) && isValideUsername(username) && isValideEmail(email) && isValidePhoneNo(phoneNo) && isValidePassword(password))
                    {
                        reference.child(phoneNo).setValue(helperClass);
                        Intent intent = new Intent(SignUp.this, Login.class);
                        startActivity(intent);

                        Toast.makeText(SignUp.this, "Nouveau compte créer avec succès!", Toast.LENGTH_LONG).show();

                    }
                }
                else{
                    Toast.makeText(SignUp.this, "Les mots de passe diffèrent!", Toast.LENGTH_LONG).show();
                }
            }
        });

        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, Login.class);

                Pair[] pairs = new Pair[7];
                pairs[0] = new Pair<View, String>(logo, "logo_image");
                pairs[1] = new Pair<View, String>(logoText, "logo_text");
                pairs[2] = new Pair<View, String>(message, "message");
                pairs[3] = new Pair<View, String>(regName, "username_trans");
                pairs[4] = new Pair<View, String>(regPassword, "password_trans");
                pairs[5] = new Pair<View, String>(regButton, "login_trans");
                pairs[6] = new Pair<View, String>(backToLogin, "back_to_singup_trans");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp.this, pairs);
                    startActivity(intent, options.toBundle());
                }
            }
        });

    }

    public Boolean isValideName() {
        String val = regName.getEditText().getText().toString();
        if (val.isEmpty()) {
            regName.setError("Champs ne peut etre vide");
            return false;
        } else {
            regName.setError(null);
            regName.setErrorEnabled(false);
            return true;
        }

    }

    public Boolean isValideName(String str) {
        str = regName.getEditText().getText().toString();
        if (str.isEmpty()) {
            regName.setError("Champs ne peut être vide");
            return false;
        } else {
            regName.setError(null);
            regName.setErrorEnabled(false);
            return true;
        }

    }

    public Boolean isValideUsername() {
        String val = regPseudo.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";
        if (val.isEmpty()) {
            regPseudo.setError("Champs ne peut être vide");
            return false;
        } else if (val.length() > 15) {
            regPseudo.setError("Pseudo trop long");
            return false;
        } else if (!val.matches(noWhiteSpace)) {
            regPseudo.setError("Espace et symboles non authorisés");
            return false;
        } else {
            regPseudo.setError(null);
            regPseudo.setErrorEnabled(false);
            return true;
        }

    }

    public Boolean isValideUsername(String str) {
        str = regPseudo.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";
        if (str.isEmpty()) {
            regPseudo.setError("Champs ne peut être vide");
            return false;
        } else if (str.length() > 15) {
            regPseudo.setError("Pseudo trop long");
            return false;
        } else if (!str.matches(noWhiteSpace)) {
            regPseudo.setError("Espace et symboles non authorisés");
            return false;
        } else {
            regPseudo.setError(null);
            regPseudo.setErrorEnabled(false);
            return true;
        }

    }

    public Boolean isValideEmail() {
        String val = regEmail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (val.isEmpty()) {
            regEmail.setError("Champs ne peut être vide");
            return false;
        } else if (!val.matches(emailPattern)) {
            regEmail.setError("Email invalide");
            return false;
        } else {
            regEmail.setError(null);
            regEmail.setErrorEnabled(false);
            return true;
        }

    }

    public Boolean isValideEmail(String str) {
        str = regEmail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (str.isEmpty()) {
            regEmail.setError("Champs ne peut être vide");
            return false;
        } else if (!str.matches(emailPattern)) {
            regEmail.setError("Email invalide");
            return false;
        } else {
            regEmail.setError(null);
            regEmail.setErrorEnabled(false);
            return true;
        }

    }

    public Boolean isValidePhoneNo() {
        String val = regPhoneNo.getEditText().getText().toString();
        if (val.isEmpty()) {
            regPhoneNo.setError("Champs ne peut être vide");
            return false;
        } else {
            regPhoneNo.setError(null);
            regPhoneNo.setErrorEnabled(false);
            return true;
        }

    }

    public Boolean isValidePhoneNo(String str) {
        str = regPhoneNo.getEditText().getText().toString();
        if (str.isEmpty()) {
            regPhoneNo.setError("Champs ne peut être vide");
            return false;
        } else {
            regPhoneNo.setError(null);
            regPhoneNo.setErrorEnabled(false);
            return true;
        }

    }

    public Boolean isValidePassword() {
        String val = regPassword.getEditText().getText().toString();
        String passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        if (val.isEmpty()) {
            regPassword.setError("Champs ne peut être vide");
            return false;
        } else if (!val.matches(passwordPattern)) {
            regPassword.setError("Mot de passe trop faible");
            return false;
        } else {
            regPassword.setError(null);
            regPassword.setErrorEnabled(false);
            return true;
        }

    }

    public Boolean isValidePassword(String str) {
        str = regPassword.getEditText().getText().toString();
        String passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        if (str.isEmpty()) {
            regPassword.setError("Champs ne peut être vide");
            return false;
        } else if (!str.matches(passwordPattern)) {
            regPassword.setError("Mot de passe trop faible");
            return false;
        } else {
            regPassword.setError(null);
            regPassword.setErrorEnabled(false);
            return true;
        }

    }

    public Boolean isValideComfirmedPassword() {
        String val = regComfirmPassword.getEditText().getText().toString();
        if (val.isEmpty()) {
            regComfirmPassword.setError("Champs ne peut être vide");
            return false;
        } else {
            regComfirmPassword.setError(null);
            regComfirmPassword.setErrorEnabled(false);
            return true;
        }

    }

    public void isValideUser(View view) {

        if (!isValideName() | !isValideUsername() | !isValideEmail() | !isValidePhoneNo() | !isValidePassword() | !isValideComfirmedPassword()) {
            return;
        }
    }
}