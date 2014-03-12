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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((node1 == null) ? 0 : node1.hashCode());
		result = prime * result + ((node2 == null) ? 0 : node2.hashCode());
		result = prime * result + ((weight == null) ? 0 : weight.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Edge other = (Edge) obj;
		if (node1 == null) {
			if (other.node1 != null)
				return false;
		} else if (!node1.equals(other.node1))
			return false;
		if (node2 == null) {
			if (other.node2 != null)
				return false;
		} else if (!node2.equals(other.node2))
			return false;
		if (weight == null) {
			if (other.weight != null)
				return false;
		} else if (!weight.equals(other.weight))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "Edge [node1=" + node1 + ", node2=" + node2 + ", weight="
				+ weight + "]";
	}
	
	
}
