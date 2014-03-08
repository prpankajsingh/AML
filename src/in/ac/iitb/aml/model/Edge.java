/**
 * 
 */
package in.ac.iitb.aml.model;

/**
 * @author ratish
 *
 */
public class Edge {
	private Integer node1;
	private Integer node2;
	private Integer weight;
	
	public Edge() {
		// TODO Auto-generated constructor stub
	}
	
	
	public Edge(Integer node1, Integer node2, Integer weight) {
		super();
		this.node1 = node1;
		this.node2 = node2;
		this.weight = weight;
	}

	public Integer getNode1() {
		return node1;
	}
	public void setNode1(Integer node1) {
		this.node1 = node1;
	}
	public Integer getNode2() {
		return node2;
	}
	public void setNode2(Integer node2) {
		this.node2 = node2;
	}
	public Integer getWeight() {
		return weight;
	}
	public void setWeight(Integer weight) {
		this.weight = weight;
	}


	@Override
	public String toString() {
		return "Edge [node1=" + node1 + ", node2=" + node2 + ", weight="
				+ weight + "]";
	}
	
	
}
