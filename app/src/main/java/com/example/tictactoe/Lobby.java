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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        UID = getIntent().getStringExtra("UID");

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


    private void loadData(){
        FirebaseDatabase.getInstance().getReference().child("online")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Player> temp = new ArrayList<>();
                        for(DataSnapshot ds:dataSnapshot.getChildren()){
                            Player player = ds.getValue(Player.class);
//
//                            //Condition checking or such kind of extra code looses the reference to the object
//                            //thats why it is not showing data
//
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
}
