package com.github.netopt.rsmsdm.network;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author plechowicz
 * created on 18.02.19.
 */
public final class DemandsFactory {

	private DemandsFactory() {
		// intentionally private and empty
	}

	private final static int GUARDBAND = 1;

	public static List<Demand> createFromFile(String demandsPath, String candidatesPathsPath, String slotsPath, long summaryBitrate, int numberOfCandidatePaths, int maxNumberOfCandidatePaths, Graph graph) throws NetworkFilesReadingException {
		NetworkCandidatePaths candidatePathsForNetworkFromFile = NetworkCandidatePathsFactory.createCandidatePathsForNetworkFromFile(candidatesPathsPath, maxNumberOfCandidatePaths, graph);
		CandidatePathSlots slotsForCandidatePathsFromFile = CandidatePathSlotsFactory.createSlotsForCandidatePathsFromFile(slotsPath, maxNumberOfCandidatePaths, graph.getNrOfNodes());
		return createFromFile(demandsPath, candidatePathsForNetworkFromFile, slotsForCandidatePathsFromFile, summaryBitrate, numberOfCandidatePaths);
	}

	public static List<Demand> createFromFile(String path, NetworkCandidatePaths candidatePathsForNetwork, CandidatePathSlots slotsForCandidatePaths, long summaryBitrate, int numberOfCandidatePaths) throws NetworkFilesReadingException {
		if (candidatePathsForNetwork.getMaxNumberOfCandidatePaths() != slotsForCandidatePaths.getMaxNrOfCandidatePaths()) {
			throw new NetworkFilesReadingException(String.format("max number of path candidates differ in candidatePathsForNetwork file and in slotsForCandidatePaths file while reading demands file={%s}", path));
		}
		if (summaryBitrate < 0) {
			throw new NetworkFilesReadingException("Summary bitrate cannot be below 0");
		}
		var currentSummaryBitrate = 0;
		int demandId = 0;
		List<Demand> demands = new ArrayList<>();
		try (Scanner scanner = new Scanner(new File(path))) {
			var nrOfRequests = scanner.nextInt();
			while (currentSummaryBitrate < summaryBitrate) {
				if (!scanner.hasNext()) {
					throw new NetworkFilesReadingException(String.format("File={%s} is to short for provided summary bitrate={%d}", path, summaryBitrate));
				}
				var sourceNode = scanner.nextInt();
				var destinationNode = scanner.nextInt();
				var bitrate = scanner.nextInt();

				List<DemandCandidatePath> candidatePathsForDemands = new ArrayList<>();
				List<CandidatePath> candidatePathsForSourceDestination = candidatePathsForNetwork.getCandidatePathsForSourceDestination(sourceNode, destinationNode);

				if (numberOfCandidatePaths > candidatePathsForSourceDestination.size()) {
					throw new NetworkFilesReadingException(String.format("The requested number of candidate paths for demand file={%s} is to big. It is " +
									"equal to={%d} while the NetworkCandidatePaths is loaded with max value of candidate paths equal to={%d}",
							path, numberOfCandidatePaths, candidatePathsForSourceDestination.size()));
				}

				for (int pathId = 0; pathId < numberOfCandidatePaths; pathId++) {
					CandidatePath candidatePath = candidatePathsForSourceDestination.get(pathId);
					int slices = slotsForCandidatePaths.getRequiredNumberOfSlots(sourceNode, destinationNode, pathId, bitrate);
					DemandCandidatePath candidatePathForDemand = new DemandCandidatePath(candidatePath, slices + GUARDBAND);
					candidatePathsForDemands.add(candidatePathForDemand);
				}
				Demand demand = new Demand(demandId, bitrate, sourceNode, destinationNode, candidatePathsForDemands);
				demandId++;
				currentSummaryBitrate += bitrate;
				demands.add(demand);
			}
			return demands;
		} catch (FileNotFoundException e) {
			throw new NetworkFilesReadingException("File not found", e);
		} catch (Exception e) {
			throw new NetworkFilesReadingException("other exception occurred", e);
		}
	}
}
