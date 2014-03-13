package in.ac.iitb.aml.test;

import in.ac.iitb.aml.JunctionTreeConstruction;
import in.ac.iitb.aml.MaximalCliqueDiscovery;
import in.ac.iitb.aml.MessagePassingAlgorithm;
import in.ac.iitb.aml.model.AdjacencyGraph;
import in.ac.iitb.aml.model.CliquePotential;
import in.ac.iitb.aml.model.Edge;
import in.ac.iitb.aml.model.Graph;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestJTConstruction {

	public static void main(String[] args) {
		TestJTConstruction testJTConstruction = new TestJTConstruction();
//		testJTConstruction.execute();
		testJTConstruction.testMpa();
	}

	private void execute() {
		MaximalCliqueDiscovery maximalCliqueDiscovery = new MaximalCliqueDiscovery();
		Map<Integer,Long> cliqueNodesMapping = new HashMap<Integer,Long>();
		cliqueNodesMapping.put(0, 0xC0L);//true
		cliqueNodesMapping.put(1, 0x68L);//true
		cliqueNodesMapping.put(2, 0x38L);//true
		cliqueNodesMapping.put(3, 0x36L);//true
		cliqueNodesMapping.put(4, 0x23L);//true
		cliqueNodesMapping.put(5, 0x26L);//false
		cliqueNodesMapping.put(6, 0x6L);//false
		
		Map<Integer, Long> maximalCliqueOutput = maximalCliqueDiscovery.findMaximalClique(cliqueNodesMapping);
		Graph outputGraph = maximalCliqueDiscovery.constructGraph(maximalCliqueOutput);
		JunctionTreeConstruction junctionTreeConstruction = new JunctionTreeConstruction();
		outputGraph = junctionTreeConstruction.constructJuctionTree(outputGraph);
		
		System.out.println(outputGraph);
	}
	
	private void testMpa(){
		MessagePassingAlgorithm messagePassingAlgorithm = new MessagePassingAlgorithm();
		Map<Integer,BitSet> cliques = new HashMap<Integer, BitSet>();
		BitSet abd = BitSet.valueOf(new byte[]{13}); //1101
		cliques.put(0, abd);
		BitSet bcd = BitSet.valueOf(new byte[]{7}); //0111
		cliques.put(1, bcd);
		List<BitSet> edges = new ArrayList<BitSet>();
		edges.add(BitSet.valueOf(new byte[]{5})); //0101
//		messagePassingAlgorithm.constructInitialPotentials(cliques, edges, edgePotential)
		Map<Integer,CliquePotential> cliquePotentialMap = new HashMap<Integer, CliquePotential>();
		HashMap<BitSet, Float> potential0 = new HashMap<BitSet,Float>();
		CliquePotential cliquePotential0 = new CliquePotential(abd, potential0);
//		potential0.put(key, value)
		potential0.put(BitSet.valueOf(new byte[]{0b0000}), 600000F);
		potential0.put(BitSet.valueOf(new byte[]{0b0001}), 300030F);
		potential0.put(BitSet.valueOf(new byte[]{0b0100}), 5000500F);
		potential0.put(BitSet.valueOf(new byte[]{0b0101}), 1000F);
		potential0.put(BitSet.valueOf(new byte[]{0b1000}), 200F);
		potential0.put(BitSet.valueOf(new byte[]{0b1001}), 1000100F);
		potential0.put(BitSet.valueOf(new byte[]{0b1100}), 100010F);
		potential0.put(BitSet.valueOf(new byte[]{0b1101}), 200000F);
		cliquePotentialMap.put(0, cliquePotential0);
		
		HashMap<BitSet, Float> potential1 = new HashMap<BitSet,Float>();
		CliquePotential cliquePotential1 = new CliquePotential(bcd, potential1);
		potential1.put(BitSet.valueOf(new byte[]{0b0000}), 300100F);
		potential1.put(BitSet.valueOf(new byte[]{0b0001}), 1300000F);
		potential1.put(BitSet.valueOf(new byte[]{0b0010}), 300100F);
		potential1.put(BitSet.valueOf(new byte[]{0b0011}), 130F);
		potential1.put(BitSet.valueOf(new byte[]{0b0100}), 510F);
		potential1.put(BitSet.valueOf(new byte[]{0b0101}), 100500F);
		potential1.put(BitSet.valueOf(new byte[]{0b0110}), 5100000F);
		potential1.put(BitSet.valueOf(new byte[]{0b0111}), 100500F);
		cliquePotentialMap.put(1, cliquePotential1);
		
		AdjacencyGraph originalGraph = new AdjacencyGraph();
		originalGraph.setNumberNodes(2);
		int [][] adjacencyMatrix = new int [2][2];
		adjacencyMatrix[0]= new int [] {0,1};
		adjacencyMatrix[1]= new int [] {1,0};
		originalGraph.setAdjacencyMatrix(adjacencyMatrix);
		
		Map<Edge,CliquePotential> message = new HashMap<Edge, CliquePotential>();
		messagePassingAlgorithm.computeMarginal(BitSet.valueOf(new byte[]{0b0110}), cliquePotentialMap, originalGraph, message);
	}
	
	private BitSet constructBitSet(byte [] bits){
		BitSet bitSet = new BitSet(4);
		for (byte b : bits) {
			bitSet.flip(b);	
		}
		return bitSet;
	}
}
