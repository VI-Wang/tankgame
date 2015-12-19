//因为敌人坦克会随机移动，所以要把敌人坦克做成线程
//当敌人坦克打中我的坦克
package com.test3;

import java.util.*;
import java.io.*;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

//播放声音的类
class AePlayWave extends Thread
{
	private String filename;
	public AePlayWave(String wavfile)
	{
		filename=wavfile;
	}
	
	public void run()
	{
		File soundFile=new File(filename);
		
		AudioInputStream audioInputStream=null;
		
		try{
			audioInputStream=AudioSystem.getAudioInputStream(soundFile);
			
		}catch(Exception e)
		{
			e.printStackTrace();
			return;
		}
		
		AudioFormat format=audioInputStream.getFormat();
		SourceDataLine auline=null;
		DataLine.Info info=new DataLine.Info(SourceDataLine.class, format);
		
		try{
			auline=(SourceDataLine)AudioSystem.getLine(info);
			auline.open(format);
			
		}catch(Exception e)
		{
			e.printStackTrace();
			return;
		}
		auline.start();
		int nBytesRead=0;
		//这是缓冲
		byte[] abData=new byte[1024];
		
		try{
			//循环播放，可能这个声音很大
			while(nBytesRead!=-1)
			{
				nBytesRead=audioInputStream.read(abData,0,abData.length);
				if(nBytesRead>=0)
				{
					auline.write(abData, 0, nBytesRead);
				}
			}
		}catch(Exception e)
		{
			e.printStackTrace();
			return;
		}finally{
			auline.drain();
			auline.close();
		}
		
		
	}
}



//恢复点
class Mode
{
	int x;
	int y;
	int direct;
	public Mode(int x,int y,int direct)
	{
		this.x=x;
		this.y=y;
		this.direct=direct;
	}
}


//记录类，可以保存玩家设置
class Recorder
{
	//记录消灭了多少敌人
	private static int allEnNum=0;
	//做成static 的原因是，无论是哪个线程，敌人的生命和我的生命都是访问同一个地方的
		//设置记录每关有多少敌人
	private static int enNum=20;
		//设置我有多少可用的人
	private static int myLife=3;
	//从文件中恢复记录点，因为不知道有多少坦克，所以要用vector
	static Vector<Mode> modes=new Vector<Mode>();
	
	
	private static FileWriter fw=null;
	private static BufferedWriter bw=null;
	
	private static FileReader fr=null;
	private static BufferedReader br=null;
	
	//要知道外面敌人坦克的情况
	private static Vector<EnemyTank> ets=new Vector<EnemyTank>();
	
	public static Vector<EnemyTank> getEts() {
		return ets;
	}
	public static void setEts(Vector<EnemyTank> ets) {
		Recorder.ets = ets;
		
	}

	//完成读取任务
	//恢复
	public static Vector<Mode> getModesAndEnNums()
	{
		try{
			fr=new FileReader("d:\\myRecording.txt");
			br=new BufferedReader(fr);
			String n="";
			//先读取第一行
			n=br.readLine();
			//字符串转成int
			allEnNum=Integer.parseInt(n);
			//接着往下读
			while((n=br.readLine())!=null)
			{
				//按照有多少空格返回,分成了三块
				String[]xyz=n.split(" ");
				Mode mode=new Mode(Integer.parseInt(xyz[0]),Integer.parseInt(xyz[1]),Integer.parseInt(xyz[2]));
				modes.add(mode);
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally{
			try{
				//后打开则先关闭
				br.close();
				fr.close();
			}catch(Exception e2)
			{
				e2.printStackTrace();
			}
		}
		return modes;
	}
	//保存击毁敌人的数量和敌人坦克坐标方向
	public static void keepRecAndEnemyTank()
	{
		try{
			//创建文件流
			fw=new FileWriter("d:\\myRecording.txt");
			bw=new BufferedWriter(fw);
			//记录击毁多少敌人坦克
			bw.write(allEnNum+"\r\n");
			//保存当前活着的敌人坦克的坐标和方向
			//遍历
			for(int i=0;i<ets.size();i++)
			{
				//取出第一个坦克
				EnemyTank et=ets.get(i);
				if(et.isLive==true)
				{
					//活着才保存
					String record=et.x+" "+et.y+" "+et.direct;
					//写入到文件中
					bw.write(record+"\r\n");
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//关闭流
			try{
				//后开先关闭
				bw.close();
				fw.close();
			}catch(Exception e2)
			{
				e2.printStackTrace();
			}
		}
	}
	
	
	//从文件中读取记录
	public static void getRecording()
	{
		try{
			fr=new FileReader("d:\\myRecording.txt");
			br=new BufferedReader(fr);
			String n=br.readLine();
			//字符串转成int
			allEnNum=Integer.parseInt(n);
		
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally{
			try{
				//后打开则先关闭
				br.close();
				fr.close();
			}catch(Exception e2)
			{
				e2.printStackTrace();
			}
		}
	}
	
	
	
	
	//把玩家击毁敌人坦克数辆保存到文件中
	public static void keepRecording()
	{
		//用文件流
		//字符流1234一定是文本
		try{
			//创建文件流
			fw=new FileWriter("d:\\myRecording.txt");
			bw=new BufferedWriter(fw);
			
			bw.write(allEnNum+"\r\n");
			
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//关闭流
			try{
				//后开先关闭
				bw.close();
				fw.close();
			}catch(Exception e2)
			{
				e2.printStackTrace();
			}
		}
	}
	public static int getAllEnNum() {
		return allEnNum;
	}
	public static void setAllEnNum(int allEnNum) {
		Recorder.allEnNum = allEnNum;
	}

	
	public static int getEnNum() {
		return enNum;
	}
	public static void setEnNum(int enNum) {
		Recorder.enNum = enNum;
	}
	public static int getMyLife() {
		return myLife;
	}
	public static void setMyLife(int myLife) {
		Recorder.myLife = myLife;
	}
	//每关消灭了多少敌人
	public static void reduce(Tank tank)
	{
		//击中敌人
		if(tank.getType()==0)
		{
			enNum--;
		}
		//击中我
		if(tank.getType()==1)
		{
			myLife--;
		}
	}
	//总共消灭多少敌人
	public static void addRed(Tank tank)
	{
		if(tank.getType()==0)
		{
			allEnNum++;
		}
	}
	
	
}


//炸弹类（炸弹不会移动，所以不用做成线程）
class Bomb
{
	//定义炸弹坐标
	int x;
	int y;
	//炸弹是有生命的
	int life=9;
	boolean isLive=true;
	public Bomb(int x,int y)
	{
		this.x=x;
		this.y=y;
	}
	
	
	//生命会缩短，减少生命
	public void lifeDown()
	{
		if(life>0)
		{
			life--;
		}else{
			this.isLive=false;
			//可以看一下还要不要画
		}
	}
}



//子弹是属于坦克的
//子弹一旦被创建，我们就根据子弹的方向不断修改坐标
class Shot implements Runnable
{
	int x;
	int y;
	int direct;
	int speed=2;
	//是否还活着
	boolean isLive=true;
	public Shot(int x,int y,int direct)
	{
		this.x=x;
		this.y=y;
		this.direct=direct;
	}
	
	public void run()
	{
		while(true)
		{
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			switch(direct)
			{
			case 0:
				//往上
				y-=speed;
				break;
			case 1:
				//往右
				x+=speed;
				break;
			case 2:
				y+=speed;
				break;
			case 3:
				x-=speed;
				break;
			}
		
			
		
			//判断该子弹是否碰到边缘
			if(x<0||x>400||y<0||y>300)
			{
				//false就不会画了
				this.isLive=false;
				break;
			}
		
		}
		
		//子弹何时死亡？
		//子弹碰到边缘或者边框就死了
	}
}



//坦克类
class Tank
{
	
	//x为坦克横坐标
	
	int x=0;
	//坦克纵坐标
	int y=0;
	boolean isLive=true;
	//坦克方向
	//0表示上，1表示右，2表示下，3表示左
	int direct=0;
	int color=0;
	int type=0;
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	//抽象出设置坦克的速度
	//这里改一下速度就很方便
	int speed=1;
	
	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getDirect() {
		return direct;
	}

	public void setDirect(int direct) {
		this.direct = direct;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Tank(int x,int y)
	{
		this.x=x;
		this.y=y;
	}
}


//敌人的坦克
class EnemyTank extends Tank implements Runnable
{
	int type=0;
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	int times=0;
	//定义一个向量，可以访问到mypanel上所有敌人坦克
	Vector <EnemyTank> ets=new Vector<EnemyTank>();
	
	//定义一个向量，可以存放敌人子弹
	Vector <Shot> shots=new Vector<Shot>();
	//敌人添加子弹应该在刚刚创建坦克和敌人坦克子弹死亡后
	
	
	public EnemyTank(int x,int y)
	{
		super(x,y);
	}
	
	//得到myPanel的敌人坦克向量
	//非常重要
	public void setEts(Vector<EnemyTank> vv)
	{
		this.ets=vv;
	}
		
	//判断是否碰到了别的敌人坦克
	public boolean isTouchOtherEnemy()
	{
		boolean b=false;
		
		//怎么知道碰到了别人的坦克
		switch(this.direct)
		{
		case 0:
			//我的坦克向上
			//取出所有的敌人坦克
			for(int i=0;i<ets.size();i++)
			{
				//取出第一个坦克
				EnemyTank et=ets.get(i);
				//如果不是自己，不能自己跟自己比
				if(et!=this)
				{
					//如果敌人的方向是乡下或者向上
					if(et.direct==0||et.direct==2)
					{
						
						if(this.x>=et.x&&this.x<=et.x+20&&this.y>=et.y&&this.y<=et.y+30)
						{
							return b=true;
						}
						if(this.x+20>=et.x&&this.x+20<=et.x+20&&this.y>=et.y&&this.y<=et.y+30)
						{
							return b=true;
						}
						
					}else if(et.direct==1||et.direct==3)
					{
						if(this.x>=et.x&&this.x<=et.x+30&&this.y>=et.y&&this.y<=et.y+20)
						{
							return b=true;
						}
						if(this.x+20>=et.x&&this.x+20<=et.x+30&&this.y>=et.y&&this.y<=et.y+20)
						{
							return b=true;
						}
					}
				}
			}
			break;
		case 1:
			for(int i=0;i<ets.size();i++)
			{
				//取出第一个坦克
				EnemyTank et=ets.get(i);
				//如果不是自己，不能自己跟自己比
				if(et!=this)
				{
					//如果敌人的方向是乡下或者向上
					if(et.direct==0||et.direct==2)
					{
						
						if(this.x+30>=et.x&&this.x+30<=et.x+20&&this.y>=et.y&&this.y<=et.y+30)
						{
							return b=true;
						}
						if(this.x+30>=et.x&&this.x+30<=et.x+20&&this.y+20>=et.y&&this.y+20<=et.y+30)
						{
							return b=true;
						}
						
					}else if(et.direct==1||et.direct==3)
					{
						if(this.x+30>=et.x&&this.x+30<=et.x+30&&this.y>=et.y&&this.y<=et.y+20)
						{
							return b=true;
						}
						if(this.x+30>=et.x&&this.x<=et.x+30&&this.y+20>=et.y&&this.y+20<=et.y+20)
						{
							return b=true;
						}
					}
				}
			}
			break;
		case 2:
			for(int i=0;i<ets.size();i++)
			{
				//取出第一个坦克
				EnemyTank et=ets.get(i);
				//如果不是自己，不能自己跟自己比
				if(et!=this)
				{
					//如果敌人的方向是乡下或者向上
					if(et.direct==0||et.direct==2)
					{
						
						if(this.x>=et.x&&this.x<=et.x+20&&this.y>=et.y&&this.y<=et.y+30)
						{
							return b=true;
						}
						
						if(this.x+20>=et.x&&this.x+20<=et.x+20&&this.y>=et.y&&this.y<=et.y+30)
						{
							return b=true;
						}
						
					}else if(et.direct==1||et.direct==3)
					{
						if(this.x>=et.x&&this.x<=et.x+30&&this.y>=et.y&&this.y<=et.y+20)
						{
							return b=true;
						}
						if(this.x+20>=et.x&&this.x+20<=et.x+30&&this.y>=et.y&&this.y<=et.y+20)
						{
							return b=true;
						}
					}
				}
			}
			break;
		case 3:
			//向左
			for(int i=0;i<ets.size();i++)
			{
				//取出第一个坦克
				EnemyTank et=ets.get(i);
				//如果不是自己，不能自己跟自己比
				if(et!=this)
				{
					//如果敌人的方向是乡下或者向上
					if(et.direct==0||et.direct==2)
					{
						//我的上一点
						if(this.x>=et.x&&this.x<=et.x+20&&this.y>=et.y&&this.y<=et.y+30)
						{
							return b=true;
						}
						//我的下一点
						if(this.x>=et.x&&this.x<=et.x+20&&this.y+20>=et.y&&this.y+20<=et.y+30)
						{
							return b=true;
						}
						
					}else if(et.direct==1||et.direct==3)
					{
						if(this.x>=et.x&&this.x<=et.x+30&&this.y>=et.y&&this.y<=et.y+20)
						{
							return b=true;
						}
						if(this.x>=et.x&&this.x<=et.x+30&&this.y+20>=et.y&&this.y+20<=et.y+20)
						{
							return b=true;
						}
					}
				}
			}
			break;
		
		}
		
		return b;
	}
	
//敌人坦克如果向上的话，还要求没有碰到自己队友才可以运动
	public void run()
	{
		
		while(true)
		{
			//要让敌人坦克的坐标不断变换
			switch(this.direct)
			{
			//坦克正在向上走
			case 0:
				//让坦克再走两步
				for(int i=0;i<30;i++)
				{
					if(y>0&&this.isTouchOtherEnemy()==false)
					{
						y-=speed;
					}
					//走一下休息会
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			//向右
			case 1:
				for(int i=0;i<30;i++)
				{
					//保证坦克不出边界
					if(x<400&&this.isTouchOtherEnemy()==false)
					{
						x+=speed;
					}
					//走一下休息会
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case 2:
				for(int i=0;i<30;i++)
				{
					if(y<300&&this.isTouchOtherEnemy()==false)
					{
						y+=speed;
					}
					//走一下休息会
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case 3:
				for(int i=0;i<30;i++)
				{
					if(x>0&&this.isTouchOtherEnemy()==false)
					{
						x-=speed;
					}
					//走一下休息会
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			}
			this.times++;
			if(times%2==0)
			{
				if(isLive)
				{
					if(shots.size()<5)
					{
			
						Shot s=null;
							//没有子弹
							//添加
							switch(direct)
							{
							case 0:
								//创建一颗子弹
								s=new Shot(x+10,y,0);
								//把子弹加入向量
								shots.add(s);
								break;
							case 1:
								s=new Shot(x+30,y+10,1);
								shots.add(s);
								break;
							case 2:
								s=new Shot(x+10,y+30,2);
								shots.add(s);
								break;
							case 3:
								s=new Shot(x,y+10,3);
								shots.add(s);
								break;
							}
							//启动子弹线程
							Thread t=new Thread(s);
							t.start();
					}
				}
			}
			
			
			
			//方向要随机变化
			//让坦克随机产生一个新的方向
			//可以让坦克不冲着你来，也可以专门冲着你来
			this.direct=(int)(Math.random()*4);
			
			//判断敌人坦克是否死亡
			if(this.isLive==false)
			{
				//让坦克死亡后退出线程
				//如果只写了一个等号，这个线程会成为僵尸线程？
				//不会进入到break，这个线程永远活着，但是没有用，会占用资源
				break;
			}
			
			
			
			
		}
	}
}


//我的坦克是英雄
class Hero extends Tank
{
	int type=1;
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	
	//子弹从我这发出
	//但是这样只能发射一颗子弹
	//Shot s=null;
	//为了能够发出多发子弹，能够连发
	//做成集合的形式
	Vector<Shot>shots=new Vector<Shot>();
	Shot s=null;
	public Hero(int x,int y)
	{
		super(x,y);
		
	}
	//坦克具有发射子弹的功能
	//子弹在这里创建
	//在那里创建线程就在哪里
	public void shotEnemy()
	//每开火一次，s都只是一个引用，可以指向这个人也可以指向那个人
	//根据不同的情况，指向不同的子弹
	{
		
		//子弹也有方向
		
		switch(this.direct)
		{
		case 0:
			//创建一颗子弹
			s=new Shot(x+10,y,0);
			//把子弹加入向量
			shots.add(s);
			break;
		case 1:
			s=new Shot(x+30,y+10,1);
			shots.add(s);
			break;
		case 2:
			s=new Shot(x+10,y+30,2);
			shots.add(s);
			break;
		case 3:
			s=new Shot(x,y+10,3);
			shots.add(s);
			break;
		}
		
		
		//启动子弹线程
		//创建在哪，线程就在哪
		Thread t=new Thread(s);
		t.start();
	}
	
	//坦克的动作也是一种能力
	//坦克向上移动
	//封装起来
	//马上能达到速度的变化，而且可读性好
	public void moveUp()
	{
		this.y-=speed;
	}
	//向右移动
	public void moveRight()
	{
		this.x+=speed;
	}
	//向下移动
	public void moveDown()
	{
		this.y+=speed;
	}
	//向左移动
	public void moveLeft()
	{
		this.x-=speed;
	}
	
}