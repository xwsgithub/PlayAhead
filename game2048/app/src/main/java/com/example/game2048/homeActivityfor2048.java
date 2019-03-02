package com.example.game2048;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.SharedPreferences;


public class homeActivityfor2048 extends Activity {

    private SharedPreferences sp;
    private static homeActivityfor2048 instance=null;
    public boolean load=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepagefor2048);

        instance=this;

        sp=getSharedPreferences("position_file",0);

        TextView textView1=findViewById(R.id.newgame);
        TextView textView2=findViewById(R.id.loadinggame);
        TextView textView3=findViewById(R.id.instructor);

        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(homeActivityfor2048.this,MainActivityfor2048.class);
                startActivity(intent);

            }
        });

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                load=true;

                Intent intent=new Intent(homeActivityfor2048.this,MainActivityfor2048.class);
                startActivity(intent);



            }
        });


        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertDialog1 = new AlertDialog.Builder(homeActivityfor2048.this)
                        .setTitle("游戏说明")//标题
                        .setMessage("你需要控制所有方块向同一个方向运动，两个相同数字方块撞在一起之后合并成为他们的和，每次操作之后会随机生成一个2或者4，最终得到一个“2048”的方块就算胜利了。")//内容
                        .create();
                alertDialog1.show();

            }
        });
    }

    public static homeActivityfor2048 getInstance(){
        return instance;
    }
    public String getpoints(){
        SharedPreferences ssp=getSharedPreferences("position_file",0);
        return ssp.getString("Position","");

    }
    public int getscore(){
        SharedPreferences ssp=getSharedPreferences("score_file",0);
        return ssp.getInt("Score",0);
    }
}
