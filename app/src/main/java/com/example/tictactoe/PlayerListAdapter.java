package com.example.tictactoe;

import android.content.Context;
import android.content.Intent;
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
    Context context;

    public PlayerListAdapter() {
        //default constructor
    }

    public PlayerListAdapter(List<Player> playerList) {
        this.playerList = playerList;

    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
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
        Player player;
        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_playerName = itemView.findViewById(R.id.tv_playerName);
            btn_invite = itemView.findViewById(R.id.btn_invite);

            btn_invite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,Matching.class);
                    intent.putExtra("opponentId",player.getUserId());
                    context.startActivity(intent);
                }
            });
        }
        public void setData(Player player){
            this.player = player;
            tv_playerName.setText(player.name);
            //btn_invite.setText(player.status);

            //status
            if(player.status.equals("idle")){
                btn_invite.setEnabled(true);
                btn_invite.setText("Invite");
            }
            else{
                btn_invite.setEnabled(false);
                btn_invite.setText("Busy");
            }

            //visibility
            if(player.userId.equals(UserData.UID)){
                btn_invite.setVisibility(Button.INVISIBLE);
            }
            else{
                btn_invite.setVisibility(Button.VISIBLE);
            }
        }

    }
}
