package com.briup.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ErrorPageUtil {
	private static Properties p;

	static {
		p = new Properties();
		InputStream in = null;
		try {
			in = ErrorPageUtil.class.getResourceAsStream("error_page.properties");
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

	public static String getErrorPagePath(String errorCode) {
		return p.getProperty(errorCode);
	}

	public static String getErrorPagePath(int errorCode) {
		return p.getProperty(errorCode + "");
	}
}
