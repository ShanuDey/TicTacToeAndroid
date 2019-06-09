package com.example.tictactoe;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


    }

    public void onClickSP(View v){
        Log.v("shanu","Single Player Clicked");
    }
    public void onClickTP(View v){
        Log.v("shanu","Two Player Clicked");
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
