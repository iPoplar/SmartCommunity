import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.Timer;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class UpperC 
{
	private JFrame jfrmMain;
	private Container con;
	
	private JLabel jlblTopic;/*标题*/
	private JLabel jlblTip;/*安防提示信息*/
	
	private JLabel jlblUserInfo;/*选择的用户信息*/
	private JLabel jlblTime;/*当前时间*/
	
	private JButton jbtnExit;/*退出按钮*/
	private JButton jbtbPrint;/*打印报表*/
	
	private JButton jbtnHint;/*余额不足提示*/
	private JButton jbtnPay;/*缴费详情*/
	private JButton jbtnBreak;/*停用操作*/
	private JButton jbtnInterrupt;/*中断提示*/
	
	private JButton jbtnStealed;/*盗窃报警*/
	private JButton jbtnFire;/*火灾报警*/
	private JButton jbtnSmoke;/*烟雾报警*/
	private JButton jbtnFlood;/*水寝报警*/
	
	private JScrollPane jspaneTree;/*社区树状图的格式工厂*/
	private JScrollPane jspaneEnergy;/*能源信息的格式工厂*/
	private JScrollPane jspaneSecurity;/*安防信息的格式工厂*/
	private JScrollPane jspaneEnergyBt;/*控制能源按钮的格式工厂*/
	private JScrollPane jspaneSecurityBt;/*控制安防按钮的格式工厂*/
	
	private static DefaultListModel dlmEnergyList;/*能源信息列表框*/
	private static JList jlstEnergyList;
	private static DefaultListModel dlmSecurityList;/*安防信息列表框*/
	private static JList jlstSecurityList;
	private static volatile boolean stop=false;
	
	//TODO 中转list，隐藏中转
	private JList jlstTrains;	
	private static DefaultListModel dlmTrains;
	private JScrollPane jspaneTrains;
	
	private JRadioButton[] jrdbStatus;/*设置单选按钮*/
	private ButtonGroup bgStatus;/*设置按钮群*/
	
	JTree simpletree;
	private DefaultMutableTreeNode tempnode,tempnode2, root;
	private TreeSelectionEvent e ;
	
	private Image image;
	private JLabel background = new JLabel(new ImageIcon(getClass().getResource("temporary.jpg")));
	
	
	/*************************************************************
	* Function Name: 	UpperC
	* Purpose: 			构造函数
	**************************************************************/
	private  UpperC()
	{
		initFrame();
		reinitFrame();	
		dealAction();		
	}
	
	/*************************************************************
	* Function Name: 	reinitFrame
	* Purpose: 			界面的再次初始化
	**************************************************************/
	public void reinitFrame()
	{
		demojtree();
	}
	
	/*************************************************************
	* Function Name: 	initFrame
	* Purpose: 			界面的初始化
	**************************************************************/
	public void initFrame()
	{
		Dimension dim;
		Font normalFont;
		Font boldFont;
		normalFont = new Font("宋体",Font.PLAIN,17);
		boldFont = new Font("宋体",Font.BOLD,17);
		
		jfrmMain = new JFrame("智慧社区管理系统");
		con = jfrmMain.getContentPane();
		con.setLayout(null);
		
		jfrmMain.setSize(13560/15, 9930/15);
		dim = Toolkit.getDefaultToolkit().getScreenSize();
		jfrmMain.setLocation(dim.width-jfrmMain.getWidth(),dim.height-jfrmMain.getHeight());
		jfrmMain.setVisible(true);
		jfrmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
		/******************************************/
		/**			                 添加背景                                   */
		/******************************************/		
		JPanel imagePanel;
		ImageIcon background = new ImageIcon(jfrmMain.getClass().getResource("/img/ee.jpg"));
		JLabel label = new JLabel(background);// 把背景图片显示在一个标签里面
		
		label.setBounds(0, 0, background.getIconWidth(),background.getIconHeight());
		
		//**把内容窗格转化为JPanel，否则不能用方法setOpaque()来使内容窗格透明*//
		imagePanel = (JPanel) jfrmMain.getContentPane();
		imagePanel.setOpaque(false);
		jfrmMain.getLayeredPane().setLayout(null);
		
		// 把背景图片添加到分层窗格的最底层作为背景
		jfrmMain.getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));
		/******************************************/
		String stricon = "/img/icon2.jpg";
		try
		{
			Image image = ImageIO.read(jfrmMain.getClass().getResource(stricon));			
			jfrmMain.setIconImage(image);
		} catch (IOException e) 
		{
			e.printStackTrace();
		}		
		
		jlblTopic = new JLabel("智慧社区管理系统");
		jlblTopic.setFont(new Font("微软雅黑",Font.PLAIN,27));
		jlblTopic.setForeground(Color.BLUE);
		jlblTopic.setBounds(4800/15,0,3480/15,570/15);
		con.add(jlblTopic);
		
		jlblTip = new JLabel();
		jlblTip.setFont(normalFont);
		jlblTip.setBounds(8400/15, 6120/15, 4455/15, 375/15);
		con.add(jlblTip);
		
		jlblUserInfo = new JLabel("住户信息");
		jlblUserInfo.setFont(boldFont);
		jlblUserInfo.setBounds(3120/15, 960/15, 4695/15, 375/15);
		con.add(jlblUserInfo);
		
		jlblTime = new JLabel();
		jlblTime.setFont(normalFont);
		jlblTime.setBounds(240/15, 8520/15, 2415/15+10, 555/15);
		con.add(jlblTime);	
		TimeThread date = new TimeThread(5);
		
		jbtnExit = new JButton("退出");
		jbtnExit.setFont(normalFont);
		jbtnExit.setBounds(11760/15, 8640/15, 1335/15, 615/15);
		con.add(jbtnExit);
		
		jbtnPay = new JButton("打印报表");
		jbtnPay.setFont(normalFont);
		jbtnPay.setBounds(3000/15, 8640/15, 1335/15+23, 615/15);
		con.add(jbtnPay);

		jbtnHint = new JButton("余额不足");
		jbtnHint.setFont(normalFont);
		jbtnHint.setBounds(3480/15, 6720/15, 1815/15, 615/15);
		con.add(jbtnHint);
		
		jbtnPay = new JButton("缴费详情");
		jbtnPay.setFont(normalFont);
		jbtnPay.setBounds(5640/15, 6720/15, 1815/15, 615/15);
		con.add(jbtnPay);
		
		jbtnInterrupt = new JButton("中断提示");
		jbtnInterrupt.setFont(normalFont);
		jbtnInterrupt.setBounds(3480/15, 7560/15, 1815/15, 615/15);
		con.add(jbtnInterrupt);
		
		jbtnBreak = new JButton("停用操作");
		jbtnBreak.setFont(normalFont);
		jbtnBreak.setBounds(5640/15, 7560/15, 1815/15, 615/15);
		con.add(jbtnBreak);
		
		jbtnStealed = new JButton("盗窃报警");
		jbtnStealed.setFont(normalFont);
		jbtnStealed.setBounds(8640/15, 6720/15, 1815/15, 615/15);
		con.add(jbtnStealed);
		
		jbtnFire = new JButton("火灾报警");
		jbtnFire.setFont(normalFont);
		jbtnFire.setBounds(10800/15, 6720/15, 1815/15, 615/15);
		con.add(jbtnFire);
		
		jbtnSmoke = new JButton("烟雾报警");
		jbtnSmoke.setFont(normalFont);
		jbtnSmoke.setBounds(8640/15, 7560/15, 1815/15, 615/15);
		con.add(jbtnSmoke);
		
		jbtnFlood = new JButton("水侵报警");
		jbtnFlood.setFont(normalFont);
		jbtnFlood.setBounds(10800/15, 7560/15, 1815/15, 615/15);
		con.add(jbtnFlood);
		
		bgStatus = new ButtonGroup();
		jrdbStatus = new JRadioButton[3];
		
		jrdbStatus[0] = new JRadioButton("电费");		
		jrdbStatus[0].setFont(normalFont);
		jrdbStatus[0].setBounds(3480/15, 6120/15, 1215/15, 375/15);
		bgStatus.add(jrdbStatus[0]);
		con.add(jrdbStatus[0]);
		jrdbStatus[0].setSelected(true);
		
		jrdbStatus[1] = new JRadioButton("水费");
		jrdbStatus[1].setFont(normalFont);
		jrdbStatus[1].setBounds(4800/15, 6120/15, 1215/15, 375/15);
		bgStatus.add(jrdbStatus[1]);
		con.add(jrdbStatus[1]);

		jrdbStatus[2] = new JRadioButton("燃气表");
		jrdbStatus[2].setFont(normalFont);
		jrdbStatus[2].setBounds(6120/15, 6120/15, 1335/15, 375/15);
		bgStatus.add(jrdbStatus[2]);
		con.add(jrdbStatus[2]);
		
		dlmEnergyList = new DefaultListModel();
		jlstEnergyList = new JList(dlmEnergyList);
		jspaneEnergy = new JScrollPane(jlstEnergyList);		
		jspaneEnergy.setBounds(3000/15, 1320/15, 4935/15, 4455/15);
		jspaneEnergy.setBorder(BorderFactory.createTitledBorder("能源信息"));
		con.add(jspaneEnergy);
		
		dlmSecurityList = new DefaultListModel();
		jlstSecurityList = new JList(dlmSecurityList);
		jspaneSecurity = new JScrollPane(jlstSecurityList);		
		jspaneSecurity.setBounds(8160/15, 1320/15, 4935/15,4455/15);
		jspaneSecurity.setBorder(BorderFactory.createTitledBorder("安防信息"));
		con.add(jspaneSecurity);		
		
		//TODO 这是个中转的list，也是一个隐身的中转器
		dlmTrains = new DefaultListModel();						
		jlstTrains = new JList(dlmTrains);				
		jlstTrains.setFont(normalFont);				
		jspaneTrains = new JScrollPane(jlstTrains);	
		jspaneTrains.setBounds(120/15, 120/15, 3135/15, 855/15);
		//con.add(jspaneTrains);
		
		jspaneEnergyBt = new JScrollPane();
		jspaneEnergyBt.setBounds(3000/15, 5760/15, 4935/15, 2655/15);
		jspaneEnergyBt.setBorder(BorderFactory.createTitledBorder("能源控制"));
		con.add(jspaneEnergyBt);
		
		jspaneSecurityBt = new JScrollPane();
		jspaneSecurityBt.setBounds(8160/15, 5760/15, 4935/15, 2655/15);
		jspaneSecurityBt.setBorder(BorderFactory.createTitledBorder("安防控制"));
		con.add(jspaneSecurityBt);

	}

	/*************************************************************
	* Function Name: 	demojtree
	* Purpose: 			树的菜单初始化
	**************************************************************/
	public void demojtree()
	{
		//创造节点
		root = new DefaultMutableTreeNode("智慧社区");
		//创建第一个节点
		tempnode = new DefaultMutableTreeNode("01楼");
		tempnode2 = new DefaultMutableTreeNode("02楼");
		//添加节点
		root.add(tempnode);
		root.add(tempnode2);
		//添加第一个节点的子节点
		tempnode.add(new DefaultMutableTreeNode("1001住户"));
		tempnode.add(new DefaultMutableTreeNode("1002住户"));
		tempnode.add(new DefaultMutableTreeNode("1003住户"));
		tempnode.add(new DefaultMutableTreeNode("1004住户"));
		tempnode.add(new DefaultMutableTreeNode("1005住户"));
		tempnode2.add(new DefaultMutableTreeNode("2001住户"));
		tempnode2.add(new DefaultMutableTreeNode("2002住户"));
		tempnode2.add(new DefaultMutableTreeNode("2003住户"));
		tempnode2.add(new DefaultMutableTreeNode("2004住户"));
		tempnode2.add(new DefaultMutableTreeNode("2005住户"));
		//创建树， 以root为根
		simpletree = new JTree(root);
		
		jspaneTree = new JScrollPane(simpletree);
		jspaneTree.setBounds(240/15, 960/15, 2535/15, 7455/15);
		jspaneTree.setBorder(BorderFactory.createTitledBorder("社区列表"));
		con.add(jspaneTree);
	}
	
	/*************************************************************
	* Function Name: 	initSecurityList
	* Purpose: 			安防的列表数据初始化
	**************************************************************/
	public static void initSecurityList(String temp, String hum ,String Element1,String Element2,String Element3,String Element4)
	{
	
		jlstSecurityList.setCellRenderer(new listView());
		
		listIcon item1 = new listIcon("/img/pic3.png","火灾报警: "+Element1);	
		listIcon item2 = new listIcon("/img/smook.png","烟雾报警: "+Element2);	
		listIcon item3 = new listIcon("/img/forFlood.png","水侵报警: "+Element3);	
		listIcon item4 = new listIcon("/img/stealTip.png","盗窃报警: "+Element4);	
		listIcon item5 = new listIcon("/img/temp.png","温度提示: "+temp+"℃");	
		listIcon item6 = new listIcon("/img/shidu.png","湿度提示: "+hum+"%");	

		dlmSecurityList.addElement(item5);
		dlmSecurityList.addElement(item6);
		dlmSecurityList.addElement(item1);
		dlmSecurityList.addElement(item2);
		dlmSecurityList.addElement(item3);
		dlmSecurityList.addElement(item4);
		
	}
	
	/*************************************************************
	* Function Name: 	initEnergyList
	* Purpose: 			能源的列表数据初始化
	**************************************************************/
	public static void initEnergyList(int fo, int using,int free,int surplus)
	{
	
		jlstEnergyList.setCellRenderer(new listView());
		listIcon item = new listIcon("/img/pic1.jpg","当天电表数据: "+fo+"kW•h");
		listIcon item1 = new listIcon("/img/pic22.png","当天使用电量: "+using+"kW•h");
		listIcon item2 = new listIcon("/img/pic4.png","当天电费: " + using+"元");
		listIcon item3 = new listIcon("/img/pic5.png","剩余电费: " + surplus+"元");
		listIcon item4 = new listIcon("/img/Pay.png","缴费详情,预交了1000元" );
		listIcon item5 = new listIcon("/img/pic6.png","余额不足,中断供电提示");		
		
		dlmEnergyList.addElement(item);
		
		dlmEnergyList.addElement(item1);
		dlmEnergyList.addElement(item2);
		dlmEnergyList.addElement(item3);
		dlmEnergyList.addElement(item4);
		dlmEnergyList.addElement(item5);
		
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
				System.out.println("已经连接到数据库了。。。。");
				statement = conn.createStatement();
				System.out.println("这是statement的那一关了！！！！");
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			
		}
	
	/*************************************************************
	* Function Name: 	initnotes
	* Purpose: 			在数据库查询sys_wit_notes表中的所有数据
	**************************************************************/
	public static void initnotes(String Hnum)
	{
		dlmTrains.removeAllElements();//每次清空中转站的jlist
		ResultSet rs;
		ResultSet rs1;
		String Address;
		String DayInfo;
		String Date;
		int fo = 0;
		int Element0=0;
		int Element1=0;
		int using =0;
		int surplus=0;
		/**
		 * 先根据门牌号获得地址号，再根据地址提取出改地址下的所有数据，将此数据按照当天数据（电表数）降序排列，在提取前两条（即选取最大的两条数据）
			select *from(select witday from sys_wit_notes where witaddress=(select witaddress from sys_wit_info where (witno='1002'))order by witday desc)where rownum<=2;
		 */
		String SQLString ="select *from(select witday from sys_wit_notes where witaddress=(select witaddress from sys_wit_info where (witno='"+Hnum+"'))order by witday desc)where rownum<=2";
		String SQLString1 ="select *from(select witday from sys_wit_notes where witaddress=(select witaddress from sys_wit_info where (witno='"+Hnum+"'))order by witday desc)where rownum<=1"; 
		connection();
		try
		{	//数据的查询
			rs = statement.executeQuery(SQLString);	
			
			while(rs.next())
			{
				DayInfo = rs.getString("WitDay");
				String lig = DayInfo.subSequence(0, 0+4)+"";
				System.out.println("这是电表数据： "+DayInfo.subSequence(0, 0+4));
				//十六进制字符串转化为十进制字符串
				String foo = Integer.valueOf(lig,16).toString();
				System.out.println("将十六进制字符串转化为十进制字符串：" + foo);
				fo = Integer.parseInt(foo);
				System.out.println("将十进制字符串转化为int类型："+fo);					
				
				dlmTrains.addElement(fo);
				if(dlmTrains.getSize()<2)
				{
					Element0 = (int)dlmTrains.getElementAt(0);
					initEnergyList(Element0,Element0,Element0,(1000-Element0));
					System.out.println("dlmTrains.getElementAt(0)=="+dlmTrains.getElementAt(0));
				}
				else
				{
					Element0 = (int)dlmTrains.getElementAt(0);
					Element1 = (int)dlmTrains.getElementAt(1);
					if(Element0>Element1)
					{							
						using = Element0-Element1;//剩余电费
						surplus = 1000-using;//剩余电费量 							
					}
					else
					{
						using = Element1-Element0;
						surplus = 1000-using;
					}
					System.out.println("dlmTrains.getElementAt(0)=="+Element0);
					System.out.println("dlmTrains.getElementAt(1)=="+Element1);
				}
				dlmEnergyList.removeAllElements();
				initEnergyList(Element0,using,using,surplus);		
				
			}			
			
			rs1 = statement.executeQuery(SQLString1);	
			while(rs1.next())
			{				
				System.out.println("^^^^^^^^^^^^^^^^^^^^这是sql1de ");
				DayInfo = rs1.getString("WitDay");
				//十六进制字符串转化为十进制字符串
				String tip = DayInfo.substring(12, 12+2);//警报信息
				System.out.println(tip+"DSFFFFFFFFFFFFFFFFFFFFFFFFF");
				String foo = Integer.valueOf(tip,16).toString();
				System.out.println("将十六进制字符串转化为十进制字符串：这是警报的信系" + foo);					
									
				String tip1;
				for(int i = 1; i <=4 ; i++)
				{
					int tiph = ((fo & 1)>>1);
					if(tiph==0)
						tip1 = "安全";
					else
						tip1 = "危险";
					//TODO 当危险时，启动报警音频的线程
					
					System.out.println("第" +i+"cidd"+tip1);
					dlmTrains.addElement(tip1);
				}
				System.out.println(hexString2binaryString(tip));
		        String bString = hexString2binaryString(tip);
		        
		        char ss [] = bString.toCharArray();
		        char cc = '1';
		        int e ;
		        for(int i = 0; i < 4;i++ )
		        {
		        	e = (int)(ss[i]&cc)-48;
		            if(e==0)
						tip1 = "安全";
					else
					{
						tip1 = "危险";
						 
					}
					//TODO 当危险时，启动报警音频的线程
		        
					System.out.println("第" +i+"cidd"+tip1);
					dlmTrains.addElement(tip1);
		        }	
		        String ElementA=dlmTrains.getElementAt(dlmTrains.getSize()-4)+"";
				String ElementB=dlmTrains.getElementAt(dlmTrains.getSize()-3)+"";
				String ElementC=dlmTrains.getElementAt(dlmTrains.getSize()-2)+"";
				String ElementD=dlmTrains.getElementAt(dlmTrains.getSize()-1)+"";
				
				System.out.println("@@"+ElementA+"@@"+ElementB+"@@"+ElementC+"@@"+ElementD);
				String temp = DayInfo.substring(14, 14+2);//温度
				String hum = DayInfo.substring(16, 18);//湿度
				System.out.println("w温度+湿度"+temp+"@"+hum);
				dlmSecurityList.removeAllElements();
				initSecurityList(temp,hum,ElementA,ElementB,ElementC,ElementD);
			}
		} catch (SQLException e1)
		{
			e1.printStackTrace();
		}
		try 
		{
			statement.close();
		} catch (Exception e) 
		{
			System.out.println("关闭异常！"+e);
		}
	}
			
	/*************************************************************
	* Function Name: 	SameClient
	* Purpose: 			向下位机发送警报提示
	**************************************************************/
	public static void SameClient(String send)//TODO
	{
		try
		{
			Socket socket = new Socket("localhost",61122);
			OutputStream out = socket.getOutputStream();
			out.write(send.getBytes());

			out.close();
			socket.close();
			
		} catch (UnknownHostException e) 
		{
			e.printStackTrace();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	/*************************************************************
	* Function Name: 	getHnum
	* Purpose: 			对"员工信息"的JLabel的数据处理
	**************************************************************/												
	public void getHnum()
	{
		String text = jlblUserInfo.getText(); 
		
		String regEx="[^0-9]";     
		Pattern p = Pattern.compile(regEx);     
		Matcher m = p.matcher(text);  
		String num = m.replaceAll("").trim();
		if(num.length()>=3)
		{	
			String Hnum = num.substring(2, 2+4);
			System.out.println( Hnum);
			initnotes(Hnum);
		}
		
	}
		
	
	/*************************************************************
	* Function Name: 	hexString2binaryString
	* Purpose: 			将接收到的HEX进行转化
	**************************************************************/	
	public static String hexString2binaryString(String hexString)  
    {  
        if (hexString == null || hexString.length() % 2 != 0)  
            return null;  
        String bString = "", tmp;
        
        for (int i = 0; i < hexString.length(); i++)  
        {  
            tmp = "0000"  
                    + Integer.toBinaryString(Integer.parseInt(hexString  
                            .substring(i, i + 1), 16));  
           bString += tmp.substring(tmp.length() - 4);  
       
        }
        return bString;  
    }
	/*************************************************************
	* Function Name: 	getTip
	* Purpose: 			提取用户基本信息，以便提示
	**************************************************************/												
	public void  getTip()
	{
		/*String Hnum;
		String text = jlblUserInfo.getText(); 
		
		String regEx="[^0-9]";     
		Pattern p = Pattern.compile(regEx);     
		Matcher m = p.matcher(text);  
		String num = m.replaceAll("").trim();
		if(num.length()>=3)
		{	
			Hnum = num.substring(2, 2+4);
			System.out.println( Hnum);			
		}
		*/
		
	}
	
	/*************************************************************
	* Function Name: 	dealAction
	* Purpose: 			事件处理
	**************************************************************/	
	public void dealAction()
	{
		/*simpletree.addTreeSelectionListener
		(
				   new TreeSelectionListener()
				   {
				      public void valueChanged(TreeSelectionEvent e)
				      {
				    	  TreePath path = simpletree.getPathForLocation(e.getX(), e.getY());
				    	  jlblUserInfo.setText(path+"");
				      }
				   }
				  );//增加选中节点的监听器
*/

		/*树菜单的监听*/
		simpletree.addMouseListener
		(
				 new MouseListener()
	            	{
						public void mouseClicked(MouseEvent e) 
						{
							TreePath path = simpletree.getPathForLocation(e.getX(), e.getY());
							jlblUserInfo.setText(path+"");
							getHnum();
							jlblTip.setText("");
						}

						@Override
						public void mouseEntered(MouseEvent arg0) {
						}

						@Override
						public void mouseExited(MouseEvent arg0) {
						}

						@Override
						public void mousePressed(MouseEvent arg0) {
						}

						@Override
						public void mouseReleased(MouseEvent arg0) {
							
						}
	            	}
				
				);
		
		jbtnStealed.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0) 
				{
					String Hnum = null;
					String text = jlblUserInfo.getText(); 					
					String regEx="[^0-9]";     
					Pattern p = Pattern.compile(regEx);     
					Matcher m = p.matcher(text);  
					String num = m.replaceAll("").trim();
					if(num.length()>=3)
					{	
						Hnum = num.substring(2, 2+4);
					}
					
					String Stealedtext = jbtnStealed.getText();
					System.out.println(jbtnStealed.getText());
					jlblTip.setText("向"+Hnum +"住户"+"发出"+Stealedtext+"信息");
					//TODO 向服务器发送报警提示
					MusicThread TipMusic  = new MusicThread();
					TipMusic.start();			
					
					String Help = "0";
					SameClient(Help);
				}
			}
		);
		
		jbtnFire.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0) 
				{
					String Hnum = null;
					String text = jlblUserInfo.getText(); 					
					String regEx="[^0-9]";     
					Pattern p = Pattern.compile(regEx);     
					Matcher m = p.matcher(text);  
					String num = m.replaceAll("").trim();
					if(num.length()>=3)
					{	
						Hnum = num.substring(2, 2+4);
					}					
					String Firetext = jbtnFire.getText();
					System.out.println(jbtnFire.getText());
					jlblTip.setText("向"+Hnum +"住户"+"发出"+Firetext+"信息");
					
				}
			}
		);
		
		jbtnSmoke.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0) 
				{
					String Hnum = null;
					String text = jlblUserInfo.getText(); 					
					String regEx="[^0-9]";     
					Pattern p = Pattern.compile(regEx);     
					Matcher m = p.matcher(text);  
					String num = m.replaceAll("").trim();
					if(num.length()>=3)
					{	
						Hnum = num.substring(2, 2+4);
					}
					
					String Smoketext = jbtnSmoke.getText();
					System.out.println(jbtnSmoke.getText());
					jlblTip.setText("向"+Hnum +"住户"+"发出"+Smoketext+"信息");
					
				}
			}
		);
		
		jbtnFlood.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0) 
				{
					String Hnum = null;
					String text = jlblUserInfo.getText(); 					
					String regEx="[^0-9]";     
					Pattern p = Pattern.compile(regEx);     
					Matcher m = p.matcher(text);  
					String num = m.replaceAll("").trim();
					if(num.length()>=3)
					{	
						Hnum = num.substring(2, 2+4);
					}
					
					String Floodtext = jbtnFlood.getText();
					System.out.println(jbtnFlood.getText());
					jlblTip.setText("向"+Hnum +"住户"+"发出"+Floodtext+"信息");
					
				}
			}
		);
	}
	
	/*************************************************************
	* Function Name: 	TimeThread
	* Purpose: 			系统时间线程
	**************************************************************/	
	public class TimeThread extends JTextField implements ActionListener
	{
		private Timer t ;
		Calendar c;
		public TimeThread()
		{
			super();
			s();
			this.addActionListener(this);
			t.start();
			
		}
		public TimeThread(int i)
		{

			super(i);
			s();
			this.addActionListener(this);
			t.start();
			
		}
		private void s()
		{
			t=new Timer(1000,this);
		}
		public void actionPerformed(ActionEvent e) 
		{
			
			Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
			int year = c.get(Calendar.YEAR); 
			int month = c.get(Calendar.MONTH); 
			int date = c.get(Calendar.DATE); 
			int hour = c.get(Calendar.HOUR_OF_DAY); 
			int minute = c.get(Calendar.MINUTE); 
			int second = c.get(Calendar.SECOND);
			if((second%5)==0)
			{
				getHnum();
			}			
			String time = year + "/" + month + "/" + date + " " +hour + ":" +minute + ":" + second;
			jlblTime.setText(time+"");
			
		}
	}
	
	/*************************************************************
	* Function Name: 	MusicThread
	* Purpose: 			音频线程
	**************************************************************/	
	public class MusicThread extends Thread
	{
		public void run()
		{
			UpperC.Play("E:/铃声 消防报警声8s.wav");			
		}
		
	}

	/*************************************************************
	* Function Name: 	Play
	* Purpose: 			播放音频文件
	**************************************************************/		
    public static void Play(String fileurl) 
    {
  
        try
        {
        	int nByte = 0;
            int writeByte = 0;
            double value=2;
            final int SIZE=1024*64;
            final SourceDataLine sdl;
            
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File(fileurl));
            AudioFormat aif = ais.getFormat();
            System.out.println(aif);            
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, aif);
            sdl = (SourceDataLine) AudioSystem.getLine(info);
            sdl.open(aif);
            sdl.start();
            FloatControl fc=(FloatControl)sdl.getControl(FloatControl.Type.MASTER_GAIN);
            //value可以用来设置音量，从0-2.0
            
            float dB = (float)
                  (Math.log(value==0.0?0.0001:value)/Math.log(10.0)*20.0);
            fc.setValue(dB);            
            byte[] buffer = new byte[SIZE];
            while (nByte != -1)
            {
                nByte = ais.read(buffer, 0, SIZE);
                sdl.write(buffer, 0, nByte);
            }
           sdl.stop();
 
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
	
	/*************************************************************
	* Function Name: 	main
	* Purpose: 			主函数
	**************************************************************/	
	public static void main(String[] arys)
	{
		new UpperC();
		
	}
	
}


