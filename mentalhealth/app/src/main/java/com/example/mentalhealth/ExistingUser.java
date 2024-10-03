package com.example.mentalhealth;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ExistingUser extends AppCompatActivity {
    private FirebaseAuth mAuth;

    Button signup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing_user);

        signup = findViewById(R.id.button5);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExistingUser.this,Authentication.class);
                startActivity(intent);

            }
        });

        mAuth = FirebaseAuth.getInstance();
    }



    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            //User is signed in use an intent to move to another activity
            Intent intent = new Intent(ExistingUser.this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void signin(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user'sinformation
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(ExistingUser.this, "Successfully logged in !",
                            Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ExistingUser.this,MainActivity.class);
                            startActivity(intent);
                        }



                        else {


                                Log.w("MainActivity", "signInWithEmail:failure", task.getException());
                                Toast.makeText(ExistingUser.this, "Authentication failed. Incorrect username or password",
                                        Toast.LENGTH_SHORT).show();



                            // If sign in fails, display a message to the user.


                        }
                    }
                });
    }

    public void Loginclicked(View view){
        EditText email = findViewById(R.id.editTextTextEmailAddress);
        EditText password = findViewById(R.id.editTextTextPassword);

        String lEmail = email.getText().toString();
        String lPassword = password.getText().toString();

        if (TextUtils.isEmpty(lEmail) || TextUtils.isEmpty(lPassword)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter email!! Or Password!!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }


        signin(lEmail, lPassword);







    }


}