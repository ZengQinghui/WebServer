package com.briup.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.briup.http.HttpRequest;
import com.briup.utils.ConfigUtil;
import com.briup.utils.ServletMappingUtil;

public class HttpRequestImpl implements HttpRequest {
	private String protocol;
	private String requestMethod;
	private String requestPath;
	private Map<String, String> requestHeader;
	private Map<String, String> parameters;
	private Socket client;
	private BufferedReader br;
	private boolean isNullRequest;

	public HttpRequestImpl(Socket client) {
		this.client = client;
		requestHeader = new HashMap<String, String>();
		parameters = new HashMap<String, String>();
		getInfo();
	}

	private void getInfo() {
		br = null;
		try {
			br = new BufferedReader(new InputStreamReader(client.getInputStream()));
			String requestLine = br.readLine();

			// 处理浏览器发送空请求的情况
			if (requestLine == null) {
				this.isNullRequest = true;
				return;
			}

			parseRequestLine(requestLine);

			String rh = null;
			while (!"".equals(rh = br.readLine())) {
				parseRequestHeader(rh);
			}

			if (br.ready()) {
				char[] buf = new char[1024];
				int len = br.read(buf);
				parseParameterByPost(new String(buf, 0, len));
			} else {
				parseParameterByGet(requestPath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 解析请求行字符串
	private void parseRequestLine(String requestLine) throws Exception {
		if (requestLine == null)
			return;
		String[] str = requestLine.split(" ");
		if (str.length != 3) {
			throw new Exception("请求行的格式不符合http协议");
		}
		this.requestMethod = str[0];
		this.requestPath = str[1];
		this.protocol = str[2];
	}

	// 解析请求报头字符串
	private void parseRequestHeader(String rh) throws Exception {
		String[] str = rh.split(": ");
		if (str.length != 2) {
			throw new Exception("request");
		}
		requestHeader.put(str[0], str[1]);
	}

	// 解析post方法的参数字符串
	private void parseParameterByPost(String strParameter) {
		String[] str = strParameter.split("&");
		for (String kv : str) {
			String[] kvStr = kv.split("=");
			String parameterName = kvStr[0];
			String parameterValue = kvStr[1];
			parameters.put(parameterName, parameterValue);
		}
	}

	// 解析get方法的参数字符串
	private void parseParameterByGet(String requestPath) {
		// /test.html?id=2&name=tom
		String[] str = requestPath.split("[?]");
		if (str.length <= 1) {
			return;
		}
		parseParameterByPost(str[1]);
		this.requestPath = str[0];

	}

	@Override
	// 获得请求的协议
	public String getProtocol() {
		return this.protocol;
	}

	@Override
	// 获得请求的方法
	public String getRequestMethod() {
		return this.requestMethod;
	}

	@Override
	// 获得请求的路径
	public String getRequestPath() {
		return this.requestPath;
	}

	@Override
	// 获得请求的消息报头
	public Map<String, String> getRequestHeader() {
		return this.requestHeader;
	}

	@Override
	// 根据参数的名字获得请求带过来的参数值
	public String getParameter(String parameterName) {
		return parameters.get(parameterName);
	}

	@Override
	// 判断当前请求的否是静态资源
	public boolean isStaticResource() {
		File file = new File(ConfigUtil.getConfigValue("rootPath"), requestPath);
		return file.exists();
	}

	@Override
	// 判断当前请求的否是动态资源
	public boolean isDynamicResource() {
		return ServletMappingUtil.isContainKey(requestPath);
	}

	@Override
	// 判断当前请求的否是为空请求(有些浏览器会自动发送空请求)
	public boolean isNullRequest() {
		return this.isNullRequest;
	}

}
