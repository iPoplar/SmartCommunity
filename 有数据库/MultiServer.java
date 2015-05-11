

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 多客户端和服务器的Socket通信
 */
public class MultiServer
{
	public static void main(String[] args)
	{
		ServerSocket server = null;
		int count = 0;
		boolean listening = true;
		try
		{
			server = new ServerSocket(61122);
			System.out.println("Server starting ....");
			//永远循环
			while(listening)
			{	
				//等待客户请求
				Socket socket = server.accept();
				count++;
				System.out.println("Accept " + count + "Client!");
				//启动客户端处理Socket线程
				
				Thread read = new ReadThread(socket,"[client"+count+"]");
				
				read.start(); 				
			}
			server.close();
			
		} catch (IOException e)
		{
			System.out.println("Can not listen to:" + e);
		}
	}
}
