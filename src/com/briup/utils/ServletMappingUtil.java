package com.briup.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ServletMappingUtil {
	private static Properties p;

	static {
		p = new Properties();
		InputStream in = null;
		try {
			in = ServletMappingUtil.class.getResourceAsStream("servlet_mapping.properties");
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

	public static boolean isContainKey(String servletPath) {
		return p.containsKey(servletPath);
	}

	public static String getServletClass(String servletPath) {
		return p.getProperty(servletPath);
	}
}
