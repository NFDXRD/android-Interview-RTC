# CHANGELOG

## Version 1.1.2

*2018-11-15*

* 解决一个入会时，setNumber不生效的问题

## Version 1.1.1

*2018-11-13*

* 增加预定人数设置接口call.setNumber(int number)，根据人数可提前留出位置

## Version 1.1.0

*2018-11-9*

* 取消语音播报，可一个人长时间待在会中
* 增加录制接口和录制是否开启监听接口

## Version 1.0.9

*2018-11-8*

* ZjVideoManager.disconnect()回调CALL_END

## Version 1.0.8

*2018-11-8*

* ZjRTCViewManager.onDestory(Activity activity)中做mReactInstanceManager.destroy();
* 修改demo,在Application中ZjRTCViewManager.init(this);

## Version 1.0.7

*2018-11-7*

* 解决大屏下，切换摄像头镜像的问题。
* 取消切换摄像头接口调用限制，由sdk使用者控制
* 解决绿屏问题
* Address和Pwd可不用设置，sdk使用默认值

## Version 1.0.6

*2018-11-6*

* 解决候选人先入会，收不到面试官流的问题
* 切换摄像头接口调用限制，成功调用一次后，两秒内再次调用无效

## Version 1.0.5

*2018-11-5*

* 解决大小缩放闪现背景的问题
* 退会接口对js类进行释放

## Version 1.0.4

*2018-11-1*

* 提供setReactRootView()接口
* 没有视频的地方显示透明
* 处理后置摄像头画面成像反的问题

## Version 1.0.3

*2018-10-30*

* 提取出rootView，可自定义显示位置
* 解决视频播放前的黑屏问题

## Version 1.0.2

*2018-10-24*

* 名称过长显示三个字加...
* 新增回调与会者列表信息

## Version 1.0.1

*2018-10-23*

* 新增点击放大缩小功能
* 新增静画（即语音模式）显示语音模式图片，说话时显示语音icon
* 新增发消息和接收消息接口

## Version 1.0.0

*2018-10-22*

* first commit

