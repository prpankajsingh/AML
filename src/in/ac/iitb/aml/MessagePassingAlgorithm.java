/**
 * 
 */
package in.ac.iitb.aml;

import in.ac.iitb.aml.model.AdjacencyGraph;
import in.ac.iitb.aml.model.CliquePotential;
import in.ac.iitb.aml.model.Edge;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ratish
 *
 */
public class MessagePassingAlgorithm {
	
	private Map<Integer, CliquePotential> constructInitialPotentials(Map<Integer,BitSet> cliques, List<BitSet> edges, Map<BitSet,Map<BitSet,Float>> edgePotential){
		Map<Integer,CliquePotential> cliquePotentialMap = new HashMap<Integer, CliquePotential>();
		Map<Integer, List<BitSet>> edgeCliqueMapping = new HashMap<Integer, List<BitSet>>();
		for(BitSet edge: edges){
			for (Map.Entry<Integer, BitSet> clique : cliques.entrySet()) {
				BitSet originalValue = clique.getValue();
				BitSet newvalue = (BitSet) originalValue.clone();
				newvalue.or(edge);
				if(originalValue.equals(newvalue)){
					Integer key = clique.getKey();
					if(edgeCliqueMapping.containsKey(key)){
						edgeCliqueMapping.get(key).add(edge);
					}else{
						List<BitSet> bitSets = new ArrayList<BitSet>();
						bitSets.add(edge);
						edgeCliqueMapping.put(key, bitSets);
					}
					break;
				}
			}
		}
		
		for(Map.Entry<Integer, List<BitSet>> edgeCliqueMappingentry :edgeCliqueMapping.entrySet()){
			List<BitSet> edgesInClique = edgeCliqueMappingentry.getValue();
			
//			Map<BitSet,Float> product = edgePotential.get(edgesInClique.get(0));
			BitSet cliqueNodesProduct = edgesInClique.get(0);
			CliquePotential product = new CliquePotential(cliqueNodesProduct, edgePotential.get(cliqueNodesProduct));
			for(int i =1; i<edgesInClique.size(); i++){
				BitSet cliqueNodesOperand = edgesInClique.get(i);
				product = factorProduct(product, new CliquePotential(cliqueNodesOperand, edgePotential.get(cliqueNodesOperand))); 
			}
			cliquePotentialMap.put(edgeCliqueMappingentry.getKey(), product);
		}
		return cliquePotentialMap;
	}

	private CliquePotential factorProduct(CliquePotential operand1, CliquePotential operand2){
		Map<BitSet,Float> product = new HashMap<BitSet,Float>();	
		BitSet intersectionMeta = (BitSet) operand1.getCliqueNodes().clone();
		intersectionMeta.and(operand2.getCliqueNodes());
		for(Map.Entry<BitSet, Float> operand1Entry:operand1.getPotential().entrySet()){
			for(Map.Entry<BitSet, Float> operand2Entry:operand2.getPotential().entrySet()){
				BitSet productEntry = operand1Entry.getKey();
				productEntry.and(operand2Entry.getKey());
				
				BitSet intesectionMetaEntry1 = (BitSet) intersectionMeta.clone();
				intesectionMetaEntry1.and(operand1Entry.getKey());
				
				BitSet intesectionMetaEntry2 = (BitSet) intersectionMeta.clone();
				intesectionMetaEntry2.and(operand2Entry.getKey());
				
				if(productEntry.equals(intesectionMetaEntry1) && productEntry.equals(intesectionMetaEntry2)){
					BitSet unionOperand = (BitSet)operand1Entry.getKey();
					unionOperand.or(operand2Entry.getKey());
					product.put(unionOperand, operand1Entry.getValue()*operand2Entry.getValue());
				}
				
			}
		}
		BitSet union = (BitSet) operand1.getCliqueNodes().clone();
		union.or(operand2.getCliqueNodes());
		CliquePotential factorProduct = new CliquePotential(union, product);
		return factorProduct;
	}

	
	private void mpa(Map<Integer,CliquePotential> cliquePotentialMap, AdjacencyGraph originalGraph, AdjacencyGraph truncatedGraph,Map<Edge,CliquePotential> message){
		//find leaf node in graph
		int numNodes = truncatedGraph.getNumberNodes();
		int [][] graph = truncatedGraph.getAdjacencyMatrix();
		int i=0;
		boolean found = false;
		outerloop:for(; i<numNodes; i++){
			int sum = 0;
			for(int j=0; j<numNodes; j++){
				sum += graph[i][j];
			}
			if(sum == 1){
				found = true;
				break outerloop;
			}
		}
		/*if(!found){
			return;	//break out of loop
		}*/
		//i contains leaf node
		int k=0;
		for(; k<numNodes; k++){
			if(graph[i][k]==1){
				break;
			}
		}
		//k contains node to which it is connected
		BitSet sepSet = (BitSet) cliquePotentialMap.get(i).getCliqueNodes().clone();
		sepSet.and(cliquePotentialMap.get(k).getCliqueNodes());
		
		BitSet marginalSet = (BitSet)cliquePotentialMap.get(i).getCliqueNodes().clone();
		marginalSet.andNot(sepSet);
		
		Map<BitSet,Float> cliquePotentialValues = cliquePotentialMap.get(i).getPotential();
		
		CliquePotential messagePotential = cliquePotentialMap.get(i);
		for(int j=0; j<numNodes; j++){
			if(message.containsKey(new Edge(j,i,1))){
				messagePotential = factorProduct(messagePotential, message.get(new Edge(j,i,1)));
			}
		}
		marginalize(message, i, k, marginalSet, sepSet, cliquePotentialValues);
		
		for(int j=0; j<numNodes; j++){
			graph[i][j]=0;
			graph[j][i]=0;
		}
		truncatedGraph.setAdjacencyMatrix(graph);
		mpa(cliquePotentialMap, originalGraph, truncatedGraph, message);
	}

	private void marginalize(Map<Edge, CliquePotential> message, int i,
			int k, BitSet marginalSet, BitSet sepSet, Map<BitSet, Float> cliquePotentialValues) {
		Map<BitSet,Float> messagePotentialValues = new HashMap<BitSet, Float>(); 
		for(Map.Entry<BitSet, Float> cliquePotentialEntry: cliquePotentialValues.entrySet()){
			BitSet messageKey = (BitSet) cliquePotentialEntry.getKey().clone();
			messageKey.andNot(marginalSet);
			if(messagePotentialValues.containsKey(messageKey)){
				messagePotentialValues.put(messageKey, messagePotentialValues.get(messageKey) + cliquePotentialEntry.getValue());
			}else{
				messagePotentialValues.put(messageKey, cliquePotentialEntry.getValue());
			}
		}
		
		Edge edge = new Edge(i, k, 1);
		
		CliquePotential messageValue = new CliquePotential(sepSet, messagePotentialValues);
		message.put(edge, messageValue);
	}
	
}
