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
import java.util.Set;

/**
 * @author ratish
 *
 */
public class MessagePassingAlgorithm {
	
	public Map<Integer, CliquePotential> constructInitialPotentials(Map<Integer,BitSet> cliques, List<BitSet> edges, Map<BitSet,Map<BitSet,Double>> edgePotential){
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
			
//			Map<BitSet,Double> product = edgePotential.get(edgesInClique.get(0));
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
		Map<BitSet,Double> product = new HashMap<BitSet,Double>();	
		BitSet intersectionMeta = (BitSet) operand1.getCliqueNodes().clone();
		intersectionMeta.and(operand2.getCliqueNodes());
		for(Map.Entry<BitSet, Double> operand1Entry:operand1.getPotential().entrySet()){
			for(Map.Entry<BitSet, Double> operand2Entry:operand2.getPotential().entrySet()){
//				BitSet productEntry = operand1Entry.getKey();
//				productEntry.and(operand2Entry.getKey());
				
				BitSet intesectionMetaEntry1 = (BitSet) intersectionMeta.clone();
				intesectionMetaEntry1.and(operand1Entry.getKey());
				
				BitSet intesectionMetaEntry2 = (BitSet) intersectionMeta.clone();
				intesectionMetaEntry2.and(operand2Entry.getKey());
				
//				if(productEntry.equals(intesectionMetaEntry1) && productEntry.equals(intesectionMetaEntry2)){
				if(intesectionMetaEntry1.equals(intesectionMetaEntry2)){
					BitSet unionOperand = (BitSet)operand1Entry.getKey().clone();
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

	
	public void computeMarginal(BitSet inputVariables,Map<Integer,CliquePotential> cliquePotentialMap, AdjacencyGraph truncatedGraph,Map<Edge,CliquePotential> message){
		//iterate through cliques
		//identify one clique as root node
		//run mpa with not touching root node
		//at final step, marginalize over remaining nodes in root clique

		Integer rootNode = null;
		for (Map.Entry<Integer, CliquePotential> clique : cliquePotentialMap.entrySet()) {
			BitSet originalValue = clique.getValue().getCliqueNodes();
			BitSet newvalue = (BitSet) originalValue.clone();
			newvalue.or(inputVariables);
			if(originalValue.equals(newvalue)){
				rootNode = clique.getKey();
			}
		}
		
		if(rootNode == null){
			throw new IllegalArgumentException("Nodes are not contained within a clique");
		}
		
		computeMarginal(inputVariables, rootNode, cliquePotentialMap, truncatedGraph, message);
		
	}
	
	private void computeMarginal(BitSet inputNode, Integer rootNode, Map<Integer,CliquePotential> cliquePotentialMap, AdjacencyGraph truncatedGraph,Map<Edge,CliquePotential> message){
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
			if(sum == 1 && i!= rootNode){
				found = true;
				break outerloop;
			}
		}
		
		//i contains leaf node
		
		BitSet marginalSet = null;

		if(!found){
			marginalSet = new BitSet(numNodes);
			marginalSet.or(cliquePotentialMap.get(rootNode).getCliqueNodes());
			marginalSet.andNot(inputNode);
			
			Map<BitSet, Double> cliquePotentialValues = computeFactorProduct(
					cliquePotentialMap, message, numNodes, rootNode).getPotential();
			Map<BitSet, Double> marginalOutput = marginalize(marginalSet, cliquePotentialValues);
			System.out.println("marginalOutput" +marginalOutput);
		}else{
			int k=0;
			for(; k<numNodes; k++){
				if(graph[i][k]==1){
					break;
				}
			}
			//k contains node to which it is connected
			BitSet sepSet = (BitSet) cliquePotentialMap.get(i).getCliqueNodes().clone();
			sepSet.and(cliquePotentialMap.get(k).getCliqueNodes());
			
			
			marginalSet = (BitSet)cliquePotentialMap.get(i).getCliqueNodes().clone();
			marginalSet.andNot(sepSet);
			
			Map<BitSet, Double> cliquePotentialValues = computeFactorProduct(
					cliquePotentialMap, message, numNodes, i).getPotential();
			marginalize(message, i, k, marginalSet, sepSet, cliquePotentialValues);
			
			for(int j=0; j<numNodes; j++){
				graph[i][j]=0;
				graph[j][i]=0;
			}
			truncatedGraph.setAdjacencyMatrix(graph);
			computeMarginal(inputNode,rootNode, cliquePotentialMap, truncatedGraph, message);
		}
	}
	
	public void computeNormalizationConstant(Map<Integer,CliquePotential> cliquePotentialMap, AdjacencyGraph truncatedGraph,Map<Edge,CliquePotential> message, Set<Integer> nodes){
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
				nodes.remove(i);
				found = true;
				break outerloop;
			}
		}
		
		//i contains leaf node
		
		BitSet marginalSet = null;
		if(!found){
			marginalSet = new BitSet(numNodes);
			marginalSet.flip(0, numNodes);
			
			Map<BitSet, Double> cliquePotentialValues = computeFactorProduct(
					cliquePotentialMap, message, numNodes, nodes.iterator().next()).getPotential();
			Double normalizationConstant = marginalize(marginalSet, cliquePotentialValues).get(new BitSet(numNodes));
			System.out.println("normalizationConstant "+normalizationConstant);
		}else{
		
			int k=0;
			for(; k<numNodes; k++){
				if(graph[i][k]==1){
					break;
				}
			}
			//k contains node to which it is connected
			BitSet sepSet = (BitSet) cliquePotentialMap.get(i).getCliqueNodes().clone();
			sepSet.and(cliquePotentialMap.get(k).getCliqueNodes());
			
			
			marginalSet = (BitSet)cliquePotentialMap.get(i).getCliqueNodes().clone();
			marginalSet.andNot(sepSet);
			
			Map<BitSet, Double> cliquePotentialValues = computeFactorProduct(
					cliquePotentialMap, message, numNodes, i).getPotential();
			marginalize(message, i, k, marginalSet, sepSet, cliquePotentialValues);
			
			for(int j=0; j<numNodes; j++){
				graph[i][j]=0;
				graph[j][i]=0;
			}
			truncatedGraph.setAdjacencyMatrix(graph);
			computeNormalizationConstant(cliquePotentialMap, truncatedGraph, message, nodes);
		}
	}

	private CliquePotential computeFactorProduct(
			Map<Integer, CliquePotential> cliquePotentialMap,
			Map<Edge, CliquePotential> message, int numNodes, int i) {
		Map<BitSet,Double> cliquePotentialValues = cliquePotentialMap.get(i).getPotential();
		
		CliquePotential messagePotential = cliquePotentialMap.get(i);
		for(int j=0; j<numNodes; j++){
			if(message.containsKey(new Edge(j,i,1))){
				messagePotential = factorProduct(messagePotential, message.get(new Edge(j,i,1)));
			}
		}
		return messagePotential;
	}

	private void marginalize(Map<Edge, CliquePotential> message, int i,
			int k, BitSet marginalSet, BitSet sepSet, Map<BitSet, Double> cliquePotentialValues) {
		Map<BitSet, Double> messagePotentialValues = marginalize(marginalSet,
				cliquePotentialValues);
		
		Edge edge = new Edge(i, k, 1);
		CliquePotential messageValue = new CliquePotential(sepSet, messagePotentialValues);
		System.out.println("delta "+i + " "+k + " "+messagePotentialValues);
		message.put(edge, messageValue);
	}

	private Map<BitSet, Double> marginalize(BitSet marginalSet,
			Map<BitSet, Double> cliquePotentialValues) {
		Map<BitSet,Double> messagePotentialValues = new HashMap<BitSet, Double>(); 
		for(Map.Entry<BitSet, Double> cliquePotentialEntry: cliquePotentialValues.entrySet()){
			BitSet messageKey = (BitSet) cliquePotentialEntry.getKey().clone();
			messageKey.andNot(marginalSet);
			if(messagePotentialValues.containsKey(messageKey)){
				messagePotentialValues.put(messageKey, messagePotentialValues.get(messageKey) + cliquePotentialEntry.getValue());
			}else{
				messagePotentialValues.put(messageKey, cliquePotentialEntry.getValue());
			}
		}
		return messagePotentialValues;
	}
	
}
