package com.example.tictactoe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Multiplayer extends AppCompatActivity {
    private CoordinatorLayout coordinatorLayout;
    private Snackbar snackbar;

    private int board[][] = new int[3][3];
    //private TextView tv_player_x, tv_player_o, tv_reset, tv_play_again;
    //private MaterialAlertDialogBuilder alertBuilder;


    private Boolean currentPlayerTurn;
//    private String PLAYER_X = "Player X : ";
//    private String PLAYER_O = "Player O : ";
//    private int SCORE_PLAYER_X = 0;
//    private int SCORE_PLAYER_O = 0;

    int[][] btn_id = new int[3][3];
    Button[][] btn_board = new Button[3][3];


    private DatabaseReference databaseReference;
    private DatabaseReference boardReference,statusReference;
    private String gameID;

    private int currentPlayer;
    private int opponentPlayer;
    private String status;
    private TextView tv_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);

        //firebase
        databaseReference = FirebaseDatabase.getInstance().getReference();
        gameID = getIntent().getStringExtra("gameId");
        boardReference = databaseReference.child("game").child(gameID).child("board_data");
        statusReference = databaseReference.child("game").child(gameID).child("board_status");

        if(gameID.indexOf(UserData.UID)==0){
            currentPlayer = 1;
            opponentPlayer = 2;
            currentPlayerTurn =true;
        }
        else {
            currentPlayer = 2;
            opponentPlayer = 1;
            currentPlayerTurn = false;
        }
        Log.v("shanu","multiplayer curr->"+currentPlayer +"oppo->"+opponentPlayer);

        //casting
//        tv_play_again = (TextView) findViewById(R.id.btn_play_again);
//        tv_player_o = (TextView) findViewById(R.id.tv_player_o);
//        tv_player_x = (TextView) findViewById(R.id.tv_player_x);
//        tv_reset = (TextView) findViewById(R.id.btn_reset);
//
//
//        tv_reset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                restart();
//                SCORE_PLAYER_X = 0;
//                SCORE_PLAYER_O = 0;
//            }
//        });
//
//        tv_play_again.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                restart();
//            }
//        });

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String id = "button_" + i + j;
                int res_id = getResources().getIdentifier(id, "id", getPackageName());
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


        //alertBuilder = new MaterialAlertDialogBuilder(this);//,R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered);
//        tv_player_x.setText(PLAYER_X + SCORE_PLAYER_X);
//        tv_player_o.setText(PLAYER_O + SCORE_PLAYER_O);

        coordinatorLayout = findViewById(R.id.id_CoordinateLayout);
        tv_status = findViewById(R.id.tv_status);

        checkStatus();
        loadGameBoard();

    }

    private void alert(String msg) {
        snackbar = Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_INDEFINITE)
                .setAction("Lobby", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        backToLobby();
                    }
                });

        View snackView = snackbar.getView();
        Resources resources = getResources();

        snackView.setBackgroundColor(resources.getColor(R.color.gray800));
        snackbar.setActionTextColor(resources.getColor(R.color.green));
        snackbar.show();
        //snackbar.show();

        //// disable all button click
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0) {
                    btn_board[i][j].setEnabled(false);
                }
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backToLobby();
    }

    public void onClickBackButton(View view){
        backToLobby();
    }

    public void backToLobby(){
        boardReference.removeValue();
        Intent intent = new Intent(this,Lobby.class);
        intent.putExtra("UID",UserData.UID);
        startActivity(intent);
        finish();
    }


    public void buttonClicked(View v) {
        int i,j;
        if(currentPlayerTurn) {
            for (i = 0; i < 3; i++) {
                for (j = 0; j < 3; j++) {
                    if (v.getId() == btn_id[i][j]) {
                        Log.v("shanu", "clicked " + i + " " + j);
                        boardReference.child("board_" + i + j).setValue("" + currentPlayer);
                        board[i][j] = currentPlayer;
                        if(currentPlayer==1)
                            btn_board[i][j].setText("X");
                        else
                            btn_board[i][j].setText("O");
                        break;

                    }
                }
                if(j!=3)
                    break;
            }
            loadGameBoard();
            gameController();

        }
        else{
            v.setEnabled(true);
        }

    }




    public void winner() {
        Log.v("shanu", "winner");
        alert("Winner");
    }

    public void gameOver() {
        Log.v("shanu", "looser");
        alert("Game Over");
    }

    public void draw() {
        Log.v("shanu", "draw");
        alert("Draw");
    }

    class Cell{
        public String key;
        public String value;

        public Cell(){
            //default constructor
        }
        public Cell(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public void loadGameBoard(){
        final List<Cell> cellList=new ArrayList<>();
        //rendering data from server
        boardReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                        String key = ds.getKey();
                        String value = ds.getValue(String.class);
//                        Cell cell = new Cell(key,value);
//                        cellList.add(cell);
                        int i = Integer.parseInt("" + key.charAt(6));
                        int j = Integer.parseInt("" + key.charAt(7));
                        board[i][j] = Integer.parseInt(value);
                        if(board[i][j]==1){
                            btn_board[i][j].setText("X");
                            btn_board[i][j].setEnabled(false);
                        }
                        else if(board[i][j]==2){
                            btn_board[i][j].setText("O");
                            btn_board[i][j].setEnabled(false);
                        }
                        else{
                            btn_board[i][j].setEnabled(true);
                        }
                        Log.v("shanu",key+"->"+value);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //decoding data
//        int i,j;
//        for(Cell cell:cellList){
//            String key = cell.getKey();
//            Log.v("shanu","key="+key);
//            i = Integer.parseInt("" + key.charAt(6));
//            j = Integer.parseInt("" + key.charAt(7));
//            board[i][j] = Integer.parseInt(cell.getValue());
//
//        }
        printBoard();

        //loading board
//        int i,j;
//        for(i=0;i<3;i++){
//            for(j=0;j<3;j++){
//                if(board[i][j]==1){
//                    btn_board[i][j].setText("X");
//                    btn_board[i][j].setEnabled(false);
//                }
//                else if(board[i][j]==2){
//                    btn_board[i][j].setText("O");
//                    btn_board[i][j].setEnabled(false);
//                }
//                else{
//                    btn_board[i][j].setEnabled(true);
//                }
//            }
//        }
    }

    private void checkStatus(){
        status = new String();
        statusReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 if (dataSnapshot!=null){
                     status = dataSnapshot.getValue(String.class);
                     Log.v("shanu","status --> "+status);
                     if(status.equals("turn"+currentPlayer)){
                         currentPlayerTurn =true;
                         tv_status.setText("Your's\nTurn");
                     }else if(status.equals("turn"+opponentPlayer)){
                         currentPlayerTurn = false;
                         tv_status.setText("Oppnent's\nTurn");
                     }else if (status.equals("draw")) {
                         draw();
                     } else if (status.equals("win"+currentPlayer)) {
                         winner();
                     } else if (status.equals("win"+opponentPlayer)) {
                         gameOver();
                     }


                 }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.v("shanu","status oncancel "+databaseError.getMessage());
            }
        });
    }

    public void printBoard(){
        String s;
        Log.v("shanu","---Board---");
        for(int i=0;i<3;i++){
            s = "";
            for(int j=0;j<3;j++){
                s+=board[i][j]+" ";
            }
            Log.v("shanu",s);
        }
    }



    //////////////////////////
    //// Game Controller /////
    //////////////////////////

    private void gameController(){
        int score = evaluate(board);
        if (score==10) { //+10means current player winner
            status = "win"+currentPlayer;
            winner();
        }
        else if(score==-10) { //-10 means current player looses
            status = "win"+opponentPlayer;
            gameOver();
        }
        else if(isMoveLeft(board)==false){
            status ="draw";
            draw();
        }
        else if(currentPlayerTurn){
            //currentPlayerTurn=false;
            status = "turn"+opponentPlayer;
        }
        else if(!currentPlayerTurn){
            //currentPlayerTurn =true;
            status = "turn"+currentPlayer;
        }
        statusReference.setValue(status);
        loadGameBoard();
        checkStatus();

    }
    public  int evaluate(int[][] board){
        //for win +10
        //for loss -10
        //for draw 0
        for(int i=0;i<3;i++){
            //row wise matching
            if(board[i][0]==board[i][1] && board[i][1]==board[i][2]){
                if(board[i][0]==currentPlayer){
                    return 10;
                }
                if(board[i][0]==opponentPlayer){
                    return -10;
                }
            }

            //column wise matching
            if(board[0][i]==board[1][i] && board[1][i]==board[2][i]){
                if(board[0][i]==currentPlayer){
                    return 10;
                }
                if(board[0][i]==opponentPlayer){
                    return -10;
                }
            }
        }

        //primary diagonal matching
        if(board[0][0]==board[1][1] && board[1][1]==board[2][2]){
            if(board[0][0]==currentPlayer){
                return 10;
            }
            if(board[0][0]==opponentPlayer){
                return -10;
            }
        }

        //secondary diagonal matching
        if(board[0][2]==board[1][1] && board[1][1]==board[2][0]) {
            if (board[0][2] == currentPlayer) {
                return 10;
            }
            if (board[0][2] == opponentPlayer) {
                return -10;
            }
        }
        return 0;
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

    private String getCellData(int value){
        if(value==1)
            return "X";
        else
            return "O";
    }
}