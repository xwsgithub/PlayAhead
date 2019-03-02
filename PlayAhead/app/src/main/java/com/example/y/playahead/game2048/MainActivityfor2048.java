package com.example.y.playahead.game2048;


import android.content.SharedPreferences.Editor;

import android.os.Bundle;
import android.view.View;

import android.widget.TextView;
import android.content.SharedPreferences;
import android.app.Activity;

import com.example.y.playahead.R;


public class MainActivityfor2048 extends Activity {


    private TextView reset;

    public MainActivityfor2048(){
        mainActivity=this;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainfor2048);
        sp=getSharedPreferences("TopScore",0);
        editor=sp.edit();

        tvScore=findViewById(R.id.tvScore);
        tvBestScore=findViewById(R.id.tvbestscore);
        if(homeActivityfor2048.getInstance().load){
            loadingscoresp=getSharedPreferences("score_file",0);
            tvScore.setText(loadingscoresp.getInt("Score",0)+"");
            homeActivityfor2048.getInstance().load=false;
        }else {
            tvScore.setText("0");
        }
        tvScore.setTextSize(25);
        tvScore.setTextColor(0xfff5f5f5);

        tvBestScore.setText(sp.getInt("Best",0)+"");
        tvBestScore.setTextSize(25);
        tvBestScore.setTextColor(0xfff5f5f5);



        reset=findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GameViewfor2048.getGameview().startGame();

            }
        });



    }

    private GameViewfor2048 game;
    public int Score=0;

    private TextView tvScore;
    private TextView tvBestScore;
    private SharedPreferences sp;
    private SharedPreferences loadinggamesp;
    private SharedPreferences loadingscoresp;
    private Editor loadinggameeditor;
    private Editor editor;
    private Editor loadingscoreeditor;
    public String string;


    private static MainActivityfor2048 mainActivity=null;

    public void showScore(){

        tvScore.setText(Score+"");
        tvScore.setTextSize(25);
        tvScore.setTextColor(0xfff5f5f5);

        tvBestScore.setText(sp.getInt("Best",0)+"");
        tvBestScore.setTextSize(25);
        tvBestScore.setTextColor(0xfff5f5f5);
    }

    public void addScore(int s){
        Score+=s;
        if(Score>sp.getInt("Best",0)){
            editor.putInt("Best",Score);
            editor.commit();
        }
        showScore();
    }

    public void clearScore(){
        if(Score!=0){
            Score=0;
            showScore();
        }
    }
    public static MainActivityfor2048 getMainActivity(){
        return mainActivity;
    }

    public void record(String points){

        string=points;
        loadinggamesp=getSharedPreferences("position_file",0);
        loadinggameeditor=loadinggamesp.edit();
        loadinggameeditor.putString("Position",points);
        loadinggameeditor.apply();

        loadingscoresp=getSharedPreferences("score_file",0);
        loadingscoreeditor=loadingscoresp.edit();
        loadingscoreeditor.putInt("Score",Score);
        loadingscoreeditor.apply();


    }


}
