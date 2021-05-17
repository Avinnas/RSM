package com.github.netopt.rsmsdm.network;

import org.junit.Test;

/**
 * @author plechowicz
 * created on 18.02.19.
 */
public class NetworkCandidatePathsIT {

	private String graphRelativePath = "./networks-data/US26/ss.net";
	private String candidatePathsRelativePath = "./networks-data/US26/s.pat";

	private String graphPath = getClass().getClassLoader().getResource(graphRelativePath).getPath();
	private String candidatePathsPath = getClass().getClassLoader().getResource(candidatePathsRelativePath).getPath();

	@Test
	public void canCreateCandidatePathsForNetwork() throws NetworkFilesReadingException {
		var graph = GraphFactory.createFromFile(graphPath);
		var candidatePathsForNetwork = NetworkCandidatePathsFactory.createCandidatePathsForNetworkFromFile(
				candidatePathsPath, 30, graph
		);
		System.out.println(candidatePathsForNetwork.getCandidatePathsForSourceDestination(0, 0));
		System.out.println(candidatePathsForNetwork.getCandidatePathsForSourceDestination(0, 1));
	}
}