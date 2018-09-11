package com.example.myproject.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import com.example.myproject.constant.ServerConstant;

public class chatServer {
	public static ConcurrentHashMap<String, Socket> socketMap= new ConcurrentHashMap<String, Socket>();//线程安全（ip，socket）
	public static int onlineCount = 0;

	public static void startServer() {
		ServerSocket serverSocket=null;
		Socket socket = null; 
		try {
			serverSocket = new ServerSocket(ServerConstant.port);
			System.out.println("***服务器即将启动，等待客户端的链接***"); 
			while (true){ 
				//调用accept()方法开始监听，等待客户端的链接 
				socket = serverSocket.accept(); 
				String ip=socket.getInetAddress().getHostAddress();
				socketMap.put(ip, socket);
				//创建一个新的线程 
				Thread thread=new Thread(new ServerThread(socket,socketMap));
				//启动线程 
				thread.start(); 
				onlineCount=socketMap.size(); //统计客户端的数量 
				System.out.println("客户端的数量: " + onlineCount); 
			} 
		} catch (Exception e) {
			e.printStackTrace();
			try {
				serverSocket.close();
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} 
	}
	public static void main(String[] args) {
		startServer();
	}

}
