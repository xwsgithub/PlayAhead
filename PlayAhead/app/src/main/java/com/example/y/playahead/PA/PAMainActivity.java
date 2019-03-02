package com.example.y.playahead.PA;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.y.playahead.R;


public class PAMainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pa_activitymain);

        ImageButton ibt_sd=(ImageButton)findViewById(R.id.ibt_sd);
        ImageButton ibt_2048=(ImageButton)findViewById(R.id.ibt_2048);
        ImageButton ibt_szhrd=(ImageButton)findViewById(R.id.ibt_szhrd);

        ibt_sd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PAMainActivity.this,PALoading.class);
                intent.putExtra("game",1);
                startActivity(intent);
            }
        });
        ibt_2048.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PAMainActivity.this, PALoading.class);
                intent.putExtra("game", 2);
                startActivity(intent);
            }
        });
        ibt_szhrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PAMainActivity.this,PALoading.class);
                intent.putExtra("game",3);
                startActivity(intent);
            }
        });
    }
}
