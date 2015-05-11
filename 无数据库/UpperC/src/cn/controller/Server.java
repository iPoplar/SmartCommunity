package cn.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server 
{
	public static void tcp_txt(String txt)
	{
		String s = new String();
		String s1 = new String();
		try
		{	   
		   BufferedReader input = new BufferedReader(new FileReader("F:/智慧社区的临时文件测试\\智能电表数据采集.txt"));
		   
		   while((s = input.readLine())!=null)
		   {
			   s1 += (s+"\r\n");
		   }
		  
		   input.close();		   
		   s1 += txt;
		   
		BufferedWriter output = new BufferedWriter(new FileWriter("F:/智慧社区的临时文件测试\\智能电表数据采集.txt"));
		output.write(s1+"\r\n");
		output.close();
		 } catch (Exception e) 
		 {
		   System.out.println("tcp_txt(String txt)出错了");		  
		 }
	}
	
	public static void main(String[] args) 
	{
		try 
		{
			PrintWriter pw = new PrintWriter( new FileWriter( "智能电表数据采集.txt" ) );
			pw.close();
			
			ServerSocket server = null;
			try
			{
				//在端口监听客户请求
				server = new ServerSocket(54199);
			} catch (Exception e)
			{
				System.out.println("Can not listen to :" + e);
			}
			
			Socket socket = null;
			try 
			{
				//使用accept()阻塞等待客户请求
				socket = server.accept();
			} catch (Exception e)
			{
				System.out.println("Error" + e);
			}
		
			for(int i = 1; i == 1;)
			{	
				String ip = socket.getInetAddress().getHostAddress();
				
				InputStream in = socket.getInputStream();
				byte[] buf = new byte[1024];
				int len = in.read(buf);
				String text = new String(buf, 0, len);
				tcp_txt(text);

				System.out.println(text+"@text");			
				
				//判断结束
				Boolean result=text.equals("bye");
				if(result)
				{
					System.out.println(ip + "通讯结束！");
					socket.close();
				}
				else
				{
				//发送数据
					OutputStream out = socket.getOutputStream();
					out.write("receive\n".getBytes());
				}
			}
			
			String line = "";
			
			//通过Socket对象的输入流构造BufferedReader对象			
			BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			//如果该字符串为“bye”，则停止循环
			while(line!=null && !line.equals("bye"))
			{
				//再一次读入一字符串
				line = is.readLine();
				//输出读入的字符串
				System.out.println("Server:" + line);
			}
			//关闭socket输入流
			is.close();
			//关闭socket
			socket.close();
			//关闭ServerSocket
			server.close();
		} catch (Exception e) 
		{
			System.out.println("Error" + e);
		}
		
	}

}
