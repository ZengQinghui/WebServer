package com.briup.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MIMEUtil {
	private static Properties p;

	static {
		p = new Properties();
		InputStream in = null;
		try {
			in = MIMEUtil.class.getResourceAsStream("mime.properties");
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

	public static String getContentType(String contentType) {
		return p.getProperty(contentType);
	}
}
