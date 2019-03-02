package com.example.y.playahead.sd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;

import com.example.y.playahead.R;

public class sd_Sudoku extends Activity implements OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.sd_start);

        View continueButton = findViewById(R.id.continue_button);
        continueButton.setOnClickListener(this);

        View newGameButton = findViewById(R.id.new_game_button);
        newGameButton.setOnClickListener(this);

        View aboutButton = findViewById(R.id.about_button);
        aboutButton.setOnClickListener(this);

        View exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.about_button:
                Intent intent = new Intent(this, sd_About.class);
                startActivity(intent);
                break;
            case R.id.new_game_button:
                openNewGameDialog();
                break;
            case R.id.continue_button:
                startGame(sd_Game.DIFFICULTY_CONTINUE);
                break;
            case R.id.exit_button:
                finish();
                break;
        }
    }

    private void openNewGameDialog() {
        new AlertDialog.Builder(this).setTitle("请选择难度").setItems(R.array.数独难度, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startGame(which);
            }
        }).show();
    }

    private void startGame(int which) {
        Intent intent = new Intent(this, sd_Game.class);
        intent.putExtra(sd_Game.KEY_DIFFICULTY, which);
        startActivity(intent);
    }
}
