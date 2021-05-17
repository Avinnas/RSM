package com.github.netopt.rsmsdm.spectrum;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.function.Supplier;

/**
 * Represents core. Core contains slices which can be occupied or by demands.
 *
 * @author plechowicz
 * created on 18.02.19.
 */
public class Core {

	/**
	 * Set representing slots in the network.
	 * 0 represents free slot, 1 represent allocated slot
	 */
	BitSet slices;

	private List<Channel> allocatedChannels = new ArrayList<>();
	private List<Integer> demandIdsForEachSlice = new ArrayList<>();

	public Core() {
		slices = new BitSet(10);
	}

	public Core deepCopy() {
		Core core = new Core();
		core.slices.or(slices);
		core.allocatedChannels = new ArrayList<>(allocatedChannels);
		core.demandIdsForEachSlice = new ArrayList<>(demandIdsForEachSlice);
		return core;
	}

	public int lastAllocatedSlice() {
		return slices.length();
	}

	public int amountOfAllocatedSlices() {
		return slices.cardinality();
	}

	@Override
	public String toString() {
		return slices.toString();
	}

	public void allocate(Channel channel) {
		allocatedChannels.add(channel);
		allocateByRange(channel.getStartIndex(), channel.getStopIndex(), channel.getDemand().getName());
	}

	/**
	 * Package private for testing
	 *
	 * @param start
	 * @param end
	 */
	void allocateByRange(int start, int end, int demandId) {
		int size = slices.cardinality();
		slices.set(start, end);
		if (slices.cardinality() - size != end - start) {
			throw new IllegalStateException("Allocating slices which are already allocated");
		}
		if (demandIdsForEachSlice.size() < end) {
			resizeList(demandIdsForEachSlice, end + 10, () -> -1);
		}
		for (int i = start; i < end; i++) {
			demandIdsForEachSlice.set(i, demandId);
		}
	}

	public void deallocate(Channel channel) {
		allocatedChannels.remove(channel);
		deallocateByRange(channel.getStartIndex(), channel.getStopIndex());
	}

	/**
	 * Package private for testing
	 *
	 * @param start
	 * @param end
	 */
	void deallocateByRange(int start, int end) {
		int size = slices.cardinality();
		slices.clear(start, end);
		if (size - slices.cardinality() != end - start) {
			throw new IllegalStateException("Deallocating slices which are already deallocated");
		}
		for (int i = start; i < end; i++) {
			demandIdsForEachSlice.set(i, -1);
		}
	}

	public int findFirstFreeSize(int size) {
		return findFirstFreeSize(size, 0);
	}

	public int findFirstFreeSize(int size, int minIndex) {
		int start;
		int end = minIndex;
		do {
			start = slices.nextClearBit(end);
			end = slices.nextSetBit(start);
		} while (end != -1 && end - start < size);
		return start;
	}

	private static <T> void resizeList(List<T> list, int newSize, Supplier<T> supplier) {
		for (int curSize = list.size(); curSize < newSize; curSize++) {
			list.add(supplier.get());
		}
	}
}
