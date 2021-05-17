package com.github.netopt.rsmsdm.network;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * @author plechowicz
 * created on 18.02.19.
 */
public class Graph {

	private final int nrOfEdges;
	private final int nrOfNodes;

	private int[][] graphMatrix;

	private List<Edge> edges;
	private List<Node> nodes;

	public Graph(int[][] graphMatrix, List<Node> nodes, List<Edge> edges) {
		this.graphMatrix = graphMatrix;

		this.nodes = new ArrayList<>(nodes);
		this.edges = new ArrayList<>(edges);

		this.nrOfNodes = nodes.size();
		this.nrOfEdges = edges.size();
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public int getNrOfEdges() {
		return nrOfEdges;
	}

	public int getNrOfNodes() {
		return nrOfNodes;
	}

	public int[][] getGraphMatrix() {
		return graphMatrix;
	}

	@Override
	public String toString() {
		StringBuffer result = new StringBuffer(100);
		for (int[] rows : graphMatrix) {
			for (int cell : rows) {
				result.append(cell + " ");
			}
			result.append("\n");

		}
		return result.toString();
	}

	public Map<Integer, ? extends Edge> getEdgesMap() {
		return getEdges().stream().collect(Collectors.toMap(Edge::getName, e -> e));
	}

	public static Graph createFromMatrix(int[][] graphMatrix) {
		List<Node> nodes = new ArrayList<>();

		for (int j = 0; j < graphMatrix.length; j++) {
			nodes.add(new Node(j));
		}

		List<Edge> edges = new ArrayList<>();
		for (int j = 0; j < graphMatrix.length; j++) {
			for (int i = 0; i < graphMatrix[j].length; i++) {
				int distance = graphMatrix[j][i];
				if (distance > 0) {
					Edge edge = new Edge(edges.size(), j, i, distance);
					edges.add(edge);
					nodes.get(j).addOutgoingEdge(edge);
					nodes.get(i).addIncomingEdge(edge);
				}
			}
		}
		return new Graph(graphMatrix, nodes, edges);
	}
}
