package com.github.netopt.rsmsdm.algorithm;

import com.github.netopt.rsmsdm.experiment.ExperimentProperties;
import com.github.netopt.rsmsdm.network.Demand;
import com.github.netopt.rsmsdm.spectrum.Channel;
import com.github.netopt.rsmsdm.spectrum.Spectrum;
import javafx.util.Pair;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class RandomSearch extends Algorithm{
    private static final String name = RandomSearch.class.getSimpleName();

    @Override
    protected void execution(Spectrum spectrum, List<Demand> demands) {
        demands.sort(Comparator.comparing(Demand::getVolume).reversed());
        int steps = 10000;

        Spectrum emptySpectrum = spectrum.deepCopy();
        List<Demand> bestSolution = new ArrayList<>(demands);
        int bestValue = allocateAndEvaluateSpectrum(spectrum, demands);

        for (int i = 0; i < steps; i++) {
            List<Demand> currentDemands = new ArrayList<>(bestSolution);
            int demand1 = ThreadLocalRandom.current().nextInt(0, demands.size()-1);
            int demand2 = ThreadLocalRandom.current().nextInt(0, demands.size()-1);

            Collections.swap(currentDemands, demand1, demand2);

            spectrum = emptySpectrum.deepCopy();
            int currentValue = allocateAndEvaluateSpectrum(spectrum, currentDemands);

            if(currentValue < bestValue){
                bestValue = currentValue;
                bestSolution = new ArrayList<>(currentDemands);
            }
        }
        allocateAndEvaluateSpectrum(emptySpectrum, bestSolution);

    }

    protected int allocateAndEvaluateSpectrum(Spectrum spectrum, List<Demand> demands){
        allocateSpectrum(spectrum, demands);
        return spectrum.getSpectrumCost();
    }
    protected void allocateSpectrum(Spectrum spectrum, List<Demand> demands){
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
