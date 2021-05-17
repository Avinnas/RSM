package com.github.netopt.rsmsdm.spectrum;

import com.github.netopt.rsmsdm.network.Edge;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Contains all available fibers and cores in the network
 *
 * @author plechowicz
 * created on 18.02.19.
 */
public class Spectrum {

	private final List<Fiber> fibers;
	private final List<Edge> edges;

	/**
	 * List of allocated channels in the spectrum
	 */
	private List<Channel> allocatedChannels = new ArrayList<>();

	private Integer highestAllocatedSliceInTheNetwork = 0;

	public Spectrum(List<Edge> edges, int nrOfCores) {
		fibers = new ArrayList<>();
		this.edges = edges;
		for (int i = 0; i < edges.size(); i++) {
			fibers.add(new Fiber(edges.get(i).getName(), edges.get(i), nrOfCores));
		}
	}

	public Spectrum deepCopy() {
		Spectrum spectrum = shallowCopy();

		for (Fiber fiber : fibers) {
			spectrum.fibers.add(fiber.deepCopy());
		}
		return spectrum;
	}

	public Spectrum shallowCopy() {
		return new Spectrum(this.edges, this.fibers.get(0).getNrOfCores());
	}

	public List<Fiber> getFibers() {
		return fibers;
	}

	/**
	 * Get fibers corresponding to given list of indices of edges
	 * @param edgeIds
	 * @return
	 */
	public List<Fiber> getFibers(List<Integer> edgeIds) {
		return edgeIds.stream().map(fibers::get).collect(Collectors.toList());
	}

	/**
	 * Allocates channel in the spectrum.
	 *
	 * @param channel Channel which will be allocated
	 */
	public void allocate(Channel channel) {
		channel.getDemandCandidatePath().getCandidatePath().getEdgesIds().forEach(
				_index -> fibers.get(_index).allocate(channel)
		);
		updateSpectrumCost(channel);
		allocatedChannels.add(channel);
	}

	/**
	 * Deallocates channel in the spectrum
	 *
	 * @param channel
	 */
	public void deallocate(Channel channel) {
		channel.getDemandCandidatePath().getCandidatePath().getEdgesIds().forEach(
				_index -> fibers.get(_index).deallocate(channel)
		);
		revalidateSpectrumCostAfterDeallocation();
		allocatedChannels.add(channel);
	}

	private void updateSpectrumCost(Channel channel) {
		if (highestAllocatedSliceInTheNetwork < channel.getStopIndex()) {
			highestAllocatedSliceInTheNetwork = channel.getStopIndex();
		}
	}

	private void revalidateSpectrumCostAfterDeallocation() {
		int maxAllocatedIndex = 0;
		for (Fiber fiber : fibers) {
			int lastOccupiedIndex = fiber.lastOccupiedIndex();
			if (lastOccupiedIndex == highestAllocatedSliceInTheNetwork) {
				return;
			}
			if (lastOccupiedIndex > maxAllocatedIndex) {
				maxAllocatedIndex = lastOccupiedIndex;
			}
		}
		highestAllocatedSliceInTheNetwork = maxAllocatedIndex;
	}

	public Integer getSpectrumCost() {
		return highestAllocatedSliceInTheNetwork;
	}

	public List<Channel> getAllocatedChannels() {
		return allocatedChannels;
	}
}
