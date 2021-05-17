package com.github.netopt.rsmsdm.network;

import java.util.Objects;

/**
 * @author plechowicz
 * created on 18.02.19.
 */
public class Edge {

	private final int name;
	private final int sourceNode;
	private final int destinationNode;
	private final int distance;

	public Edge(int name, int sourceNode, int destinationNode, int distance) {
		this.name = name;
		this.sourceNode = sourceNode;
		this.destinationNode = destinationNode;
		this.distance = distance;
	}

	public int getName() {
		return name;
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
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Edge edge = (Edge) o;
		return name == edge.name &&
				sourceNode == edge.sourceNode &&
				destinationNode == edge.destinationNode &&
				distance == edge.distance;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, sourceNode, destinationNode, distance);
	}
}
