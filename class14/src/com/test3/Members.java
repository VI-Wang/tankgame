//��Ϊ����̹�˻�����ƶ�������Ҫ�ѵ���̹�������߳�
//������̹�˴����ҵ�̹��
package com.test3;

import java.util.*;
import java.io.*;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

//������������
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
		//���ǻ���
		byte[] abData=new byte[1024];
		
		try{
			//ѭ�����ţ�������������ܴ�
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



//�ָ���
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


//��¼�࣬���Ա����������
class Recorder
{
	//��¼�����˶��ٵ���
	private static int allEnNum=0;
	//����static ��ԭ���ǣ��������ĸ��̣߳����˵��������ҵ��������Ƿ���ͬһ���ط���
		//���ü�¼ÿ���ж��ٵ���
	private static int enNum=20;
		//�������ж��ٿ��õ���
	private static int myLife=3;
	//���ļ��лָ���¼�㣬��Ϊ��֪���ж���̹�ˣ�����Ҫ��vector
	static Vector<Mode> modes=new Vector<Mode>();
	
	
	private static FileWriter fw=null;
	private static BufferedWriter bw=null;
	
	private static FileReader fr=null;
	private static BufferedReader br=null;
	
	//Ҫ֪���������̹�˵����
	private static Vector<EnemyTank> ets=new Vector<EnemyTank>();
	
	public static Vector<EnemyTank> getEts() {
		return ets;
	}
	public static void setEts(Vector<EnemyTank> ets) {
		Recorder.ets = ets;
		
	}

	//��ɶ�ȡ����
	//�ָ�
	public static Vector<Mode> getModesAndEnNums()
	{
		try{
			fr=new FileReader("d:\\myRecording.txt");
			br=new BufferedReader(fr);
			String n="";
			//�ȶ�ȡ��һ��
			n=br.readLine();
			//�ַ���ת��int
			allEnNum=Integer.parseInt(n);
			//�������¶�
			while((n=br.readLine())!=null)
			{
				//�����ж��ٿո񷵻�,�ֳ�������
				String[]xyz=n.split(" ");
				Mode mode=new Mode(Integer.parseInt(xyz[0]),Integer.parseInt(xyz[1]),Integer.parseInt(xyz[2]));
				modes.add(mode);
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally{
			try{
				//������ȹر�
				br.close();
				fr.close();
			}catch(Exception e2)
			{
				e2.printStackTrace();
			}
		}
		return modes;
	}
	//������ٵ��˵������͵���̹�����귽��
	public static void keepRecAndEnemyTank()
	{
		try{
			//�����ļ���
			fw=new FileWriter("d:\\myRecording.txt");
			bw=new BufferedWriter(fw);
			//��¼���ٶ��ٵ���̹��
			bw.write(allEnNum+"\r\n");
			//���浱ǰ���ŵĵ���̹�˵�����ͷ���
			//����
			for(int i=0;i<ets.size();i++)
			{
				//ȡ����һ��̹��
				EnemyTank et=ets.get(i);
				if(et.isLive==true)
				{
					//���Ųű���
					String record=et.x+" "+et.y+" "+et.direct;
					//д�뵽�ļ���
					bw.write(record+"\r\n");
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//�ر���
			try{
				//���ȹر�
				bw.close();
				fw.close();
			}catch(Exception e2)
			{
				e2.printStackTrace();
			}
		}
	}
	
	
	//���ļ��ж�ȡ��¼
	public static void getRecording()
	{
		try{
			fr=new FileReader("d:\\myRecording.txt");
			br=new BufferedReader(fr);
			String n=br.readLine();
			//�ַ���ת��int
			allEnNum=Integer.parseInt(n);
		
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally{
			try{
				//������ȹر�
				br.close();
				fr.close();
			}catch(Exception e2)
			{
				e2.printStackTrace();
			}
		}
	}
	
	
	
	
	//����һ��ٵ���̹���������浽�ļ���
	public static void keepRecording()
	{
		//���ļ���
		//�ַ���1234һ�����ı�
		try{
			//�����ļ���
			fw=new FileWriter("d:\\myRecording.txt");
			bw=new BufferedWriter(fw);
			
			bw.write(allEnNum+"\r\n");
			
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//�ر���
			try{
				//���ȹر�
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
	//ÿ�������˶��ٵ���
	public static void reduce(Tank tank)
	{
		//���е���
		if(tank.getType()==0)
		{
			enNum--;
		}
		//������
		if(tank.getType()==1)
		{
			myLife--;
		}
	}
	//�ܹ�������ٵ���
	public static void addRed(Tank tank)
	{
		if(tank.getType()==0)
		{
			allEnNum++;
		}
	}
	
	
}


//ը���ࣨը�������ƶ������Բ��������̣߳�
class Bomb
{
	//����ը������
	int x;
	int y;
	//ը������������
	int life=9;
	boolean isLive=true;
	public Bomb(int x,int y)
	{
		this.x=x;
		this.y=y;
	}
	
	
	//���������̣���������
	public void lifeDown()
	{
		if(life>0)
		{
			life--;
		}else{
			this.isLive=false;
			//���Կ�һ�»�Ҫ��Ҫ��
		}
	}
}



//�ӵ�������̹�˵�
//�ӵ�һ�������������Ǿ͸����ӵ��ķ��򲻶��޸�����
class Shot implements Runnable
{
	int x;
	int y;
	int direct;
	int speed=2;
	//�Ƿ񻹻���
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
				//����
				y-=speed;
				break;
			case 1:
				//����
				x+=speed;
				break;
			case 2:
				y+=speed;
				break;
			case 3:
				x-=speed;
				break;
			}
		
			
		
			//�жϸ��ӵ��Ƿ�������Ե
			if(x<0||x>400||y<0||y>300)
			{
				//false�Ͳ��ử��
				this.isLive=false;
				break;
			}
		
		}
		
		//�ӵ���ʱ������
		//�ӵ�������Ե���߱߿������
	}
}



//̹����
class Tank
{
	
	//xΪ̹�˺�����
	
	int x=0;
	//̹��������
	int y=0;
	boolean isLive=true;
	//̹�˷���
	//0��ʾ�ϣ�1��ʾ�ң�2��ʾ�£�3��ʾ��
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

	//���������̹�˵��ٶ�
	//�����һ���ٶȾͺܷ���
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


//���˵�̹��
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
	//����һ�����������Է��ʵ�mypanel�����е���̹��
	Vector <EnemyTank> ets=new Vector<EnemyTank>();
	
	//����һ�����������Դ�ŵ����ӵ�
	Vector <Shot> shots=new Vector<Shot>();
	//��������ӵ�Ӧ���ڸոմ���̹�˺͵���̹���ӵ�������
	
	
	public EnemyTank(int x,int y)
	{
		super(x,y);
	}
	
	//�õ�myPanel�ĵ���̹������
	//�ǳ���Ҫ
	public void setEts(Vector<EnemyTank> vv)
	{
		this.ets=vv;
	}
		
	//�ж��Ƿ������˱�ĵ���̹��
	public boolean isTouchOtherEnemy()
	{
		boolean b=false;
		
		//��ô֪�������˱��˵�̹��
		switch(this.direct)
		{
		case 0:
			//�ҵ�̹������
			//ȡ�����еĵ���̹��
			for(int i=0;i<ets.size();i++)
			{
				//ȡ����һ��̹��
				EnemyTank et=ets.get(i);
				//��������Լ��������Լ����Լ���
				if(et!=this)
				{
					//������˵ķ��������»�������
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
				//ȡ����һ��̹��
				EnemyTank et=ets.get(i);
				//��������Լ��������Լ����Լ���
				if(et!=this)
				{
					//������˵ķ��������»�������
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
				//ȡ����һ��̹��
				EnemyTank et=ets.get(i);
				//��������Լ��������Լ����Լ���
				if(et!=this)
				{
					//������˵ķ��������»�������
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
			//����
			for(int i=0;i<ets.size();i++)
			{
				//ȡ����һ��̹��
				EnemyTank et=ets.get(i);
				//��������Լ��������Լ����Լ���
				if(et!=this)
				{
					//������˵ķ��������»�������
					if(et.direct==0||et.direct==2)
					{
						//�ҵ���һ��
						if(this.x>=et.x&&this.x<=et.x+20&&this.y>=et.y&&this.y<=et.y+30)
						{
							return b=true;
						}
						//�ҵ���һ��
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
	
//����̹��������ϵĻ�����Ҫ��û�������Լ����Ѳſ����˶�
	public void run()
	{
		
		while(true)
		{
			//Ҫ�õ���̹�˵����겻�ϱ任
			switch(this.direct)
			{
			//̹������������
			case 0:
				//��̹����������
				for(int i=0;i<30;i++)
				{
					if(y>0&&this.isTouchOtherEnemy()==false)
					{
						y-=speed;
					}
					//��һ����Ϣ��
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			//����
			case 1:
				for(int i=0;i<30;i++)
				{
					//��֤̹�˲����߽�
					if(x<400&&this.isTouchOtherEnemy()==false)
					{
						x+=speed;
					}
					//��һ����Ϣ��
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
					//��һ����Ϣ��
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
					//��һ����Ϣ��
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
							//û���ӵ�
							//���
							switch(direct)
							{
							case 0:
								//����һ���ӵ�
								s=new Shot(x+10,y,0);
								//���ӵ���������
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
							//�����ӵ��߳�
							Thread t=new Thread(s);
							t.start();
					}
				}
			}
			
			
			
			//����Ҫ����仯
			//��̹���������һ���µķ���
			//������̹�˲�����������Ҳ����ר�ų�������
			this.direct=(int)(Math.random()*4);
			
			//�жϵ���̹���Ƿ�����
			if(this.isLive==false)
			{
				//��̹���������˳��߳�
				//���ֻд��һ���Ⱥţ�����̻߳��Ϊ��ʬ�̣߳�
				//������뵽break������߳���Զ���ţ�����û���ã���ռ����Դ
				break;
			}
			
			
			
			
		}
	}
}


//�ҵ�̹����Ӣ��
class Hero extends Tank
{
	int type=1;
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	
	//�ӵ������ⷢ��
	//��������ֻ�ܷ���һ���ӵ�
	//Shot s=null;
	//Ϊ���ܹ������෢�ӵ����ܹ�����
	//���ɼ��ϵ���ʽ
	Vector<Shot>shots=new Vector<Shot>();
	Shot s=null;
	public Hero(int x,int y)
	{
		super(x,y);
		
	}
	//̹�˾��з����ӵ��Ĺ���
	//�ӵ������ﴴ��
	//�����ﴴ���߳̾�������
	public void shotEnemy()
	//ÿ����һ�Σ�s��ֻ��һ�����ã�����ָ�������Ҳ����ָ���Ǹ���
	//���ݲ�ͬ�������ָ��ͬ���ӵ�
	{
		
		//�ӵ�Ҳ�з���
		
		switch(this.direct)
		{
		case 0:
			//����һ���ӵ�
			s=new Shot(x+10,y,0);
			//���ӵ���������
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
		
		
		//�����ӵ��߳�
		//�������ģ��߳̾�����
		Thread t=new Thread(s);
		t.start();
	}
	
	//̹�˵Ķ���Ҳ��һ������
	//̹�������ƶ�
	//��װ����
	//�����ܴﵽ�ٶȵı仯�����ҿɶ��Ժ�
	public void moveUp()
	{
		this.y-=speed;
	}
	//�����ƶ�
	public void moveRight()
	{
		this.x+=speed;
	}
	//�����ƶ�
	public void moveDown()
	{
		this.y+=speed;
	}
	//�����ƶ�
	public void moveLeft()
	{
		this.x-=speed;
	}
	
}