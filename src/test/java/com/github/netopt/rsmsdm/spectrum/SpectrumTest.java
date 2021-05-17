package com.github.netopt.rsmsdm.spectrum;

import com.github.netopt.rsmsdm.network.CandidatePath;
import com.github.netopt.rsmsdm.network.Demand;
import com.github.netopt.rsmsdm.network.DemandCandidatePath;
import com.github.netopt.rsmsdm.network.Edge;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author plechowicz
 * created on 19.02.19.
 */
public class SpectrumTest {


	private Spectrum spectrum;
	private List<Fiber> fibers;
	private List<Edge> edges;
	private List<Demand> demands;

	/**
	 * Creates following topology
	 * <pre>
	 * 0 --- 1
	 * |     |
	 * |--2--|
	 * </pre>
	 * where
	 * <pre>
	 * edge 0: (0, 1)
	 * edge 1: (0, 2)
	 * edge 2: (1, 2)
	 * </pre>
	 * Each edge contains 2 spatial resources.
	 *
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		edges = new ArrayList<>();
		edges.add(new Edge(0, 0, 1, 1));
		edges.add(new Edge(1, 0, 2, 1));
		edges.add(new Edge(2, 1, 2, 1));
		spectrum = new Spectrum(edges, 2);
		fibers = spectrum.getFibers();
	}

	private final int nrOfChannels = 3;


	/**
	 * 3 demands:
	 * d1 on edges (1, 2), 4 slices
	 * d2 on edge (0), 7 slices
	 * d3 on edge (0, 1), 7 slices
	 * <p>
	 * <p>
	 * Source and destination node are irrelevant
	 */
	public void givenDemands() {
		demands = new ArrayList<>();
		Demand d1 = new Demand(1, 100, 1, 2, Arrays.asList(
				new DemandCandidatePath(
						new CandidatePath(Arrays.asList(edges.get(1), edges.get(2)), Arrays.asList(1, 2), 1, 2), 4))
		);
		Demand d2 = new Demand(2, 100, 1, 2, Arrays.asList(
				new DemandCandidatePath(
						new CandidatePath(Arrays.asList(edges.get(0)), Arrays.asList(0), 1, 2), 7))
		);
		Demand d3 = new Demand(3, 100, 1, 2, Arrays.asList(
				new DemandCandidatePath(
						new CandidatePath(Arrays.asList(edges.get(0), edges.get(1)), Arrays.asList(0, 1), 1, 2), 7))
		);
		demands.add(d1);
		demands.add(d2);
		demands.add(d3);
	}


	/**
	 * array of channels which can be allocated.
	 * <p>
	 * Channel 0: slices (1, 4) on edges (1, 2) core 0, cost after allocation 5 <br/>
	 * Channel 1: slices (3, 9) on edges (0) core 1, cost after allocation 10 <br/>
	 * Channel 2: slices (2, 8) edges (0, 1) cores (0, 1), cost after allocation 9 <br/>
	 * </p>
	 *
	 * @return
	 */
	public Channel[] givenChannels() {
		Channel[] channels = new Channel[3];
		channels[0] = new Channel(demands.get(0), demands.get(0).getDemandCandidatePaths().get(0), 1);
		channels[0].setSelectedCoreForEdge(edges.get(1), 0);
		channels[0].setSelectedCoreForEdge(edges.get(2), 0);
		channels[1] = new Channel(demands.get(1), demands.get(1).getDemandCandidatePaths().get(0), 3);
		channels[1].setSelectedCoreForEdge(edges.get(0), 1);
		channels[2] = new Channel(demands.get(2), demands.get(2).getDemandCandidatePaths().get(0), 2);
		channels[2].setSelectedCoreForEdge(edges.get(0), 0);
		channels[2].setSelectedCoreForEdge(edges.get(1), 1);
		return channels;
	}

	public int[] givenHighestSliceIndexAfterAllocatingCorrespondingChannel() {
		return new int[]{5, 10, 9};
	}

	;

	@Test
	public void canAllocateChannel() {
		givenDemands();
		Channel[] channels = givenChannels();
		int[] highestIdx = givenHighestSliceIndexAfterAllocatingCorrespondingChannel();
		Assert.assertEquals(0, (int) spectrum.getSpectrumCost());
		spectrum.allocate(channels[0]);
		Assert.assertEquals(highestIdx[0], (int) spectrum.getSpectrumCost());
		spectrum.allocate(channels[1]);
		Assert.assertEquals(Math.max(highestIdx[0], highestIdx[1]), (int) spectrum.getSpectrumCost());
		spectrum.allocate(channels[2]);
		Assert.assertEquals(Math.max(Math.max(highestIdx[0], highestIdx[1]), highestIdx[2]), (int) spectrum.getSpectrumCost());
	}

	@Test
	public void canStoreListOfAllocatedChannels() {
		givenDemands();
		Channel[] channels = givenChannels();
		spectrum.allocate(channels[0]);
		spectrum.allocate(channels[1]);
		List<Channel> allocatedChannels = spectrum.getAllocatedChannels();
		Assert.assertEquals(channels[0], allocatedChannels.get(0));
		Assert.assertEquals(channels[1], allocatedChannels.get(1));
	}

	@Test
	public void canDeallocateChannel() {
		givenDemands();
		Channel[] channels = givenChannels();
		int[] highestIdx = givenHighestSliceIndexAfterAllocatingCorrespondingChannel();
		Assert.assertEquals(0, (int) spectrum.getSpectrumCost());
		spectrum.allocate(channels[0]);
		Assert.assertEquals(highestIdx[0], (int) spectrum.getSpectrumCost());
		spectrum.allocate(channels[1]);
		Assert.assertEquals(Math.max(highestIdx[0], highestIdx[1]), (int) spectrum.getSpectrumCost());
		spectrum.deallocate(channels[1]);
		Assert.assertEquals(highestIdx[0], (int) spectrum.getSpectrumCost());
		spectrum.allocate(channels[2]);
		Assert.assertEquals(Math.max(highestIdx[0], highestIdx[2]), (int) spectrum.getSpectrumCost());
	}

	@Test(expected = IllegalStateException.class)
	public void cannotAllocateTheSameChannelTwice() {
		givenDemands();
		Channel[] channels = givenChannels();
		int[] highestIdx = givenHighestSliceIndexAfterAllocatingCorrespondingChannel();
		Assert.assertEquals(0, (int) spectrum.getSpectrumCost());
		spectrum.allocate(channels[0]);
		spectrum.allocate(channels[0]);
	}

	@Test
	public void canAllocateTheSameChannelAfterDeallocation() {
		givenDemands();
		Channel[] channels = givenChannels();
		int[] highestIdx = givenHighestSliceIndexAfterAllocatingCorrespondingChannel();
		spectrum.allocate(channels[0]);
		spectrum.allocate(channels[1]);
		Assert.assertEquals(Math.max(highestIdx[0], highestIdx[1]), (int) spectrum.getSpectrumCost());
		spectrum.deallocate(channels[1]);
		Assert.assertEquals(highestIdx[0], (int) spectrum.getSpectrumCost());
		spectrum.allocate(channels[1]);
		Assert.assertEquals(Math.max(highestIdx[0], highestIdx[1]), (int) spectrum.getSpectrumCost());
	}
}