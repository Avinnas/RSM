package com.github.netopt.rsmsdm.network;

import java.util.List;

/**
 * @author plechowicz
 * created on 18.02.19.
 */
public class CandidatePathSlots {

	/**
	 * List of required slots for given source-destination node pair. If source is s and destination
	 * is d, and p is path id for corresponding nodes, the list of slots is given with
	 * getListOfSlots(s, d, p, bitrate), where bit-rate is assumed bit-rate of the request.
	 */
	private final List<List<List<List<Integer>>>> slotsForEachPathForEachNodePair;

	private int modulationInterval = 50; // the interval for which various number of subcarriers is assigned
	private final int maxNrOfCandidatePaths;

	public CandidatePathSlots(List<List<List<List<Integer>>>> slotsForEachPathForEachNodePair, int maxNrOfCandidatePaths) {
		this.slotsForEachPathForEachNodePair = slotsForEachPathForEachNodePair;
		this.maxNrOfCandidatePaths = maxNrOfCandidatePaths;
	}

	public void setModulationInterval(int modulationInterval) {
		this.modulationInterval = modulationInterval;
	}

	public Integer getRequiredNumberOfSlots(int sourceNode, int destinationNode, int candidatePath, int bitrate) {
		if (slotsForEachPathForEachNodePair.get(sourceNode).get(destinationNode).size() != 0) {
			return slotsForEachPathForEachNodePair.get(sourceNode).get(destinationNode).get(candidatePath).get(
					getSlotsIndexForVolume(bitrate)
			);
		} else {
			return -1;
		}

	}

	/**
	 * Calculates index of slots value in matrix for given volume based on
	 * modulation interval
	 *
	 * @param volume
	 * @return
	 */
	private int getSlotsIndexForVolume(final int volume) {
		return (volume - 1) / modulationInterval;
	}

	public int getMaxNrOfCandidatePaths() {
		return maxNrOfCandidatePaths;
	}
}
