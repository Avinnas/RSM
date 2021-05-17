package com.github.netopt.rsmsdm.algorithm;

import com.github.netopt.rsmsdm.experiment.ExperimentProperties;
import com.github.netopt.rsmsdm.network.Demand;
import com.github.netopt.rsmsdm.spectrum.Spectrum;
import com.google.common.base.Stopwatch;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Extend that class if you want to implement new algorithm
 *
 * @author plechowicz
 * created on 19.02.19.
 */
public abstract class Algorithm {

	private final Stopwatch stopwatch = Stopwatch.createUnstarted();

	public final List<Demand> execute(Spectrum spectrum, List<Demand> demands, ExperimentProperties experimentProperties, Properties algorithmProperties) {
		stopwatch.reset();
		setUp(experimentProperties, algorithmProperties);
		stopwatch.start();
		execution(spectrum, demands);
		stopwatch.stop();
		cleanUp();
		return demands;
	}

	/**
	 * Set up whatever is needed in algorithm.
	 * @param experimentProperties
	 * @param algorithmProperties
	 */
	protected void setUp(ExperimentProperties experimentProperties, Properties algorithmProperties) {
	}

	/**
	 * Finds channel for given set of demands in given spectrum.
	 * Note, after finding a channel, it should be set in that list of demands.
	 * @param spectrum
	 * @param demands
	 */
	protected abstract void execution(Spectrum spectrum, List<Demand> demands);

	/**
	 * Clean up after algorithms execution
	 */
	protected void cleanUp() {
	}

	/**
	 * Execution time in given time units
	 *
	 * @return
	 */
	public long getTime(TimeUnit units) {
		return stopwatch.elapsed(units);
	}

	/**
	 * Pretty printed execution time
	 * @return
	 */
	public String getTime() {
		return stopwatch.toString();
	}

	public abstract String getName();
}
