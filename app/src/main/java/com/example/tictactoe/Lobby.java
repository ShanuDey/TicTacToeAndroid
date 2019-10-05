package com.example.tictactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Lobby extends AppCompatActivity {
    private String UID;
    private RecyclerView recyclerView;
    private List<Player> playerList =new ArrayList<>();
    private PlayerListAdapter playerListAdapter;
    //private TextView tv_online;
    private DatabaseReference databaseReference;

    private MaterialAlertDialogBuilder materialAlertDialogBuilder;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        UID = getIntent().getStringExtra("UID");
        Log.v("shanu","lobby userid "+UID);
        UserData.UID = UID;

        databaseReference = FirebaseDatabase.getInstance().getReference();

        createPlayer();

        //tv_online = (TextView) findViewById(R.id.tv_online);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        //adapter
        //playerList = new ArrayList<>();
        playerListAdapter = new PlayerListAdapter(playerList);

        //set adapter
        recyclerView.setAdapter(playerListAdapter);

        materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this);

        checkStatus();
    }

    public void checkStatus(){
        databaseReference.child("online").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Player player = dataSnapshot.getValue(Player.class);
                if(player!=null && player.getStatus().equals("requested")){
                    UserData.CURRENT_PLAYER = player;
                    inviteAlert("Invitation Request","Will you want to play?");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.v("shanu","CheckStatus cancelled");
            }
        });
    }

    private void inviteAlert(String title,String msg){
        alertDialog = materialAlertDialogBuilder.setTitle(title)
                .setMessage(msg)
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onAccept();
                    }
                })
                .setNegativeButton("Reject", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onReject();
                    }
                })
                .setCancelable(false)
                .show();
        Button posBtn = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        posBtn.setBackgroundColor(getResources().getColor(R.color.gray500));
        Button negBtn = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negBtn.setBackgroundColor(getResources().getColor(R.color.gray500));

    }
    public void onAccept(){
        Log.v("shanu"," on accept");
        databaseReference.child("online").child(UID).child("status").setValue("playing");
        gameLoading();

    }
    public void gameLoading(){
        String gameId =UserData.CURRENT_PLAYER.opponentId+"~"+UID;
        databaseReference.child("game").child(UserData.CURRENT_PLAYER.opponentId+"~"+UID).child("board_status").setValue("accepted");
        Intent intent = new Intent(this,Multiplayer.class);
        intent.putExtra("gameId",gameId);
        startActivity(intent);
    }

    public void onReject(){
        Log.v("shanu","on reject");
        databaseReference.child("online").child(UID).child("status").setValue("idle");
        databaseReference.child("game").child(UserData.CURRENT_PLAYER.opponentId+"~"+UID).child("board_status").setValue("rejected");
    }

    public void createPlayer(){
        try {
            databaseReference.child("user").child(UID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        Player player = new Player(user.getEmail(), user.getName(), UID, "idle", "none");
                        UserData.CURRENT_PLAYER = player;
                        databaseReference.child("online").child(UID).setValue(player);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.v("shanu", "getplaer" + databaseError.getDetails());
                }
            });
        }catch (Exception e){

        }
    }

    private void loadData(){
        databaseReference.child("online")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Player> temp = new ArrayList<>();
                        for(DataSnapshot ds:dataSnapshot.getChildren()){
                            Player player = ds.getValue(Player.class);
//                            Log.v("shanu","players list player-> "+player.getEmail() + "name->"+player.getName());
//                            if(!UID.equals(player.userId)){
//                                playerList.add(player);
//                            }
                            temp.add(player);

                        }

                        playerList.clear();
                        playerList.addAll(temp);
                        playerListAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.v("shanu","Lobby data load onCancelled"+databaseError.getDetails());
                    }
                });
    }


    public void onClickLogout(View v){
        Log.v("shanu","Logout");
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
        finish();
    }
    public void onClickStartMatching(View view){
        Log.v("shanu","Start Matching");
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this,Matching.class);
        intent.putExtra("UID",UID);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        removeOnline();
    }

    private void removeOnline(){
        databaseReference.child("online").child(UID).removeValue();
    }
}
