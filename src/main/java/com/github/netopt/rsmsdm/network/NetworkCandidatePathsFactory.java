package com.github.netopt.rsmsdm.network;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Cached factory for NetworkCandidatePaths
 *
 * @author plechowicz
 * created on 18.02.19.
 */
public final class NetworkCandidatePathsFactory {

	private static LoadingCache<Key, NetworkCandidatePaths> cache = CacheBuilder.newBuilder().maximumSize(1).build(
			new CacheLoader<>() {
				@Override
				public NetworkCandidatePaths load(Key key) throws NetworkFilesReadingException {
					return loadCandidatePathsForNetworkFromFile(key.path, key.maxNrOfCandidatePathsForNodePair, key.graph);
				}
			}
	);

	private NetworkCandidatePathsFactory() {
		// intentionally left blank
	}

	public static NetworkCandidatePaths createCandidatePathsForNetworkFromFile(String path, int maxNrOfCandidatePathsForNodePair, Graph graph) {
		Key key = new Key();
		key.maxNrOfCandidatePathsForNodePair = maxNrOfCandidatePathsForNodePair;
		key.path = path;
		key.nrOfEdges = graph.getNrOfEdges();
		key.nrOfNodes = graph.getNrOfNodes();
		key.graph = graph;
		return cache.getUnchecked(key);
	}

	private static NetworkCandidatePaths loadCandidatePathsForNetworkFromFile(String path, int maxNrOfCandidatePathsForNodePair, Graph graph) throws NetworkFilesReadingException {
		try(Scanner scanner = new Scanner(new File(path))) {
			var nrOfLines = scanner.nextInt();
			scanner.nextLine();
			if (nrOfLines != graph.getNrOfNodes() * (graph.getNrOfNodes() - 1) * maxNrOfCandidatePathsForNodePair) {
				throw new NetworkFilesReadingException("The number of candidate paths in network does not correspond to values of maximum number of " +
						"candidate paths for each node pair and number of network nodes. Check maxNrOfCandidatePathsForNodePairs settings");
			}

			List<List<List<CandidatePath>>> candidatePaths = new ArrayList<>();
			for (var sourceNode = 0; sourceNode < graph.getNrOfNodes(); sourceNode++) {
				List<List<CandidatePath>> candidatePathsSource = new ArrayList<>();
				for (var destinationNode = 0; destinationNode < graph.getNrOfNodes(); destinationNode++) {
					// for each distinct source-destination node pairs
					if (sourceNode != destinationNode) {
						List<CandidatePath> candidatePathsSourceDestination = new ArrayList<>();
						// for each candidate path
						for (var pathId = 0; pathId < maxNrOfCandidatePathsForNodePair; pathId++) {
							// list of edges as 0s and 1s
							String edges = scanner.nextLine();
							String[] split = edges.split("\\s+");
							// fetch edges from graph in considered path
							List<Edge> edgesInPath = new ArrayList<>();
							List<Integer> edgesIndicesInPath = new ArrayList<>();
							for (int edgeId = 0; edgeId < split.length; edgeId++) {
								if (Integer.valueOf(split[edgeId]) == 1) {
									edgesInPath.add(graph.getEdges().get(edgeId));
									edgesIndicesInPath.add(edgeId);
								}
							}
							// create candidate path
							candidatePathsSourceDestination.add(new CandidatePath(edgesInPath, edgesIndicesInPath, sourceNode, destinationNode));
						}
						candidatePathsSource.add(candidatePathsSourceDestination);
					} else {
						candidatePathsSource.add(Collections.emptyList());
					}
				}
				candidatePaths.add(candidatePathsSource);
			}
			return new NetworkCandidatePaths(candidatePaths, maxNrOfCandidatePathsForNodePair);
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
		private int nrOfEdges;
		private Graph graph;

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			Key key = (Key) o;
			return maxNrOfCandidatePathsForNodePair == key.maxNrOfCandidatePathsForNodePair &&
					nrOfNodes == key.nrOfNodes &&
					nrOfEdges == key.nrOfEdges &&
					Objects.equals(path, key.path);
		}

		@Override
		public int hashCode() {
			return Objects.hash(path, maxNrOfCandidatePathsForNodePair, nrOfNodes, nrOfEdges);
		}
	}
}
