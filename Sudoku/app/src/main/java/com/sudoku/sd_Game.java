package com.sudoku;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class sd_Game extends Activity implements View.OnClickListener {
    //difficulty settings
    public static final String KEY_DIFFICULTY = "difficulty";
    private static final int DIFFICULTY_EASY = 0;
    private static final int DIFFICULTY_MEDIUM = 1;
    private static final int DIFFICULTY_HARD = 2;
    private static final int DIFFICULTY_GUHUI = 3;
    private int difficulty;

    //screen
    private double startX, startY, endX, endY;
    int screenwidth, screenheight;
    private double width, height;
    private int redx, redy;
    ImageView image;

    //panel
    private int[][] current = new int[9][9];
    private int[][] orgin = new int[9][9];
    private int[][] result;

    //cells
    private int MinFilled;
    private int MinKnow;
    int choose = 0;

    //random
    private Random random = new Random();

    //store
    private static final String PREF_NUMS = "nums";
    protected static final int DIFFICULTY_CONTINUE = -1;

    //paint
    private Typeface numtype;
    private Paint mypaint;
    private Bitmap bitmap;
    private Canvas canvas;
    private Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //全屏显示
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.sd_main);

        //获取难度
        difficulty = getIntent().getIntExtra(KEY_DIFFICULTY, DIFFICULTY_EASY);
        screen();
        painter();

        init();
        game();

    }

    //获取屏幕大小
    private void screen() {
        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        screenwidth = outMetrics.widthPixels;
        screenheight = outMetrics.heightPixels;
        getSize();
    }

    //获取格子大小
    private void getSize() {
        startX = screenwidth * 0.8 / 10.6;
        endX = screenwidth * 9.8 / 10.6;
        startY = screenheight * 8.5 / 43.05;
        endY = screenheight * 28.5 / 42.05;
        width = (endX - startX) / 9;
        height = (endY - startY) / 9;
    }

    //画笔初始化
    private void painter() {
        image = (ImageView) findViewById(R.id.get_width);
        button = (Button) findViewById(R.id.delete);
        button.setTypeface(Typeface.createFromAsset(getAssets(), "Fonts/方正清刻本悦宋简_0.TTF"));
        mypaint = new Paint();
        mypaint.setAntiAlias(true);
        mypaint.setTextSize(60);
        mypaint.setStyle(Paint.Style.FILL);
        mypaint.setTextAlign(Paint.Align.CENTER);
        numtype = Typeface.createFromAsset(getAssets(), "Fonts/ORATORSTD.ttf");
        mypaint.setTypeface(numtype);
    }

    //初始化
    private void init() {
        //clear
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                current[i][j] = 0;
                orgin[i][j] = 0;
            }
        }

        switch (difficulty) {
            case DIFFICULTY_CONTINUE:
                String data = getPreferences(MODE_PRIVATE).getString(PREF_NUMS, "");
                if (data == "") {
                    difficulty = 0;
                    MinFilled = 45 + random.nextInt(5);
                    MinKnow = 4;
                    break;
                } else {
                    for (int i = 0; i < 9; i++) {
                        for (int j = 0; j < 9; j++) {
                            current[i][j] = data.charAt(i * 9 + j) - '0';
                            orgin[i][j] = data.charAt(81 + i * 9 + j) - '0';
                        }
                    }
                    paint(true);
                    return;
                }
            case DIFFICULTY_GUHUI:
                MinFilled = 17 + random.nextInt(10);
                MinKnow = 0;
                break;
            case DIFFICULTY_HARD:
                MinFilled = 21 + random.nextInt(10);
                MinKnow = 2;
                break;
            case DIFFICULTY_MEDIUM:
                MinFilled = 31 + random.nextInt(10);
                MinKnow = 3;
                break;
            case DIFFICULTY_EASY:
            default:
                MinFilled = 45 + random.nextInt(5);
                MinKnow = 4;
        }

        build();

        for (int i = 0; i < 9; i++) {
            System.arraycopy(current[i], 0, orgin[i], 0, 9);
        }
        paint(true);
    }

    //构建数独数据信息
    private void build() {
        int x = random.nextInt(9), y = random.nextInt(9);
        int old_x = x, old_y = y;
        int FilledCount = 81;

        BuildKnow(11);
        if (solve(current, true)) {
            for (int i = 0; i < 9; i++) {
                System.arraycopy(result[i], 0, current[i], 0, 9);
            }
            current = EqualChange(current);

            Point NextP;
            do {
                int tempMinKnow = getMinKnow(current, x, y);
                if (isOnlyAnswer(current, x, y) && tempMinKnow >= MinKnow) {
                    current[x][y] = 0;
                    FilledCount--;
                }

                NextP = next(x, y);
                x = NextP.x;
                y = NextP.y;
                while (current[x][y] == 0 && (old_x != x || old_y != y)) {
                    NextP = next(x, y);
                    x = NextP.x;
                    y = NextP.y;
                }
                if (difficulty == 0) {
                    while (old_x == x && old_y == y) {
                        NextP = next(x, y);
                        x = NextP.x;
                        y = NextP.y;
                    }
                }
            } while (FilledCount > MinFilled && (old_x != x || old_y != y));
        } else build();
    }

    //构建一些已知格子
    private void BuildKnow(int n) {
        Point[] know = new Point[n];
        for (int i = 0; i < n; i++) {
            know[i] = new Point(random.nextInt(9), random.nextInt(9));
            for (int j = 0; j < i; j++) {
                if (know[j].equals(know[i])) {
                    i--;
                    break;
                }
            }
        }

        int num;
        Point p;
        for (int i = 0; i < n; i++) {
            num = 1 + random.nextInt(9);
            p = know[i];
            current[p.x][p.y] = num;
            if (!ValidatePos(current, p.x, p.y, false)) {
                i--;
            }
        }
    }

    //判断数独可解
    private boolean solve(int[][] data, boolean flag) {
        int SpaceNum = 0;
        int[][] temp = new int[9][9];
        ArrayList<Point> spaces = new ArrayList(70);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                temp[i][j] = data[i][j];
                if (temp[i][j] == 0) {
                    SpaceNum++;
                    spaces.add(new Point(i, j));
                }
            }
        }

        if (!validate(temp)) return false;
        if (SpaceNum == 0) return true;
        else if (put(temp, 0, spaces)) {
            if (flag) result = temp;
            return true;
        }
        return false;
    }

    //填满
    private boolean put(int[][] data, int pos, ArrayList<Point> spaces) {
        if (pos >= spaces.size()) return true;
        Point p = spaces.get(pos);
        for (int i = 1; i <= 9; i++) {
            data[p.x][p.y] = i;
            if (ValidatePos(data, p.x, p.y, false) && put(data, pos + 1, spaces)) return true;
        }
        data[p.x][p.y] = 0;
        return false;
    }

    //挖格子
    private Point next(int x, int y) {
        Point p;
        switch (difficulty) {
            case DIFFICULTY_GUHUI:
                p = new Point();
                if (y == 8) {
                    if (x == 8) {
                        p.x = 0;
                    } else p.x = x + 1;

                    p.y = 0;
                } else {
                    p.x = x;
                    p.y = y + 1;
                }
                break;
            case DIFFICULTY_HARD:
                p = new Point();
                if (x == 8 && y == 8) p.y = 0;
                else if (x % 2 == 0 && y < 8) p.y = y + 1;
                else if (x % 2 == 1 && y > 0) p.y = y - 1;
                else p.y = y;

                if (x == 8 && y == 8) p.x = 0;
                else if ((x % 2 == 0 && y == 8) || (x % 2 == 1 && y == 0)) p.x = x + 1;
                else p.x = x;
                break;
            case DIFFICULTY_MEDIUM:
                if (x == 8 && y == 7) p = new Point(0, 0);
                else if (x == 8 && y == 8) p = new Point(0, 1);
                else if ((x % 2 == 0 && y == 7) || (x % 2 == 1 && y == 0))
                    p = new Point(x + 1, y + 1);
                else if ((x % 2 == 0 && y == 8) || (x % 2 == 1 && y == 1))
                    p = new Point(x + 1, y - 1);
                else if (x % 2 == 0) p = new Point(x, y + 2);
                else p = new Point(x, y - 2);
                break;
            case DIFFICULTY_EASY:
            default:
                p = new Point(random.nextInt(9), random.nextInt(9));
        }
        return p;
    }

    //等量代换
    private int[][] EqualChange(int[][] data) {
        int num1 = 1 + random.nextInt(9);
        int num2 = 1 + random.nextInt(9);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (data[i][j] == 1) data[i][j] = num1;
                else if (data[i][j] == num1) data[i][j] = 1;
                if (data[i][j] == 2) data[i][j] = num2;
                else if (data[i][j] == num2) data[i][j] = 2;
            }
        }
        return data;
    }

    //获取当前局面最少的一行或一列中已知的格子数
    private int getMinKnow(int[][] data, int x, int y) {
        int temp = data[x][y];
        int MinKnow = 9;
        int tempKnow = 9;
        data[x][y] = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (data[i][j] == 0) {
                    tempKnow--;
                    if (tempKnow < MinKnow) {
                        MinKnow = tempKnow;
                    }
                }
            }
            tempKnow = 9;
        }
        for (int j = 0; j < 9; j++) {
            for (int i = 0; i < 9; i++) {
                if (data[i][j] == 0) {
                    tempKnow--;
                    if (tempKnow < MinKnow) {
                        MinKnow = tempKnow;
                    }
                }
            }
            tempKnow = 9;
        }
        data[x][y] = temp;
        return MinKnow;
    }

    //判断解是否唯一
    private boolean isOnlyAnswer(int[][] data, int x, int y) {
        int num = data[x][y];
        for (int i = 1; i <= 9; i++) {
            data[x][y] = i;
            if (i != num && solve(data, false)) {
                data[x][y] = num;
                return false;
            }
        }
        data[x][y] = num;
        return true;
    }

    ///判断所有位置合法
    private boolean validate(int[][] data) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (data[i][j] != 0 && !ValidatePos(data, i, j, false)) return false;
            }
        }
        return true;
    }

    ///判断位置合法
    private boolean ValidatePos(int[][] data, int x, int y, boolean flag) {
        for (int i = 0; i < 9; i++) {
            if ((i != x) && (data[i][y] == data[x][y])) {
                if (flag) {
                    paint(i, y, true);
                    redx = i;
                    redy = y;
                }
                return false;
            }
        }
        for (int i = 0; i < 9; i++) {
            if ((i != y) && (data[x][i] == data[x][y])) {
                if (flag) {
                    paint(x, i, true);
                    redx = x;
                    redy = i;
                }
                return false;
            }
        }
        int NineX = x / 3 * 3;
        int NineY = y / 3 * 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (((NineX + i != x) || (NineY + j != y)) && (data[NineX + i][NineY + j] == data[x][y])) {
                    if (flag) {
                        paint(NineX + i, NineY + j, true);
                        redx = NineX + i;
                        redy = NineY + j;
                    }
                    return false;
                }
            }
        }
        return true;
    }

    //生成初始格子
    private void MakeOrgin() {
        for (int i = 0; i < 9; i++) {
            System.arraycopy(orgin[i], 0, current[i], 0, 9);
        }
    }

    //储存数字信息
    @Override
    protected void onPause() {
        super.onPause();
        getPreferences(MODE_PRIVATE).edit().putString(PREF_NUMS, Num_String(current) + Num_String(orgin)).commit();
    }

    //数字转字符串
    private static String Num_String(int[][] num) {
        StringBuilder data = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                data.append(num[i][j]);
            }
        }
        return data.toString();
    }

    //填充数字
    private void paint(boolean mode) {
        if (mode) {
            bitmap = Bitmap.createBitmap((int) (endX - startX), (int) (endY - startY), Bitmap.Config.ARGB_4444);
            canvas = new Canvas(bitmap);
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                paint(i, j, false);
            }
        }
    }

    //指定位置填充数字
    private void paint(int x, int y, boolean warn) {
        float[] pos = new float[]{(float) (y * width + width / 2), (float) (x * height + height * 2 / 3)};
        if (warn) {
            mypaint.setColor(Color.RED);
            canvas.drawPosText(current[x][y] + "", pos, mypaint);
            image.setImageBitmap(bitmap);
            return;
        }
        if (current[x][y] == 0) return;
        if (orgin[x][y] != 0) mypaint.setColor(Color.WHITE);
        else mypaint.setColor(Color.BLACK);
        canvas.drawPosText(current[x][y] + "", pos, mypaint);
        image.setImageBitmap(bitmap);
    }

    //游戏运行
    private void game() {
        image.setOnTouchListener(new View.OnTouchListener() {
            private float click_x, click_y;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        click_x = motionEvent.getX();
                        click_y = motionEvent.getY();

                        Point p = FindPos(click_x, click_y);
                        if (choose != -1) {
                            if (choose == 0) {
                                if (orgin[p.x][p.y] == 0 && current[p.x][p.y] != 0) {
                                    current[p.x][p.y] = 0;
                                    paint(true);
                                }
                            } else {
                                if (current[p.x][p.y] == 0) {
                                    current[p.x][p.y] = choose;
                                    if (ValidatePos(current, p.x, p.y, true)) {
                                        paint(p.x, p.y, false);
                                    } else current[p.x][p.y] = 0;
                                }
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        paint(redx, redy, false);
                        if (isSuccess()) {
                            final AlertDialog.Builder dialog = new AlertDialog.Builder(sd_Game.this);
                            dialog.setCancelable(false);
                            dialog.setTitle("Congratulations");
                            dialog.setNeutralButton("Back", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            });
                            dialog.setNegativeButton("        ", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    difficulty = 3;
                                    Toast.makeText(sd_Game.this, "恭喜你发现隐藏关卡骨灰级数独", Toast.LENGTH_SHORT).show();
                                    init();
                                    game();
                                }
                            });
                            dialog.setPositiveButton("New Game", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    new AlertDialog.Builder(sd_Game.this).setTitle("请选择难度").setItems(R.array.数独难度, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(final DialogInterface dialog, int which) {
                                            difficulty = which;
                                            init();
                                            game();
                                        }
                                    }).show();
                                }
                            });
                            dialog.show();
                        }
                    default:
                }
                return true;
            }

            private Point FindPos(float x, float y) {
                Point p = new Point();
                p.x = (int) (y / height);
                if (p.x < 0) p.x = 0;
                if (p.x > 8) p.x = 8;
                p.y = (int) (x / width);
                if (p.y < 0) p.y = 0;
                if (p.y > 8) p.y = 8;
                return p;
            }
        });
    }

    //判断成功
    private boolean isSuccess() {
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                if (current[i][j] == 0) return false;
        return true;
    }

    @Override
    public void onClick(View view) {
        if (button != null) {
            button.setBackgroundColor(0x00000000);
            button.setTextColor(0xaa000000);
        }
        choose = -1;
        int id = view.getId();
        switch (id) {
            case R.id.restart:
                MakeOrgin();
                paint(true);
                game();
                break;
            case R.id.select_difficulty:
                new AlertDialog.Builder(this).setTitle("请选择难度").setItems(R.array.数独难度, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        difficulty = which;
                        init();
                        game();
                    }
                }).show();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.delete:
                choose = 0;
                button = (Button) findViewById(R.id.delete);
                button.setTextColor(0xaaff0000);
                break;
            case R.id.num1:
                choose = 1;
                button = (Button) findViewById(R.id.num1);
                button.setBackgroundColor(0x88ffffff);
                break;
            case R.id.num2:
                choose = 2;
                button = (Button) findViewById(R.id.num2);
                button.setBackgroundColor(0x88ffffff);
                break;
            case R.id.num3:
                choose = 3;
                button = (Button) findViewById(R.id.num3);
                button.setBackgroundColor(0x88ffffff);
                break;
            case R.id.num4:
                choose = 4;
                button = (Button) findViewById(R.id.num4);
                button.setBackgroundColor(0x88ffffff);
                break;
            case R.id.num5:
                choose = 5;
                button = (Button) findViewById(R.id.num5);
                button.setBackgroundColor(0x88ffffff);
                break;
            case R.id.num6:
                choose = 6;
                button = (Button) findViewById(R.id.num6);
                button.setBackgroundColor(0x88ffffff);
                break;
            case R.id.num7:
                choose = 7;
                button = (Button) findViewById(R.id.num7);
                button.setBackgroundColor(0x88ffffff);
                break;
            case R.id.num8:
                choose = 8;
                button = (Button) findViewById(R.id.num8);
                button.setBackgroundColor(0x88ffffff);
                break;
            case R.id.num9:
                choose = 9;
                button = (Button) findViewById(R.id.num9);
                button.setBackgroundColor(0x88ffffff);
                break;
        }
    }

}