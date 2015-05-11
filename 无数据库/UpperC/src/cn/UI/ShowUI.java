package cn.UI;


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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.Timer;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;



public class ShowUI 
{
	private JFrame jfrmMain;
	private Container con;
	
	private JLabel jlblTopic;/*标题*/
	private JLabel jlblTip;/*安防提示信息*/
	
	private static JLabel jlblUserInfo;/*选择的用户信息*/
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
	
	/*************************************************************
	* Function Name: 	UpperC
	* Purpose: 			构造函数
	**************************************************************/
	public  ShowUI()
	{
		initFrame();
		reinitFrame();	
		dealAction();		
	}
	
	/*************************************************************
	* Function Name: 	dealDate
	* Purpose: 			处理数据并显示
	**************************************************************/	
	public void dealDate(String Hnum)
	{
		int fo = 0;		
		int using =0;
		int surplus=0;
		
		boolean flag = true;
		String data = null;
		String num1 = Hnum.substring(0, 1)+Hnum.substring(3, 4);
		
		String filename = "F:/智慧社区的临时文件测试\\智能电表数据采集.txt";
		Map<Integer, String> filemaps = readTxtFile(filename);
		int num = filemaps.size();// 行数
		String[]strings= new String[]{"a","b","c","d","d","b","c","d","d"};
		
		for(int i=0; i <num && flag; i++)
		{
			String str = filemaps.get(i);
			String HTnum = str.substring(0, 0+2);
			if(HTnum.equals(num1))
			{
				System.out.println("数据："+str);
				data = str;
				flag = false;
			}
		}		
		String Odata = data.replaceAll(" ", "");
		String lig = Odata.subSequence(4, 4+2)+"";
		System.out.println(Odata);
		System.out.println("这是电表数据： "+Odata.subSequence(4, 4+2));
		
		//十六进制字符串转化为十进制字符串
		String foo = Integer.valueOf(lig,16).toString();
		System.out.println("将十六进制字符串转化为十进制字符串：" + foo);
		fo = Integer.parseInt(foo);
		System.out.println("将十进制字符串转化为int类型："+fo);	
		
		//电表数据：fo 
		using = fo/2;
		surplus = 1000-using;
	
		dlmEnergyList.removeAllElements();
		dlmSecurityList.removeAllElements();
		initEnergyList(fo,using,surplus);
		initSecurityList();		
	}	
	
	/*************************************************************
	* Function Name: 	toHexString
	* Purpose: 			将字符串转化为HEXString
	**************************************************************/	
	 public static String toHexString(String s) 
	 { 
		 String str=""; 
		 for (int i=0;i<s.length();i++) 
		 { 
			 int ch = (int)s.charAt(i); 
			 String s4 = Integer.toHexString(ch); 
			 str = str + s4; 
		 } 
		 return str; 	 
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
	* Function Name: 	readTxtFile
	* Purpose: 			将文件中的数据保存到map中
	**************************************************************/
	public static Map<Integer,String> readTxtFile(String filePath)
	{
		//存放内容的map对象
		Map<Integer,String> filemaps = new HashMap<Integer,String>();
		
		try 
		{
			String encoding = "GBK";
			int count = 0;//定义顺序变量
			
			File file = new File(filePath);		
			if (file.isFile() && file.exists())
			{ 
				String lineTxt = null;
				// 判断文件是否存在
				InputStreamReader read = new InputStreamReader(
				new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				
				while ((lineTxt = bufferedReader.readLine()) != null)
				{
					//按行读取
					// System.out.println(“lineTxt=” + lineTxt);
					if(!"".equals(lineTxt))
					{
						String reds = lineTxt.split("\\+")[0];//对行的内容进行分析处理后再放入map里。
						// System.out.println(reds);
						filemaps.put(count, reds);//放入map
						count ++;
					}
				}
				read.close();//关闭InputStreamReader 
				bufferedReader.close();//关闭BufferedReader 
			}
			else 
			{
				System.out.println("找不到指定的文件");
			}
			} catch (Exception e)
			{
				System.out.println("读取文件内容出错");
				e.printStackTrace();
			}
			return filemaps;
	}
	
	/*************************************************************
	* Function Name: 	getHnum
	* Purpose: 			对"用户楼号信息"的JLabel的数据处理
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
			System.out.println( "用户门牌号："+Hnum);
			dealDate(Hnum);
		}
		
	}
	
	/*************************************************************
	* Function Name: 	dealAction
	* Purpose: 			事件处理
	**************************************************************/	
	public void dealAction()
	{
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
	public static void initSecurityList()
	{
	
		jlstSecurityList.setCellRenderer(new listView());
		
		listIcon item1 = new listIcon("/img/pic3.png","火灾报警: 安全");	
		listIcon item2 = new listIcon("/img/smook.png","烟雾报警: 安全");	
		listIcon item3 = new listIcon("/img/forFlood.png","水侵报警:安全 ");	
		listIcon item4 = new listIcon("/img/stealTip.png","盗窃报警: 安全");	
		listIcon item5 = new listIcon("/img/temp.png","温度提示: 26℃");	
		listIcon item6 = new listIcon("/img/shidu.png","湿度提示: 50%");	

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
	public static void initEnergyList(int fo,int using,int surplus)
	{
	
		jlstEnergyList.setCellRenderer(new listView());
		listIcon item = new listIcon("/img/pic1.jpg","当天电表数据: "+fo+"kW•h");
		listIcon item1 = new listIcon("/img/pic22.png","当天使用电量: "+fo+"kW•h");
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

}
