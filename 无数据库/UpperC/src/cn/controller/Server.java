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
		   BufferedReader input = new BufferedReader(new FileReader("F:/�ǻ���������ʱ�ļ�����\\���ܵ�����ݲɼ�.txt"));
		   
		   while((s = input.readLine())!=null)
		   {
			   s1 += (s+"\r\n");
		   }
		  
		   input.close();		   
		   s1 += txt;
		   
		BufferedWriter output = new BufferedWriter(new FileWriter("F:/�ǻ���������ʱ�ļ�����\\���ܵ�����ݲɼ�.txt"));
		output.write(s1+"\r\n");
		output.close();
		 } catch (Exception e) 
		 {
		   System.out.println("tcp_txt(String txt)������");		  
		 }
	}
	
	public static void main(String[] args) 
	{
		try 
		{
			PrintWriter pw = new PrintWriter( new FileWriter( "���ܵ�����ݲɼ�.txt" ) );
			pw.close();
			
			ServerSocket server = null;
			try
			{
				//�ڶ˿ڼ����ͻ�����
				server = new ServerSocket(54199);
			} catch (Exception e)
			{
				System.out.println("Can not listen to :" + e);
			}
			
			Socket socket = null;
			try 
			{
				//ʹ��accept()�����ȴ��ͻ�����
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
				
				//�жϽ���
				Boolean result=text.equals("bye");
				if(result)
				{
					System.out.println(ip + "ͨѶ������");
					socket.close();
				}
				else
				{
				//��������
					OutputStream out = socket.getOutputStream();
					out.write("receive\n".getBytes());
				}
			}
			
			String line = "";
			
			//ͨ��Socket���������������BufferedReader����			
			BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			//������ַ���Ϊ��bye������ֹͣѭ��
			while(line!=null && !line.equals("bye"))
			{
				//��һ�ζ���һ�ַ���
				line = is.readLine();
				//���������ַ���
				System.out.println("Server:" + line);
			}
			//�ر�socket������
			is.close();
			//�ر�socket
			socket.close();
			//�ر�ServerSocket
			server.close();
		} catch (Exception e) 
		{
			System.out.println("Error" + e);
		}
		
	}

}
