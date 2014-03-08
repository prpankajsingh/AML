/**
 * 
 */
package in.ac.iitb.aml;

import in.ac.iitb.aml.model.Edge;
import in.ac.iitb.aml.model.EdgeComparator;
import in.ac.iitb.aml.model.Graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author ratish
 *
 */
public class JunctionTreeConstruction {
	public static void main(String[] args) {
		JunctionTreeConstruction junctionTreeConstruction = new JunctionTreeConstruction();
		List<Integer> nodes = new ArrayList<Integer>();
		nodes.add(1);
		nodes.add(2);
		nodes.add(3);
		nodes.add(4);
		nodes.add(5);
		Graph graph = new Graph();
		graph.setNodes(nodes);
		List<Edge> edges = new ArrayList<Edge>();
		edges.add(new Edge(1,2,1));
		edges.add(new Edge(2,3,2));
		edges.add(new Edge(2,4,1));
		edges.add(new Edge(2,5,1));
		edges.add(new Edge(3,4,2));
		edges.add(new Edge(3,5,1));
		edges.add(new Edge(4,5,2));
		graph.setEdges(edges);
		Graph output = junctionTreeConstruction.constructJuctionTree(graph);
		System.out.println(output);
	}
	public Graph constructJuctionTree(Graph input){
		Graph output = new Graph();
		List<Edge> outputEdges = new ArrayList<Edge>();
		Set<Integer> nodesInput = new HashSet<Integer>();
		List<Edge> edges = input.getEdges();
		Collections.sort(edges, new EdgeComparator());
		for (Edge edge : edges) {
			if(!(nodesInput.contains(edge.getNode1()) && nodesInput.contains(edge.getNode2()))){
				nodesInput.add(edge.getNode1());
				nodesInput.add(edge.getNode2());
				outputEdges.add(edge);
			}
		}
		output.setNodes(input.getNodes());
		output.setEdges(outputEdges);
		return output;
	}
}
