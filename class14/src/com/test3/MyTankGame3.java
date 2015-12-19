/**
 * ���ܣ�̹����Ϸ��3.0��
 * 1.����̹��
 * 2.�ҵ�̹�˶�����������ʦ������
 * 3.�ӵ����������������������Լ����ܣ�����һ���̣߳��ӵ�Ҳ��һ���������ٶ�������ɫ����״��
 * 4.�ӵ������������������5�ţ�
 * 5.дһ��ר���ж��ӵ���û�л��е���̹�˵ĺ���
 * 6.ʲô�ط����øú�������Ҫ��ʱȥ�ж��ӵ���û�л���̹�ˣ�������run���ж�,����hit��
 * 7.��ըЧ��������ͼƬ˲���л���
 * 8.��ֹ�ص��˶�(����һ������̹���Լ��ж��Լ���û�кͶ�����ײ���������Լ���һ������
 * ��֮ǰ��������һ�ֱ�������������Ա����з�panel��������û����ײ���Լ�������)
 * 9.���Էֹأ���ͣ�ͼ���*
 * ��һ����˸��panel���յģ���ʾ��
 * �û������ͣ���ӵ���̹���ٶ�Ϊ0��̹�˷��򲻱�
 * 10.��¼�ɼ�*
 * ���ļ�������дһ����¼�࣬��ɶ���Ҽ�¼
 * ����ɱ��湲�����˶���������̹�˵Ĺ���
 * �����˳������Լ�¼��ʱ����̹�����꣬���ҿ��Իָ�
 * 11.java��β��������ļ�* 
 * �����ļ�296��
 * 
 * 12.�����ս��һ����ͬһ����Ϸ��
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
	
	//����һ����ʼ���
	MyStartPanel msp=null;
	
	
	//��������Ҫ�Ĳ˵�
	JMenuBar jmb=null;
	//��ʼ��Ϸ
	JMenu jm1=null;
	JMenuItem jmi1=null;
	//�˳���Ϸ
	JMenuItem jmi2=null;
	//�����˳�
	JMenuItem jmi3=null;
	//������һ����
	JMenuItem jmi4=null;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MyTankGame3 mtg=new MyTankGame3();
	}
	//���캯��
	public MyTankGame3()
	{
		//�����˵����˵�ѡ��
		jmb=new JMenuBar();
		jm1=new JMenu("��Ϸ(G)");
		//�������Ƿ�
		jm1.setMnemonic('G');
		jmi1=new JMenuItem("��ʼ����Ϸ(N)");
		jmi2=new JMenuItem("�˳���Ϸ(E)");
		jmi3=new JMenuItem("�����˳�(C)");
		jmi4=new JMenuItem("�����Ͼ���Ϸ(S)");
		jmi1.setMnemonic('N');
		jmi2.setMnemonic('E');
		//ע�����
		jmi4.addActionListener(this);
		jmi4.setActionCommand("continueGame");
		jmi3.addActionListener(this);
		jmi3.setActionCommand("saveExit");
		jmi2.addActionListener(this);
		jmi2.setActionCommand("exit");
		jmi1.addActionListener(this);
		jmi1.setActionCommand("newgame");
		//��jmi1��Ӧ
		jm1.add(jmi1);
		jm1.add(jmi2);
		jm1.add(jmi3);
		jm1.add(jmi4);
		jmb.add(jm1);
		
		
		msp=new MyStartPanel();
		Thread t=new Thread(msp);
		t.start();
		//�ѿ�ʼ�����ӽ�ȥ
		this.add(msp);
		this.setJMenuBar(jmb);
		this.setSize(600, 500);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//���û���ͬ�ĵ��������ͬ�Ĵ���
		if(e.getActionCommand().equals("newgame"))
		{
			
			//����ս�����
			mp=new MyPanel("newGame");
			//����mp�߳�
			Thread t=new Thread(mp);
			t.start();
			
			//Ҫ�Ѿɵ�ɾ��
			this.remove(msp);
			this.add(mp);
			//ע�����
			this.addKeyListener(mp);
			//��ʾ��ˢ��JFrame
			this.setVisible(true);
		}else if(e.getActionCommand().equals("exit"))
		{
			//�û�������˳�ϵͳ�Ĳ˵�
			//������ٵ�������������
			//����recorder��д����װ˼��
			//����ɶ��Ժ�ǿ
			Recorder.keepRecording();
			
			System.exit(0);
		}//�Դ����˳�������
		else if(e.getActionCommand().equals("saveExit"))
		{
			//�ܶ๤��
			
			Recorder.setEts(mp.ets);
			//1.������ٵ��˵�����������λ��
			Recorder.keepRecAndEnemyTank();
			
			//�����˳���-1���쳣�˳���
			System.exit(0);
		}else if(e.getActionCommand().equals("continueGame"))
		{
			//Ҫ�ָ�����̹������������Ϣ
			//�ȶ�ȡ
			//���ǲ�֪���ж��ٵ���̹��
			//����ͷ��Ϊ����һ�������꣬�����꣬�ӷ��򹹳�һ���㣬����һ��̹��
			//����ս�����
			
			mp=new MyPanel("continue");
			
			
			//����mp�߳�
			Thread t=new Thread(mp);
			t.start();
			
			//Ҫ�Ѿɵ�ɾ��
			this.remove(msp);
			this.add(mp);
			//ע�����
			this.addKeyListener(mp);
			//��ʾ��ˢ��JFrame
			this.setVisible(true);
		
		
		}
	}
}
//����һ����ʾ����
class MyStartPanel extends JPanel implements Runnable
{
	
	int times=0;
	public void paint(Graphics g)
	{
		super.paint(g);
		g.fillRect(0, 0, 400, 300);
		
		if(times%2==0)
		{
			//��������
			Font myFont=new Font("������κ",Font.BOLD,30);
			g.setFont(myFont);
			g.setColor(Color.yellow);
			//��ʾ��Ϣ
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
			//�ػ�
			this.repaint();
		}
	}
}


//�ҵ����(����廭̹��)
class MyPanel extends JPanel implements KeyListener,Runnable
{
	//̹�����������ģ�
	//̹��������Panel��
	//����̹���������һ����Ա����
	//����һ���ҵ�̹��
	Hero hero=null;
	
	//�ж��Ǽ����Ͼֵ���Ϸ����������Ϸ
	
	
	//�������̹�˲���
	Vector<EnemyTank> ets=new Vector<EnemyTank>();
	//���ʧȥ��һ����ô���Ͳ�������
	int enSize=3;
	Vector<Mode> modes=new Vector<Mode>();
	
	//����ը������
	Vector<Bomb> bombs=new Vector<Bomb>();
	//��������ͼƬ������ͼƬ��ϳ�һ��ը����ը��Ҳ�����������Ա��ܶ��
	Image image1=null;
	Image image2=null;
	Image image3=null;
	
	//����ͨ��hero���ʵ��������꣬set,get
	//���캯��
	public MyPanel(String flag)
	{
		//�ָ���¼
		Recorder.getRecording();
		hero=new Hero(100,200);
		
		//��ʼ�����˵�̹��
		if(flag.equals("newGame"))
		{
			for(int i=0;i<enSize;i++)
			{
				//����һ�����˵�̹�˶���
				EnemyTank et=new EnemyTank((i+1)*50,0);
				et.setColor(0);
				et.setDirect(2);
				//���뵽Vector
				ets.add(et);
			
				//��MyPanel�ĵ���̹�����������õ���̹��
				et.setEts(ets);
				Thread t=new Thread(et);
				t.start();
				
				//������̹�˼���һ���ӵ�
				Shot s=new Shot(et.x+10,et.y+30,2);
				et.shots.add(s);
				//shot��һ���߳�
				Thread t2=new Thread(s);
				t2.start();
			}
		}else{
			modes=Recorder.getModesAndEnNums();
			for(int i=0;i<modes.size();i++)
			{
				Mode mode=modes.get(i);
				//����һ�����˵�̹�˶���
				EnemyTank et=new EnemyTank(mode.x,mode.y);
				et.setColor(0);
				et.setDirect(mode.direct);
				//���뵽Vector
				ets.add(et);
			
				//��MyPanel�ĵ���̹�����������õ���̹��
				et.setEts(ets);
				Thread t=new Thread(et);
				t.start();
				
				//������̹�˼���һ���ӵ�
				Shot s=new Shot(et.x+10,et.y+30,2);
				et.shots.add(s);
				//shot��һ���߳�
				Thread t2=new Thread(s);
				t2.start();
			}
		}
		
		//��������ķ����ڵ�һ�λ��е�ʱ��ûɶЧ��
		//�����ñ�ķ���
		try {
			image1=ImageIO.read(new File("src/boom1.jpg"));
			image2=ImageIO.read(new File("src/boom2.jpg"));
			image3=ImageIO.read(new File("src/boom3.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//���ſ�ս����
		//AePlayWave apw=new AePlayWave("d:\\xx.wav");
		//apw.start();
		//��ʼ��ͼƬ
		//��ʱ������������
//		image1=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/boom1.jpg"));
//		image2=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/boom2.jpg"));
//		image3=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/boom3.jpg"));
	}
	//������ʾ��Ϣ
	
	public void showInfo(Graphics g)
	{
		//������ʾ��Ϣ̹�ˣ���̹�˲�����ս����
		this.drawTank(80, 330, g, 0, 0);
		//����
		g.setColor(Color.black);
		g.drawString(Recorder.getEnNum()+"", 110, 350);
		
		//�ҵ�̹�˵�����
		this.drawTank(150, 330, g, 0, 1);
		g.setColor(Color.black);
		g.drawString(Recorder.getMyLife()+"", 180, 350);
		
		
		//������ҵ��ܳɼ�
		g.setColor(Color.black);
		Font f=new Font("����",Font.BOLD,20);
		g.setFont(f);
		g.drawString("�����ܳɼ�", 420, 30);
		
		//��������̹��
		this.drawTank(420, 60, g, 0, 0);
		g.setColor(Color.black);
		g.drawString(Recorder.getAllEnNum()+"", 460, 80);
	
		//
	}
	
	
	
	//����paint
	
	//Ҫ���ӵ��������ͱ����һ��ʱ����ػ�
	//Ҫ�ػ�Ķ�������panel���淢����
	//�����ӵ�ʱ����Ļ�ڲ���ˢ��
	//����Ҫ��mypanelװ�߳�
	public void paint(Graphics g)
	{
		super.paint(g);
		g.fillRect(0,0,400,300);//������ɫ��ɫ
		//������ʾ��Ϣ
		this.showInfo(g);
		
		//�������滭̹�˵ĺ���
		//�����Լ���̹�˺��Լ����ӵ�
		if(hero.isLive==true)
		{
			this.drawTank(hero.getX(), hero.getY(), g, hero.getDirect(),1);
			//��shorts��ȡ��ÿ���ӵ���������
			for(int i=0;i<hero.shots.size();i++)
			//�����ӵ���������һ���ӵ���
			//Ҫ�ж���û���ӵ�
			//��̹�˸ճ�����û��J��ʱ������ӵ��ǿգ���������ͻ�������ӵ��������ڣ��ǻ����쳣 
			{
				//ȡ����������ǿת�����Ƿ���
				//�������ӵ�
				Shot myShot=hero.shots.get(i);
				if(myShot!=null&&myShot.isLive==true)
				{
					g.draw3DRect(myShot.x, myShot.y, 2, 2, false);
					//���ӵ�����������˼���ǣ��ӵ������겻ͣ���޸�
				}
				
				if(myShot.isLive==false)
				{
					//��shorts������ȥ�����ӵ�
					hero.shots.remove(myShot);
					
				}
			}
		}
		
		//��������̹��
		for(int i=0;i<ets.size();i++)
		{
			EnemyTank et=ets.get(i);
			if(et.isLive==true)
			{
				//������̹��
				this.drawTank(et.getX(), et.getY(), g, et.getDirect(), 0);
				//�������ӵ�
				for(int k=0;k<et.shots.size();k++)
				{
					//ȡ���ӵ�
					Shot enemyShot=et.shots.get(k);
					if(enemyShot.isLive==true)
					{
						g.draw3DRect(enemyShot.x, enemyShot.y, 2, 2, false);
					}else{
						//������˵��ӵ�������Remove
						et.shots.remove(enemyShot);
					}
				}
			}
		}
		
		
		//����ը��
		for(int i=0;i<bombs.size();i++)
		{
			//ȡ��ը��
			Bomb bomb=bombs.get(i);
			//����ͨ��lifeDownչ��ͼƬ
			if(bomb.life>6)
			{
				//this�������panel���滭
				g.drawImage(image1,bomb.x,bomb.y,30,30,this);
			}else if(bomb.life>3)
			{
				g.drawImage(image2,bomb.x,bomb.y,30,30,this);
			}else{
				g.drawImage(image3,bomb.x,bomb.y,30,30,this);
			}
			//û��һ��������ֵ��С
			bomb.lifeDown();
			//���ը������ֵΪ0���ͰѸ�ը����������ȥ��
			if(bomb.life==0)
			{
				bombs.remove(bomb);
			}
		}
	}
	
	
	
	//�ж��ӵ��Ƿ���е���̹��
	
	public void hitEnemyTank()
	{
		//�ж��Ƿ���е���̹��
		//�кö�ö��ӵ��ö�ö�̹�ˣ���Ҫƥ��һ�¿�����û�л���
		for(int i=0;i<hero.shots.size();i++)
		{
			//ȡ���ӵ�
			Shot myShot=hero.shots.get(i);
			//�ж��ӵ��Ƿ����
			if(myShot.isLive==true)
			{
				//ȡ��ÿ������̹�ˣ���֮�ж�
				for(int j=0;j<ets.size();j++)
				{
					//ȡ��̹��
					EnemyTank et=ets.get(j);
					if(et.isLive==true)
					{
						this.hitTank(myShot, et);
					}
				}
			}
			
		}
		
		
	}
	//�жϵ����ӵ��Ƿ������
	public void hitMe()
	{
		//ȡ��ÿһ�����˵�̹��
		for(int i=0;i<this.ets.size();i++)
		{
			EnemyTank et=ets.get(i);
			
			//ȡ���ӵ�
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
	
	
	//дһ�������ж��ӵ��Ƿ����̹��
	public void hitTank(Shot s,Tank tank)
	{
		
		//�жϸ�̹�˵ķ���
		switch(tank.direct)
		{
		//����̹�����ϻ������£���״��һ����
		case 0:
		case 2:
			if(s.x>tank.x&&s.x<tank.x+20&&s.y>tank.y&&s.y<tank.y+30)
			{
				//����
				//�ӵ�������̹��Ҳ����
				s.isLive=false;
				tank.isLive=false;
				//�����ҵ�ս��
				Recorder.addRed(tank);
				//���ٵ�������
				Recorder.reduce(tank);
				//����һ��ը��������Vector
				Bomb bomb=new Bomb(tank.x,tank.y);
				bombs.add(bomb);
			}
			break;
		case 1:
		case 3:
			if(s.x>tank.x&&s.x<tank.x+30&&s.y>tank.y&&s.y<tank.y+20)
			{
				//����
				//�ӵ�������̹��Ҳ����
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
	
	//����̹�˵ĺ���(̹�˵ĺ������꣬���л���,̹�˷���,̹�������ǵ��˻�����)
	//��չ����
	//��̹�˵�ʱ��������������Ҫ������̹�˺��Լ�̹��д��һ��Ѵ��룬������װ
	public void drawTank(int x,int y,Graphics g,int direct,int type)
	{
		//�ж���ʲô���͵�̹��,�ı仭����ɫ
		switch(type)
		{
			case 0:
				g.setColor(Color.CYAN);
				break;
			case 1:
				g.setColor(Color.yellow);
				break;
		
		}
		//�жϷ���
		switch(direct)
		{
			//����
			case 0:
				//�����ҵ�̹��(��ʱ�ٷ�װ��һ������)
				//�ָ��һ����
				//1.�Ȼ���ߵľ���
				g.fill3DRect(x, y, 5, 30,false);
				//2.���ұ߾���
				g.fill3DRect(x+15,y, 5, 30,false);
				//�����м����
				g.fill3DRect(x+5, y+5, 10, 20,false);
				//4.����Բ��
				g.fillOval(x+5, y+10, 10, 10);
				//5.������
				g.drawLine(x+10, y+15, x+10,y);
				break;
			//��Ͳ����
			case 1:
				//��������ľ���
				g.fill3DRect(x, y, 30, 5, false);
				//��������ľ���
				g.fill3DRect(x, y+15, 30, 5, false);
				//�����м�ľ���
				g.fill3DRect(x+5, y+5, 20, 10, false);
				//����Բ��
				g.fillOval(x+10, y+5, 10, 10);
				//������
				g.drawLine(x+15, y+10, x+30, y+10);
				break;
			//����
			case 2:
				//1.�Ȼ���ߵľ���
				g.fill3DRect(x, y, 5, 30,false);
				//2.���ұ߾���
				g.fill3DRect(x+15,y, 5, 30,false);
				//�����м����
				g.fill3DRect(x+5, y+5, 10, 20,false);
				//4.����Բ��
				g.fillOval(x+5, y+10, 10, 10);
				//5.������
				g.drawLine(x+10, y+15, x+10,y+30);
				break;
			//����
			case 3:
				//��������ľ���
				g.fill3DRect(x, y, 30, 5, false);
				//��������ľ���
				g.fill3DRect(x, y+15, 30, 5, false);
				//�����м�ľ���
				g.fill3DRect(x+5, y+5, 20, 10, false);
				//����Բ��
				g.fillOval(x+10, y+5, 10, 10);
				//������
				g.drawLine(x+15, y+10, x, y+10);
				break;
				
		}
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	//�����´��� 
	//a��ʾ����s��ʾ���£�w��ʾ���ϣ�d��ʾ����
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode()==KeyEvent.VK_W)
		{
			//�����ҵ�̹�˵ķ���
			this.hero.setDirect(0);
			this.hero.moveUp();
			//һ����ʦ��������дthis.hero.y--,��ΪҪ���ٶȵ�ʱ��Ҫһ�����ģ��ܲ����
		}else if(e.getKeyCode()==KeyEvent.VK_D)
		{
			//����
			this.hero.setDirect(1);
			this.hero.moveRight();
		}else if(e.getKeyCode()==KeyEvent.VK_S)
		{
			//����
			this.hero.setDirect(2);
			this.hero.moveDown();
		}else if(e.getKeyCode()==KeyEvent.VK_A)
		{
			//����
			this.hero.setDirect(3);
			this.hero.moveLeft();
		} 
		
		//����һ����һ�߷��ӵ�
		//������������һ��д����Ϊelse if�����һ����ھͲ��жϵڶ������
		//Ϊ����һ����һ�߷��ӵ����ͷֿ�д
		if(e.getKeyCode()==KeyEvent.VK_J)
		{
			//�����ӵ�,��Ұ���J��
			//����5�Σ��Ͳ��ܴ�����
			
			if(this.hero.shots.size()<=4)
			{
				this.hero.shotEnemy();
				//�ͺ���һ������Ϊ��shotEnemy�ż���һ��1
				//���ڷ�������ӵ��Ժ�Ͳ��ܷ����ӵ��ˣ�����Ҫ��һ������Ĺ��ܣ����ǰ���������ӵ���������
			}
		}
		
		
		
		
		
		//�������»��ƴ���
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void run()
	{
		while(true)
		{//ÿ��100����ȥ�ػ�
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
