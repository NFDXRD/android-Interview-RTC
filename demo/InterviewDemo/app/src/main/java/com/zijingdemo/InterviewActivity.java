package com.zijingdemo;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.zjrtc.ZjCall;
import com.zjrtc.ZjVideoPreferences;

public class InterviewActivity extends AppCompatActivity {

    private EditText etNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview);

        etNumber = (EditText) findViewById(R.id.et_number);

        //设置服务器地址、视频参数等偏好设置
        ZjVideoPreferences prefs = new ZjVideoPreferences(this);
        prefs.setDomain(getString(R.string.domain));
        prefs.setVideoSize(480,640);
        prefs.setBandwidth(800);
        prefs.setVideoFps(20);

//        prefs.setPrintLogs(false); //打印日志
//        prefs.setSoftCode(true);  //软编软解
//        prefs.setH264SoftDocoder(false);    //h264软解

        findViewById(R.id.btn_interview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //构建呼叫参数类，设置显示名称、呼叫地址、呼叫密码；
                ZjCall call = new ZjCall();
                call.setDisplayName("面试官");
                //会议室地址和密码不设置，使用默认设置
                call.setAddress("9343");
                call.setPwd("123456");//会议室主持人密码
                call.setCheckDup(MD5Util.MD5(Build.MODEL+"面试官"));
                call.setInterviewer(true);
                call.setNumber(Integer.parseInt(etNumber.getText().toString()));

                Intent intent = new Intent(InterviewActivity.this,MyVideoActivity.class);
                intent.putExtra("call",call);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_candidate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //构建呼叫参数类，设置显示名称、呼叫地址、呼叫密码；
                ZjCall call = new ZjCall();
                call.setDisplayName("应聘者");
                //会议室地址和密码不设置，使用默认设置
                call.setAddress("9343");
                call.setPwd("123456");//会议室访客密码
                call.setCheckDup(MD5Util.MD5(Build.MODEL+"应聘者"));
                call.setInterviewer(false);
                call.setNumber(Integer.parseInt(etNumber.getText().toString()));

                Intent intent = new Intent(InterviewActivity.this,MyVideoActivity.class);
                intent.putExtra("call",call);
                startActivity(intent);
            }
        });

    }
}
