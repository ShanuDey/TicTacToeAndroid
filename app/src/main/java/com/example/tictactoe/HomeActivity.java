package com.example.tictactoe;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class HomeActivity extends AppCompatActivity {
    private SwitchMaterial s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);


//        Log.v("shanu","DarkTheme "+s.isChecked());
//        if(s.isChecked()){
//            //setTheme(R.style.darkTheme);
//            setContentView(R.layout.activity_home_dark);
//        }
//        else{
//            //setTheme(R.style.AppTheme);
//            setContentView(R.layout.activity_home);
//        }

        s = findViewById(R.id.id_switch);
//
//
//
//
//        s.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.v("shanu","DarkTheme onclick "+s.isChecked());
//                recreate();
//            }
//        });
    }


    public void onClickSP(View v){
        Log.v("shanu","Single Player Clicked");
        Intent intent = new Intent(this,SinglePlayer.class);
        intent.putExtra("darkTheme",s.isChecked());
        startActivity(intent);
    }
    public void onClickTP(View v){
        Log.v("shanu","Two Player Clicked");
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("darkTheme",s.isChecked());
        startActivity(intent);
    }
}
