package com.zq.webridgetest.util;

import java.lang.reflect.Method;

public class InvokeMethod {

	/**
	 * 执行cls方法
	 * 
	 * @param methodName
	 *            方法名
	 * @param args
	 *            参数
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object invokeMethod(Object owner, String methodName,
			Object[] args) throws Exception {
		Class cls = owner.getClass();
		Class[] argclass;
		if (args == null || args.length == 0) {
			argclass = null;
		} else {
			argclass = new Class[args.length];
			for (int i = 0, j = argclass.length; i < j; i++) {
				argclass[i] = args[i].getClass();
			}
		}
		Method method = cls.getMethod(methodName, argclass);
		return method.invoke(owner, args);
	}
}
