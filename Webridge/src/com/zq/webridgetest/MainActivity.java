package com.zq.webridgetest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.zq.webridgetest.util.WBWebView;

public class MainActivity extends Activity {
	private WBWebView wv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void init() {
		wv = (WBWebView) findViewById(R.id.wv);

		String url = "file:///android_asset/index.html";
		wv.loadUrl(url);

		findViewById(R.id.nativeToJsIncludeReturn).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						String js = "javascript:wbNativeToHTML(\"wbNativeToHTMLIncludeReturn\",\"\")";
						wv.loadUrl(js);
					}
				});

		findViewById(R.id.jsToNativeIncludeReturn).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						String method = "'queryPerson'";
						String parma = "{\"name\":\"zq\"}";
						String callback = "'wbCallback'";
						String js = "javascript:wbHTMLToNative(" + method + ","
								+ parma + "," + callback + ")";
						wv.loadUrl(js);
					}
				});
	}
}
