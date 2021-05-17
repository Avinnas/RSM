package com.github.netopt.rsmsdm.network;

import java.util.ArrayList;
import java.util.List;

/**
 * @author plechowicz
 * created on 18.02.19.
 */
public class Node {

	private int name;
	private List<Edge> incomingEdges;
	private List<Edge> outgoingEdges;

	public Node(int name) {
		this.name = name;
		incomingEdges = new ArrayList<>();
		outgoingEdges = new ArrayList<>();
	}

	public List<Edge> getIncomingEdges() {
		return incomingEdges;
	}

	public List<Edge> getOutgoingEdges() {
		return outgoingEdges;
	}

	public void addIncomingEdge(Edge edge) throws IllegalStateException {
		if (incomingEdges.contains(edge))
			throw new IllegalStateException();
		incomingEdges.add(edge);
	}

	public void addIncomingEdges(Edge... edges) throws IllegalStateException {
		for (Edge edge : edges) {
			if (incomingEdges.contains(edge))
				throw new IllegalStateException();
			incomingEdges.add(edge);
		}
	}

	public void addIncomingEdges(List<Edge> edges) throws IllegalStateException {
		if (incomingEdges.containsAll(edges))
			throw new IllegalStateException();
		for (Edge edge : edges) {
			incomingEdges.add(edge);
		}
	}

	public void removeIncomingEdge(Edge edge) {
		incomingEdges.remove(edge);
	}

	public void addOutgoingEdge(Edge edge) throws IllegalStateException {
		if (outgoingEdges.contains(edge))
			throw new IllegalStateException();
		outgoingEdges.add(edge);
	}

	public void addOutgoingEdges(Edge... edges) throws IllegalStateException {
		for (Edge edge : edges) {
			if (outgoingEdges.contains(edge))
				throw new IllegalStateException();
			outgoingEdges.add(edge);
		}
	}

	public void addOutgoingEdges(List<Edge> edges) throws IllegalStateException {
		if (outgoingEdges.containsAll(edges))
			throw new IllegalStateException();
		for (Edge edge : edges) {
			outgoingEdges.add(edge);
		}
	}

	public void removeOutgoingEdge(Edge edge) {
		outgoingEdges.remove(edge);
	}

	public int getName() {
		return name;
	}
}
