/**
 * 
 */
package in.ac.iitb.aml.model;

import java.util.List;

/**
 * @author ratish
 *
 */
public class Graph {
	private List<Integer> nodes;
	private List<Edge> edges;
	public List<Integer> getNodes() {
		return nodes;
	}
	public void setNodes(List<Integer> nodes) {
		this.nodes = nodes;
	}
	public List<Edge> getEdges() {
		return edges;
	}
	public void setEdges(List<Edge> edges) {
		this.edges = edges;
	}
	@Override
	public String toString() {
		return "Graph [nodes=" + nodes + ", edges=" + edges + "]";
	}
	
	
}
