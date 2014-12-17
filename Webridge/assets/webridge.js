// command   原生方法名 (客户端的方法名)
// params    参数，格式为json对象
// callback  回调函数名 (js的方法名)
function wbHTMLToNative(command, params, callback) {
    var message={"command":command,"params":params,"callback":callback};
    var isiOS=navigator.userAgent.match('iPad')
    || navigator.userAgent.match('iPhone')
    || navigator.userAgent.match('iPod');
    var isAndroid=navigator.userAgent.match('Android');
    var isDesktop=(!isiOS && !isAndroid);
    if (isiOS) {
        window.webkit.messageHandlers.webridge.postMessage({body:message});
    } else if (isAndroid) {
        window.androidWebridge.postMessage(JSON.stringify(message));
    } else if (isDesktop) {
        // TODO: desktop
    }
}

// jsCommand 需要调用的js方法名
// jsParams  需要调用的js方法参数
function wbNativeToHTML(jsCommand, jsParams) {
    var jsParamsString = JSON.stringify(jsParams);
    var jsScript = jsCommand + '(' + jsParamsString + ')';
    var result = eval(jsScript);
    var isAndroid = navigator.userAgent.match('Android');
    if (isAndroid) {
        window.androidWebridge.returnMessage(JSON.stringify(result));
    }
    else {
        // iOS or other platform
        return result;
    }
}