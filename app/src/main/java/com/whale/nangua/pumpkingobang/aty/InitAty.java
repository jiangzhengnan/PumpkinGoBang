package com.whale.nangua.pumpkingobang.aty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.whale.nangua.pumpkingobang.R;

/**
 * Created by nangua on 2016/6/3.
 */
public class InitAty extends Activity {
    private Button init_renjibtn;
    private Button init_lanyabtn;
    private Button init_socketbtn;
    private Button init_rankbtn;
    private Button init_helpbtn;
    private InitButtonListener initButtonListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.init_layout);
        initView();
    }

    private void initView() {
        init_renjibtn = (Button) findViewById(R.id.init_renjibtn);
        init_lanyabtn = (Button) findViewById(R.id.init_lanyabtn);
        init_socketbtn = (Button) findViewById(R.id.init_socketbtn);
        init_rankbtn = (Button) findViewById(R.id.init_rankbtn);
        init_helpbtn = (Button) findViewById(R.id.init_helpbtn);
        initButtonListener = new InitButtonListener();
        init_renjibtn.setOnClickListener(initButtonListener);
        init_lanyabtn.setOnClickListener(initButtonListener);
        init_socketbtn.setOnClickListener(initButtonListener);
        init_rankbtn.setOnClickListener(initButtonListener);
        init_helpbtn.setOnClickListener(initButtonListener);
    }


    private class InitButtonListener implements View.OnClickListener{
        Intent i ;
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.init_renjibtn:
                    i = new Intent(InitAty.this,RenjiGameAty.class);
                    //设置从右边出现
                    InitAty.this.overridePendingTransition(R.anim.initactivity_open, 0);
                    startActivity(i);
                break;
                case R.id.init_lanyabtn:
                    i = new Intent(InitAty.this,FindOthersAty.class);
                    //设置从右边出现
                    InitAty.this.overridePendingTransition(R.anim.initactivity_open, 0);
                    startActivity(i);
                break;
                case R.id.init_socketbtn:

                break;
                case R.id.init_rankbtn:

                break;
                case R.id.init_helpbtn:

                break;


            }
        }
    }
}
