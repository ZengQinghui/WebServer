package com.briup.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class StatusCodeUtil {
	private static Properties p;

	static {
		p = new Properties();
		InputStream in = null;
		try {
			in = StatusCodeUtil.class.getResourceAsStream("status_code.properties");
			p.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String getStatusMsg(String statusCode) {
		return p.getProperty(statusCode);
	}

	public static String getStatusMsg(int statusCode) {
		return getStatusMsg(statusCode + "");
	}
}
