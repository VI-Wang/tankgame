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
		//启动该线程并播放
		apw.start();
	}

}


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