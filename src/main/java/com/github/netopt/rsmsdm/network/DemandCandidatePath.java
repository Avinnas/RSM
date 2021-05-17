package com.github.netopt.rsmsdm.network;

/**
 * @author plechowicz
 * created on 18.02.19.
 */
public class DemandCandidatePath {

	private final CandidatePath candidatePath;
	private final int nrOfSlices;

	public DemandCandidatePath(CandidatePath candidatePath, int nrOfSlices) {
		this.candidatePath = candidatePath;
		this.nrOfSlices = nrOfSlices;
	}

	public int getNrOfSlices() {
		return nrOfSlices;
	}

	public CandidatePath getCandidatePath() {
		return candidatePath;
	}
}
