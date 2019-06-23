package com.example.tictactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText et_username,et_email,et_password;
    private FirebaseAuth mAuth;
    private String username,email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_username= findViewById(R.id.et_username);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_pass);

        mAuth = FirebaseAuth.getInstance();
    }
    public void onClickRegister(View v){
        username = et_username.getText().toString();
        email = et_email.getText().toString();
        password = et_password.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.v("shanu", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.v("shanu", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });

    }
    public void onClickBackToLogin(View v){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void updateUI(FirebaseUser currentUser){
        if(currentUser!=null){
            Log.v("shanu","register done adding data to server");

            DatabaseReference mDatabase;

            mDatabase = FirebaseDatabase.getInstance().getReference("/user");

            String uid = currentUser.getUid();

            User user = new User(username,email,uid);

            String pushKey = mDatabase.push().getKey();
            mDatabase.child(pushKey).setValue(user);

            Toast.makeText(this, "Register Successful", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,Lobby.class);
            startActivity(intent);
            finish();
        }
    }
}
