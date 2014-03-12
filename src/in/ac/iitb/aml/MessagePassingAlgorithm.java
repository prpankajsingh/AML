/**
 * 
 */
package in.ac.iitb.aml;

import in.ac.iitb.aml.model.AdjacencyGraph;
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
	
	private void constructInitialPotentials(Map<Integer,BitSet> cliques, List<BitSet> edges, Map<BitSet,Map<List<Integer>,Float>> edgePotential){
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
			
			Map<List<Integer>,Float> product = edgePotential.get(edgesInClique.get(0));
			for(int i =1; i<edgesInClique.size(); i++){
				product = factorProduct(product, edgesInClique.get(i)); 
			}
			 
			
		}
	}
	
	private Map<List<Integer>, Float> factorProduct(
			Map<List<Integer>, Float> product, BitSet bitSet) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private Map<BitSet,Float> factorProduct(Map<BitSet,Float> operand1, Map<BitSet,Float> operand2, BitSet operand1Meta, BitSet operand2Meta, BitSet cardinality ){
		Map<BitSet,Float> product = new HashMap<BitSet,Float>();	
		BitSet intersectionMeta = (BitSet) operand1Meta.clone();
		intersectionMeta.and(operand2Meta);
		for(Map.Entry<BitSet, Float> operand1Entry:operand1.entrySet()){
			for(Map.Entry<BitSet, Float> operand2Entry:operand2.entrySet()){
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
		return product;
	}

	
	private void marginalization(Map<Integer,Map<BitSet,Float>> cliquePotential, AdjacencyGraph adjacencyGraph, Map<Integer,BitSet> clique, Map<Edge,Map<BitSet,Float>> message){
		//find leaf node in graph
		int numNodes = adjacencyGraph.getNumberNodes();
		int [][] graph = adjacencyGraph.getAdjacencyMatrix();
		int i=0;
		outerloop:for(; i<numNodes; i++){
			int sum = 0;
			for(int j=0; j<numNodes; j++){
				sum += graph[i][j];
			}
			if(sum == 1){
				break outerloop;
			}
		}
		//i contains leaf node
		int k=0;
		for(; k<numNodes; k++){
			if(graph[i][k]==1){
				break;
			}
		}
		//k contains node to which it is connected
		BitSet sepSet = (BitSet) clique.get(i).clone();
		sepSet.and(clique.get(k));
		
		BitSet marginalSet = (BitSet)clique.get(i).clone();
		marginalSet.andNot(sepSet);
		
		Map<BitSet,Float> cliquePotentialValues = cliquePotential.get(i);
		marginalize(message, i, k, marginalSet, cliquePotentialValues);
	}

	private void marginalize(Map<Edge, Map<BitSet, Float>> message, int i,
			int k, BitSet marginalSet, Map<BitSet, Float> cliquePotentialValues) {
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
		message.put(edge, messagePotentialValues);
	}
	
}
