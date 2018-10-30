package com.zijingdemo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.facebook.react.ReactRootView;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.zjrtc.ZjCall;
import com.zjrtc.ZjCallListenerBase;
import com.zjrtc.ZjRTCViewManager;
import com.zjrtc.ZjVideoManager;

public class MyVideoActivity extends AppCompatActivity implements DefaultHardwareBackBtnHandler {

    private final String TAG = "MyVideoActivity";

    private ReactRootView mReactRootView;

    private ZjCall call;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.video_layout);

        call = (ZjCall) getIntent().getSerializableExtra("call");
        //设置呼叫参数
        ZjVideoManager.getInstance().setCall(call);
        //初始化ZjRTCViewManager
        ZjRTCViewManager.init(getApplication());
        //获取rootView
        mReactRootView = ZjRTCViewManager.getRootView(this);
        //打开reactApplication
        ZjRTCViewManager.startReactApplication();

        //显示rootView
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(dm.widthPixels,dm.widthPixels);
        params.gravity = Gravity.TOP;
        addContentView(mReactRootView,params);

        setListener();
    }

    @Override
    public void invokeDefaultOnBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ZjRTCViewManager.onResume(this,this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ZjRTCViewManager.onDestory(this);
        ZjVideoManager.getInstance().release();
    }

    @Override
    public void onBackPressed() {
        //传递返回按钮点击事件（退会操作），不设置可以自己处理返回按钮事件，但要记得做退会操作。
        ZjRTCViewManager.onBackPressed();
    }

    private void setListener(){
        ZjVideoManager.getInstance().addZjCallListener(new ZjCallListenerBase(){
            @Override
            public void videoState(ReadableArray params) {
                Log.i(TAG,params.toString());
                super.videoState(params);
            }

            @Override
            public void onChatMessage(String uuid, String message) {
                Log.i(TAG,message);
                super.onChatMessage(uuid, message);
            }

            @Override
            public void onParticipantsUpdate(ReadableArray participants) {
                Log.i(TAG,"participants:" + participants);
                super.onParticipantsUpdate(participants);
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

    public void reconnect(View v){
        ZjVideoManager.getInstance().reconnect();
    }

    public void callOut(View v){
        final EditText editText = new EditText(this);
        final AlertDialog.Builder editDialog = new AlertDialog.Builder(this);
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
