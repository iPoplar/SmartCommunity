package cn.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client 
{
	
	public static void main(String[] args) 
	{		
		try 
		{
			//向本机的端口发出客户端的请求
			Socket socket = new Socket("127.0.0.1", 54199);
			//由系统标准输入构造BufferedReader对象
			BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));
			//得到输出流，并构造PrintWriter对象
			PrintWriter os = new PrintWriter(socket.getOutputStream());
			
			String readline;
			//从系统标准输入读入一字符串
			readline = sin.readLine();
			//若读入的字符串为“bye”，则停止循环
			while(!readline.equals("bye"))
			{
				//将读入的字符串输入到server
				os.println(readline);
				//刷新输出流，使Server马上收到该字符串
				os.flush();
				readline = sin.readLine();
			}
			//关闭Socket输出流
			os.close();
			//关闭Socket
			socket.close();
		
		} catch (Exception e) {
			System.out.println("Error" + e);
		}
		
	}
}
