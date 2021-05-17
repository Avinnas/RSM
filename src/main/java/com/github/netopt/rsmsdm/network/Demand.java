package com.github.netopt.rsmsdm.network;

import com.github.netopt.rsmsdm.spectrum.Channel;

import java.util.List;
import java.util.Objects;

/**
 * @author plechowicz
 * created on 18.02.19.
 */
public class Demand {

	private final Integer name;
	private final long volume;
	private final int sourceNode;
	private final int destinationNode;

	private final List<DemandCandidatePath> candidatePaths;
	private final int nrOfDemandCandidatePaths;

	/**
	 * Selected channel to realize given demand
	 */
	private Channel channel;

	public Demand(int name, long volume, int sourceNode, int destinationNode, List<DemandCandidatePath> candidatePaths) {
		this.name = name;
		this.volume = volume;
		this.sourceNode = sourceNode;
		this.destinationNode = destinationNode;
		this.candidatePaths = candidatePaths;
		this.nrOfDemandCandidatePaths = candidatePaths.size();
	}

	public int getSourceNode() {
		return sourceNode;
	}

	public int getDestinationNode() {
		return destinationNode;
	}

	public List<DemandCandidatePath> getDemandCandidatePaths() {
		return candidatePaths;
	}

	public DemandCandidatePath getShortestDemandCandidatePath() {
		return candidatePaths.get(0);
	}

	public int getNrOfDemandCandidatePaths() {
		return nrOfDemandCandidatePaths;
	}

	public long getVolume() {
		return volume;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	@Override
	public String toString() {
		return name.toString();
	}

	public int getName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Demand demand = (Demand) o;
		return volume == demand.volume &&
				sourceNode == demand.sourceNode &&
				destinationNode == demand.destinationNode &&
				nrOfDemandCandidatePaths == demand.nrOfDemandCandidatePaths &&
				Objects.equals(name, demand.name) &&
				Objects.equals(candidatePaths, demand.candidatePaths);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, volume, sourceNode, destinationNode, candidatePaths, nrOfDemandCandidatePaths);
	}
}
