package com.github.netopt.rsmsdm.algorithm;

import com.github.netopt.rsmsdm.experiment.ExperimentProperties;
import com.github.netopt.rsmsdm.network.Demand;
import com.github.netopt.rsmsdm.spectrum.Channel;
import com.github.netopt.rsmsdm.spectrum.Spectrum;

import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class SortedDemands extends Algorithm{
    private static final String name = SortedDemands.class.getSimpleName();

    @Override
    protected void execution(Spectrum spectrum, List<Demand> demands) {
        demands.sort(Comparator.comparing(Demand::getVolume).reversed());

        for (var demand : demands) {
            var demandCandidatePath = demand.getShortestDemandCandidatePath();

            Channel channel = ChannelFinder.findLowestOnPath(spectrum, demand, demandCandidatePath);
            demand.setChannel(channel);
            spectrum.allocate(channel);
        }

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    protected void setUp(ExperimentProperties experimentProperties, Properties algorithmProperties) {
        super.setUp(experimentProperties, algorithmProperties);
    }

    @Override
    protected void cleanUp() {
        super.cleanUp();
    }

}
