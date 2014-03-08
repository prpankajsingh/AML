/**
 * 
 */
package in.ac.iitb.aml;

import in.ac.iitb.aml.model.Edge;
import in.ac.iitb.aml.model.Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author ratish
 *
 */
public class MaximalCliqueDiscovery {
	
	public Map<Integer, Long> findMaximalClique(Map<Integer, Long> cliqueNodesMapping){

		Map<Integer,Boolean> maximalCliques = new HashMap<Integer,Boolean>();
		for (Integer key : cliqueNodesMapping.keySet()) {
			maximalCliques.put(key, Boolean.TRUE);
		}
		Set<Integer> cliqueIds = cliqueNodesMapping.keySet();
		for (int i = 0; i < cliqueIds.size(); i++) {
			for (int j = i+1; j < cliqueIds.size(); j++) {
				if((cliqueNodesMapping.get(i)|cliqueNodesMapping.get(j))== cliqueNodesMapping.get(j)){
					maximalCliques.put(i, Boolean.FALSE);
				}else if((cliqueNodesMapping.get(i)|cliqueNodesMapping.get(j))== cliqueNodesMapping.get(i)){
					maximalCliques.put(j, Boolean.FALSE);
				}
			}
		}
		
//		System.out.println(maximalCliques);
		Map<Integer,Long> maximalCliquesOutput = new HashMap<Integer, Long>();
		Set<Map.Entry<Integer, Boolean>> entrySet = maximalCliques.entrySet();
		for (Entry<Integer, Boolean> entry : entrySet) {
			if(entry.getValue()){
				maximalCliquesOutput.put(entry.getKey(), cliqueNodesMapping.get(entry.getKey()));
			}
		}
		return maximalCliquesOutput;
	}
	
	public Graph constructGraph(Map<Integer, Long> clique) {
		Graph graph = new Graph();
		List<Integer> nodes = new ArrayList<Integer>();
		nodes.addAll(clique.keySet());
		graph.setNodes(nodes);
		List<Edge> edges = new ArrayList<Edge>();
		Set<Integer> cliqueIds = clique.keySet();
		for (int i = 0; i < cliqueIds.size(); i++) {
			for (int j = i+1; j < cliqueIds.size(); j++) {
				long andOutput = clique.get(i) & clique.get(j);
				if(andOutput >0){
					edges.add(new Edge(i,j,Long.bitCount(andOutput)));
				}
			}
		}
		graph.setEdges(edges);
		return graph;
	}
}
