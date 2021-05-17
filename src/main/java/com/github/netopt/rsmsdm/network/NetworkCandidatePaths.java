package com.github.netopt.rsmsdm.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * List of all available candidate paths for each source-destination node pair in the graph.
 *
 * @author plechowicz
 * created on 18.02.19.
 */
public class NetworkCandidatePaths {

	/**
	 * List of candidate paths for given source-destination node pair. If source is s and destination is d,
	 * the list of all available candidate paths is candidatePathsForEachNodePair.get(s).get(d).
	 * If s == d, then the element contains Collections.emptyList().
	 */
	private final List<List<List<CandidatePath>>> candidatePathsForEachNodePair;

	private final int maxNumberOfCandidatePaths;

	public NetworkCandidatePaths(List<List<List<CandidatePath>>> candidatePathsForEachNodePair, int maxNumberOfCandidatePaths) {
		this.candidatePathsForEachNodePair = candidatePathsForEachNodePair;
		this.maxNumberOfCandidatePaths = maxNumberOfCandidatePaths;
	}

	public List<CandidatePath> getCandidatePathsForSourceDestination(int sourceNode, int destinationNode) {
		return candidatePathsForEachNodePair.get(sourceNode).get(destinationNode);
	}


	public int getMaxNumberOfCandidatePaths() {
		return maxNumberOfCandidatePaths;
	}
}
