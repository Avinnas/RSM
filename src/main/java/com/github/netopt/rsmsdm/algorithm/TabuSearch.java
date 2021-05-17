package com.github.netopt.rsmsdm.algorithm;

import com.github.netopt.rsmsdm.experiment.ExperimentProperties;
import com.github.netopt.rsmsdm.network.Demand;
import com.github.netopt.rsmsdm.network.DemandCandidatePath;
import com.github.netopt.rsmsdm.spectrum.Channel;
import com.github.netopt.rsmsdm.spectrum.Spectrum;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


public class TabuSearch extends Algorithm{
    private static final String name = TabuSearch.class.getSimpleName();

    @Override
    protected void execution(Spectrum spectrum, List<Demand> demands) {
        demands.sort(Comparator.comparing(Demand::getVolume).reversed());

        int steps = 1000;
        int neighbourhoodSize=demands.size();
        int candidatePathsSearched = 30;

        Queue<Integer> tabuList = new LinkedList<>();
        Spectrum emptySpectrum = spectrum.deepCopy();
        List<Demand> bestSolution = new ArrayList<>(demands);
        List<Demand> currentSolution = new ArrayList<>(demands);
        int bestValue = allocateAndEvaluateSpectrum(spectrum, demands);

        for (int i = 0; i < steps; i++) {
            int neighbourhoodBestPathImprovement = 0;
            int neighbourhoodBestPathNumber = 0;
            int neighbourhoodBestIndex = demands.size()-1;
            int neighbourhoodBestValue = spectrum.getSpectrumCost();

            for (int j = 0; j < neighbourhoodSize; j++) {

                Demand currentDemand = demands.get(demands.size()-1-j);
                int bestPathNumber = 0;
                int bestPathValue = Integer.MAX_VALUE;
                Channel channel = currentDemand.getChannel();
                deallocateChannel(spectrum,currentDemand);

                for (int k = 0; k < candidatePathsSearched; k++) {

                    var path = currentDemand.getDemandCandidatePaths().get(k);
                    int currentValue = evaluateCandidatePath(spectrum, currentDemand, path);

                    if(currentValue < bestPathValue){
                        bestPathValue = currentValue;
                        bestPathNumber = k;
                    }
                }
                int currentSpectrumValue = spectrum.getSpectrumCost();
                currentDemand.setChannel(channel);
                spectrum.allocate(channel);
                if(currentSpectrumValue < spectrum.getSpectrumCost() && bestPathValue< spectrum.getSpectrumCost()
                && spectrum.getSpectrumCost() < neighbourhoodBestValue){
                    neighbourhoodBestIndex = demands.size() - 1 - j;
                    neighbourhoodBestValue = spectrum.getSpectrumCost();
                    neighbourhoodBestPathNumber = bestPathNumber;
                    neighbourhoodBestPathImprovement = -bestPathValue + channel.getStartIndex() + channel.getSize();

                } else if(-bestPathValue + channel.getStartIndex() + channel.getSize() > neighbourhoodBestPathImprovement
                && spectrum.getSpectrumCost()<= neighbourhoodBestValue){
                    neighbourhoodBestPathImprovement = -bestPathValue + channel.getStartIndex() + channel.getSize();
                    neighbourhoodBestIndex = demands.size() - 1 - j;
                    neighbourhoodBestPathNumber = bestPathNumber;
                }

            }

            Demand move = currentSolution.get(neighbourhoodBestIndex);
            deallocateChannel(spectrum, move);
            Channel newChannel = ChannelFinder.findLowestOnPath(spectrum, move, move.getDemandCandidatePaths().get(neighbourhoodBestPathNumber));
            move.setChannel(newChannel);
            spectrum.allocate(newChannel);

            if(neighbourhoodBestValue < bestValue){
                bestValue = neighbourhoodBestValue;
                bestSolution = new ArrayList<>(currentSolution);
            }

        }
        demands = bestSolution;
        spectrum = emptySpectrum;


    }
    protected int evaluateCandidatePath(Spectrum spectrum, Demand demand, DemandCandidatePath demandCandidatePath){
        Channel channel = ChannelFinder.findLowestOnPath(spectrum, demand, demandCandidatePath);
        return channel.getStartIndex()+channel.getSize();
    }
    protected void deallocateChannel(Spectrum spectrum, Demand demand){
        spectrum.deallocate(demand.getChannel());
        demand.setChannel(null);
    }

    protected int allocateAndEvaluateSpectrum(Spectrum spectrum, List<Demand> demands){
        allocateSpectrum(spectrum, demands);
        return spectrum.getSpectrumCost();
    }
    protected int  allocateSpectrumWithPath(Spectrum spectrum, Demand demand, DemandCandidatePath demandCandidatePath){
        Channel channel = ChannelFinder.findLowestOnPath(spectrum, demand, demandCandidatePath);
        demand.setChannel(channel);
        spectrum.allocate(channel);
        return spectrum.getSpectrumCost();
    }

    protected void allocateSpectrum(Spectrum spectrum, List<Demand> demands){
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
