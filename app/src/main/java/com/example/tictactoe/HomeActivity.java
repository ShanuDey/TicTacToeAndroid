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

        s = findViewById(R.id.id_switch);

    }

    public void onClickTheme(View v){
        Intent intent = new Intent(this,HomeActivityDark.class);
        startActivity(intent);
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
