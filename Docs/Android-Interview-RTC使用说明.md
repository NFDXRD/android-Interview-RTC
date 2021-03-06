# Android-Interview-RTC使用说明

##	安装环境

1.	复制`zjrtc.aar`包到`app的libs`目录下；

2.	复制`maven`到工程根目录下；

3.	修改`工程`下的`build.gradle`文件：

    代码如下：

    ```
    allprojects {
        repositories {
            jcenter()
            maven {
                url "$rootDir/maven"
            }
        }
    }
    ```

4.	修改app下的build.gradle文件：

    代码如下：

    ```
    android {
        defaultConfig {
            minSdkVersion 16    // <---确保最低版本在16或16以上
            ndk {
                abiFilters "armeabi-v7a", "x86"
            }
        }
        configurations.all{
        	 resolutionStrategy.force'com.google.code.findbugs:jsr305:3.0.0'
        } 
        repositories {
            //配置aar
            flatDir {
                dirs 'libs'
            }
        } 
    }
    dependencies {
        compile 'com.facebook.react:react-native:+'
        compile(name:'zjrtc', ext:'aar') //添加aar包
}
    ```

    确保`External Library`中包含的`react-native`版本为`0.44.3`。
    
## 偏好设置

在进行通话前，需要先进行偏好设置。

偏好设置包括：服务器地址设置、视频参数设置、功能设置。其中服务器地址是必需设置项，没有设置将导致通话失败。视频参数和功能设置作为可选项，不设置将使用默认值。

偏好设置设置一次即可一直生效，直到重新设置将使用新参数值。

#### ZjVideoPreferences(Context context)

进行偏好设置需构造偏好设置对象。
如在Activity中可按以下代码进行构造：

```
ZjVideoPreferences prefs = new ZjVideoPreferences(this);
```

#### setDomain(String domain)(必需)

设置服务器地址，必需设置项，没有设置将导致呼叫失败。

#### setBandwidth(int bandwidth) 

设置呼叫速率，上行/下行一致。默认为800kbps。

#### setBandwidth(int upBw, int downBw)

分别设置上行/下行呼叫速率。

#### setVideoSize(int width, int height)

设置视频分辨率，发送/接收的视频分辨率一致。默认视频分辨率为480x640。

#### setVideoSize(int upWidth, int upHeight, int downWidth, int downHeight)

分别设置手机发送/接收的视频分辨率。

#### setVideoFps(int fps)

设置视频帧率，发送/接收的视频帧率一致。默认帧率20。

#### setVideoFps(int upFps, int downFps)

分别设置发送/接收视频帧率。

**视频属性参考表**


| 视频属性 | 分辨率(宽x高) | 帧率(fps) | 速率(kbps) |
| :-: | :-: | :-: | :-: |
|	720p	|	1280x720	|	25	|	800 |
|	720p	|	1280x720	|	20	|	640 |
|	720p	|	1280x720	|	15	|	512 |
|	576p	|	1024x576	|	25	|	600 |
|	576p	|	1024x576	|	20	|	512 |
|	576p	|	1024x576	|	15	|	400 |
|	448p	|	768x448	|	25	|	400 |
|	448p	|	768x448	|	20	|	360 |
|	448p	|	768x448	|	15	|	320 |
|	360p	|	640x360	|	25	|	360 |
|	360p	|	640x360	|	20	|	300 |
|	360p	|	640x360	|	15	|	240 |
|	WCIF	|	512x288	|	25	|	260 |
|	WCIF	|	512x288	|	20	|	192 |
|	WCIF	|	512x288	|	15	|	160 |

#### setSoftCode(boolean softCode)

设置使用视频编码方式为软编解。
参数为true 使用软编解，false硬编解；默认值为false。
如果设备不支持硬编解码，入会后可能出现手机端接收的视频不正常，或其他端收到手机端发送的视频有问题的情况，此时可使用此方法关闭硬编解码，使用软编解方式。

#### setH264SoftDocoder(boolean h264SoftDecoder)

设置使用h264软解。
参数为true 使用h264软解，false为h264硬解；默认值为false。
如果设备h264硬解有问题，就使用软解，还有问题就设置setSoftCode(true)

## 通话界面

### 自定义通话界面

如果需要自定义通话界面，创建自己的`Activity`，要`extends AppCompatActivity implements DefaultHardwareBackBtnHandler`。

1. Activiy需要在AndroidManifest.xml中配置一下主题Theme.ReactNative.AppCompat.Light（react native好多组件用的这个主题），最好配置下进程（react native存在内存泄漏的问题，反复入会退会会导致内存泄漏，目前最好的解决方法是开一个新的进程，在退出该界面后，杀掉该进程来释放内存）：
 
    ```
    <activity
            android:name=".MyVideoActivity"
            android:process=":myprocess"
            android:theme="@style/Theme.ReactNative.AppCompat.Light"/>
    ```
    
2. 在`onCreate()`中做以下操作

    代码示例：

    ```
    ReactRootView rootView = (ReactRootView) findViewById(R.id.root_view);
    call = (ZjCall) getIntent().getSerializableExtra("call");
    //设置呼叫参数
    ZjVideoManager.getInstance().setCall(call);
    //初始化ZjRTCViewManager
    ZjRTCViewManager.init(getApplication());
    //设置rootView
    ZjRTCViewManager.setReactRootView(rootView);
    //打开reactApplication
    ZjRTCViewManager.startReactApplication();
    ```

3. 实现`DefaultHardwareBackBtnHandler`接口的`invokeDefaultOnBackPressed()`方法，在activity的部分生命周期对ZjRTCViewManager和ZjRTCViewManager做相应操作，详见demo

    ```
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
    ```

## 建立通话

### 呼叫说明

每次呼叫，需要new一个ZjCall的对象，设置显示名、呼叫地址、密码(面试官用主持人密码，候选人用访客密码)、checkdup、是否是面试官，然后跳转至自定义通话界面，把ZjCall的实例传过去。

示例：

    ```
    //构建呼叫参数类，设置显示名称、呼叫地址、呼叫密码；
    ZjCall call = new ZjCall();
    call.setDisplayName("面试官");
    call.setAddress("9343");
    call.setPwd("123456");//会议室主持人密码
    call.setCheckDup(MD5Util.MD5(Build.MODEL+"面试官"));
    call.setInterviewer(true);

    Intent intent = new Intent(InterviewActivity.this,MyVideoActivity.class);
    intent.putExtra("call",call);
    startActivity(intent);
    ```

### ZjCall

在通话建立前，需要new一个ZjCall的对象进行呼叫参数设置。

方法如下：

#### setDisplayName(String displayName)(必需)

设置显示名称。

#### setAddress(String address)(必需)

设置呼叫地址。

#### setPwd(String pwd)

设置呼叫地址所需要的密码。

#### setCheckDup(String checkDup)

用于检查重复参会者。
入会时会检查同一会议室中是否已存在同名且checkDup值一样的参会者，如果存在则入会，并将同名参会者踢出会议。checkDup是一个30位以上长度的字符串，一般用MD5 Hash生成（32位）。（建议用手机序列号+名称拼接，并用MD5加密生成）

## 通话管理

### ZjVideoManager

在通话过程中，可调用ZjVideoManager进行结束通话、关闭麦克风等其他操作。

#### disconnect()

断开通话连接，即挂断。

#### disconnectAll()

断开所有通话连接，即结束会议。

#### toggleMicrophone()

打开/关闭麦克风。

#### toggleCamera()

打开/关闭摄像头。

#### switchCamera()

切换摄像头。

#### reconnect()

重连，用于异常时重新连接

#### sendMessage(String message)

发消息，群发。

#### callOut(String destination, String protocol, String role)

外呼方法，destination:外呼地址，protocol:协议(如：‘sip’,'h.323')，role:角色('host','guest')

#### openSpeaker(Context context, boolean on)

打开/关闭系统扬声器。(建立通话前调用)

参数说明：

* `context`-`Context`：Android上下文；
* `on`-`boolean`：true打开;false关闭；

#### addZjCallListener(ZjCallListener listener)

添加呼叫相关回调方法，当呼叫状态改变、静音状态改变时，其中回调方法会被调用。(建立通话前调用)
每次呼叫都需要创建新的ZjCallListenerBase对象，在通话结束后对象会被销毁。

参数说明：

* ZjCallListener 回调接口，可创建接口的实现类ZjCallListenerBase，按需求重写其中方法。

ZjCallListener 接口方法说明：

```
/**
 * 呼叫状态回调方法，当呼叫状态改变时此方法会被调用
 * @param state 呼叫状态
 *              CALL_INIT：      呼叫初始化
 *              CALL_CONNECTING：呼叫连接中
 *              CALL_CONNECTED： 呼叫成功
 *              CALL_END：       呼叫断开
 *              CALL_ERROR：     呼叫错误
 * @param info 额外信息
 *             CALL_CONNECTED：  uuid
 *             CALL_END：        断开原因
 *             CALL_ERROR：      错误信息
 */
void callState(String state, String info);

/**
 * 音视频状态信息 数据格式
 *               [["","channal","codec","resolution","frameRate","bitrate","PacketLoss"],
 *               ["local","audio-send","opus","--","--",35,"0.0%"],
 *               ["remote","audio-receive","opus","--","--",6,"0.0%"],
 *               ["应聘者","video-send","VP8","368x480","30",202,"0.0%"],
 *               ["面试官","video-receive","VP8","256x144","16",127,"0.0%"],
 *               ["候选人","video-receive","VP8","256x144","16",130,"0.0%"]]
 *
 *             额外说明：初次状态值为[]；"NaN%"表示正在收集状态值。
 * @param params
 */
void videoState(ReadableArray params);

/**
 * 被动静音
 * 静音状态回调方法，当静音状态改变时此方法会被调用
 * @param muted 是否被静音
 */
void onMuteChanged(boolean muted);

/**
 * 静画状态回调，当静画状态改变时此方法会被调用
 * @param isPause 是否静画
 */
void onCameraState(boolean isPause);

/**
 * 主动静音
 * 静音状态回调方法，当静音状态改变时此方法会被调用
 * @param isPause
 */
void onMicrophoneState(boolean isPause);

/**
 * 接收到消息回调
 * @param uuid
 * @param message
 */
void onChatMessage(String uuid,String message);

/**
 * 与会者列表更新
 * @param participants
 */
void onParticipantsUpdate(ReadableArray participants);
```

使用示例：

```
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
});
```

