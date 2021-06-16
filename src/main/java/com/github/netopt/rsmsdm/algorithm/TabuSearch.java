package com.github.netopt.rsmsdm.algorithm;

import com.github.netopt.rsmsdm.algorithmUtils.AlgorithmHelpers;
import com.github.netopt.rsmsdm.algorithmUtils.MoveTS;
import com.github.netopt.rsmsdm.experiment.ExperimentProperties;
import com.github.netopt.rsmsdm.network.Demand;
import com.github.netopt.rsmsdm.network.DemandCandidatePath;
import com.github.netopt.rsmsdm.spectrum.Channel;
import com.github.netopt.rsmsdm.spectrum.Spectrum;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static java.util.Comparator.reverseOrder;


public class TabuSearch extends Algorithm {
    private static final String name = TabuSearch.class.getSimpleName();

    @Override
    protected void execution(Spectrum spectrum, List<Demand> demands) {
        demands.sort(Comparator.comparing(Demand::getVolume).reversed());

        int steps = 1000;
        int neighbourhoodSize = demands.size()/10;
        int tabuListSize = demands.size() / 5;

        Queue<Integer> tabuList = new LinkedList<>();
        Spectrum emptySpectrum = spectrum.deepCopy();
        List<Demand> bestSolution = new ArrayList<>(demands);
        List<Demand> currentSolution = new ArrayList<>(demands);

        AlgorithmHelpers.locateDemandsOnFirstPath(demands, spectrum);
        int bestValue = spectrum.getSpectrumCost();

        for (int i = 0; i < steps; i++) {
            List<MoveTS> moveList = new ArrayList<>();

            for (int j = 0; j < neighbourhoodSize; j++) {
                final int[] indexes = new Random().ints(0, currentSolution.size()).distinct().limit(neighbourhoodSize).toArray();

                int demandIndex = indexes[j];
                Demand currentDemand = currentSolution.get(demandIndex);
                int bestPathNumber = 0;
                int bestPathValue = Integer.MAX_VALUE;
                Channel channel = currentDemand.getChannel();
                deallocateChannel(spectrum, currentDemand);

                for (int k = 0; k < demands.get(0).getNrOfDemandCandidatePaths(); k++) {

                    var path = currentDemand.getDemandCandidatePaths().get(k);
                    int currentValue = AlgorithmHelpers.evaluateCandidatePath(spectrum, currentDemand, path);

                    if (channel.getDemandCandidatePath().equals(path)) continue;

                    if (currentValue < bestPathValue) {
                        bestPathValue = currentValue;
                        bestPathNumber = k;
                    }
                }
                int currentSpectrumValue = spectrum.getSpectrumCost();
                currentDemand.setChannel(channel);
                spectrum.allocate(channel);

                moveList.add(new MoveTS(
                        Math.max(bestPathValue, currentSpectrumValue),
                        -bestPathValue + channel.getStartIndex() + channel.getSize(),
                        demandIndex,
                        bestPathNumber));

            }

            moveList.sort(Comparator
                    .comparing(MoveTS::getValue)
                    .thenComparing(MoveTS::getPathImprovement, reverseOrder()));

            int moveIndex = 0;
            while (tabuList.contains(moveList.get(moveIndex).getIndex())) {
                if (moveList.get(moveIndex).getValue() < bestValue) break;
                moveIndex++;
            }
            if (tabuList.size() == tabuListSize) tabuList.remove();
            tabuList.add(moveList.get(moveIndex).getIndex());

            MoveTS bestMove = moveList.get(moveIndex);
            Demand move = currentSolution.get(bestMove.getIndex());

            deallocateChannel(spectrum, move);
            Channel newChannel = ChannelFinder.findLowestOnPath(spectrum, move, move.getDemandCandidatePaths().get(bestMove.getPathNumber()));
            move.setChannel(newChannel);
            spectrum.allocate(newChannel);

            if (bestMove.getValue() < bestValue) {
                bestValue = bestMove.getValue();
                bestSolution = new ArrayList<>(currentSolution);
            }

        }
        demands = bestSolution;
        spectrum = emptySpectrum;


    }



    protected void deallocateChannel(Spectrum spectrum, Demand demand) {
        spectrum.deallocate(demand.getChannel());
        demand.setChannel(null);
    }


    protected int allocateSpectrumWithPath(Spectrum spectrum, Demand demand, DemandCandidatePath demandCandidatePath) {
        Channel channel = ChannelFinder.findLowestOnPath(spectrum, demand, demandCandidatePath);
        demand.setChannel(channel);
        spectrum.allocate(channel);
        return spectrum.getSpectrumCost();
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
