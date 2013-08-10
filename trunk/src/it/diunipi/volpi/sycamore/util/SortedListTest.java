package it.diunipi.volpi.sycamore.util;

public class SortedListTest
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		SortedList<Integer> newList = new SortedList<Integer>();
		System.out.println(newList.toString());
		
		newList.add(10);
		newList.add(15);
		newList.add(0);
		newList.add(1);
		newList.add(16);
		newList.add(7);
		newList.add(15);
		
		System.out.println(newList.toString());
	}

}
