package com.briup.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	private ServerSocket server;
	// 服务器监听端口
	private int port;
	// 控制服务器停止运行
	private boolean flag;

	public void startServer() {

		try {
			System.out.println("服务器启动,监听端口" + port + ",等待客户端浏览器的访问");
			System.out.println("*************************************************");
			server = new ServerSocket(port);

			while (flag) {
				Socket client = server.accept();
				System.out.println(client);
				Thread t = new ServiceThread(client);
				t.start();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (server != null)
					server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void stopServer() {
		this.flag = false;
	}

	public Server() {
		this.port = 8989;
		this.flag = true;
	}

	public static void main(String[] args) {
		Server s = new Server();
		s.startServer();
	}

}
