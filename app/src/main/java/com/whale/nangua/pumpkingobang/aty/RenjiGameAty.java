package com.whale.nangua.pumpkingobang.aty;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.whale.nangua.pumpkingobang.R;
import com.whale.nangua.pumpkingobang.utils.DBUtils;
import com.whale.nangua.pumpkingobang.view.RenjiGobangView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class RenjiGameAty extends Activity implements RenjiGobangView.OnWinListener {
    public   RenjiGameAty renjiGameAty = this;
    RenjiGobangView gbv;
    TextView textView;
    Button huiqi;
    Button shuaxin;
    TextView showtime;
    ImageView renjibeijing;
    AnimationDrawable animationDrawable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.renjigame_layout);
        renjiGameAty = this;
        initView();
    }

    private void initView() {
        showtime = (TextView) findViewById(R.id.showtime);
        gbv = (RenjiGobangView) this.findViewById(R.id.gobangview);
        textView = (TextView) findViewById(R.id.text);
        renjibeijing = (ImageView) findViewById(R.id.renjibeijingdongtu);
        renjibeijing.setImageResource(R.drawable.renjibackground_animation);
        animationDrawable = (AnimationDrawable) renjibeijing.getDrawable();
        animationDrawable.start();
        huiqi = (Button) findViewById(R.id.btn1);
        shuaxin = (Button) findViewById(R.id.btn2);
        SimpleDateFormat simpleDateFormat = null;
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        textView.setText("当前时间：" + simpleDateFormat.format(new Date()));

        gbv.setTextView(textView);
        gbv.setButtons(huiqi, shuaxin);
        gbv.setShowTimeTextViewTime(jishitime);
        gbv.setWinListner(this);
        Timer timer = new Timer();
        JishiTask myTask = new JishiTask();
        timer.schedule(myTask, 1000, 1000);
        renjisetflag(getIntent().getIntExtra("flag",0));//设置难度，默认为简单
    }

    public  void renjisetflag(int flag){
        gbv.setflag(flag);
    };

    int[] jishitime = {0, 0, 0, 0};//秒，分，时，总

    EditText editText;
    @Override
    public void onWin(int i) {
        final AlertDialog mydialog= new AlertDialog.Builder(RenjiGameAty.this).create();
        if(i==1) {
            //如果黑棋赢了
            mydialog.show();
            mydialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            mydialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            mydialog.getWindow().setContentView(R.layout.win_dialog);

            editText = (EditText) mydialog.getWindow().findViewById(R.id.windialog_ev);


            TextView showview = (TextView) mydialog.getWindow().findViewById(R.id.windialog_tv);
            showview.setText("你赢辣~！");
            mydialog.getWindow()
                    .findViewById(R.id.windialog_submit)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //这里提交名字和时间数据到数据库
                            int totaltime = jishitime[3];
                            DBUtils dbUtils = new DBUtils(RenjiGameAty.this);
                            boolean result = dbUtils.insertTime(totaltime, editText.getText().toString()); //插入时间和名字
                            if (result == true) {
                                mydialog.dismiss();
                                Toast.makeText(RenjiGameAty.this,"插入成功！",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            mydialog.getWindow()
                    .findViewById(R.id.windialog_cancle)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mydialog.dismiss();
                        }
                    });
        }else {
            //如果白棋赢了
            mydialog.show();
            mydialog.getWindow().setContentView(R.layout.lose_dialog);
            mydialog.getWindow()
                    .findViewById(R.id.losedialog_submit)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                          mydialog.dismiss();
                        }
                    });
        }
    }

    private class JishiTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    jishitime[0]++;
                    jishitime[3]++;
                    if (jishitime[0] == 60) {
                        jishitime[1]++;
                        jishitime[0] = 0;
                    }
                    if (jishitime[1] == 60) {
                        jishitime[2]++;
                        jishitime[1] = 0;
                    }
                    if (jishitime[2] == 24) {
                        jishitime[2] = 0;
                    }
                    showtime.setText(String.format("%02d:%02d:%02d", jishitime[2], jishitime[1], jishitime[0]));
                }
            });
        }
    }
}
