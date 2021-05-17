package com.github.netopt.rsmsdm.network;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author plechowicz
 * created on 18.02.19.
 */
public final class GraphFactory {

	private static LoadingCache<String, Graph> cache = CacheBuilder.newBuilder()
			.maximumSize(1).build(
					new CacheLoader<>() {
						@Override
						public Graph load(String path) throws NetworkFilesReadingException {
							return loadFromFile(path);
						}
					}
			);

	private GraphFactory() {
		// intentionally private and empty
	}

	public static Graph createFromFile(String path) {
		return cache.getUnchecked(path);
	}

	private static Graph loadFromFile(String path) throws NetworkFilesReadingException {
		try (Scanner scanner = new Scanner(new File(path))) {
			var nrOfNodes = scanner.nextInt();
			var nrOfEdges = scanner.nextInt();
			int graphMatrix[][] = new int[nrOfNodes][nrOfNodes];
			for (var i = 0; i < nrOfNodes; i++) {
				for (var j = 0; j < nrOfNodes; j++) {
					graphMatrix[j][i] = scanner.nextInt();
				}
			}
			return Graph.createFromMatrix(graphMatrix);
		} catch (IOException e) {
			throw new NetworkFilesReadingException(String.format("Exception in reading file={%s}", path), e);
		}
	}
}
