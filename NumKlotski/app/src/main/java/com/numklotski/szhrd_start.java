package com.numklotski;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class szhrd_start extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.szhrd_start_layout);

        Button three = (Button) findViewById(R.id.three);                                              //点击3*3
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(szhrd_start.this, szhrd_MainActivity.class);
                intent.putExtra("module", 3);
                startActivity(intent);
            }
        });

        Button four = (Button) findViewById(R.id.four);                                                //点击4*4
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(szhrd_start.this, szhrd_MainActivity.class);
                intent.putExtra("module", 4);
                startActivity(intent);
            }
        });

        Button five = (Button) findViewById(R.id.five);                                                //点击5*5
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(szhrd_start.this, szhrd_MainActivity.class);
                intent.putExtra("module", 5);
                startActivity(intent);
            }
        });
    }
}
