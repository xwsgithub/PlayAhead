package com.example.y.playahead.sd;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;

import com.example.y.playahead.R;

public class sd_About extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sd_about);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        finish();
        return true;
    }

}