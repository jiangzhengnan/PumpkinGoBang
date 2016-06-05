package com.whale.nangua.pumpkingobang.view;

/**
 * Created by nangua on 2016/6/3.
 */
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.whale.nangua.pumpkingobang.R;
import com.whale.nangua.pumpkingobang.aty.BlueToothGameAty;
import com.whale.nangua.pumpkingobang.aty.InitAty;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Stack;

public class BlueToothGoBangView extends View {
    protected static int GRID_SIZE = 14;    //设置为国际标准
    protected static int GRID_WIDTH = 42; // 棋盘格的宽度
    protected static int CHESS_DIAMETER = 37; // 棋的直径
    protected static int mStartX;// 棋盘定位的左上角X
    protected static int mStartY;// 棋盘定位的左上角Y

    private static int[][] mGridArray; // 网格
    private Stack<String> storageArray;


    int wbflag = 1; //该下白棋了=2，该下黑棋了=1. 这里先下黑棋（黑棋以后设置为机器自动下的棋子）
    int mLevel = 1; //游戏难度
    int mWinFlag = 0;
    private final int BLACK = 1;
    private final int WHITE = 2;


    //private TextView mStatusTextView; //  根据游戏状态设置显示的文字
    private TextView mStatusTextView; //  根据游戏状态设置显示的文字

    private Bitmap btm1;
    private final Paint mPaint = new Paint();

    CharSequence mText;
    CharSequence STRING_WIN = "白棋赢啦!  ";
    CharSequence STRING_LOSE = "黑棋赢啦!  ";
    CharSequence STRING_EQUAL = "和棋！  ";

    public BlueToothGoBangView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);

        init();
    }

    //按钮监听器
    MyButtonListener myButtonListener;

    public void setActionCallbak(BlueToothGameAty blueToothGameAty) {
        this.blueToothGameAty = blueToothGameAty;
    }

    private  BlueToothGameAty blueToothGameAty;

    // 初始化黑白棋的Bitmap
    public void init() {
        storageArray = new Stack<>();
        myButtonListener = new MyButtonListener();
        wbflag = BLACK; //初始为先下黑棋
        mWinFlag = 0; //清空输赢标志。
        mGridArray = new int[GRID_SIZE - 1][GRID_SIZE - 1];


        Bitmap bitmap = Bitmap.createBitmap(CHESS_DIAMETER, CHESS_DIAMETER, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Resources r = this.getContext().getResources();

    }

    //设置显示的textView
    public void setTextView(TextView tv) {
        mStatusTextView = tv;
    }

    //悔棋按钮
    Button huiqi;
    //刷新那妞
    Button refresh;

    //设置两个按钮
    public void setButtons(Button huiqi, Button refresh) {
        this.huiqi = huiqi;
        this.refresh = refresh;
        huiqi.setOnClickListener(myButtonListener);
        refresh.setOnClickListener(myButtonListener);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mStartX = w / 2 - GRID_SIZE * GRID_WIDTH / 2;
        mStartY = h / 2 - GRID_SIZE * GRID_WIDTH / 2;
    }

    /**
     * 点下出现棋子
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x;
        int y;
        float x0 = GRID_WIDTH - (event.getX() - mStartX) % GRID_WIDTH;
        float y0 = GRID_WIDTH - (event.getY() - mStartY) % GRID_WIDTH;
        if (x0 < GRID_WIDTH / 2) {
            x = (int) ((event.getX() - mStartX) / GRID_WIDTH);
        } else {
            x = (int) ((event.getX() - mStartX) / GRID_WIDTH) - 1;
        }
        if (y0 < GRID_WIDTH / 2) {
            y = (int) ((event.getY() - mStartY) / GRID_WIDTH);
        } else {
            y = (int) ((event.getY() - mStartY) / GRID_WIDTH) - 1;
        }
        if ((x >= 0 && x < GRID_SIZE - 1)
                && (y >= 0 && y < GRID_SIZE - 1)) {
            if (mGridArray[x][y] == 0) {
                if (wbflag == BLACK) {
                    putChess(x, y, BLACK);
                    //this.mGridArray[x][y] = 1;
                    if (checkWin(BLACK)) { //如果是黑棋赢了
                        mText = STRING_LOSE;
                        showTextView(mText);
                        blueToothGameAty.onPutChess("BLACK");
                    } else if (checkFull()) {//如果棋盘满了
                        mText = STRING_EQUAL;
                        showTextView(mText);
                    }
                    wbflag = WHITE;
                } else if (wbflag == WHITE) {
                    putChess(x, y, WHITE);
                    //this.mGridArray[x][y] = 2;
                    if (checkWin(WHITE)) {
                        mText = STRING_WIN;
                        showTextView(mText);
                        blueToothGameAty.onPutChess("WHITE");
                    } else if (checkFull()) {//如果棋盘满了
                        mText = STRING_EQUAL;
                        showTextView(mText);
                    }
                    wbflag = BLACK;
                }
            }
        }

        this.invalidate();
        return true;

    }

    @Override
    public void onDraw(Canvas canvas) {
        //canvas.drawColor(Color.YELLOW);
        //先画实木背景
        Paint paintBackground = new Paint();
        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.lanya_backgournd);
        canvas.drawBitmap(bitmap, null, new Rect(mStartX, mStartY, mStartX + GRID_WIDTH * GRID_SIZE, mStartY + GRID_WIDTH * GRID_SIZE), paintBackground);
        // 画棋盘
        Paint paintRect = new Paint();
        paintRect.setColor(Color.BLACK);
        paintRect.setStrokeWidth(2);
        paintRect.setStyle(Style.STROKE);
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                int mLeft = i * GRID_WIDTH + mStartX;
                int mTop = j * GRID_WIDTH + mStartY;
                int mRright = mLeft + GRID_WIDTH;
                int mBottom = mTop + GRID_WIDTH;
                canvas.drawRect(mLeft, mTop, mRright, mBottom, paintRect);
            }
        }
        //画棋盘的外边框
        paintRect.setStrokeWidth(4);
        canvas.drawRect(mStartX, mStartY, mStartX + GRID_WIDTH * GRID_SIZE, mStartY + GRID_WIDTH * GRID_SIZE, paintRect);

        //画棋子

        for (int i = 0; i < GRID_SIZE - 1; i++) {
            for (int j = 0; j < GRID_SIZE - 1; j++) {
                if (mGridArray[i][j] == BLACK) {
                    //通过圆形来画
                    {
                        Paint paintCircle = new Paint();
                        paintCircle.setAntiAlias(true);
                        paintCircle.setColor(Color.BLACK);
                        canvas.drawCircle(mStartX + (i + 1) * GRID_WIDTH, mStartY + (j + 1) * GRID_WIDTH, CHESS_DIAMETER / 2, paintCircle);
                    }

                } else if (mGridArray[i][j] == WHITE) {
                    //通过圆形来画
                    {
                        Paint paintCircle = new Paint();
                        paintCircle.setAntiAlias(true);
                        paintCircle.setColor(Color.WHITE);
                        canvas.drawCircle(mStartX + (i + 1) * GRID_WIDTH, mStartY + (j + 1) * GRID_WIDTH, CHESS_DIAMETER / 2, paintCircle);
                    }
                }
            }
        }
    }

    /**
     * 下棋，黑1 白2
     * @param x
     * @param y
     * @param blackwhite
     */
    public void putChess(int x, int y, int blackwhite) {
        mGridArray[x][y] = blackwhite;
        String temp = x + ":" + y;
        storageArray.push(temp);
        //通过回调方法通知Activity下棋动作
        blueToothGameAty.onPutChess(temp + ":" + blackwhite);
        invalidate();
    }

    //收到对方传来的棋子
    public void xiaqi(String command) {
        if ((command==null)) {
            return;
        }
        if ((command.equals("HUIQI"))) {
            if (storageArray.size()==0) {
                Toast.makeText(getContext(),"开局并不能悔棋",Toast.LENGTH_SHORT).show();
            }else {
                if (storageArray.size()==1) {
                    storageArray.pop();
                    mGridArray = new int[GRID_SIZE - 1][GRID_SIZE - 1];
                    invalidate();
                } else {
                    String temp = storageArray.pop();
                    String[] temps = temp.split(":");

                    int a = Integer.parseInt(temps[0]);
                    int b = Integer.parseInt(temps[1]);
                    mGridArray[a][b] = 0;
                    invalidate();
                }
            }
        } else if ((command.equals("REFRESH")))
        {
            setVisibility(View.VISIBLE);
            mStatusTextView.invalidate();
            init();
            invalidate();
            for (int i = 0; i < showtime.length; i++) {
                showtime[i] = 0;
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            mStatusTextView.setText("蓝牙对战模式 当前时间：" + simpleDateFormat.format(new Date()));
        }
        else if ((command.equals("WHITE"))) {
            mStatusTextView.setText("白棋赢辣");
        } else if (command.equals("BLACK")) {
            mStatusTextView.setText("黑棋赢辣");
        } else {
            Log.d("whalea", "收到的指令:" + command);
            String[] temps = command.split(":");
            int a = Integer.parseInt(temps[0]);
            int b = Integer.parseInt(temps[1]);
            int c = Integer.parseInt(temps[2]);
            mGridArray[a][b] = c;
            //  wbflag = c;//该下白棋了=2，该下黑棋了=1. 这里先下黑棋（黑棋以后设置为机器自动下的棋子）
            if (wbflag == 1) {
                wbflag = 2;
            } else {
                wbflag = 1;
            }
            String temp = a + ":" + b;
            storageArray.push(temp);
        }
        invalidate();
    }


    public interface BlueToothActionListner {
        void  onPutChess(String temp);
        void onBtnPress(int i); //0悔棋  1刷新
    }

    public boolean checkWin(int wbflag) {
        for (int i = 0; i < GRID_SIZE - 1; i++) //i表示列(根据宽度算出来的)
            for (int j = 0; j < GRID_SIZE - 1; j++) {//i表示行(根据高度算出来的)
                //检测横轴五个相连
                if (((i + 4) < (GRID_SIZE - 1)) &&
                        (mGridArray[i][j] == wbflag) && (mGridArray[i + 1][j] == wbflag) && (mGridArray[i + 2][j] == wbflag) && (mGridArray[i + 3][j] == wbflag) && (mGridArray[i + 4][j] == wbflag)) {
                    mWinFlag = wbflag;
                }

                //纵轴5个相连
                if (((j + 4) < (GRID_SIZE - 1)) &&
                        (mGridArray[i][j] == wbflag) && (mGridArray[i][j + 1] == wbflag) && (mGridArray[i][j + 2] == wbflag) && (mGridArray[i][j + 3] == wbflag) && (mGridArray[i][j + 4] == wbflag)) {
                    mWinFlag = wbflag;
                }

                //左上到右下5个相连
                if (((j + 4) < (GRID_SIZE - 1)) && ((i + 4) < (GRID_SIZE - 1)) &&
                        (mGridArray[i][j] == wbflag) && (mGridArray[i + 1][j + 1] == wbflag) && (mGridArray[i + 2][j + 2] == wbflag) && (mGridArray[i + 3][j + 3] == wbflag) && (mGridArray[i + 4][j + 4] == wbflag)) {
                    mWinFlag = wbflag;
                }

                //右上到左下5个相连
                if (((i - 4) >= 0) && ((j + 4) < (GRID_SIZE - 1)) &&
                        (mGridArray[i][j] == wbflag) && (mGridArray[i - 1][j + 1] == wbflag) && (mGridArray[i - 2][j + 2] == wbflag) && (mGridArray[i - 3][j + 3] == wbflag) && (mGridArray[i - 4][j + 4] == wbflag)) {
                    mWinFlag = wbflag;
                }
            }
        if (mWinFlag == wbflag) {
            return true;
        } else
            return false;
    }

    /**
     * 检查棋盘是否满了
     *
     * @return
     */
    public boolean checkFull() {
        int mNotEmpty = 0;
        for (int i = 0; i < GRID_SIZE - 1; i++)
            for (int j = 0; j < GRID_SIZE - 1; j++) {
                if (mGridArray[i][j] != 0) mNotEmpty += 1;
            }

        if (mNotEmpty == (GRID_SIZE - 1) * (GRID_SIZE - 1)) return true;
        else return false;
    }

    public void showTextView(CharSequence mT) {
        this.mStatusTextView.setText(mT);
        mStatusTextView.setVisibility(View.VISIBLE);
    }

    private int[] showtime;

    public void setShowTimeTextViewTime(int[] showtime) {
        this.showtime = showtime;
    }

    class MyButtonListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //如果是悔棋
                case R.id.bluetooth_btn1:
                    blueToothGameAty.onBtnPress(0);
                    if (storageArray.size()==0) {
                        Toast.makeText(getContext(),"开局并不能悔棋",Toast.LENGTH_SHORT).show();
                    }else {
                        if (storageArray.size()==1) {
                            storageArray.pop();
                            mGridArray = new int[GRID_SIZE - 1][GRID_SIZE - 1];
                            invalidate();
                        } else {
                            String temp = storageArray.pop();
                            String[] temps = temp.split(":");

                            int a = Integer.parseInt(temps[0]);
                            int b = Integer.parseInt(temps[1]);
                            mGridArray[a][b] = 0;
                            invalidate();
                        }
                    }
                    break;
                //如果是刷新
                case R.id.bluetooth_btn2:
                    blueToothGameAty.onBtnPress(1);
                    setVisibility(View.VISIBLE);
                    mStatusTextView.invalidate();
                    init();
                    invalidate();
                    for (int i = 0; i < showtime.length; i++) {
                        showtime[i] = 0;
                    }
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
                    mStatusTextView.setText("蓝牙对战模式 当前时间：" + simpleDateFormat.format(new Date()));
                    break;
            }
        }
    }
}
