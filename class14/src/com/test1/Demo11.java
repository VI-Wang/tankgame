package com.test1;

public class Demo11 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int b=10;
		AAA.add(1, b);
		
	}

}



class AAA
{
	private static int a=1;
	int b=0;
	public static void add(int a,int b)
	{
		int c=100;
		System.out.println(a+b+c);
		
	}
}