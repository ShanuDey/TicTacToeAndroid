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
    private String gameId;

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

        Log.v("shanu","opponetId ->"+OPPONENT_ID);

        gameId = UserData.UID+"~"+OPPONENT_ID;

        databaseReference.child("online").child(OPPONENT_ID).child("status").setValue("requested");
        databaseReference.child("online").child(OPPONENT_ID).child("opponentId").setValue(UserData.UID);
        databaseReference.child("game").child(gameId).child("board_status").setValue("requested");

    }
    public void matching(){
        //UserData.CURRENT_PLAYER
        databaseReference.child("game").child(gameId).child("board_status")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String status = dataSnapshot.getValue(String.class);
                        if(status!=null) {
                           if (status.equals("accepted")) {
                                Log.v("shanu", "accepted");
                                gameLoading();
                           }
                           else if(status.equals("rejected")){
                                rejected();
                           }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void gameLoading(){
        //databaseReference.child("game").child(gameId).child("board").setValue(-1);
        Intent intent = new Intent(this,Multiplayer.class);
        //intent.putExtra("gameId",gameId);
        startActivity(intent);
        finish();
    }

    public void rejected(){
        Intent intent = new Intent(this,Lobby.class);
        intent.putExtra("UID",UserData.UID);
        startActivity(intent);
        finish();
    }

    public void cancel(){
        //databaseReference.child("game").child(onlineToken).removeValue();
        databaseReference.child("online").child(OPPONENT_ID).child("status").setValue("idle");
        rejected();
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
