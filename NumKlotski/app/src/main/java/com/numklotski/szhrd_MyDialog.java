package com.numklotski;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

public class szhrd_MyDialog extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.szhrd_dialog);

        Button restart=(Button)findViewById(R.id.restart);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                timer.stop();
//                timer.setText("00:00");
//                initialize();
//                RunGame();
            }
        });

        Button back=(Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(szhrd_MyDialog.this, szhrd_MainActivity.class);
                intent.putExtra("module", -1);
                startActivity(intent);
            }
        });
    }
}
