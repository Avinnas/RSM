package com.github.netopt.rsmsdm.spectrum;

import com.github.netopt.rsmsdm.network.Demand;
import com.github.netopt.rsmsdm.network.DemandCandidatePath;
import com.github.netopt.rsmsdm.network.Edge;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Describes channel which can be allocated on spectrum.
 * Channel contains fibers involved in path, and coresponding cores
 * for each one of them. </br>
 * It contains starting index and size.
 *
 * @author plechowicz
 * created on 18.02.19.
 */
public class Channel {

	private final int startIndex;
	private final int size;
	private final Demand demand;

	private final List<Integer> selectedCoresForPathEdges = new ArrayList<>(); // elements corresponds to consecutive values for edges in demandCandidatePath
	private final DemandCandidatePath demandCandidatePath;

	public Channel(Demand demand, DemandCandidatePath demandCandidatePath, int startIndex) {
		this.demand = demand;
		this.demandCandidatePath = demandCandidatePath;
		this.startIndex = startIndex;
		this.size = demandCandidatePath.getNrOfSlices();
		demandCandidatePath.getCandidatePath().getEdges().forEach(e -> selectedCoresForPathEdges.add(-1));
	}

	public int getStartIndex() {
		return startIndex;
	}

	public int getSize() {
		return size;
	}

	public void setSelectedCoreForIthEdgeInPath(int edgeInPathId, int coreId) {
		selectedCoresForPathEdges.set(edgeInPathId, coreId);
	}

	public void setSelectedCoreForEdge(Edge edge, int coreId) {
		selectedCoresForPathEdges.set(demandCandidatePath.getCandidatePath().getEdges().indexOf(edge), coreId);
	}

	public void setSelectedCoreForEdgesInPath(List<Integer> coreIds) {
		if (coreIds.size() != selectedCoresForPathEdges.size()) {
			throw new IllegalStateException(String.format("assigning too small value of coreIds to given candidatePath." +
					"number of coreIds={%d}, number of edges in path={%s}", coreIds.size(), selectedCoresForPathEdges.size()));
		} else {
			for (int id = 0; id < coreIds.size(); id++) {
				setSelectedCoreForIthEdgeInPath(id, coreIds.get(id));
			}
		}
	}

	public Map<Edge, Integer> getSelectedCoreForEdgeAsMapOfEdges() {
		List<Edge> edges = demandCandidatePath.getCandidatePath().getEdges();
		List<Integer> selectedCoreForEdgesInPathAsList = getSelectedCoreForEdgesInPathAsList();
		return IntStream.range(0, edges.size()).boxed()
				.collect(Collectors.toMap(edges::get, selectedCoreForEdgesInPathAsList::get));
	}

	public int getStopIndex() {
		return startIndex + size;
	}

	public List<Integer> getSelectedCoreForEdgesInPathAsList() {
		return selectedCoresForPathEdges;
	}

	public DemandCandidatePath getDemandCandidatePath() {
		return demandCandidatePath;
	}

	public Demand getDemand() {
		return demand;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Channel channel = (Channel) o;
		return startIndex == channel.startIndex &&
				size == channel.size &&
				Objects.equals(demand, channel.demand) &&
				Objects.equals(selectedCoresForPathEdges, channel.selectedCoresForPathEdges) &&
				Objects.equals(demandCandidatePath, channel.demandCandidatePath);
	}

	@Override
	public int hashCode() {
		return Objects.hash(startIndex, size, demand, selectedCoresForPathEdges, demandCandidatePath);
	}

	@Override
	public String toString() {
		return "Channel{" +
				"startIndex=" + startIndex +
				", size=" + size +
				", demand=" + demand +
				", selectedCoresForPathEdges=" + selectedCoresForPathEdges +
				", demandCandidatePath=" + demandCandidatePath +
				'}';
	}

}
