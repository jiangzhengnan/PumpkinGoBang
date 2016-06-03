package com.whale.nangua.pumpkingobang.aty;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.whale.nangua.pumpkingobang.R;
import com.whale.nangua.pumpkingobang.view.RenRenGoBang;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class RenRenGameAty extends Activity {

    private RenRenGoBang gbv;
    TextView renren_textView;
    Button renren_huiqi;
    Button renren_shuaxin;
    TextView renren_showtime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.renrengame_layout);
        initView();
    }

    private void initView() {
        renren_showtime = (TextView) findViewById(R.id.renren_showtime);
        gbv = (RenRenGoBang)findViewById(R.id.renren_gobangview);
        renren_textView = (TextView) findViewById(R.id.renren_text);
        renren_huiqi = (Button) findViewById(R.id.renren_btn1);
        renren_shuaxin = (Button) findViewById(R.id.renren_btn2);
        SimpleDateFormat simpleDateFormat = null;
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        renren_textView.setText("当前时间：" + simpleDateFormat.format(new Date()));
        gbv.setTextView(renren_textView);
        gbv.setButtons(renren_huiqi, renren_shuaxin);
        gbv.setShowTimeTextViewTime(jishitime);
        Timer timer = new Timer();
        JishiTask myTask = new JishiTask();
        timer.schedule(myTask, 1000,1000);
    }

    int[] jishitime = {0,0,0,0};//秒，分，时，总
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
                        jishitime[2]=0;
                    }
                    renren_showtime.setText(String.format("%02d:%02d:%02d",jishitime[2],jishitime[1],jishitime[0]));
                }
            });
        }
    }
}
