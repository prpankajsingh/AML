package in.ac.iitb.aml.test;

import in.ac.iitb.aml.JunctionTreeConstruction;
import in.ac.iitb.aml.MaximalCliqueDiscovery;
import in.ac.iitb.aml.model.Graph;

import java.util.HashMap;
import java.util.Map;

public class TestJTConstruction {

	public static void main(String[] args) {
		TestJTConstruction testJTConstruction = new TestJTConstruction();
		testJTConstruction.execute();
	}

	private void execute() {
		MaximalCliqueDiscovery maximalCliqueDiscovery = new MaximalCliqueDiscovery();
		Map<Integer,Long> cliqueNodesMapping = new HashMap<Integer,Long>();
		cliqueNodesMapping.put(0, 0xC0L);//true
		cliqueNodesMapping.put(1, 0x68L);//true
		cliqueNodesMapping.put(2, 0x38L);//true
		cliqueNodesMapping.put(3, 0x36L);//true
		cliqueNodesMapping.put(4, 0x23L);//true
		cliqueNodesMapping.put(5, 0x26L);//false
		cliqueNodesMapping.put(6, 0x6L);//false
		
		Map<Integer, Long> maximalCliqueOutput = maximalCliqueDiscovery.findMaximalClique(cliqueNodesMapping);
		Graph outputGraph = maximalCliqueDiscovery.constructGraph(maximalCliqueOutput);
		JunctionTreeConstruction junctionTreeConstruction = new JunctionTreeConstruction();
		outputGraph = junctionTreeConstruction.constructJuctionTree(outputGraph);
		
		System.out.println(outputGraph);
	}
}
