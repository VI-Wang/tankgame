package com.tets2;
import java.io.*;
import javax.sound.sampled.*;

public class TestSound {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AePlayWave apw=new AePlayWave("d:\\xx.wav");
		//�������̲߳�����
		apw.start();
	}

}


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