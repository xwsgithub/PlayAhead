package com.example.game2048;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

import android.graphics.Point;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;
import android.content.DialogInterface;









public class GameViewfor2048 extends GridLayout {

    public Cardfor2048[][] cardsmap = new Cardfor2048[4][4];
    private List<Point> emptyPoints = new ArrayList<Point>();
    private String points=new String();
    private int a;



    public GameViewfor2048(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        gameview=this;
        initGameView();
    }

    public GameViewfor2048(Context context) {
        super(context);

        gameview=this;
        initGameView();
    }

    public GameViewfor2048(Context context, AttributeSet attrs) {
        super(context, attrs);

        gameview=this;
        initGameView();
    }


    public void initGameView() {



        setColumnCount(4);
        //setBackgroundColor(0xffbbada0);

        int screenWidth;
        Point p = new Point();
//获取窗口管理器
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getSize(p);
        screenWidth = (p.x - 200) / 4; // 屏幕宽度





        addCards(screenWidth,screenWidth);

        if(homeActivityfor2048.getInstance().load){
            loadgame();
        }else {
            startGame();
        }


        setOnTouchListener(new OnTouchListener() {

            private float startX, startY, offsetX, offsetY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        offsetX = event.getX() - startX;
                        offsetY = event.getY() - startY;

                        if (Math.abs(offsetX) > Math.abs(offsetY)) {
                            if (offsetX < -5) {         //左
                                swipeLeft();

                            } else if (offsetX > 5) {    //右
                                swipeRight();
                            }
                        } else {
                            if (offsetY < -5) {         //上
                                swipeUp();
                            } else if (offsetY > 5) {      //下
                                swipeDown();

                            }
                        }

                        break;
                }

                return true;
            }
        });


    }


    private void addCards(int cardWidth, int cardHeight) {
        Cardfor2048 c;

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                c = new Cardfor2048(getContext());
                c.setNum(2);
                addView(c, cardWidth, cardHeight);
                cardsmap[x][y] = c;
            }
        }

    }

    public void startGame() {

        MainActivityfor2048.getMainActivity().clearScore();

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {

                cardsmap[x][y].setNum(0);
            }
        }
        addRandomNum();
        addRandomNum();
        for(int y=0;y<Configfor2048.LINES;y++){
            for(int x=0;x<Configfor2048.LINES;x++){
                if(cardsmap[x][y].getNum()>0){
                    points+=(cardsmap[x][y].getNum()+" ");
                }
                else{
                    points+="0 ";
                }
            }
        }
        MainActivityfor2048.getMainActivity().record(points);

    }

    private void loadgame(){
        MainActivityfor2048.getMainActivity().Score=homeActivityfor2048.getInstance().getscore();
        //MainActivityfor2048.getMainActivity().showScore();

        String[] strings=homeActivityfor2048.getInstance().getpoints().split(" ");
        int i=0;
        for(int y=0;y<Configfor2048.LINES;y++){
            for(int x=0;x<Configfor2048.LINES;x++){
                cardsmap[x][y].setNum(Integer.parseInt(strings[i++]));
            }
        }
    }



    private void addRandomNum() {

        emptyPoints.clear();

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if (cardsmap[x][y].getNum() <= 0) {
                    emptyPoints.add(new Point(x, y));
                }
            }
        }
        Point p = emptyPoints.remove((int) (Math.random() * emptyPoints.size()));
        cardsmap[p.x][p.y].setNum(Math.random() > 0.1 ? 2 : 4);
    }

    private void swipeLeft() {
        boolean merge = false;
        points="";

        for (int y = 0; y < Configfor2048.LINES; y++) {
            for (int x = 0; x < Configfor2048.LINES; x++) {

                for (int x1 = x + 1; x1 < Configfor2048.LINES; x1++) {
                    if (cardsmap[x1][y].getNum() > 0) {

                        if (cardsmap[x][y].getNum() <= 0) {


                            cardsmap[x][y].setNum(cardsmap[x1][y].getNum());
                            cardsmap[x1][y].setNum(0);
                            merge = true;
                            x--;

                        } else if (cardsmap[x][y].equals(cardsmap[x1][y])) {

                            cardsmap[x][y].setNum(cardsmap[x][y].getNum() * 2);
                            cardsmap[x1][y].setNum(0);
                            MainActivityfor2048.getMainActivity().addScore(cardsmap[x][y].getNum());
                            merge = true;
                        }
                        break;
                    }
                }
            }
        }



        if (merge) {
            addRandomNum();
            for(int y=0;y<Configfor2048.LINES;y++){
                for(int x=0;x<Configfor2048.LINES;x++){
                    if(cardsmap[x][y].getNum()>0){
                        points+=(cardsmap[x][y].getNum()+" ");
                    }
                    else{
                        points+="0 ";
                    }
                }
            }
            MainActivityfor2048.getMainActivity().record(points);
            checkComplete();
        }

    }

    private void swipeRight() {
        boolean merge = false;
        points="";

        for (int y = 0; y < Configfor2048.LINES; y++) {
            for (int x = Configfor2048.LINES - 1; x >= 0; x--) {

                for (int x1 = x - 1; x1 >= 0; x1--) {
                    if (cardsmap[x1][y].getNum() > 0) {

                        if (cardsmap[x][y].getNum() <= 0) {
                            cardsmap[x][y].setNum(cardsmap[x1][y].getNum());
                            cardsmap[x1][y].setNum(0);

                            x++;
                            merge = true;
                        } else if (cardsmap[x][y].equals(cardsmap[x1][y])) {
                            cardsmap[x][y].setNum(cardsmap[x][y].getNum() * 2);
                            cardsmap[x1][y].setNum(0);
                            merge = true;
                            MainActivityfor2048.getMainActivity().addScore(cardsmap[x][y].getNum());
                        }

                        break;
                    }
                }
            }
        }



        if (merge) {
            addRandomNum();
            for(int y=0;y<Configfor2048.LINES;y++){
                for(int x=0;x<Configfor2048.LINES;x++){
                    if(cardsmap[x][y].getNum()>0){
                        points+=(cardsmap[x][y].getNum()+" ");
                    }
                    else{
                        points+="0 ";
                    }
                }
            }
            MainActivityfor2048.getMainActivity().record(points);
            checkComplete();
        }


    }

    private void swipeUp() {
        boolean merge = false;
        points="";

        for (int x = 0; x < Configfor2048.LINES; x++) {
            for (int y = 0; y < Configfor2048.LINES; y++) {

                for (int y1 = y + 1; y1 < Configfor2048.LINES; y1++) {
                    if (cardsmap[x][y1].getNum() > 0) {

                        if (cardsmap[x][y].getNum() <= 0) {

                            cardsmap[x][y].setNum(cardsmap[x][y1].getNum());
                            cardsmap[x][y1].setNum(0);

                            y--;

                            merge = true;
                        } else if (cardsmap[x][y].equals(cardsmap[x][y1])) {

                            cardsmap[x][y].setNum(cardsmap[x][y].getNum() * 2);
                            cardsmap[x][y1].setNum(0);
                            MainActivityfor2048.getMainActivity().addScore(cardsmap[x][y].getNum());

                            merge = true;
                        }

                        break;

                    }
                }
            }

        }




        if (merge) {
            addRandomNum();
            for(int y=0;y<Configfor2048.LINES;y++){
                for(int x=0;x<Configfor2048.LINES;x++){
                    if(cardsmap[x][y].getNum()>0){
                        points+=(cardsmap[x][y].getNum()+" ");
                    }
                    else{
                        points+="0 ";
                    }
                }
            }
            MainActivityfor2048.getMainActivity().record(points);
            checkComplete();
        }

    }

    private void swipeDown() {
        boolean merge = false;
        points="";

        for (int x = 0; x < Configfor2048.LINES; x++) {
            for (int y = Configfor2048.LINES - 1; y >= 0; y--) {

                for (int y1 = y - 1; y1 >= 0; y1--) {
                    if (cardsmap[x][y1].getNum() > 0) {

                        if (cardsmap[x][y].getNum() <= 0) {

                            cardsmap[x][y].setNum(cardsmap[x][y1].getNum());
                            cardsmap[x][y1].setNum(0);

                            y++;
                            merge = true;
                        } else if (cardsmap[x][y].equals(cardsmap[x][y1])) {

                            cardsmap[x][y].setNum(cardsmap[x][y].getNum() * 2);
                            cardsmap[x][y1].setNum(0);
                            MainActivityfor2048.getMainActivity().addScore(cardsmap[x][y].getNum());

                            merge = true;
                        }

                        break;
                    }
                }
            }
        }




        if (merge) {
            addRandomNum();
            for(int y=0;y<Configfor2048.LINES;y++){
                for(int x=0;x<Configfor2048.LINES;x++){
                    if(cardsmap[x][y].getNum()>0){
                        points+=(cardsmap[x][y].getNum()+" ");
                    }
                    else{
                        points+="0 ";
                    }
                }
            }

            MainActivityfor2048.getMainActivity().record(points);
            checkComplete();
        }

    }


    private void checkComplete() {

        boolean complete = true;

        ALL:
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if (cardsmap[x][y].getNum() == 0 ||
                        (x > 0 && cardsmap[x][y].equals(cardsmap[x - 1][y])) ||
                        (x < 3 && cardsmap[x][y].equals(cardsmap[x + 1][y])) ||
                        (y > 0 && cardsmap[x][y].equals(cardsmap[x][y - 1])) ||
                        (y < 3 && cardsmap[x][y].equals(cardsmap[x][y + 1]))) {

                    complete = false;
                    break ALL;
                }
            }
        }
        ALL:
        for(int y = 0;y <4; y++){
            for (int x=0;x<4;x++){
                if(cardsmap[x][y].getNum()==2048){
                    complete=true;
                    break ALL;
                }
            }
        }

        if (complete) {
            new AlertDialog.Builder(getContext()).setTitle("你好").setMessage("游戏结束").setPositiveButton("重新开始", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startGame();
                }
            }).show();
        }

    }
    public static GameViewfor2048 gameview=null;
    public static GameViewfor2048 getGameview(){
        return gameview;
    }
}

