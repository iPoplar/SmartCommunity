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
			//�򱾻��Ķ˿ڷ����ͻ��˵�����
			Socket socket = new Socket("127.0.0.1", 54199);
			//��ϵͳ��׼���빹��BufferedReader����
			BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));
			//�õ��������������PrintWriter����
			PrintWriter os = new PrintWriter(socket.getOutputStream());
			
			String readline;
			//��ϵͳ��׼�������һ�ַ���
			readline = sin.readLine();
			//��������ַ���Ϊ��bye������ֹͣѭ��
			while(!readline.equals("bye"))
			{
				//��������ַ������뵽server
				os.println(readline);
				//ˢ���������ʹServer�����յ����ַ���
				os.flush();
				readline = sin.readLine();
			}
			//�ر�Socket�����
			os.close();
			//�ر�Socket
			socket.close();
		
		} catch (Exception e) {
			System.out.println("Error" + e);
		}
		
	}
}
