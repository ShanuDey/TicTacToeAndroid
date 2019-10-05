package com.example.tictactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText et_email, et_pass;
    private FirebaseAuth mAuth;

    MaterialAlertDialogBuilder materialAlertDialogBuilder;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_email = findViewById(R.id.et_email);
        et_pass = findViewById(R.id.et_pass);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        alertDialog = getAlertDialog().create();

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public MaterialAlertDialogBuilder getAlertDialog() {
        if(materialAlertDialogBuilder==null){
            materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this);
            materialAlertDialogBuilder.setView(R.layout.loading)
                    .setCancelable(false);
        }
        return materialAlertDialogBuilder;
    }

    public void onClickLogin(View v){

        String email = et_email.getText().toString();
        String password = et_pass.getText().toString();
        Log.v("shanu","Email ="+email);

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(!email.matches(emailPattern)){
            et_email.setError("Inalid Email");
            //Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.length()<6){
            et_pass.setError("Length must be greater than 6 letter");
            return;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alertDialog.show();
            }
        });

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.v("shanu", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.v("shanu", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
    public void onClickRegister(View v){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }
    private void updateUI(FirebaseUser currentUser){
        if(currentUser!=null) {
            Log.v("shanu", "updateUI called from Login || user is logded in");
            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Lobby.class);
            startActivity(intent);
            finish();
        }
        else{
            Log.v("shanu", "updateUI called from Login || user is not login");
        }

        try {
            alertDialog.dismiss();
        }catch (Exception e){

        }
    }

}
