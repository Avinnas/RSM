package com.github.netopt.rsmsdm;

import org.junit.Assert;

import java.util.Iterator;
import java.util.List;

/**
 * @author plechowicz
 * created on 22.02.19.
 */
public class TestUtils {
	public static <T> void compareLists(List<T> expectedList, List<T> actualList) {
		Assert.assertEquals(expectedList.size(), actualList.size());
		Iterator<T> exIt = expectedList.iterator();
		Iterator<T> acIt = actualList.iterator();
		while (exIt.hasNext() && acIt.hasNext()) {
			Assert.assertEquals(exIt.next(), acIt.next());
		}
	}
}
