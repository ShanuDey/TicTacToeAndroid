package com.example.tictactoe;

import android.content.DialogInterface;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;


public class MainActivity extends AppCompatActivity{

    private TextView tv_player_x,tv_player_o,tv_reset,tv_play_again;

    private int board[][] = new int[3][3];

    private MaterialAlertDialogBuilder alertBuilder;


    private Boolean playerXturn = true;
    private int round = 0;
    private String PLAYER_X = "Player X : ";
    private String PLAYER_O = "Player O : ";
    private int SCORE_PLAYER_X = 0;
    private int SCORE_PLAYER_O = 0;

    int[][] btn_id = new int[3][3];
    Button[][] btn_board = new Button[3][3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tv_play_again = (TextView) findViewById(R.id.btn_play_again);
        tv_player_o = (TextView) findViewById(R.id.tv_player_o);
        tv_player_x = (TextView) findViewById(R.id.tv_player_x);
        tv_reset = (TextView) findViewById(R.id.btn_reset);

        tv_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restart();
                SCORE_PLAYER_X = 0;
                SCORE_PLAYER_O = 0;
            }
        });

        tv_play_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restart();
            }
        });

        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                String id = "button_"+i+j;
                int res_id = getResources().getIdentifier(id,"id",getPackageName());
                btn_id[i][j] = res_id;
                btn_board[i][j] = (Button) findViewById(res_id);
                btn_board[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buttonClicked(v);
                    }
                });
            }
        }


        alertBuilder = new MaterialAlertDialogBuilder(this);//,R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered);
        tv_player_x.setText(PLAYER_X+SCORE_PLAYER_X);
        tv_player_o.setText(PLAYER_O+SCORE_PLAYER_O);

    }

    private void alert(String title,String msg){
        AlertDialog dialog = alertBuilder.setTitle(title)
                .setMessage(msg)
                .setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        restart();
                    }
                })
                .setCancelable(false)
                .create();
        dialog.show();
        Button posBtn = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        posBtn.setBackgroundColor(getResources().getColor(R.color.gray500));

    }

    public void buttonClicked(View v){
        round++;
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(v.getId()==btn_id[i][j]){
                    Log.v("shanu","clicked "+i+" "+j);
                    if(playerXturn){
                        board[i][j] = 1;
                        ((Button)v).setText("X");
                        playerXturn = false;
                    }
                    else{
                        board[i][j]=2;
                        ((Button)v).setText("O");
                        playerXturn = true;
                    }
                    ((Button)v).setEnabled(false);
                    break;
                }
            }
        }
        check();
    }


    public void check(){
        int diagonal_x_count = 0;
        int diagonal_y_count = 0;
        for(int i=0;i<3;i++){
            //horinzontal
            if(board[i][0]==1 && board[i][1]==1 && board[i][2]==1){
                playerXwin();
                return;
            }
            if(board[i][0]==2 && board[i][1]==2 && board[i][2]==2){
                playerOwin();
                return;
            }
            //vertical
            if(board[0][i]==1 && board[1][i]==1 && board[2][i]==1){
                playerXwin();
                return;
            }
            if(board[0][i]==2 && board[1][i]==2 && board[2][i]==2){
                playerOwin();
                return;
            }
            if(board[i][i]==1)
                diagonal_x_count++;
            else if(board[i][i]==2)
                diagonal_y_count++;
        }
        if(diagonal_x_count==3) {
            playerXwin();
            return;
        }
        else if(diagonal_y_count==3) {
            playerOwin();
            return;
        }
        if(board[0][2]==1 && board[1][1]==1 && board[2][0]==1){
            playerXwin();
            return;
        }
        if(board[0][2]==2 && board[1][1]==2 && board[2][0]==2){
            playerOwin();
            return;
        }

        if(round==9)
            draw();
    }

    public void playerXwin(){
        Log.v("shanu","player x won");
        alert("Winner","Player X won");
        SCORE_PLAYER_X++;
    }
    public void playerOwin(){
        Log.v("shanu","player O won");
        alert("Winner","Player O won");
        SCORE_PLAYER_O++;
    }
    public void draw(){
        Log.v("shanu","draw");
        alert("Winner","Draw");
    }

    private void restart(){
        playerXturn =true;
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                btn_board[i][j].setEnabled(true);
                btn_board[i][j].setText("");
                board[i][j]=0;
            }
        }
        round=0;
        tv_player_x.setText(PLAYER_X+SCORE_PLAYER_X);
        tv_player_o.setText(PLAYER_O+SCORE_PLAYER_O);

    }
}
