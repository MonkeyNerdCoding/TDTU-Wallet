package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    EditText Login_name,Login_password;
    TextView Signupredirecttext;
    Button Btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        Login_name = findViewById(R.id.login_name);
        Login_password = findViewById(R.id.login_password);
        Signupredirecttext = findViewById(R.id.signupredirecttext);
        Btn_login = findViewById(R.id.btn_login);


        Btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validateUsername()||!validateUserpassword()){

                }else{
                    checkUser();
                }
            }
        });

        Signupredirecttext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });

    }

    public Boolean validateUsername(){
        String val = Login_name.getText().toString();
        if(val.isEmpty()){
            Login_name.setError("User name is Empty");
            return false;
        }else{
            Login_name.setError(null);
            return true;
        }
    }


    public Boolean validateUserpassword(){
        String val = Login_password.getText().toString();
        if(val.isEmpty()){
            Login_password.setError("User name is Empty");
            return false;
        }else{
            Login_password.setError(null);
            return true;
        }
    }

    public void checkUser(){
        String userUsername = Login_name.getText().toString().trim();
        String userUserpassword = Login_password.getText().toString().trim();
        String username = Login_name.getText().toString();
        String password = Login_password.getText().toString();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Login_name.setError(null);
                    String passwordFromDB = snapshot.child(userUsername).child("password").getValue(String.class);
                    if (passwordFromDB.equals(userUserpassword)){
                        Login_name.setError(null);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("username", username);
                        intent.putExtra("password", password);
                        startActivity(intent);
                    }else{
                        Login_password.setError("Invalid");
                        Login_password.requestFocus();
                    }
                }else{
                    Login_name.setError("User does not exits");
                    Login_name.requestFocus();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}