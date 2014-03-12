/**
 * 
 */
package in.ac.iitb.aml.model;

import java.util.BitSet;
import java.util.List;
import java.util.Map;

/**
 * @author ratish
 *
 */
public class CliquePotential {
	public CliquePotential(BitSet cliqueNodes,
			Map<List<Integer>, Float> potential) {
		super();
		this.cliqueNodes = cliqueNodes;
		this.potential = potential;
	}
	private BitSet cliqueNodes;
	private Map<List<Integer>,Float> potential;
	public BitSet getCliqueNodes() {
		return cliqueNodes;
	}
	public void setCliqueNodes(BitSet cliqueNodes) {
		this.cliqueNodes = cliqueNodes;
	}
	public Map<List<Integer>, Float> getPotential() {
		return potential;
	}
	public void setPotential(Map<List<Integer>, Float> potential) {
		this.potential = potential;
	}
	
	
}
