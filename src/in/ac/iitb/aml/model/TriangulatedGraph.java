package in.ac.iitb.aml.model;

import java.math.BigInteger;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class TriangulatedGraph {
	//stores edge matrix as an 2D array. if there is an edge between i and j, then edgeMatrixTG[i][j]=edgeMatrixTG[j][i]=1
int [][] edgeMatrixTG;

// stores the cardinality of each node where index number represents node no.
int[] nodeCardinality;

// total no. of nodes
int nodeCount=0;

//total no. of edges before triangulation
int edgeCount=0;

// To store extra edge count that has been added as a part of triangulation
int extraEdgeCount=0;

//an efficient adjacency array. same info as edgeMatrixTG
BitSet[] adjArrayTG;

// Total cliques according to elimination order. Integer no. represents clique no while BitSet represents partcipating nodes. see it's use in display function.
Map<Integer,BitSet> clique2Node = new HashMap<Integer,BitSet>();

// total no. of currentCliqueCount
int currentCliqueCount=0;

/**
 * @param edgeMatrixTG
 * @param nodeCardinality
 * @param nodeCount
 * @param edgeCount
 */
public TriangulatedGraph(BitSet[] adjArray,int[][] edgeMatrixTG, int[] nodeCardinality, int nodeCount,int edgeCount) {
	super();
	this.edgeMatrixTG = edgeMatrixTG;
	this.nodeCardinality = nodeCardinality;
	this.nodeCount=nodeCount;
	this.edgeCount=edgeCount;
	this.adjArrayTG=adjArray;
}

public boolean runTriangulation(int method) {
	boolean success=false;
	switch(method)
	{
	//min degree
	case 1:
		break;
	//min-weight
	case 2:
		break;
	//min-fill
	case 3:
		break;
	//min-weight fill
	default:
		success=runMinWeightFill();
	}
	return success;
}

private boolean runMinWeightFill() {
	// TODO Auto-generated method stub
	//bitset of all active nodes
	BitSet activeNodeSet = new BitSet(this.nodeCount);
	//Initially all nodes are active
	activeNodeSet.flip(0,nodeCount);
	
	//loop until no nodes are active
	while (activeNodeSet.cardinality()>0)
	{
		//System.out.println("INSIDE runMinWeightFill..........active set "+activeNodeSet.toString());
		//get next node to be eliminated based on min-weight fill score
		int n = getNextNode(activeNodeSet);
		System.out.println("Current Eliminating Node is " + n);
		//add edge to its active neighbours 
		extraEdgeCount+=addedge(activeNodeSet,n); 
		//check this method... It adds maxclique for current elimination node
		addMaxClique(activeNodeSet,n);
		activeNodeSet.set(n, false);
		
	}
	
	return true;
	
}

private void addMaxClique(BitSet b,int n) {
	// TODO Auto-generated method stub
	BitSet tmp = new BitSet(nodeCount);
	tmp.set(0, nodeCount, true);
	tmp.and(b);
	tmp.and(this.adjArrayTG[n]);
	tmp.set(n);
	this.clique2Node.put(currentCliqueCount, tmp);
	currentCliqueCount++;
	System.out.println("Current clique"+tmp.toString());
}

private int getNextNode(BitSet b) {
	// TODO Auto-generated method stub
	//use activeNodeSet to decide next node
	// put negative for each score... hence maximum feasible score would be 0.
	long minScore=0;
	boolean isScoreZero=false;
	int minScoreIndex=-1;
	System.out.println("and current active set is "+b.toString());
	// Just for testing next node is sequentially chosen i.e. no heuristic...
	//TODO .. implement heuristic
	//comment below line after implemnting heuristic
	for (int i = b.nextSetBit(0); i >= 0; i = b.nextSetBit(i+1)) {
		System.out.println("index to be checked"+i);
		long score=getScore(b,i);
		System.out.println("score for index "+i+" is "+score);
		if (score==0)
		{
			isScoreZero=true;
			minScore=0;
			minScoreIndex=i;
			break;
		}
		else if(!isScoreZero)
		{
			if(minScore==0)
			{
				minScore=score;
				minScoreIndex=i;
			}
			else if (score < minScore)
			{
				minScore=score;
				minScoreIndex=i;
			}
		}
			
	}
	
	return minScoreIndex;
}

private long getScore(BitSet b,int k) {
	// TODO Auto-generated method stub
	long s=0;
	// store temporary adjacemcy matrix
	BitSet adjArraytmp[] = new BitSet[nodeCount];
	for (int i=0;i<nodeCount;i++)
	{
		 adjArraytmp[i]=new BitSet(nodeCount);
		 adjArraytmp[i]=(BitSet) adjArrayTG[i].clone();
	}
	
	BitSet tmp = new BitSet(nodeCount);
	tmp.flip(0, nodeCount);
	tmp.and(b);
	tmp.and(adjArraytmp[k]);
	//TODO..Now add edges such that tmp are completely connected and assign value to extEdgeCount
	for (int i = tmp.nextSetBit(0); i >= 0; i = tmp.nextSetBit(i+1)) {
	     // operate on index i here
		BitSet tmp2=new BitSet();
		tmp2=(BitSet) adjArraytmp[i].clone();
		tmp2.set(i);
		//values which are in tmp but not in tmp2... means missing edges
		BitSet tmp3=new BitSet();
		tmp3=(BitSet) tmp.clone();
		
		tmp3.andNot(tmp2);
		System.out.println("for vertex "+ k+ "tmp2 is "+tmp2.toString()+ "and tmp3 is "+tmp3.toString() );
		//add edges from i where tmp3 is set
		for (int j = tmp3.nextSetBit(0); j >= 0; j = tmp3.nextSetBit(j+1)) {
			s+=(this.nodeCardinality[i]*this.nodeCardinality[j])/2;
			System.out.println("for vertex "+ k+"missing edges are "+i+" and "+j);
			//adjArraytmp[i].set(j);
			//adjArraytmp[j].set(i);
		}
	 }

	return s;
}

private int addedge(BitSet b,int n) {
	// TODO Auto-generated method stub
	// use activeNodeSet to add edge and return no. of. newly added edges
	int extEdgeCount=0;
	BitSet tmp = new BitSet(nodeCount);
	tmp.flip(0, nodeCount);
	tmp.and(b);
	tmp.and(this.adjArrayTG[n]);
	//TODO..Now add edges such that tmp are completely connected and assign value to extEdgeCount
	for (int i = tmp.nextSetBit(0); i >= 0; i = tmp.nextSetBit(i+1)) {
	     // operate on index i here
		BitSet tmp2=new BitSet();
		tmp2=(BitSet) adjArrayTG[i].clone();
		tmp2.set(i);
		//values which are in tmp but not in tmp2... means missing edges
		BitSet tmp3=new BitSet();
		tmp3=(BitSet) tmp.clone();
		
		tmp3.andNot(tmp2);
		extEdgeCount+=tmp3.cardinality();
		System.out.println("Adding Edges for node "+n+"edge count is "+ extEdgeCount+"value of tmp3 for "+i+" is "+tmp3.toString());
		//add edges from i where tmp3 is set
		for (int j = tmp3.nextSetBit(0); j >= 0; j = tmp3.nextSetBit(j+1)) {
			adjArrayTG[i].set(j);
			adjArrayTG[j].set(i);
			edgeMatrixTG[i][j]=1;
			edgeMatrixTG[j][i]=1;
		}
	 }
	System.out.println("Extra edge at this step is "+extEdgeCount);
	return extEdgeCount;
}

public void displayClique2Node()
{
	System.out.println("Extra Edge Added : "+this.extraEdgeCount);
	for (int i=0;i<this.currentCliqueCount;i++)
	{
		System.out.println("Clique No. "+i+" and participating nodes"+clique2Node.get(i).toString());
	}
	
}
}
