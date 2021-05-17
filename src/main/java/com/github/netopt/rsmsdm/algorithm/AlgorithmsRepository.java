package com.github.netopt.rsmsdm.algorithm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author plechowicz
 * created on 20.02.19.
 */
public class AlgorithmsRepository {

	private static Map<String, Algorithm> repository = new HashMap<>();

	public static void register(String name, Algorithm algorithm) {
		repository.put(name, algorithm);
	}

	public static Algorithm getOrElseThrow(String name) {
		Algorithm algorithm = repository.get(name);
		if (algorithm == null) {
			throw new IllegalStateException(String.format("Algorithm with name={%s} not found. " +
					"Either the name is misspelled, or algorithm is not added to the repository. To add the algorithm use " +
					"AlgorithmsRepository#register(String name, Algorithm algorithm) method", name));
		}
		return algorithm;
	}

	public static Algorithm get(String name) {
		return repository.get(name);
	}

	public static Collection<Algorithm> getAll() {
		return repository.values();
	}

	public static void init() {
		repository.put(new FirstFit().getName(), new FirstFit());
	}
}
