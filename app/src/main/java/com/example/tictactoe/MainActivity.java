package com.example.tictactoe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private TextView tv_player_x,tv_player_o,tv_reset,tv_play_again;

    private int board[][] = new int[3][3];

    private Button btn00,btn01,btn02,btn10,btn11,btn12,btn20,btn21,btn22;

    private Button[][] btn_board = new Button[][]{
            {btn00,btn01,btn02},
            {btn10,btn11,btn12},
            {btn20,btn21,btn22}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tv_play_again = (TextView) findViewById(R.id.btn_play_again);
        tv_player_o = (TextView) findViewById(R.id.tv_player_o);
        tv_player_x = (TextView) findViewById(R.id.tv_player_x);
        tv_reset = (TextView) findViewById(R.id.btn_reset);

        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                String id = "button_"+i+j;
                int res_id = getResources().getIdentifier(id,"id",getPackageName());

            }
        }

    }

    private void restart(){
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                btn_board[i][j].setEnabled(true);
            }
        }
    }

    private void onBoardButtonClick(int i,int j){

    }
}
