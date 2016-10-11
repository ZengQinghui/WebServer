package com.briup.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import com.briup.http.HttpRequest;
import com.briup.http.HttpResponse;
import com.briup.utils.ConfigUtil;
import com.briup.utils.MIMEUtil;
import com.briup.utils.StatusCodeUtil;

public class HttpResponseImpl implements HttpResponse {
	private Socket client;
	private OutputStream out;
	private PrintWriter pw;
	private StringBuffer sBuffer;
	private HttpRequest request;

	public HttpResponseImpl(Socket client, HttpRequest request) {
		try {
			this.request = request;
			this.client = client;
			out = client.getOutputStream();
			pw = new PrintWriter(new OutputStreamWriter(out));
			sBuffer = new StringBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	// 获得一个指向客户端的字节流
	public OutputStream getOutputStream() throws Exception {
		return this.out;
	}

	@Override
	// 获得一个指向客户端的字符流
	public PrintWriter getPrintWriter() throws Exception {
		return this.pw;
	}

	@Override
	// 设置响应状态行 参数为String类型
	public void setStatusLine(String statusCode) {
		sBuffer.append("HTTP/1.1 " + statusCode + " " + StatusCodeUtil.getStatusMsg(statusCode));
		setCRLF();
	}

	@Override
	// 设置响应状态行 参数为int类型
	public void setStatusLine(int statusCode) {
		setStatusLine(statusCode + "");
	}

	@Override
	// 设置响应信息报头
	public void setResponseHeader(String hName, String hValue) {
		sBuffer.append(hName + ": " + hValue);
		setCRLF();
	}

	@Override
	// 设置响应消息报头中Content-Type属性
	public void setContentType(String contentType) {
		setResponseHeader("Content-Type", contentType);
	}

	@Override
	// 设置响应消息报头中Content-Type属性 并且同时设置编码
	public void setContentType(String contentType, String charsetName) {
		setResponseHeader("Content-Type", contentType + ";charset=" + charsetName);
	}

	@Override
	// 设置CRLF 回车换行 \r\n
	public void setCRLF() {
		sBuffer.append("\r\n");
	}

	@Override
	// 把设置好的响应状态行、响应消息报头、固定空行这三部分写给浏览器
	public void printResponseHeader() {
		String sb = sBuffer.toString();
		pw.print(sb);
		pw.flush();

	}

	@Override
	// 把响应正文写给浏览器
	public void printResponseContent(String requestPath) {
		FileInputStream fis = null;
		try {
			File file = new File(ConfigUtil.getConfigValue("rootPath"), requestPath);
			fis = new FileInputStream(file);

			byte[] buf = new byte[1024];
			int len = -1;
			while ((len = fis.read(buf)) != -1) {
				out.write(buf, 0, len);
			}
			out.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
