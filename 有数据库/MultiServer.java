

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * ��ͻ��˺ͷ�������Socketͨ��
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
			//��Զѭ��
			while(listening)
			{	
				//�ȴ��ͻ�����
				Socket socket = server.accept();
				count++;
				System.out.println("Accept " + count + "Client!");
				//�����ͻ��˴���Socket�߳�
				
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
