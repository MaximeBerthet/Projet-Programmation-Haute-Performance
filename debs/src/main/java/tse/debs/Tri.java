package tse.debs;

import java.util.ArrayList;

public class Tri {
	int[]L=new int[3];
	public Tri() {
		super();
		
	}
	public int[] Trier(ArrayList<Integer> tab){
		for ( int j=0; j<L.length;j++)
		{
			L[j]=0;
		}
		int M1=0;
		int M2=0;
		int M3=0;
		for ( int i=0; i<tab.size();i++)
		{
			if (tab.get(i)>M3)
			{
				if (tab.get(i)>M2)
				{
					if (tab.get(i)>M1)
					{
						M3=M2;
						L[2]=L[1];
						M2=M1;
						L[1]=L[0];
						M1=tab.get(i);
						L[0]=i;
					}
					else{
						M3=M2;
						L[2]=L[1];
						M2=tab.get(i);
						L[1]=i;
						
					}
				}
				else
				{
					M3=tab.get(i);
					L[2]=i;

				}
			}
		}
		return L;
		
	}
}
