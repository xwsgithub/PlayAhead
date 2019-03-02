package com.example.y.playahead.szhrd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.y.playahead.R;

import java.util.Random;

public class szhrd_MainActivity extends Activity {
    private int x,y,steps,flag,module;
    private ImageView imageview;
    private int imagegroup[]={0, R.drawable.szhrd_p1, R.drawable.szhrd_p2, R.drawable.szhrd_p3, R.drawable.szhrd_p4,
            R.drawable.szhrd_p5,R.drawable.szhrd_p6,R.drawable.szhrd_p7,R.drawable.szhrd_p8,R.drawable.szhrd_p9,
            R.drawable.szhrd_p10,R.drawable.szhrd_p11,R.drawable.szhrd_p12,R.drawable.szhrd_p13,R.drawable.szhrd_p14,
            R.drawable.szhrd_p15,R.drawable.szhrd_p16,R.drawable.szhrd_p17,R.drawable.szhrd_p18,R.drawable.szhrd_p19,
            R.drawable.szhrd_p20,R.drawable.szhrd_p21,R.drawable.szhrd_p22,R.drawable.szhrd_p23,R.drawable.szhrd_p24,R.drawable.szhrd_pmax};
    private Chronometer timer;
    TextView text1;
    LinearLayout layout;
    AlertDialog alertdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);

        Intent intent=getIntent();
        module=intent.getIntExtra("module",-1);                                                                    //把对应的布局填到显示的中间
        switch (module){
            case 3:
                setContentView(R.layout.szhrd_three);
                break;
            case 4:
                setContentView(R.layout.szhrd_four);
                break;
            case 5:
                setContentView(R.layout.szhrd_five);
        }                                                                      //确认点击的是3*3|4*4|5*5

        ;
        if(module!=5){                                                                                                  //将空白格换到最后一格
            imagegroup[module*module]=imagegroup[module*module]+imagegroup[25];
            imagegroup[25]=imagegroup[module*module]-imagegroup[25];
            imagegroup[module*module]=imagegroup[module*module]-imagegroup[25];
        }


        layout=(LinearLayout)findViewById(R.id.layout1);
        text1=(TextView)findViewById(R.id.text1);
        timer=(Chronometer)findViewById(R.id.timer);
        imageview=(ImageView)findViewById(R.id.image0);
        x=module;y=module;

        initialize();
        RunGame();

        Button restart=(Button)findViewById(R.id.restart);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.stop();
                timer.setText("00:00");
                initialize();
                RunGame();
            }
        });

        Button back=(Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initialize(){
        text1.setText("0");                                                                        //步数置零

        Random random=new Random();
        int cnt=150+random.nextInt(50);                                                             //随机移动的次数控制在150-200之内
        for(int i=0;i<cnt;i++){
            int direction=random.nextInt(4);                                                        //随机方向
            switch (direction){
                case 0:
                    leftmove();
                    break;
                case 1:
                    rightmove();
                    break;
                case 2:
                    upmove();
                    break;
                case 3:
                    downmove();
            }
        }

        steps=0;
    }

    private void RunGame(){
        flag=1;
        layout.setOnTouchListener(new View.OnTouchListener() {                                      //监测滑动
            private float start_x,start_y,dx,dy;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){                                                         //判断滑动方向
                    case MotionEvent.ACTION_DOWN:
                        start_x=event.getX();
                        start_y=event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        dx=event.getX()-start_x;
                        dy=event.getY()-start_y;

                        if(Math.abs(dx)>Math.abs(dy)){
                            if(dx<-60) leftmove();
                            else if(dx>60) rightmove();
                            else break;
                        }
                        else{
                            if(dy<-60) upmove();
                            else if(dy>60) downmove();
                            else break;
                        }
                        text1.setText(""+steps);
                }

                if(flag==1){                                                                        //开始计时
                    timer.setBase(SystemClock.elapsedRealtime());
                    timer.start();
                    flag=0;
                }

                if(check()) {                                                                       //结束提示
                     final AlertDialog.Builder dialog=new AlertDialog.Builder(szhrd_MainActivity.this);

                    dialog.setCancelable(false);
                    View view=LayoutInflater.from(szhrd_MainActivity.this).inflate(R.layout.szhrd_dialog,null);
                    dialog.setView(view);

                    Button back=(Button)view.findViewById(R.id.back);
                    back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(alertdialog!=null) alertdialog.dismiss();
                            finish();
                        }
                    });

                    Button again=(Button)view.findViewById(R.id.restart);
                    again.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            timer.stop();
                            timer.setText("00:00");
                            initialize();
                            RunGame();
                            if(alertdialog!=null) alertdialog.dismiss();
                        }
                    });

                    alertdialog=dialog.create();
                    alertdialog.getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
                    alertdialog.show();

                    Window dialogwindow=alertdialog.getWindow();
                    WindowManager windowManager = getWindowManager();
                    Display display=windowManager.getDefaultDisplay();
                    WindowManager.LayoutParams layoutParams=dialogwindow.getAttributes();
                    layoutParams.height=(int)(display.getWidth()*0.4);
                    layoutParams.width=(int)(display.getWidth()*0.9);
                    layoutParams.gravity=Gravity.CENTER;
                    layoutParams.alpha=0.85f;
                    dialogwindow.setAttributes(layoutParams);
                }

                return true;
            }
        });
    }

    private void rightmove(){                                                                       //右移
        if(y==1) return;
        y--;

        int index=module*(x-1)+y;
        imageview.setImageResource(imagegroup[index]);
        change(index);
        imageview.setImageResource(R.drawable.szhrd_pmax);

        imagegroup[index+1]=imagegroup[index]+imagegroup[index+1];
        imagegroup[index]=imagegroup[index+1]-imagegroup[index];
        imagegroup[index+1]=imagegroup[index+1]-imagegroup[index];
        steps++;
    }

    private void leftmove(){                                                                        //左移
        if(y==module) return;
        y++;

        int index=module*(x-1)+y;
        imageview.setImageResource(imagegroup[index]);
        change(index);
        imageview.setImageResource(R.drawable.szhrd_pmax);

        imagegroup[index-1]=imagegroup[index]+imagegroup[index-1];
        imagegroup[index]=imagegroup[index-1]-imagegroup[index];
        imagegroup[index-1]=imagegroup[index-1]-imagegroup[index];
        steps++;
    }

    private void downmove(){                                                                        //下移
        if(x==1) return;
        x--;

        int index=module*(x-1)+y;
        imageview.setImageResource(imagegroup[index]);
        change(index);
        imageview.setImageResource(R.drawable.szhrd_pmax);

        imagegroup[index+module]=imagegroup[index]+imagegroup[index+module];
        imagegroup[index]=imagegroup[index+module]-imagegroup[index];
        imagegroup[index+module]=imagegroup[index+module]-imagegroup[index];
        steps++;
    }

    private void upmove(){                                                                          //上移
        if(x==module) return;
        x++;

        int index=module*(x-1)+y;
        imageview.setImageResource(imagegroup[index]);
        change(index);
        imageview.setImageResource(R.drawable.szhrd_pmax);

        imagegroup[index-module]=imagegroup[index]+imagegroup[index-module];
        imagegroup[index]=imagegroup[index-module]-imagegroup[index];
        imagegroup[index-module]=imagegroup[index-module]-imagegroup[index];
        steps++;
    }

    private void change(int index){                                                                 //将当前imageview切换到空白格
        switch(index){
            case 1:
                imageview=(ImageView)findViewById(R.id.image1);
                break;
            case 2:
                imageview=(ImageView)findViewById(R.id.image2);
                break;
            case 3:
                imageview=(ImageView)findViewById(R.id.image3);
                break;
            case 4:
                imageview=(ImageView)findViewById(R.id.image4);
                break;
            case 5:
                imageview=(ImageView)findViewById(R.id.image5);
                break;
            case 6:
                imageview=(ImageView)findViewById(R.id.image6);
                break;
            case 7:
                imageview=(ImageView)findViewById(R.id.image7);
                break;
            case 8:
                imageview=(ImageView)findViewById(R.id.image8);
                break;
            case 9:
                if(module==3) imageview=(ImageView)findViewById(R.id.image0);
                else imageview=(ImageView)findViewById(R.id.image9);
                break;
            case 10:
                imageview=(ImageView)findViewById(R.id.image10);
                break;
            case 11:
                imageview=(ImageView)findViewById(R.id.image11);
                break;
            case 12:
                imageview=(ImageView)findViewById(R.id.image12);
                break;
            case 13:
                imageview=(ImageView)findViewById(R.id.image13);
                break;
            case 14:
                imageview=(ImageView)findViewById(R.id.image14);
                break;
            case 15:
                imageview=(ImageView)findViewById(R.id.image15);
                break;
            case 16:
                if(module==4) imageview=(ImageView)findViewById(R.id.image0);
                else imageview=(ImageView)findViewById(R.id.image16);
                break;
            case 17:
                imageview=(ImageView)findViewById(R.id.image17);
                break;
            case 18:
                imageview=(ImageView)findViewById(R.id.image18);
                break;
            case 19:
                imageview=(ImageView)findViewById(R.id.image19);
                break;
            case 20:
                imageview=(ImageView)findViewById(R.id.image20);
                break;
            case 21:
                imageview=(ImageView)findViewById(R.id.image21);
                break;
            case 22:
                imageview=(ImageView)findViewById(R.id.image22);
                break;
            case 23:
                imageview=(ImageView)findViewById(R.id.image23);
                break;
            case 24:
                imageview=(ImageView)findViewById(R.id.image24);
                break;
            case 25:
                imageview=(ImageView)findViewById(R.id.image0);
                break;
        }
    }

    private boolean check(){                                                                        //判断结束
        if(imagegroup[1]!=R.drawable.szhrd_p1) return false;
        if(imagegroup[2]!=R.drawable.szhrd_p2) return false;
        if(imagegroup[3]!=R.drawable.szhrd_p3) return false;
        if(imagegroup[4]!=R.drawable.szhrd_p4) return false;
        if(imagegroup[5]!=R.drawable.szhrd_p5) return false;
        if(imagegroup[6]!=R.drawable.szhrd_p6) return false;
        if(imagegroup[7]!=R.drawable.szhrd_p7) return false;
        if(imagegroup[8]!=R.drawable.szhrd_p8) return false;
        if(module==3){
            if(imagegroup[9]!=R.drawable.szhrd_pmax) return false;
            timer.stop();
            return true;
        }
        if(imagegroup[9]!=R.drawable.szhrd_p9) return false;
        if(imagegroup[10]!=R.drawable.szhrd_p10) return false;
        if(imagegroup[11]!=R.drawable.szhrd_p11) return false;
        if(imagegroup[12]!=R.drawable.szhrd_p12) return false;
        if(imagegroup[13]!=R.drawable.szhrd_p13) return false;
        if(imagegroup[14]!=R.drawable.szhrd_p14) return false;
        if(imagegroup[15]!=R.drawable.szhrd_p15) return false;
        if(module==4){
            if(imagegroup[16]!=R.drawable.szhrd_pmax) return false;
            timer.stop();
            return true;
        }
        if(imagegroup[16]!=R.drawable.szhrd_p16) return false;
        if(imagegroup[17]!=R.drawable.szhrd_p17) return false;
        if(imagegroup[18]!=R.drawable.szhrd_p18) return false;
        if(imagegroup[19]!=R.drawable.szhrd_p19) return false;
        if(imagegroup[20]!=R.drawable.szhrd_p20) return false;
        if(imagegroup[21]!=R.drawable.szhrd_p21) return false;
        if(imagegroup[22]!=R.drawable.szhrd_p22) return false;
        if(imagegroup[23]!=R.drawable.szhrd_p23) return false;
        if(imagegroup[24]!=R.drawable.szhrd_p24) return false;
        if(imagegroup[25]!=R.drawable.szhrd_pmax) return false;
        timer.stop();
        return true;
    }

}
