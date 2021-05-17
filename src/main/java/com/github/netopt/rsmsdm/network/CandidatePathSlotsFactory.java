package com.github.netopt.rsmsdm.network;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author plechowicz
 * created on 18.02.19.
 */
public final class CandidatePathSlotsFactory {

	private static LoadingCache<Key, CandidatePathSlots> cache = CacheBuilder.newBuilder().maximumSize(1).build(
			new CacheLoader<>() {
				@Override
				public CandidatePathSlots load(Key key) throws NetworkFilesReadingException {
					return loadSlotsForCandidatePathsFromFile(key.path, key.maxNrOfCandidatePathsForNodePair, key.nrOfNodes);
				}
			}
	);


	private CandidatePathSlotsFactory() {
		// intentionally left blank
	}

	public static CandidatePathSlots createSlotsForCandidatePathsFromFile(String path, int maxNrOfCandidatePathsForNodePair, int nrOfNodes) {
		Key key = new Key();
		key.maxNrOfCandidatePathsForNodePair = maxNrOfCandidatePathsForNodePair;
		key.path = path;
		key.nrOfNodes = nrOfNodes;
		return cache.getUnchecked(key);
	}

	private static CandidatePathSlots loadSlotsForCandidatePathsFromFile(
			String path, int maxNrOfCandidatePaths, int nrOfNodes) throws NetworkFilesReadingException {
		try (Scanner scanner = new Scanner(new File(path))) {
			List<List<List<List<Integer>>>> slots = new ArrayList<>();
			for (var sourceNode = 0; sourceNode < nrOfNodes; sourceNode++) {
				List<List<List<Integer>>> slotsSource = new ArrayList<>();
				for (var destinationNode = 0; destinationNode < nrOfNodes; destinationNode++) {
					if (sourceNode != destinationNode) {
						List<List<Integer>> slotsSourceDestination = new ArrayList<>();
						for (var pathId = 0; pathId < maxNrOfCandidatePaths; pathId++) {
							if (!scanner.hasNext()) {
								throw new NetworkFilesReadingException("file " + path + " is either to short, or wrong number of nodes and maxCandidatePaths is provided");
							}
							String slotsString = scanner.nextLine().strip();
							String[] slotsSourceDestinationPaths = slotsString.split("\\s+");
							slotsSourceDestination.add(
									Arrays.asList(slotsSourceDestinationPaths).stream().map(Integer::parseInt).collect(Collectors.toList())
							);
						}
						slotsSource.add(slotsSourceDestination);
					} else {
						slotsSource.add(Collections.emptyList());
					}
				}
				slots.add(slotsSource);
			}
			scanner.close();
			return new CandidatePathSlots(slots, maxNrOfCandidatePaths);
		} catch (FileNotFoundException e) {
			throw new NetworkFilesReadingException(String.format("Exception in reading file={%s}", path), e);
		} catch (Exception e) {
			throw new NetworkFilesReadingException(String.format("Exception in reading file={%s}", path), e);
		}
	}

	private static class Key {
		private String path;
		private int maxNrOfCandidatePathsForNodePair;
		private int nrOfNodes;

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			Key key = (Key) o;
			return maxNrOfCandidatePathsForNodePair == key.maxNrOfCandidatePathsForNodePair &&
					nrOfNodes == key.nrOfNodes &&
					Objects.equals(path, key.path);
		}

		@Override
		public int hashCode() {
			return Objects.hash(path, maxNrOfCandidatePathsForNodePair, nrOfNodes);
		}
	}
}
