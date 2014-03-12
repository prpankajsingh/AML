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
			Map<BitSet, Float> potential) {
		super();
		this.cliqueNodes = cliqueNodes;
		this.potential = potential;
	}
	private BitSet cliqueNodes;
	private Map<BitSet,Float> potential;
	public BitSet getCliqueNodes() {
		return cliqueNodes;
	}
	public void setCliqueNodes(BitSet cliqueNodes) {
		this.cliqueNodes = cliqueNodes;
	}
	public Map<BitSet, Float> getPotential() {
		return potential;
	}
	public void setPotential(Map<BitSet, Float> potential) {
		this.potential = potential;
	}
	
	
}
