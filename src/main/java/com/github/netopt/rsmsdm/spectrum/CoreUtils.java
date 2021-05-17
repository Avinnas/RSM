package com.github.netopt.rsmsdm.spectrum;

import java.util.List;

/**
 * @author plechowicz
 * created on 18.02.19.
 */
public final class CoreUtils {

	private CoreUtils() {
		// intentionally left blank
	}

	public static int findFirstFreeSize(int size, Core... cores) {
		return findFirstFreeSize(size, 0, cores);
	}

	public static int findFirstFreeSize(int size, List<Core> cores) {
		return findFirstFreeSize(size, cores.toArray(new Core[cores.size()]));
	}

	public static int findFirstFreeSize(int size, int minIndex, List<Core> cores) {
		return findFirstFreeSize(size, minIndex, cores.toArray(new Core[cores.size()]));
	}

	public static int findFirstFreeSize(int size, int minIndex, Core... cores) {
		Core intersection = new Core();
		for (Core core : cores) {
			intersection.slices.or(core.slices);
		}
		return intersection.findFirstFreeSize(size, minIndex);
	}
}
