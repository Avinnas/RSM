package com.github.netopt.rsmsdm;

import com.github.netopt.rsmsdm.algorithm.*;

/**
 * @author plechowicz
 * created on 23.02.19.
 */
public final class AlgorithmsConfig {

	public static void registerAlgorithms() {
		AlgorithmsRepository.register(new FirstFit().getName(), new FirstFit());
		AlgorithmsRepository.register(new FirstFitImproved().getName(), new FirstFitImproved());
		AlgorithmsRepository.register(new SortedDemands().getName(), new SortedDemands());
		AlgorithmsRepository.register(new RandomSearch().getName(), new RandomSearch());
		AlgorithmsRepository.register(new RandomSearchNeighbourhood().getName(), new RandomSearchNeighbourhood());
		AlgorithmsRepository.register(new RandomSearchCandidates().getName(), new RandomSearchCandidates());
		AlgorithmsRepository.register(new TabuSearch().getName(), new TabuSearch());

	}
}
