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
    private String UID;
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

        UID = getIntent().getStringExtra("UID");

        addUserOnQueue();
        matching();


    }
    public void matching(){
        databaseReference.child("online").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                        User user = ds.getValue(User.class);
                        Log.v("shanu","user -> "+user.name+ "userid->"+user.userId);
                        if(!UID.equals(user.userId.toString())){
                            gameLoading(user.userId);
                        }
                    }
                }
                else{
                    addUserOnQueue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.v("shanu","matchingListner cancelled");
            }
        });

    }

    private void gameLoading(String opponetId){
        String gameId = UID+"~"+opponetId;
        databaseReference.child("game").child(gameId).child("board").setValue(-1);
        Intent intent = new Intent(this,Multiplayer.class);
        intent.putExtra("gameId",gameId);
        startActivity(intent);
        finish();
    }



    private void addUserOnQueue(){
        Log.v("shanu","inside add user");
        databaseReference.child("user").orderByChild("userId")
                .equalTo(UID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            myUser = dataSnapshot.getValue(User.class);
                            Log.v("shanu","addUserOnQueue username->"+myUser.name+" userID->"+myUser.userId);
                        }
                        else{
                            myUser = new User("dummy","dummy@gmail.com",UID);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.v("shanu","add user error Matching.java");
                    }
                });

        DatabaseReference myref = databaseReference.child("online");
        onlineToken = myref.push().getKey();
        //User user = new User("Shanu"+i,"shanu"+i+"@gmail.com",UID);
        myref.child(onlineToken).setValue(myUser);
    }

    public void cancel(){
        databaseReference.child("game").child(onlineToken).removeValue();
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
