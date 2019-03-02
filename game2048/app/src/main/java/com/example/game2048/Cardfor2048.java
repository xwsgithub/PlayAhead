package com.example.game2048;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Cardfor2048 extends FrameLayout {

    private int num=0;
    private TextView lable;

    public Cardfor2048(Context context) {
        super(context);

        lable=new TextView(getContext());


        addView(lable);


        setNum(0);
    }


    public  int getNum(){
        return num;
    }

    public void setNum(int num){
        this.num=num;

        switch (num) {
            case 0:

                lable.setBackgroundResource(R.drawable.number0for2048);

                break;
            case 2:

                lable.setBackgroundResource(R.drawable.number204802);
                break;
            case 4:

                lable.setBackgroundResource(R.drawable.number204804);
                break;
            case 8:

                lable.setBackgroundResource(R.drawable.number2048008);
                break;
            case 16:

                lable.setBackgroundResource(R.drawable.number20480016);
                break;
            case 32:

                lable.setBackgroundResource(R.drawable.number20480032);
                break;
            case 64:

                lable.setBackgroundResource(R.drawable.number20480064);
                break;
            case 128:

                lable.setBackgroundResource(R.drawable.number128for2048);
                break;
            case 256:

                lable.setBackgroundResource(R.drawable.number256for2048);
                break;
            case 512:

                lable.setBackgroundResource(R.drawable.number512for2048);
                break;
            case 1024:

                lable.setBackgroundResource(R.drawable.number1024for2048);
                break;
            case 2048:

                lable.setBackgroundResource(R.drawable.number2048for2048);
                break;
            default:

                break;
        }
    }

    public boolean equals(Cardfor2048 cardfor2048){
        return getNum()== cardfor2048.getNum();
    }

}
