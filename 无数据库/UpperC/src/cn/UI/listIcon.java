package cn.UI;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class listIcon 
{
	Icon icon;
	String text;
	
	public listIcon(String icon, String text)
	{
		//this.icon = new ImageIcon(icon);								//绝对路径	
		this.icon = new ImageIcon(this.getClass().getResource(icon));	//相对路径
		this.text = text;
	}
	
	public Icon getIcon()
	{
		return icon;
	}
	
	public String getText()
	{
		return text;
	}
}
