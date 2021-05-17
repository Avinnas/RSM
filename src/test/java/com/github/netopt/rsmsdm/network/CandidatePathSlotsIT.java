package com.github.netopt.rsmsdm.network;

import org.junit.Test;

/**
 * @author plechowicz
 * created on 18.02.19.
 */
public class CandidatePathSlotsIT {

	private String graphRelativePath = "./networks-data/US26/ss.net";
	private String slotsRelativePath = "./networks-data/US26/4mod/s1.spec";

	private String graphPath = getClass().getClassLoader().getResource(graphRelativePath).getPath();
	private String slotsPath = getClass().getClassLoader().getResource(slotsRelativePath).getPath();

	@Test
	public void canCreateSlotsForCandidatePaths() throws NetworkFilesReadingException {
		var graph = GraphFactory.createFromFile(graphPath);
		var slots = CandidatePathSlotsFactory.createSlotsForCandidatePathsFromFile(slotsPath, 30, graph.getNrOfNodes());
		System.out.println(slots.getRequiredNumberOfSlots(0, 0, 0, 100));
		System.out.println(slots.getRequiredNumberOfSlots(0, 1, 1, 100));
		System.out.println(slots.getRequiredNumberOfSlots(0, 1, 3, 100));
		System.out.println(slots.getRequiredNumberOfSlots(0, 1, 3, 101));
		System.out.println(slots.getRequiredNumberOfSlots(0, 1, 3, 149));
		System.out.println(slots.getRequiredNumberOfSlots(0, 1, 3, 150));
		System.out.println(slots.getRequiredNumberOfSlots(0, 1, 3, 151));
	}

}