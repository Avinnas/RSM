package com.github.netopt.rsmsdm.spectrum;

import com.github.netopt.rsmsdm.network.CandidatePath;
import com.github.netopt.rsmsdm.network.DemandCandidatePath;
import com.github.netopt.rsmsdm.network.Demand;
import com.github.netopt.rsmsdm.network.Edge;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author plechowicz
 * created on 18.02.19.
 */
public class CoreTest {

	private Core core;

	@Before
	public void setUp() {
		core = new Core();
	}
	
	public List<Demand> givenDemandsOnSingleEdge() {
		// create two demands routed on single edge. demand 1 requires 10 slices, demand 2 requires 5 slices.
		Edge e1 = new Edge(1, 1, 2, 1); // dummy edge
		List<DemandCandidatePath> cd1 = new ArrayList<>();
		CandidatePath cd1_1 = new CandidatePath(Arrays.asList(e1), Arrays.asList(1), 1, 2);
		cd1.add(new DemandCandidatePath(cd1_1, 10));
		Demand d1 = new Demand(1, 100, 1, 2, cd1);

		List<DemandCandidatePath> cd2 = new ArrayList<>();
		CandidatePath cd1_2 = new CandidatePath(Arrays.asList(e1), Arrays.asList(1), 1, 2);
		cd1.add(new DemandCandidatePath(cd1_2, 5));
		Demand d2 = new Demand(2, 200, 1, 2, cd2);

		List<Demand> demands = new ArrayList<>();
		demands.add(d1);
		demands.add(d2);
		return demands;
	}

	public void givenAllocatedSlicesOnCore() {
		core.allocateByRange(4, 7, 2);
		core.allocateByRange(10, 13, 2);
	}

	@Test
	public void canAllocateChannel() {
		Core core = new Core();
		List<Demand> demands = givenDemandsOnSingleEdge();
		Channel channel = new Channel(demands.get(0), demands.get(0).getDemandCandidatePaths().get(0), 0);
		Channel channel2 = new Channel(demands.get(0), demands.get(0).getDemandCandidatePaths().get(0), 20);
		core.allocate(channel);
		core.allocate(channel2);
	}

	@Test
	public void canFindFirstFreeSize() {
		givenAllocatedSlicesOnCore();
		int index = core.findFirstFreeSize(3);
		assertEquals(0, index);
		index = core.findFirstFreeSize(5);
		assertEquals(13, index);
	}

	@Test
	public void nrOfAllocatedSlicesChangeAfterAllocation() {
		int cardinality = core.amountOfAllocatedSlices();
		core.allocateByRange(0, 3, 2);
		int newCardinality = core.amountOfAllocatedSlices();
		assertEquals(3, newCardinality - cardinality);
	}

	@Test
	public void searchesNextRegionAfterAllocation() {
		givenAllocatedSlicesOnCore();
		int index = core.findFirstFreeSize(3);
		core.allocateByRange(index, index + 3, 2);
		int newIndex = core.findFirstFreeSize(3);
		assertEquals(newIndex, 7);
	}

	@Test
	public void changeTotalSizeAfterAllocation() {
		givenAllocatedSlicesOnCore();
		int size = core.lastAllocatedSlice();
		int index = core.findFirstFreeSize(5);
		core.allocateByRange(index, index + 5, 2);
		int newSize = core.lastAllocatedSlice();
		assertEquals(5, newSize - size);
		assertEquals(18, newSize);
	}

	@Test(expected = IllegalStateException.class)
	public void throwsWhenAllocatingMoreThanOnceSameSlice() {
		givenAllocatedSlicesOnCore();
		core.allocateByRange(4, 5, 2);
	}

	@Test
	public void findFirstFreeSizeForManyCores() {
		givenAllocatedSlicesOnCore();
		Core core2 = new Core();
		Core core3 = new Core();

		core2.allocateByRange(0, 3, 2);
		core2.allocateByRange(5, 8, 3);
		core3.allocateByRange(4, 6, 4);
		core3.allocateByRange(11, 14, 5);

		int index = CoreUtils.findFirstFreeSize(2, core, core2, core3);
		assertEquals(8, index);

		index = CoreUtils.findFirstFreeSize(3, core, core2, core3);
		assertEquals(14, index);
	}
}