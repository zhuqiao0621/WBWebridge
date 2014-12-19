WBWebridge
==========

1、介绍

Webridge开源项目为混合式App(Native + Web)开发提供两个核心机制: WBURI + WBWebBridge

WBURI 
    一种统一资源描述符，包含格式约定和分发机制。通过注册UriListener实现功能扩展。
    网页中用WBURI形式的超链接可访问各种原生功能(资源)。

WBWebBridge
    原生代码与网页JS之间互相调用的一种机制，约定了互相调用的格式。通过设置WBWebridgeListener实现功能扩展。

1.1 WBURI基本格式

原始格式

兼容HTTP格式 http://[host]/scheme/[command]/[param1]/[param2]/[param3]

分为scheme、command、params三部分，这三部分都可以由外部自定义。

scheme 方案 command 命令 params 参数

1.2 WBWebBridge调用机制

1.2.1 原生代码调用网页内的js函数，并获得返回值。

可调用的js函数格式约定
  参数个数：一个 或 无
  参数类型：json对象
  返回值：  json对象 或 无

1.2.2 网页内的js函数调用原生代码，传递方法名、参数、回调函数名。通过回调函数获得返回值。

可调用的原生方法约定：
  参数个数：一个 或 无
  参数类型：json对象
  返回值：  json对象 或 无

回调js函数格式约定：
  参数个数：一个
  参数类型：json对象 {'result': 原生方法返回的json对象, 'error': 错误信息字符串 }
  返回值：  无

2、支持环境

2.3.3以上

3、适合场景

混合式App(Native + Web)开发

4、api

// under construction

5、例子

// under construction

6、如何引入工程

1 将WBURI WBWebView WBWebridge三个类添加到工程
2 使用WBWebView作为网页容器，指定UriListener和WBWebridgeListener
3 使用WBUri.openURI打开网页内触发的uri
4 网页中引入webridge.js，使得网页可调用WBWebridgeListener的方法

