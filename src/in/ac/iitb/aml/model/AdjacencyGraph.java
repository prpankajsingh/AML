/**
 * 
 */
package in.ac.iitb.aml.model;

/**
 * @author ratish
 *
 */
public class AdjacencyGraph {
	private int [][] adjacencyMatrix;
	private int numberNodes;
	
	public void setNumberNodes(int numberNodes) {
		this.numberNodes = numberNodes;
	}
	
	public int getNumberNodes() {
		return numberNodes;
	}
	
	public int[][] getAdjacencyMatrix() {
		return adjacencyMatrix;
	}
	
	public void setAdjacencyMatrix(int[][] adjacencyMatrix) {
		this.adjacencyMatrix = adjacencyMatrix;
	}
}
