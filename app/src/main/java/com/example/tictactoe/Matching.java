package com.example.tictactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Contacts;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Matching extends AppCompatActivity {
    private ProgressBar progressBar;
    private TextView tv_matching;
    private String OPPONENT_ID;
    private String onlineToken;
    private User myUser;

    private DatabaseReference databaseReference;

    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching);

        tv_matching = (TextView) findViewById(R.id.tv_matching);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        OPPONENT_ID = getIntent().getStringExtra("opponentId");

        Log.v("shanu","opponetId"+OPPONENT_ID);


    }
    public void matching(){
        //UserData.CURRENT_PLAYER

    }

    private void gameLoading(String opponetId){
//        String gameId = UID+"~"+opponetId;
//        databaseReference.child("game").child(gameId).child("board").setValue(-1);
//        Intent intent = new Intent(this,Multiplayer.class);
//        intent.putExtra("gameId",gameId);
//        startActivity(intent);
//        finish();
    }


    public void cancel(){
        //databaseReference.child("game").child(onlineToken).removeValue();
    }

    @Override
    protected void onResume() {
        super.onResume();
        matching();
    }

    public void onClickCancel(View view){
        Log.v("shanu","cancel clicked");
        cancel();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        cancel();
    }


}
