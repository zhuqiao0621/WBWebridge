package com.zq.webridge.util;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 根据js发送的command注册的方法
 * 
 * @author user
 *
 */
public class WBWebridgeImplement implements WBWebridgeListener {
	public static Map<String, String> map = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("zq", test(true));
			put("jane", test(false));
		}
	};

	public String queryPerson(String parma) {
		try {
			JSONObject parmaObj = new JSONObject(parma);
			String name = parmaObj.optString("name");
			if (map.containsKey(name)) {
				return map.get(name).toString();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	private static String test(boolean zq) {
		JSONObject object = new JSONObject();
		try {
			object.put("name", zq ? "zq" : "jane");
			object.put("year", zq ? 27 : 21);
			object.put("gender", "female");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object.toString();
	}
}
