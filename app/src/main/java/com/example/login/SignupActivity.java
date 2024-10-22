package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    EditText Signup_name, Signup_phone, Signup_password;
    TextView Loginredirecttext;
    Button Btn_register;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // EdgeToEdge.enable(this); // Temporarily disabled for debugging
        setContentView(R.layout.activity_signup);

        Signup_name = findViewById(R.id.signup_name);
        Signup_phone = findViewById(R.id.signup_phone);
        Signup_password = findViewById(R.id.signup_password);
        Loginredirecttext = findViewById(R.id.loginredirecttext);
        Btn_register = findViewById(R.id.btn_register);

        if (Signup_name == null || Signup_phone == null || Signup_password == null || Loginredirecttext == null || Btn_register == null) {
            Toast.makeText(this, "One or more views are not found", Toast.LENGTH_LONG).show();
            Log.e(TAG, "One or more views are null");
            return;
        }

        Btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Register button clicked");
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("User");

                String name = Signup_name.getText().toString().trim();
                String phone = Signup_phone.getText().toString().trim();
                String password = Signup_password.getText().toString().trim();

                if (name.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                HelperClass helperClass = new HelperClass(name, phone, password);
                reference.child(name).setValue(helperClass).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignupActivity.this, "You have signed up successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SignupActivity.this, "Registration failed", Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Firebase registration failed", task.getException());
                    }
                });
            }
        });

        Loginredirecttext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Login redirect text clicked");
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
