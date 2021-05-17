package com.github.netopt.rsmsdm.algorithm;

import com.github.netopt.rsmsdm.experiment.ExperimentProperties;
import com.github.netopt.rsmsdm.network.Demand;
import com.github.netopt.rsmsdm.network.DemandCandidatePath;
import com.github.netopt.rsmsdm.spectrum.Channel;
import com.github.netopt.rsmsdm.spectrum.Spectrum;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class RandomSearchCandidates extends Algorithm {
    private static final String name = RandomSearchCandidates.class.getSimpleName();

    @Override
    protected void execution(Spectrum spectrum, List<Demand> demands) {
        demands.sort(Comparator.comparing(Demand::getVolume).reversed());

        int steps = 10000;
        int candidatePathsSearched = 30;
        Spectrum emptySpectrum = spectrum.deepCopy();
        List<Demand> bestSolution = new ArrayList<>(demands);
        int bestValue = allocateAndEvaluateSpectrum(spectrum, demands);

        for (int i = 0; i < steps; i++) {
            int randomMove = ThreadLocalRandom.current().nextInt(0, demands.size() - 1);

            Demand currentMove = demands.get(randomMove);
            int bestPathNumber = 0;
            int bestPathValue = Integer.MAX_VALUE;
            deallocateChannel(spectrum, currentMove);

            for (int j = 0; j < candidatePathsSearched; j++) {

                var path = currentMove.getDemandCandidatePaths().get(j);
                int currentValue = evaluateCandidatePath(spectrum, currentMove, path);

                if (currentValue < bestPathValue) {
                    bestPathValue = currentValue;
                    bestPathNumber = j;
                }
            }
            int value = allocateSpectrumWithPath(spectrum, currentMove, currentMove.getDemandCandidatePaths().get(bestPathNumber));
            if (value < bestValue) {
                bestValue = value;
                bestSolution = new ArrayList<>(demands);
            }

        }
        demands = bestSolution;
        spectrum = emptySpectrum;


    }

    protected int evaluateCandidatePath(Spectrum spectrum, Demand demand, DemandCandidatePath demandCandidatePath) {
        Channel channel = ChannelFinder.findLowestOnPath(spectrum, demand, demandCandidatePath);
        return channel.getStartIndex() + channel.getSize();
    }

    protected void deallocateChannel(Spectrum spectrum, Demand demand) {
        spectrum.deallocate(demand.getChannel());
        demand.setChannel(null);
    }

    protected int allocateAndEvaluateSpectrum(Spectrum spectrum, List<Demand> demands) {
        allocateSpectrum(spectrum, demands);
        return spectrum.getSpectrumCost();
    }

    protected int allocateSpectrumWithPath(Spectrum spectrum, Demand demand, DemandCandidatePath demandCandidatePath) {
        Channel channel = ChannelFinder.findLowestOnPath(spectrum, demand, demandCandidatePath);
        demand.setChannel(channel);
        spectrum.allocate(channel);
        return spectrum.getSpectrumCost();
    }

    protected void allocateSpectrum(Spectrum spectrum, List<Demand> demands) {
        for (var demand : demands) {
            var demandCandidatePath = demand.getShortestDemandCandidatePath();

            allocateSpectrumWithPath(spectrum, demand, demandCandidatePath);
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
