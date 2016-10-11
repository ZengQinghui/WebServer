package com.briup.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigUtil {
	private static Properties p;

	static {
		p = new Properties();
		InputStream in = null;
		try {
			in = ConfigUtil.class.getResourceAsStream("config.properties");
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

	public static String getConfigValue(String configName) {
		return p.getProperty(configName);
	}
}
