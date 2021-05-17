package com.github.netopt.rsmsdm.network;

import org.junit.Test;

/**
 * @author plechowicz
 * created on 18.02.19.
 */
public class GraphIT {

	private String relativePath = "./networks-data/US26/ss.net";
	private String path = getClass().getClassLoader().getResource(relativePath).getPath();

	@Test
	public void canCreateGraph() throws NetworkFilesReadingException {
		System.out.println(path);
		var graph = GraphFactory.createFromFile(path);
		System.out.println(graph.toString());
	}
}
