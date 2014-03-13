/**
 * 
 */
package in.ac.iitb.aml.model;

import java.util.BitSet;
import java.util.Map;

/**
 * @author ratish
 *
 */
public class CliquePotential {
	public CliquePotential(BitSet cliqueNodes,
			Map<BitSet, Double> potential) {
		super();
		this.cliqueNodes = cliqueNodes;
		this.potential = potential;
	}
	private BitSet cliqueNodes;
	private Map<BitSet,Double> potential;
	public BitSet getCliqueNodes() {
		return cliqueNodes;
	}
	public void setCliqueNodes(BitSet cliqueNodes) {
		this.cliqueNodes = cliqueNodes;
	}
	public Map<BitSet, Double> getPotential() {
		return potential;
	}
	public void setPotential(Map<BitSet, Double> potential) {
		this.potential = potential;
	}
	
	
}
