/**
 * 功能：坦克游戏的3.0版
 * 1.画出坦克
 * 2.我的坦克动起来（和老师的区别）
 * 3.子弹发射出来（发射出来后它自己能跑，这是一个线程，子弹也是一个对象，有速度坐标颜色，形状）
 * 4.子弹可以连发（最多连发5颗）
 * 5.写一个专门判断子弹有没有击中敌人坦克的函数
 * 6.什么地方调用该函数（需要随时去判断子弹有没有击中坦克，所以在run里判断,调用hit）
 * 7.爆炸效果（三个图片瞬间切换）
 * 8.防止重叠运动(这是一个敌人坦克自己判断自己有没有和队友相撞，所以是自己的一种能力
 * 而之前被击中是一种被动的情况，所以被击中放panel，但是有没有相撞放自己的类里)
 * 9.可以分关，暂停和继续*
 * 做一个闪烁的panel，空的，显示关
 * 用户点击暂停，子弹和坦克速度为0，坦克方向不变
 * 10.记录成绩*
 * 用文件流，单写一个记录类，完成对玩家记录
 * 先完成保存共击毁了多少辆敌人坦克的功能
 * 存盘退出（可以记录当时敌人坦克坐标，并且可以恢复
 * 11.java如何操作声音文件* 
 * 声音文件296行
 * 
 * 12.网络大战（一起玩同一个游戏）
 */
package com.test3;
import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

public class MyTankGame3 extends JFrame implements ActionListener{
	MyPanel mp=null;
	
	//定义一个开始面板
	MyStartPanel msp=null;
	
	
	//做出我需要的菜单
	JMenuBar jmb=null;
	//开始游戏
	JMenu jm1=null;
	JMenuItem jmi1=null;
	//退出游戏
	JMenuItem jmi2=null;
	//存盘退出
	JMenuItem jmi3=null;
	//接着上一局玩
	JMenuItem jmi4=null;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MyTankGame3 mtg=new MyTankGame3();
	}
	//构造函数
	public MyTankGame3()
	{
		//创建菜单即菜单选项
		jmb=new JMenuBar();
		jm1=new JMenu("游戏(G)");
		//设置助记符
		jm1.setMnemonic('G');
		jmi1=new JMenuItem("开始新游戏(N)");
		jmi2=new JMenuItem("退出游戏(E)");
		jmi3=new JMenuItem("存盘退出(C)");
		jmi4=new JMenuItem("继续上局游戏(S)");
		jmi1.setMnemonic('N');
		jmi2.setMnemonic('E');
		//注册监听
		jmi4.addActionListener(this);
		jmi4.setActionCommand("continueGame");
		jmi3.addActionListener(this);
		jmi3.setActionCommand("saveExit");
		jmi2.addActionListener(this);
		jmi2.setActionCommand("exit");
		jmi1.addActionListener(this);
		jmi1.setActionCommand("newgame");
		//对jmi1响应
		jm1.add(jmi1);
		jm1.add(jmi2);
		jm1.add(jmi3);
		jm1.add(jmi4);
		jmb.add(jm1);
		
		
		msp=new MyStartPanel();
		Thread t=new Thread(msp);
		t.start();
		//把开始面板添加进去
		this.add(msp);
		this.setJMenuBar(jmb);
		this.setSize(600, 500);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//对用户不同的点击做出不同的处理
		if(e.getActionCommand().equals("newgame"))
		{
			
			//创建战场面板
			mp=new MyPanel("newGame");
			//启动mp线程
			Thread t=new Thread(mp);
			t.start();
			
			//要把旧的删掉
			this.remove(msp);
			this.add(mp);
			//注册监听
			this.addKeyListener(mp);
			//显示，刷新JFrame
			this.setVisible(true);
		}else if(e.getActionCommand().equals("exit"))
		{
			//用户点击了退出系统的菜单
			//保存击毁敌人数量的数据
			//交给recorder来写，封装思想
			//代码可读性很强
			Recorder.keepRecording();
			
			System.exit(0);
		}//对存盘退出做处理
		else if(e.getActionCommand().equals("saveExit"))
		{
			//很多工作
			
			Recorder.setEts(mp.ets);
			//1.保存击毁敌人的数量和坐标位置
			Recorder.keepRecAndEnemyTank();
			
			//正常退出（-1是异常退出）
			System.exit(0);
		}else if(e.getActionCommand().equals("continueGame"))
		{
			//要恢复敌人坦克坐标等相关信息
			//先读取
			//但是不知道有多少敌人坦克
			//韩老头认为就是一个横坐标，纵坐标，加方向构成一个点，就是一个坦克
			//创建战场面板
			
			mp=new MyPanel("continue");
			
			
			//启动mp线程
			Thread t=new Thread(mp);
			t.start();
			
			//要把旧的删掉
			this.remove(msp);
			this.add(mp);
			//注册监听
			this.addKeyListener(mp);
			//显示，刷新JFrame
			this.setVisible(true);
		
		
		}
	}
}
//就是一个提示作用
class MyStartPanel extends JPanel implements Runnable
{
	
	int times=0;
	public void paint(Graphics g)
	{
		super.paint(g);
		g.fillRect(0, 0, 400, 300);
		
		if(times%2==0)
		{
			//设置字体
			Font myFont=new Font("华文新魏",Font.BOLD,30);
			g.setFont(myFont);
			g.setColor(Color.yellow);
			//提示信息
			g.drawString("Stage 1", 150, 150);
		}
	}
	public void run()
	{
		while(true)
		{
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			times++;
			//重画
			this.repaint();
		}
	}
}


//我的面板(在面板画坦克)
class MyPanel extends JPanel implements KeyListener,Runnable
{
	//坦克是在哪里活动的？
	//坦克生活在Panel里
	//所以坦克是里面的一个成员变量
	//定义一个我的坦克
	Hero hero=null;
	
	//判断是继续上局的游戏，还是新游戏
	
	
	//定义敌人坦克部队
	Vector<EnemyTank> ets=new Vector<EnemyTank>();
	//如果失去上一局那么。就不是三辆
	int enSize=3;
	Vector<Mode> modes=new Vector<Mode>();
	
	//定义炸弹向量
	Vector<Bomb> bombs=new Vector<Bomb>();
	//定义三张图片，三张图片组合成一颗炸弹，炸弹也是向量，可以爆很多次
	Image image1=null;
	Image image2=null;
	Image image3=null;
	
	//可以通过hero访问到它的坐标，set,get
	//构造函数
	public MyPanel(String flag)
	{
		//恢复记录
		Recorder.getRecording();
		hero=new Hero(100,200);
		
		//初始化敌人的坦克
		if(flag.equals("newGame"))
		{
			for(int i=0;i<enSize;i++)
			{
				//创建一辆敌人的坦克对象
				EnemyTank et=new EnemyTank((i+1)*50,0);
				et.setColor(0);
				et.setDirect(2);
				//加入到Vector
				ets.add(et);
			
				//将MyPanel的敌人坦克向量交给该敌人坦克
				et.setEts(ets);
				Thread t=new Thread(et);
				t.start();
				
				//给敌人坦克加入一颗子弹
				Shot s=new Shot(et.x+10,et.y+30,2);
				et.shots.add(s);
				//shot是一个线程
				Thread t2=new Thread(s);
				t2.start();
			}
		}else{
			modes=Recorder.getModesAndEnNums();
			for(int i=0;i<modes.size();i++)
			{
				Mode mode=modes.get(i);
				//创建一辆敌人的坦克对象
				EnemyTank et=new EnemyTank(mode.x,mode.y);
				et.setColor(0);
				et.setDirect(mode.direct);
				//加入到Vector
				ets.add(et);
			
				//将MyPanel的敌人坦克向量交给该敌人坦克
				et.setEts(ets);
				Thread t=new Thread(et);
				t.start();
				
				//给敌人坦克加入一颗子弹
				Shot s=new Shot(et.x+10,et.y+30,2);
				et.shots.add(s);
				//shot是一个线程
				Thread t2=new Thread(s);
				t2.start();
			}
		}
		
		//由于下面的方法在第一次击中的时候，没啥效果
		//所以用别的方法
		try {
			image1=ImageIO.read(new File("src/boom1.jpg"));
			image2=ImageIO.read(new File("src/boom2.jpg"));
			image3=ImageIO.read(new File("src/boom3.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//播放开战声音
		//AePlayWave apw=new AePlayWave("d:\\xx.wav");
		//apw.start();
		//初始化图片
		//到时候可以随意调用
//		image1=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/boom1.jpg"));
//		image2=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/boom2.jpg"));
//		image3=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/boom3.jpg"));
	}
	//画出提示信息
	
	public void showInfo(Graphics g)
	{
		//画出提示信息坦克（该坦克不参与战斗）
		this.drawTank(80, 330, g, 0, 0);
		//数量
		g.setColor(Color.black);
		g.drawString(Recorder.getEnNum()+"", 110, 350);
		
		//我的坦克的生命
		this.drawTank(150, 330, g, 0, 1);
		g.setColor(Color.black);
		g.drawString(Recorder.getMyLife()+"", 180, 350);
		
		
		//画出玩家的总成绩
		g.setColor(Color.black);
		Font f=new Font("宋体",Font.BOLD,20);
		g.setFont(f);
		g.drawString("您的总成绩", 420, 30);
		
		//画出敌人坦克
		this.drawTank(420, 60, g, 0, 0);
		g.setColor(Color.black);
		g.drawString(Recorder.getAllEnNum()+"", 460, 80);
	
		//
	}
	
	
	
	//重新paint
	
	//要让子弹动起来就必须隔一段时间就重绘
	//要重绘的动作是在panel上面发生的
	//看电视的时候屏幕在不断刷新
	//所以要在mypanel装线程
	public void paint(Graphics g)
	{
		super.paint(g);
		g.fillRect(0,0,400,300);//背景颜色黑色
		//画出提示信息
		this.showInfo(g);
		
		//调用下面画坦克的函数
		//画出自己的坦克和自己的子弹
		if(hero.isLive==true)
		{
			this.drawTank(hero.getX(), hero.getY(), g, hero.getDirect(),1);
			//从shorts中取出每颗子弹，并画出
			for(int i=0;i<hero.shots.size();i++)
			//画出子弹（画出了一颗子弹）
			//要判断有没有子弹
			//当坦克刚出来，没按J的时候，这个子弹是空；如果上来就画，这个子弹都不存在，那会抛异常 
			{
				//取出来，不用强转，都是泛型
				//连续发子弹
				Shot myShot=hero.shots.get(i);
				if(myShot!=null&&myShot.isLive==true)
				{
					g.draw3DRect(myShot.x, myShot.y, 2, 2, false);
					//让子弹跑起来，意思就是，子弹的坐标不停地修改
				}
				
				if(myShot.isLive==false)
				{
					//从shorts向量中去掉该子弹
					hero.shots.remove(myShot);
					
				}
			}
		}
		
		//画出敌人坦克
		for(int i=0;i<ets.size();i++)
		{
			EnemyTank et=ets.get(i);
			if(et.isLive==true)
			{
				//画敌人坦克
				this.drawTank(et.getX(), et.getY(), g, et.getDirect(), 0);
				//画敌人子弹
				for(int k=0;k<et.shots.size();k++)
				{
					//取出子弹
					Shot enemyShot=et.shots.get(k);
					if(enemyShot.isLive==true)
					{
						g.draw3DRect(enemyShot.x, enemyShot.y, 2, 2, false);
					}else{
						//如果敌人的子弹死亡就Remove
						et.shots.remove(enemyShot);
					}
				}
			}
		}
		
		
		//划出炸弹
		for(int i=0;i<bombs.size();i++)
		{
			//取出炸弹
			Bomb bomb=bombs.get(i);
			//可以通过lifeDown展现图片
			if(bomb.life>6)
			{
				//this代表就在panel上面画
				g.drawImage(image1,bomb.x,bomb.y,30,30,this);
			}else if(bomb.life>3)
			{
				g.drawImage(image2,bomb.x,bomb.y,30,30,this);
			}else{
				g.drawImage(image3,bomb.x,bomb.y,30,30,this);
			}
			//没画一次让生命值减小
			bomb.lifeDown();
			//如果炸弹生命值为0，就把该炸弹从向量中去掉
			if(bomb.life==0)
			{
				bombs.remove(bomb);
			}
		}
	}
	
	
	
	//判断子弹是否击中敌人坦克
	
	public void hitEnemyTank()
	{
		//判断是否击中敌人坦克
		//有好多好多子弹好多好多坦克，需要匹配一下看看有没有击中
		for(int i=0;i<hero.shots.size();i++)
		{
			//取出子弹
			Shot myShot=hero.shots.get(i);
			//判断子弹是否活着
			if(myShot.isLive==true)
			{
				//取出每个敌人坦克，与之判断
				for(int j=0;j<ets.size();j++)
				{
					//取出坦克
					EnemyTank et=ets.get(j);
					if(et.isLive==true)
					{
						this.hitTank(myShot, et);
					}
				}
			}
			
		}
		
		
	}
	//判断敌人子弹是否击中我
	public void hitMe()
	{
		//取出每一个敌人的坦克
		for(int i=0;i<this.ets.size();i++)
		{
			EnemyTank et=ets.get(i);
			
			//取出子弹
			for(int j=0;j<et.shots.size();j++)
			{
				Shot enemyShot=et.shots.get(j);
				if(hero.isLive==true)
				{
					this.hitTank(enemyShot, this.hero);
				}
			}
		}
		
	}
	
	
	//写一个函数判断子弹是否击中坦克
	public void hitTank(Shot s,Tank tank)
	{
		
		//判断该坦克的方向
		switch(tank.direct)
		{
		//敌人坦克向上或者向下，形状是一样的
		case 0:
		case 2:
			if(s.x>tank.x&&s.x<tank.x+20&&s.y>tank.y&&s.y<tank.y+30)
			{
				//击中
				//子弹死亡，坦克也死亡
				s.isLive=false;
				tank.isLive=false;
				//增加我的战绩
				Recorder.addRed(tank);
				//减少敌人数量
				Recorder.reduce(tank);
				//创建一颗炸弹，放入Vector
				Bomb bomb=new Bomb(tank.x,tank.y);
				bombs.add(bomb);
			}
			break;
		case 1:
		case 3:
			if(s.x>tank.x&&s.x<tank.x+30&&s.y>tank.y&&s.y<tank.y+20)
			{
				//击中
				//子弹死亡，坦克也死亡
				s.isLive=false;
				tank.isLive=false;
				Recorder.addRed(tank);
				Recorder.reduce(tank);
				Bomb bomb=new Bomb(tank.x,tank.y);
				bombs.add(bomb);
			}
			break;
		}
		
	}
	
	//画出坦克的函数(坦克的横纵坐标，还有画笔,坦克方向,坦克类型是敌人还是我)
	//扩展函数
	//画坦克的时候尽量用这样，不要画敌人坦克和自己坦克写了一大堆代码，尽量封装
	public void drawTank(int x,int y,Graphics g,int direct,int type)
	{
		//判断是什么类型的坦克,改变画笔颜色
		switch(type)
		{
			case 0:
				g.setColor(Color.CYAN);
				break;
			case 1:
				g.setColor(Color.yellow);
				break;
		
		}
		//判断方向
		switch(direct)
		{
			//向上
			case 0:
				//画出我的坦克(到时再封装成一个函数)
				//分割成一块块的
				//1.先画左边的矩形
				g.fill3DRect(x, y, 5, 30,false);
				//2.画右边矩形
				g.fill3DRect(x+15,y, 5, 30,false);
				//画出中间矩形
				g.fill3DRect(x+5, y+5, 10, 20,false);
				//4.画出圆形
				g.fillOval(x+5, y+10, 10, 10);
				//5.画出线
				g.drawLine(x+10, y+15, x+10,y);
				break;
			//炮筒向右
			case 1:
				//画出上面的矩形
				g.fill3DRect(x, y, 30, 5, false);
				//画出下面的矩形
				g.fill3DRect(x, y+15, 30, 5, false);
				//画出中间的矩形
				g.fill3DRect(x+5, y+5, 20, 10, false);
				//画出圆形
				g.fillOval(x+10, y+5, 10, 10);
				//画出线
				g.drawLine(x+15, y+10, x+30, y+10);
				break;
			//向下
			case 2:
				//1.先画左边的矩形
				g.fill3DRect(x, y, 5, 30,false);
				//2.画右边矩形
				g.fill3DRect(x+15,y, 5, 30,false);
				//画出中间矩形
				g.fill3DRect(x+5, y+5, 10, 20,false);
				//4.画出圆形
				g.fillOval(x+5, y+10, 10, 10);
				//5.画出线
				g.drawLine(x+10, y+15, x+10,y+30);
				break;
			//向左
			case 3:
				//画出上面的矩形
				g.fill3DRect(x, y, 30, 5, false);
				//画出下面的矩形
				g.fill3DRect(x, y+15, 30, 5, false);
				//画出中间的矩形
				g.fill3DRect(x+5, y+5, 20, 10, false);
				//画出圆形
				g.fillOval(x+10, y+5, 10, 10);
				//画出线
				g.drawLine(x+15, y+10, x, y+10);
				break;
				
		}
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	//键按下处理 
	//a表示向左，s表示向下，w表示向上，d表示向右
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode()==KeyEvent.VK_W)
		{
			//设置我的坦克的方向
			this.hero.setDirect(0);
			this.hero.moveUp();
			//一般老师不会这样写this.hero.y--,因为要改速度的时候要一个个改，很不灵活
		}else if(e.getKeyCode()==KeyEvent.VK_D)
		{
			//向右
			this.hero.setDirect(1);
			this.hero.moveRight();
		}else if(e.getKeyCode()==KeyEvent.VK_S)
		{
			//向下
			this.hero.setDirect(2);
			this.hero.moveDown();
		}else if(e.getKeyCode()==KeyEvent.VK_A)
		{
			//向左
			this.hero.setDirect(3);
			this.hero.moveLeft();
		} 
		
		//可以一边跑一边发子弹
		//不能连在上面一起写，因为else if会进入一个入口就不判断第二个入口
		//为了能一边跑一边发子弹，就分开写
		if(e.getKeyCode()==KeyEvent.VK_J)
		{
			//发射子弹,玩家按了J键
			//按了5次，就不能创建了
			
			if(this.hero.shots.size()<=4)
			{
				this.hero.shotEnemy();
				//滞后了一步，因为是shotEnemy才加了一个1
				//由于发了五个子弹以后就不能发射子弹了，所以要加一个清除的功能，就是把向量里的子弹清理清理
			}
		}
		
		
		
		
		
		//必须重新绘制窗口
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void run()
	{
		while(true)
		{//每隔100毫秒去重画
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.hitEnemyTank();
			this.hitMe();
			this.repaint();
		
		}
	}
}
