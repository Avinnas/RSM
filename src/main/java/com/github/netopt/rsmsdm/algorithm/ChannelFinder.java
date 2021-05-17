package com.github.netopt.rsmsdm.algorithm;

import com.github.netopt.rsmsdm.network.Demand;
import com.github.netopt.rsmsdm.network.DemandCandidatePath;
import com.github.netopt.rsmsdm.spectrum.Channel;
import com.github.netopt.rsmsdm.spectrum.Core;
import com.github.netopt.rsmsdm.spectrum.Fiber;
import com.github.netopt.rsmsdm.spectrum.Spectrum;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author plechowicz
 * created on 20.02.19.
 */
public class ChannelFinder {

	public static Channel findLowestOnPath(Spectrum spectrum, Demand demand, DemandCandidatePath demandCandidatePath) {
		// get required slices for given demand and candidate path
		int requiredNrOfSlices = demandCandidatePath.getNrOfSlices();
		// fetch edges in given candidate path
		List<Integer> edgesIds = demandCandidatePath.getCandidatePath().getEdgesIds();
		// get fibers for given set of edges
		List<Fiber> fibersInCandidatePath = spectrum.getFibers(edgesIds);
		final int nrOfFibers = fibersInCandidatePath.size();
		if (nrOfFibers == 0) {
			// theoretically not possible, only occurs when candidate path does not contain any edge
			throw new IllegalStateException();
		}

		// stores on which core, channel is found for each fiber
		List<Integer> selectedCoreForEachFiber = new ArrayList<>();
		IntStream.range(0, nrOfFibers).forEach(i -> selectedCoreForEachFiber.add(-1));

		// find lowest spectrum window on given path
		// start searching available fibers and cores from slice 0
		int minSliceForAllFibers = 0;
		// how many fibers have been found with the same starting channel index
		int nrOfFibersWithSameIdxFound = 0;
		boolean sameStartingSliceForAllFibers = false;
		// repeat until for all channels the same starting slice is found
		for (int currentFiberId = 0; !sameStartingSliceForAllFibers; currentFiberId++) {
			if (currentFiberId == nrOfFibers) { // if we have reached the end of fibers, move pointer to its start
				currentFiberId = 0;
			}

			// get next fiber in candidate path
			Fiber fiber = fibersInCandidatePath.get(currentFiberId);

			List<Core> cores = fiber.getCores();
			int selectedCoreId = -1;
			// starting slice of channel on any core of fiber with lowest slice index not lower than minSliceForAllFibers
			int channelStartIdxOnAnyCoreOfFiber = Integer.MAX_VALUE;
			for (int currentCoreId = 0; currentCoreId < cores.size(); currentCoreId++) {
				// find first free channel in given core of requiredNrOfSlices size starting from slice index minSliceForAllFibers
				var core = cores.get(currentCoreId);
				int channelStartIdx = core.findFirstFreeSize(requiredNrOfSlices, minSliceForAllFibers);
				if (channelStartIdx < channelStartIdxOnAnyCoreOfFiber) {
					channelStartIdxOnAnyCoreOfFiber = channelStartIdx;
					selectedCoreId = currentCoreId;
				}
			}
			// store selected core for given fiber
			selectedCoreForEachFiber.set(currentFiberId, selectedCoreId);

			// if found channel on given fiber is higher than assumed starting index on other fibers
			if (channelStartIdxOnAnyCoreOfFiber > minSliceForAllFibers) {
				// reset number of fibers for which are available channels starting with the same index
				nrOfFibersWithSameIdxFound = 0;
				minSliceForAllFibers = channelStartIdxOnAnyCoreOfFiber;
			}
			nrOfFibersWithSameIdxFound++;
			// check if channel with the same starting slice index is found for all fibers in path
			if (nrOfFibersWithSameIdxFound >= nrOfFibers) {
				sameStartingSliceForAllFibers = true;
			}
		}

		// same starting slice for all fibers have been found, create a channel for demand
		Channel channel = new Channel(demand, demandCandidatePath, minSliceForAllFibers);
		// set cores for each link according to found selectedCoreForEachFiber in given candidate path
		channel.setSelectedCoreForEdgesInPath(selectedCoreForEachFiber);
		return channel;
	}
}
