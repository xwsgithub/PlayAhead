package com.example.y.playahead.PA;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;

import com.example.y.playahead.R;
import com.example.y.playahead.game2048.homeActivityfor2048;
import com.example.y.playahead.sd.sd_Sudoku;
import com.example.y.playahead.szhrd.szhrd_start;

import java.util.Timer;
import java.util.TimerTask;

public class PALoading extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pa_loading);

//        ProgressDialog progressDialog=new ProgressDialog(this);
//        progressDialog.show();

        Intent intent = getIntent();
        final int module = intent.getIntExtra("game", -1);
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent it = new Intent();
                switch (module) {
                    case 1:
                        it.setClass(PALoading.this,sd_Sudoku.class);
                        break;
                    case 2:
                        it.setClass(PALoading.this,homeActivityfor2048.class);
                        break;
                    case 3:
                        it.setClass(PALoading.this,szhrd_start.class);
                }
                startActivity(it);
                finish();
            }
        };
        timer.schedule(task, 1000);
    }
}