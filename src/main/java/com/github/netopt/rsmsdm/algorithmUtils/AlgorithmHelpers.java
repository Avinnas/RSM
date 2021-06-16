package com.github.netopt.rsmsdm.algorithmUtils;

import com.github.netopt.rsmsdm.algorithm.ChannelFinder;
import com.github.netopt.rsmsdm.network.Demand;
import com.github.netopt.rsmsdm.network.DemandCandidatePath;
import com.github.netopt.rsmsdm.spectrum.Channel;
import com.github.netopt.rsmsdm.spectrum.Spectrum;

import java.util.List;

public class AlgorithmHelpers {

    public static void locateDemandsOnBestPath(List<Demand> demands, Spectrum spectrum) {
        for (var demand : demands) {
            int bestPathValue = Integer.MAX_VALUE;
            Channel currentChannel = demand.getChannel();
            int bestPathNumber = 0;
            for (int k = 0; k < demand.getNrOfDemandCandidatePaths(); k++) {

                var path = demand.getDemandCandidatePaths().get(k);
                int currentValue = evaluateCandidatePath(spectrum, demand, path);

                if (currentValue < bestPathValue) {
                    bestPathValue = currentValue;
                    bestPathNumber = k;
                }
            }

            var demandCandidatePath = demand.getDemandCandidatePaths().get(bestPathNumber);
            Channel channel = ChannelFinder.findLowestOnPath(spectrum, demand, demandCandidatePath);

            demand.setChannel(channel);

            spectrum.allocate(channel);
        }
    }

    public static void locateDemandsOnFirstPath(List<Demand> demands, Spectrum spectrum) {
        for (var demand : demands) {

            var demandCandidatePath = demand.getDemandCandidatePaths().get(0);
            Channel channel = ChannelFinder.findLowestOnPath(spectrum, demand, demandCandidatePath);

            demand.setChannel(channel);

            spectrum.allocate(channel);
        }
    }

    public static int evaluateCandidatePath(Spectrum spectrum, Demand demand, DemandCandidatePath demandCandidatePath) {
        Channel channel = ChannelFinder.findLowestOnPath(spectrum, demand, demandCandidatePath);
        return channel.getStartIndex() + channel.getSize();
    }
}
