package com.zq.webridge.util;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.webkit.WebView;

public class WBWebridge {
	public static final int CALL_JS = 0;
	public static final String COMMAND_ERROR = "WebridgeDelegate doesn't know method: ";
	public static final String DELEGATE_ERROR = "WebridgeDelegate exception on method: ";

	private WebView mWebView;
	private WBWebridgeListener mWbWebridgeListener;

	public static String testReturn;
	public static String testJsToNative;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == CALL_JS) {
				if (msg.obj instanceof String) {
					mWebView.loadUrl((String) msg.obj);
					testJsToNative = (String) msg.obj;
				}
			}
		}

	};

	public WBWebridge(WebView webView, WBWebridgeListener listener) {
		mWebView = webView;
		mWbWebridgeListener = listener;
	}

	/**
	 * js调用的native方法名
	 * 
	 * @param json
	 */
	public void postMessage(String json) {
		System.out.println("postMessage:" + json);
		JSONObject obj = null;
		try {
			obj = new JSONObject(json);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		if (obj == null) {
			return;
		}
		// 需要执行的native方法名
		String command = obj.optString("command");
		// 需要执行的native方法参数
		String parma = obj.optString("params");
		// 执行完native方法需要调用的js方法名(js参数是native方法的返回值)
		String callback = obj.optString("callback");

		// NOTE 请求原生方法
		if (TextUtils.isEmpty(command)) {
			callbackJs(callback, "", COMMAND_ERROR + command);
			return;
		}
		Object result = null;
		try {
			if (TextUtils.isEmpty(parma)) {
				result = InvokeMethod.invokeMethod(mWbWebridgeListener,
						command, null);
			} else {
				result = InvokeMethod.invokeMethod(mWbWebridgeListener,
						command, new Object[] { parma });
			}
		} catch (Exception e) {
			e.printStackTrace();
			callbackJs(callback, "", COMMAND_ERROR + command);
		}

		// NOTE 请求js方法
		if (TextUtils.isEmpty(callback))
			return;
		if (!(result instanceof String)) {
			callbackJs(callback, "", DELEGATE_ERROR + command);
		} else {
			try {
				new JSONObject(result.toString());
				callbackJs(callback, result.toString(), "");
			} catch (Exception e) {
				e.printStackTrace();
				callbackJs(callback, "", DELEGATE_ERROR + command);
			}
		}
	}

	/**
	 * 调用js，js返回结果调用的native方法名
	 * 
	 * @param json
	 */
	public void returnMessage(final String json) {
		System.out.println("returnMessage:" + json);
		testReturn = json;
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				showDialog(json);
			}
		});
	}

	/**
	 * 创建回调js
	 * 
	 * @param callback
	 *            回调的js方法名称
	 * @param nativeReturnJson
	 *            回调的js方法参数
	 * @param errorMsg
	 *            错误信息
	 * @return
	 */
	private void callbackJs(String callback, String nativeReturnJson,
			String errorMsg) {
		String js = "javascript:" + callback;
		JSONObject resultObj = new JSONObject();
		try {
			if (!TextUtils.isEmpty(errorMsg)) {
				resultObj.put("result", "");
				resultObj.put("error", errorMsg);
				js += "(" + resultObj.toString() + ")";
			} else if (TextUtils.isEmpty(nativeReturnJson)) {
				// 没有参数
				js += "()";
			} else {
				JSONObject obj = new JSONObject(nativeReturnJson);
				resultObj.put("result", obj);
				resultObj.put("error", "");
				js += "(" + resultObj.toString() + ")";
			}
		} catch (Exception e) {
			e.printStackTrace();
			js += "()";
		}

		Message msg = new Message();
		msg.what = CALL_JS;
		msg.obj = js;
		mHandler.sendMessage(msg);
	}

	private void showDialog(String result) {
		Builder build = new AlertDialog.Builder(mWebView.getContext());
		build.setTitle("收到来自js的返回值");
		build.setMessage(result);
		build.setNegativeButton("确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		build.create().show();
	}
}
