/**
 * 
 */
package in.ac.iitb.aml.model;

import java.util.Comparator;

/**
 * @author ratish
 *
 */
public class EdgeComparator implements Comparator<Edge> {
	@Override
	public int compare(Edge o1, Edge o2) {
		return o2.getWeight().compareTo(o1.getWeight());
	}
}
