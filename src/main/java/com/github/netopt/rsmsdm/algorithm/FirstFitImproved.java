package com.github.netopt.rsmsdm.algorithm;

import com.github.netopt.rsmsdm.algorithmUtils.AlgorithmHelpers;
import com.github.netopt.rsmsdm.experiment.ExperimentProperties;
import com.github.netopt.rsmsdm.network.Demand;
import com.github.netopt.rsmsdm.network.DemandCandidatePath;
import com.github.netopt.rsmsdm.spectrum.Channel;
import com.github.netopt.rsmsdm.spectrum.Spectrum;

import java.util.Comparator;
import java.util.List;
import java.util.Properties;

/**
 * @author plechowicz
 * created on 19.02.19.
 */
public class FirstFitImproved extends Algorithm {

    private static final String name = FirstFitImproved.class.getSimpleName();

    @Override
    protected void execution(Spectrum spectrum, List<Demand> demands) {
        demands.sort(Comparator.comparing(Demand::getVolume).reversed());

        AlgorithmHelpers.locateDemandsOnBestPath(demands, spectrum);

    }

    @Override
    public String getName() {
        return name;
    }

    protected int evaluateCandidatePath(Spectrum spectrum, Demand demand, DemandCandidatePath demandCandidatePath) {
        Channel channel = ChannelFinder.findLowestOnPath(spectrum, demand, demandCandidatePath);
        return channel.getStartIndex() + channel.getSize();
    }
}
