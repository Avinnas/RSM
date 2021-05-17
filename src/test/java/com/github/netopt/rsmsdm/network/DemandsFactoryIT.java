package com.github.netopt.rsmsdm.network;

import org.junit.Test;

import java.util.List;

/**
 * @author plechowicz
 * created on 18.02.19.
 */
public class DemandsFactoryIT {

	private String graphRelativePath = "./networks-data/US26/ss.net";
	private String candidatePathsRelativePath = "./networks-data/US26/s.pat";
	private String slotsRelativePath = "./networks-data/US26/4mod/s1.spec";
	private String demandsRelativePath = "./networks-data/US26/dem/unicast/1_000_000---50-1000-50/00.dem";

	private String graphPath = getClass().getClassLoader().getResource(graphRelativePath).getPath();
	private String candidatePathsPath = getClass().getClassLoader().getResource(candidatePathsRelativePath).getPath();
	private String slotsPath = getClass().getClassLoader().getResource(slotsRelativePath).getPath();
	private String demandsPath = getClass().getClassLoader().getResource(demandsRelativePath).getPath();

	@Test
	public void canCreateUnicastDemandsFile() throws NetworkFilesReadingException {
		var graph = GraphFactory.createFromFile(graphPath);
		List<Demand> demands = DemandsFactory.createFromFile(demandsPath, candidatePathsPath, slotsPath, 100_000, 20, 30, graph);
		System.out.println(demands.size());
		System.out.println(demands.get(0).getSourceNode());
		System.out.println(demands.get(0).getDestinationNode());
		System.out.println(demands.get(0).getNrOfDemandCandidatePaths());
		System.out.println(demands.get(0).getVolume());
		System.out.println(demands.stream().mapToLong(Demand::getVolume).sum());
		System.out.println(demands.get(0).getDemandCandidatePaths().get(0).getNrOfSlices());
	}
}