package com.briup.impl;

import java.net.Socket;

import com.briup.http.HttpAccessProcessor;
import com.briup.http.HttpCreator;
import com.briup.http.HttpRequest;
import com.briup.http.HttpResponse;

public class HttpCreatorImpl implements HttpCreator {
	@SuppressWarnings("unused")
	private Socket client;
	private HttpRequest request;
	private HttpResponse response;

	public HttpCreatorImpl(Socket client) {
		this.client = client;
		this.request = new HttpRequestImpl(client);
		this.response = new HttpResponseImpl(client, this.request);
	}

	@Override
	// 返回创建好的request对象
	public HttpRequest getHttpRequest() {
		return this.request;
	}

	@Override
	// 返回创建好的response对象
	public HttpResponse getHttpResponse() {
		return this.response;
	}

	@Override
	// 返回创建好的HttpAccessProcessor对象
	public HttpAccessProcessor getHttpAccessProcessor() {
		return new HttpAccessProcessorImpl();
	}

}
