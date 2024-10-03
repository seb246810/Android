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

public class Authentication extends AppCompatActivity {

    Button login; //instance of button


    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        login = findViewById(R.id.button3); //giving button ID

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Authentication.this,ExistingUser.class); //Using a listener to go to login activity.
                startActivity(intent); //start intent to move to new activity
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
            Intent intent = new Intent(Authentication.this, MainActivity.class);
            startActivity(intent);
        }
    }


    public void signup(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password) //create entry in database with email and password
                .addOnCompleteListener(this, new
                        OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult>
                                                           task) {
                                if (task.isSuccessful()) {
                                    Log.d("MainActivity",
                                            "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(Authentication.this,
                                            "Authentication success. Use an intent to move to a new activity",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent =new Intent(Authentication.this,MainActivity.class); //intent moves to main activity on success.
                                            startActivity(intent);
                                    //user has been signed in, use an intent tomove to the next activity
                                } else {
                                    // If sign in fails, display a message to theuser.
                                            Log.w("MainActivity",
                                                    "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(Authentication.this,
                                            "Authentication failed. email already taken",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
    }

    public void signupButtonClicked(View view){
        EditText email = findViewById(R.id.editTextTextEmailAddress);
        EditText password = findViewById(R.id.editTextTextPassword);   //make instances of Edittext class.
        EditText confirmpassword = findViewById(R.id.confirmpassword);

        String sEmail = email.getText().toString();
        String sPassword = password.getText().toString();
        String sconfirmpassword = confirmpassword.getText().toString();     //store as strings  to pass to database function.

        if (TextUtils.isEmpty(sEmail) || TextUtils.isEmpty(sPassword) ) {
            Toast.makeText(getApplicationContext(),
                            "Please enter email!! Or Password!!!",      //check that text fields are filled.
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }

        else if(! sconfirmpassword.equals(sPassword) ){
            Toast.makeText(getApplicationContext(),
                            "Password and confirm password do not match !",  //check confirm password is working.
                            Toast.LENGTH_LONG)
                    .show();
        }

        else if(sPassword.length() <5){

            Toast.makeText(getApplicationContext(),
                            "Password not long enough", //password length checker too short
                            Toast.LENGTH_LONG)
                    .show();

        }

        else{

            signup(sEmail, sPassword); //load firebase function if all before has passed with the string variables.

        }


    }

}