package com.github.netopt.rsmsdm.algorithm;

import com.github.netopt.rsmsdm.experiment.ExperimentProperties;
import com.github.netopt.rsmsdm.network.Demand;
import com.github.netopt.rsmsdm.spectrum.Channel;
import com.github.netopt.rsmsdm.spectrum.Spectrum;

import java.util.List;
import java.util.Properties;

/**
 * @author plechowicz
 * created on 19.02.19.
 */
public class FirstFit extends Algorithm {

	private static final String name = FirstFit.class.getSimpleName();

	@Override
	protected void setUp(ExperimentProperties experimentProperties, Properties algorithmProperties) {
		super.setUp(experimentProperties, algorithmProperties);
		// no setup needed
		// however, it you need you can handle here some algorithmProperties or experimentProperties. This pre-processing is
		// not appended to the algorithm's execution time
	}

	@Override
	protected void execution(Spectrum spectrum, List<Demand> demands) {
		// allocate each demand on first found spectrum channel on shortest candidate path
		for (var demand : demands) {
			// get shortest candidate path
			// alternatively getDemandCandidatePaths() and iterate through it
			var demandCandidatePath = demand.getShortestDemandCandidatePath();

			// find lowest on given path.
			Channel channel = ChannelFinder.findLowestOnPath(spectrum, demand, demandCandidatePath);
			// set channel to demand
			demand.setChannel(channel);
			// allocate channel to allow for finding channels for other demands
			spectrum.allocate(channel);
		}
	}

	@Override
	protected void cleanUp() {
		super.cleanUp();
		// no cleanUp needed
	}

	@Override
	public String getName() {
		return name;
	}
}
