package com.example.myproject.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class ServerThread implements Runnable{
	private ConcurrentHashMap<String, Socket> socketMap= new ConcurrentHashMap<String, Socket>();//线程安全（ip，socket）
	private Socket socket;
	private DataInputStream in;//数据输入流

	public ServerThread(Socket soc,ConcurrentHashMap<String, Socket> socketMap) throws Exception {
		super();
		this.socket = soc;
		this.in=new DataInputStream(socket.getInputStream());
		this.socketMap=socketMap;
	}

	@Override
	public void run() {
		String message=null;//客户端发过来的消息,格式ip&+message
		while(true) {
			try {
				message=in.readUTF();
				String ip=message.split("&")[0];//要交谈的ip值
				Socket ipSocket=socketMap.get(ip);//获取交谈对象对应的socket;
				DataOutputStream dos=new DataOutputStream(ipSocket.getOutputStream());//获取交谈对象对应的输出流;
				dos.writeUTF(socket.getInetAddress().getHostAddress()+"对你说:"+message);//发送个对应的ip
				dos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
