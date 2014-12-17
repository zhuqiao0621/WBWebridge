package com.zq.webridgetest.util;

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

	private WebView mWebView;
	private WBWebridgeImplement mImplement;

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

	public WBWebridge(WebView webView, WBWebridgeImplement implement) {
		mWebView = webView;
		mImplement = implement;
	}

	/**
	 * js调用的native方法名
	 * 
	 * @param json
	 */
	public void postMessage(String json) {
		System.out.println("postMessage:" + json);
		try {
			JSONObject obj = new JSONObject(json);
			// 需要执行的native方法名
			String command = obj.optString("command");
			// 需要执行的native方法参数
			String parma = obj.optString("params");
			// 执行完native方法需要调用的js方法名(js参数是native方法的返回值)
			String callback = obj.optString("callback");

			// NOTE 请求原生方法
			if (TextUtils.isEmpty(command)) {
				return;
			}
			Object result;
			if (TextUtils.isEmpty(parma)) {
				result = InvokeMethod.invokeMethod(mImplement, command, null);
			} else {
				result = InvokeMethod.invokeMethod(mImplement, command,
						new Object[] { parma });
			}

			// NOTE 请求js方法
			if (TextUtils.isEmpty(callback))
				return;
			callBack(callback, result);
		} catch (Exception e) {
			e.printStackTrace();
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
	 * 执行完native方法调用js
	 * 
	 * @param callback
	 * @param result
	 */
	private void callBack(String callback, Object result) {
		String js = "javascript:" + callback;
		if (result instanceof String) {
			String json = (String) result;
			try {
				JSONObject obj = new JSONObject(json);
				JSONObject resultObj = new JSONObject();
				resultObj.put("result", obj);
				js += "(" + resultObj.toString() + ")";
			} catch (JSONException e) {
				e.printStackTrace();
				js += "()";
			}
		} else {
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
