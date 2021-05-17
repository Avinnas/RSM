package com.github.netopt.rsmsdm.properties;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author plechowicz
 * created on 23.02.19.
 */
public class IntegerRangeParser extends LongRangeParser {

	@Override
	public List<Integer> parse(String text, Class<? extends List> clazz) throws ParsingValueException {
		List<Long> longs = super.parse(text, clazz);
		return longs.stream().mapToInt(l -> (int)(long) l).boxed().collect(Collectors.toList());
	}
}
