package com.github.netopt.rsmsdm.spectrum;

import com.github.netopt.rsmsdm.network.Edge;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Represents fibers in the network. Each edge in graph has corresponding fiber.
 *
 * @author plechowicz
 * created on 18.02.19.
 */
public class Fiber {

	private int edgeId;
	private Edge edge; // reference to edge in the graph
	private int nrOfCores;
	private List<Core> cores;

	public Fiber(int edgeId, Edge edge, int nrOfCores) {
		this.edgeId = edgeId;
		this.nrOfCores = nrOfCores;
		this.edge = edge;
		cores = new ArrayList<>();
		for (int i = 0; i < nrOfCores; i++) {
			cores.add(new Core());
		}
	}

	public Fiber deepCopy() {
		Fiber fiber = new Fiber(edgeId, edge, nrOfCores);
		fiber.cores = new ArrayList<>();
		for (Core core : cores) {
			fiber.cores.add(core.deepCopy());
		}
		return fiber;
	}

	public int lastOccupiedIndex() {
		return cores.stream().mapToInt(Core::lastAllocatedSlice).max().orElse(-1);
	}

	public int amountOfAllocatedSlices() {
		return cores.stream().mapToInt(Core::amountOfAllocatedSlices).sum();
	}

	public long sumOfLastOccupiedIndicesOnAllCores() {
		return cores.stream().mapToInt(Core::lastAllocatedSlice).sum();
	}

	public double averageLastOccupiedIndicesCores() {
		return cores.stream().mapToInt(Core::lastAllocatedSlice).sum() / (double) cores.size();
	}

	public int findFirstFreeSize(int coreId, int size) {
		return this.cores.get(coreId).findFirstFreeSize(size);
	}

	public int findFirstFreeSize(int coreId, int size, int minIndex) {
		return this.cores.get(coreId).findFirstFreeSize(size, minIndex);
	}

	public void allocate(Channel channel) {
		int core = channel.getSelectedCoreForEdgeAsMapOfEdges().get(edge);
		cores.get(core).allocate(channel);
	}

	public void deallocate(Channel channel) {
		int core = channel.getSelectedCoreForEdgeAsMapOfEdges().get(edge);
		cores.get(core).deallocate(channel);
	}

	public int findFirstFreeSize(int[] coreIds, int size) {
		List<Core> sublistOfCores = IntStream.of(coreIds).boxed().map(i -> cores.get(i)).collect(Collectors.toList());
		return CoreUtils.findFirstFreeSize(size, sublistOfCores);
	}

	public int findFirstFreeSize(List<Integer> coreIds, int size) {
		List<Core> sublistOfCores = coreIds.stream().map(i -> cores.get(i)).collect(Collectors.toList());
		return CoreUtils.findFirstFreeSize(size, sublistOfCores);
	}

	public int findFirstFreeSize(int[] coreIds, int size, int minIndex) {
		List<Core> sublistOfCores = IntStream.of(coreIds).boxed().map(i -> cores.get(i)).collect(Collectors.toList());
		return CoreUtils.findFirstFreeSize(size, minIndex, sublistOfCores);
	}

	public int findFirstFreeSize(List<Integer> coreIds, int size, int minIndex) {
		List<Core> sublistOfCores = coreIds.stream().map(i -> cores.get(i)).collect(Collectors.toList());
		return CoreUtils.findFirstFreeSize(size, minIndex, sublistOfCores);
	}

	public Edge getEdge() {
		return edge;
	}

	public int getEdgeId() {
		return edgeId;
	}

	public int getNrOfCores() {
		return nrOfCores;
	}

	public List<Core> getCores() {
		return cores;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder(200);
		cores.forEach(c -> result.append(c + "\n"));
		return result.toString();
	}
}
