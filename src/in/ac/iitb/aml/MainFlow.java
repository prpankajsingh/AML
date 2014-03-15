package in.ac.iitb.aml;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.BitSet;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import in.ac.iitb.aml.model;


public class MainFlow {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line=null;
		int nodeCount = 0,edgeCount = 0;
		if((line=br.readLine()) != null )
		{
			nodeCount=Integer.parseInt(line);
		}
		if((line=br.readLine()) != null )
		{
			edgeCount=Integer.parseInt(line);
		}
		int[] node2CardinalityMap=new int[nodeCount];
		int c=nodeCount;
		while (c>0 )
		{
			line = br.readLine();
			String[] tmp = line.split(" ");    //Split space
			node2CardinalityMap[Integer.parseInt(tmp[0])]=Integer.parseInt(tmp[1]);
			c--;
		}
		// array of bitset of size as nodeCount
		BitSet adjArray[] = new BitSet[nodeCount];
		for (int i=0;i<nodeCount;i++)
		{
			 adjArray[i]=new BitSet(nodeCount);
		}
		  
		int[][] edgeMatrix=new int[nodeCount][nodeCount];
		c=edgeCount;
		
		
		while (c>0)
		{
			line = br.readLine();
			String[] tmp = line.split(" ");    //Split space
			int n1=Integer.parseInt(tmp[0]);
			int n2=Integer.parseInt(tmp[1]);
			edgeMatrix[n1][n2]=1;
			edgeMatrix[n2][n1]=1;
			
			adjArray[n1].set(n2);
			adjArray[n2].set(n1);
			c--;
		}
	TriangulatedGraph tG=new TriangulatedGraph(adjArray,edgeMatrix,node2CardinalityMap,nodeCount,edgeCount);
	if(tG.runTriangulation(0))
		{
			//succesfully run....
			tG.displayClique2Node();
		}
	else
	{
		//Uncessful
		System.out.println("UnsuccessFul... :( ");
	}
	}

}
