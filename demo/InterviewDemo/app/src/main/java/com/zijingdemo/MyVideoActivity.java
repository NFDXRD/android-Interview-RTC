package com.zijingdemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.facebook.react.bridge.ReadableArray;
import com.zjrtc.ZjCallListenerBase;
import com.zjrtc.ZjVideoActivity;
import com.zjrtc.ZjVideoManager;

public class MyVideoActivity extends ZjVideoActivity {

    private static final String TAG = "MyVideoActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(this).inflate(R.layout.video_layout, null);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        addContentView(view, params);

        //强制打开扬声器
        ZjVideoManager.getInstance().openSpeaker(this,true);

        setListener();
    }

    private void setListener() {
        ZjVideoManager.getInstance().addZjCallListener(new ZjCallListenerBase(){

            @Override
            public void callState(String state, String info) {
                Log.v(TAG,state + ":" + info);
            }

            @Override
            public void videoState(ReadableArray params) {
                Log.v(TAG, "videoState:"+params);
            }

            @Override
            public void onMuteChanged(boolean muted) {
                Log.v(TAG,"onMuteChange:"+muted);
            }

            @Override
            public void onCameraState(boolean isPause) {
                Log.v(TAG,"onCameraState:"+isPause);
            }

            @Override
            public void onMicrophoneState(boolean isPause) {
                Log.v(TAG,"onMicrophoneState"+isPause);
            }

            @Override
            public void onChatMessage(String uuid, String message) {
                Log.v(TAG,"onChatMessage"+uuid+message);
            }

            @Override
            public void onParticipantsUpdate(ReadableArray participants) {
                Log.v(TAG,"participants"+participants);
            }
        });
    }

    public void switchCamera(View v){
        ZjVideoManager.getInstance().switchCamera();
    }

    public void pauseCamara(View v){
        ZjVideoManager.getInstance().toggleCamera();
    }

    public void pauseMic(View v){
        ZjVideoManager.getInstance().toggleMicrophone();
    }

    public void disconnect(View v){
        ZjVideoManager.getInstance().disconnect();
    }

    public void sendMessage(View v){
        ZjVideoManager.getInstance().sendMessage("哈哈哈");
    }

    public void callOut(View v){
        final EditText editText = new EditText(MyVideoActivity.this);
        final AlertDialog.Builder editDialog = new AlertDialog.Builder(MyVideoActivity.this);
        editDialog.setTitle("请输入外呼地址")
                .setView(editText).setNegativeButton("取消",null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String to = editText.getText().toString().trim();
                                /*  参数一：呼叫地址
                                    参数二：协议：如：sip:使用name@domain格式的SIP地址
                                                    h.323:H.323 地址可以是一个数字, 一个文本字符串,一个IP地址, 或name@domain
                                                    call:请输入待拨打的电话号码。国内手机直接拨打；国内普通电话请用0开头的区号；国际请用00或+后接国际区号
                                                    aotu:地址可以是一个数字, 一个文本字符串,一个IP地址, 或name@domain
                                    参数三：host ：主持人
                                           guest：访客
                                 */
                        ZjVideoManager.getInstance().callOut(to,"auto","host");
                    }
                }).show();
    }
}
