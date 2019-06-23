package com.example.tictactoe;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.PlayerViewHolder> {
    List<Player> playerList;

    public PlayerListAdapter() {
        //default constructor
    }

    public PlayerListAdapter(List<Player> playerList) {
        this.playerList = playerList;
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.player_card,parent,false);
        PlayerViewHolder playerViewHolder = new PlayerViewHolder(view);
        return  playerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        Player player = playerList.get(position);
        holder.setData(player);
    }

    @Override
    public int getItemCount() {
        Log.v("shanu","size = "+playerList.size());
        return playerList.size();
    }

    public class PlayerViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_playerName;
        private Button btn_invite;
        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_playerName = itemView.findViewById(R.id.tv_playerName);
            btn_invite = itemView.findViewById(R.id.btn_invite);
        }
        public void setData(Player player){
            tv_playerName.setText(player.name);
        }
    }
}