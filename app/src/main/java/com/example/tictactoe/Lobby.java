package com.example.tictactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
    protected void onPause() {
        super.onPause();
        databaseReference.child("online").child(UID).removeValue();
    }
}
