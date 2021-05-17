package com.github.netopt.rsmsdm.network;

import java.util.List;

/**
 * @author plechowicz
 * created on 18.02.19.
 */
public class CandidatePath {

	private List<Edge> edges;
	private List<Integer> edgesIndices; // list of indices for edges in graph which are included in this candidate path
	private int sourceNode;
	private int destinationNode;
	private int distance;

	public CandidatePath(List<Edge> edges, List<Integer> edgesIndices, int sourceNode, int destinationNode) {
		this.edges = edges;
		this.edgesIndices = edgesIndices;
		this.sourceNode = sourceNode;
		this.destinationNode = destinationNode;
		distance = edges.stream().mapToInt(Edge::getDistance).sum();
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public List<Integer> getEdgesIds() {
		return edgesIndices;
	}

	public int getSourceNode() {
		return sourceNode;
	}

	public int getDestinationNode() {
		return destinationNode;
	}

	public int getDistance() {
		return distance;
	}

	@Override
	public String toString() {
		StringBuffer result = new StringBuffer(100);
		result.append(sourceNode);
		result.append(" -> ");
		result.append(destinationNode);
		result.append(" (");
		result.append(distance);
		result.append(" km): ");
		edges.forEach(e -> {
			result.append(e.getName());
			result.append(" ");
		});
		return result.toString();
	}
}
