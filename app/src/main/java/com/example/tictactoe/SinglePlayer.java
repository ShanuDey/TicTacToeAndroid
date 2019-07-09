package com.example.tictactoe;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

public class SinglePlayer extends AppCompatActivity {

//********************************
//    Author : Shanu Dey
//********************************



//    Note:   for player X -> 1          -> score -10
//            for player O -> 2 (bot_AI) -> score +10
//            for blank    -> 0


    private TextView tv_player_x,tv_player_o,tv_reset,tv_play_again;
    private CoordinatorLayout coordinatorLayout;
    private Snackbar snackbar;

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

    private boolean darkTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_single_player_dark);

        //set theme
        darkTheme = getIntent().getBooleanExtra("darkTheme",false);

        if(darkTheme){
            setContentView(R.layout.activity_single_player_dark);
        }
        else{
            setContentView(R.layout.activity_single_player);
        }


        //casting
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

        coordinatorLayout = findViewById(R.id.id_CoordinateLayout);

    }

    private void alert(String title,String msg){
//        AlertDialog dialog = alertBuilder.setTitle(title)
//                .setMessage(msg)
//                .setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        restart();
//                    }
//                })
//                .setCancelable(false)
//                .create();
//        dialog.show();
//        Button posBtn = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//        posBtn.setBackgroundColor(getResources().getColor(R.color.gray500));


        /////Toast
        //Toast.makeText(this, title+" : "+msg, Toast.LENGTH_LONG).show();

        ////// Snakbar -> Material Design :)
        snackbar = Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_INDEFINITE)
               .setAction("Play Again", new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                      restart();
                   }
               });

        View snackView = snackbar.getView();
        Resources resources = getResources();

        if(darkTheme){
            snackView.setBackgroundColor(resources.getColor(R.color.gray500));

        }
        else{
            snackView.setBackgroundColor(resources.getColor(R.color.gray800));
        }

        snackbar.setActionTextColor(resources.getColor(R.color.green));
        snackbar.show();


        //// disable all button click
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(board[i][j]==0){
                    btn_board[i][j].setEnabled(false);
                }
            }
        }


    }

    public void displayLog(){
        for(int i=0;i<3;i++){
            String s ="board : ";
            for(int j=0;j<3;j++){
                s+=board[i][j]+" ";
            }
            Log.v("shanu",s);
        }
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
        snackbar.dismiss();

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
        gameController();
    }


    //////////////////////////
    //// Game Controller /////
    //////////////////////////

    private void gameController(){
        displayLog();

        int score = evaluate(board);
        if(score==-10) { //-10 means player X won
            playerXwin();
            return;
        }
        else if (score==10) { //+10means player  won
            playerOwin();
            return;
        }




//        if(score == 10){
//            if(playerXturn){
//                playerXwin();
//            }
//            else{
//                playerOwin();
//            }
//            return;
//        }



        if(isMoveLeft(board)==false){
            draw();
            return;
        }
        if(playerXturn==false && score!=10){
            Position bestPos = findBestMove(board);
            //board[bestPos.i][bestPos.j] = 1;
            buttonClicked(btn_board[bestPos.i][bestPos.j]);
            return;
        }
    }


    ///////////////////////////
    /////// game logic ////////
    ///////////////////////////

    public  int evaluate(int[][] board){
        //for win +10
        //for loss -10
        //for draw 0
        for(int i=0;i<3;i++){
            //row wise matching
            if(board[i][0]==board[i][1] && board[i][1]==board[i][2]){
                if(board[i][0]==2){
                    return 10;
                }
                if(board[i][0]==1){
                    return -10;
                }
            }

            //column wise matching
            if(board[0][i]==board[1][i] && board[1][i]==board[2][i]){
                if(board[0][i]==2){
                    return 10;
                }
                if(board[0][i]==1){
                    return -10;
                }
            }
        }

        //primary diagonal matching
        if(board[0][0]==board[1][1] && board[1][1]==board[2][2]){
            if(board[0][0]==2){
                return 10;
            }
            if(board[0][0]==1){
                return -10;
            }
        }

        //secondary diagonal matching
        if(board[0][2]==board[1][1] && board[1][1]==board[2][0]) {
            if (board[0][2] == 2) {
                return 10;
            }
            if (board[0][2] == 1) {
                return -10;
            }
        }
        return 0;
    }

    private class Position{
        int i,j;
        Position(int i,int j){
            this.i = i;
            this.j = j;
        }

        public void changeValue(int x,int y){
            i=x;
            j=y;
        }
    }

    private boolean isMoveLeft(int[][] board){
        for(int i=0;i<3;i++){
            for (int j=0;j<3;j++){
                if(board[i][j]==0)
                    return true;
            }
        }
        return false;
    }

    private int minimax(int[][] board, int depth,boolean isMax){
        int score = evaluate(board);

        //if anyone wins i.e, game over
        if(score==10 || score==-10){
            return score;
        }

        if(isMoveLeft(board) == false){
            return 0;
        }

        if(isMax){
            int bestValue = Integer.MIN_VALUE;
            for(int i=0;i<3;i++){
                for(int j=0;j<3;j++){
                    if(board[i][j]==0){
                        //place a move
                        board[i][j] = 1; //changed

                        //go further
                        bestValue = Math.max(bestValue,minimax(board,depth+1,false));

                        //revert the move
                        board[i][j] = 0;
                    }
                }
            }
            return bestValue;
        }
        else{
            int worstValue = Integer.MAX_VALUE;
            for(int i=0;i<3;i++){
                for(int j=0;j<3;j++){

                    if(board[i][j]==0){
                        //place a move
                        board[i][j] = 2;//changed

                        //go further
                        worstValue = Math.min(worstValue,minimax(board,depth+1,true));

                        //revert the move
                        board[i][j] = 0;
                    }
                }
            }
            return worstValue;
        }
    }

    private Position findBestMove(int[][] board){
        int bestValue = Integer.MIN_VALUE;
        Position bestPos =new Position(-1,-1);

        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(board[i][j]==0){
                    //place a move
                    board[i][j] = 2;

                    //calculate move value
                    int moveValue = minimax(board,0,true);


                    //revert move
                    board[i][j] = 0;

                    if(moveValue>bestValue){
                        bestValue = moveValue;
                        bestPos.changeValue(i,j);
                        Log.v("shanu","moveValue = "+moveValue+" position = "+i+" "+j);
                    }
                }
            }
        }
        return bestPos;
    }


}


