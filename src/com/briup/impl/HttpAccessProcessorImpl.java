package com.briup.impl;

import com.briup.http.HttpAccessProcessor;
import com.briup.http.HttpRequest;
import com.briup.http.HttpResponse;
import com.briup.http.Servlet;
import com.briup.utils.ErrorPageUtil;
import com.briup.utils.MIMEUtil;
import com.briup.utils.ServletMappingUtil;

public class HttpAccessProcessorImpl implements HttpAccessProcessor {

	@Override
	// 处理静态资源 页面/文件/图片等等
	public void processStaticResource(HttpRequest request, HttpResponse response) {
		String[] str = request.getRequestPath().split("[.]");

		response.setStatusLine(200);

		response.setContentType(MIMEUtil.getContentType(str[str.length - 1]));

		response.setCRLF();

		response.printResponseHeader();

		response.printResponseContent(request.getRequestPath());
	}

	@Override
	// 处理动态资源 java代码 浏览器通过路径发送请求可以访问调用java代码
	public void processDynamicResource(HttpRequest request, HttpResponse response) {
		String className = ServletMappingUtil.getServletClass(request.getRequestPath());

		response.setStatusLine(200);

		response.setContentType(MIMEUtil.getContentType("html"));

		response.setCRLF();

		response.printResponseHeader();

		try {
			Object o = Class.forName(className).newInstance();
			if (o instanceof Servlet) {
				((Servlet) o).service(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	// 向浏览器返回错误信息及其页面
	public void sendError(int statusCode, HttpRequest request, HttpResponse response) {
		response.setStatusLine(statusCode);

		response.setContentType(MIMEUtil.getContentType("html"), "UTF-8");

		response.setCRLF();

		response.printResponseHeader();

		response.printResponseContent(ErrorPageUtil.getErrorPagePath(statusCode));
	}

}
