

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.JTextField;
import javax.swing.Timer;

public class ReadThread extends Thread 
{
	Socket socket = null;
	String client;
	
	public ReadThread(Socket socket, String client)
	{
		this.socket = socket;
		this.client = client;		
	}
	public ReadThread(Socket socket, String side, int clientnum)
	{
		this(socket, side);
	}
	
	/*************************************************************
	* Function Name: 	connection
	* Purpose: 			连接数据库
	**************************************************************/
		private static Statement statement ;
		public static void connection()
		{
			Connection conn;		
			final String ojdbcDRIVER = "oracle.jdbc.driver.OracleDriver";
			final String ojdbcURL =  "jdbc:oracle:thin:@localhost:1521:orcl";
			final String user = "scott";	
			final String pass = "tiger";
			try 
			{
				Class c = Class.forName(ojdbcDRIVER);
						
			} catch (ClassNotFoundException e) 
			{
				System.out.println("驱动不存在");
			}
			try {
				conn = DriverManager.getConnection(ojdbcURL, user, pass);
				statement = conn.createStatement();
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			
		}
		
		
		/*************************************************************
		* Function Name: 	addData
		* Purpose: 			向数据库添加数据
		**************************************************************/
		public static void addData(String witAddress, String witDay)
		{
			
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
			String witDate = df.format(new Date());// new Date()为获取当前系统时间
			String SQLString ="insert into sys_wit_notes (witaddress,witday,witdate) values('"+ witAddress +"','"+witDay+"','"+witDate+"')";
			connection();
			
			try
			{
				statement.executeUpdate(SQLString);
			} catch (SQLException e) 
			{
				e.printStackTrace();
			}
			try 
			{
				statement.close();
			} catch (Exception e) 
			{
				System.out.println(" 下拉状态列表初始化出错！");
			}
		}
		
		 private final static char[] mChars = "0123456789ABCDEF".toCharArray();  
		 
		    public static String byte2HexStr(byte[] b, int iLen){  
		        StringBuilder sb = new StringBuilder();  
		        for (int n=0; n<iLen; n++){  
		            sb.append(mChars[(b[n] & 0xFF) >> 4]);  
		            sb.append(mChars[b[n] & 0x0F]);  
		            sb.append(' ');  
		        }  
		        return sb.toString().trim().toUpperCase(Locale.US);  
		    }  
		
	public void run()
	{
		String line = "";
		try 
		{
			String ip = socket.getInetAddress().getHostAddress();
			InputStream in = socket.getInputStream();
			//如果该字符串为“bye”，则停止循环
			while(line != null && !line.equals("bye"))
			{
				byte[] buf = new byte[1024];
				int len = in.read(buf); 
				String s1 = byte2HexStr(buf,len);
				String s = new String();
				//String s1 = new String();
				String s2 = new String();
				String str = new String();
				try
				{	   
					InputStream input = new FileInputStream(new File("F:/智慧社区的临时文件测试\\notes.txt"));
				  
				   str = s1.replaceAll("\\s*", "");//除去所有空格及空位	
				   String ads = str.substring(0, 0+2);//地址
				   String daa = str.substring(4, 4+18);//当天数据
				  
				   //向数据库添加数据
				   addData(ads, daa);
				   
				  input.close();
				  
				 } catch (Exception e) 
				 {
				   System.out.println("出错了");		  
				 }
			}
			
			//关闭Socket
			socket.close();
			//关闭ServerSocket
			
			
		} catch (IOException e) 
		{
			System.out.println("Error:" + e);
		}
	}
	
}
