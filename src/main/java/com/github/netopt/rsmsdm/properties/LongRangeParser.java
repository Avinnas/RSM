package com.github.netopt.rsmsdm.properties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses list of space separated integers into an array.
 * Each integer may use following format:
 * <p></p>
 * {@code
 * <start>[:<stop>[:<step>]]
 * }
 * <p></p>
 * where: <br/>
 * <table valign="top">
 * <tr>
 * <td><code>start  </code></td><td>the start of range</td>
 * </tr>
 * <tr>
 * <td><code>stop</code></td><td>the end of range (optional).
 * If not provided single value is returned specified by start</td>
 * </tr>
 * <tr>
 * <td><code>step</code></td><td>the step size between start and stop range
 * (optional). If not provided the step size is assumed to be equal to 1 or -1
 * depending whether start is greater or lower than stop value
 * </td>
 * </tr>
 * </table>
 * <p>
 * As the delimiter between parameters following characters may be used<br/>
 * <code>:</code> or <code>..</code>
 * <p></p>
 * If step size in wrong direction is provided, the {@link ParsingValueException} is thrown.
 * <p></p>
 * Below are presented parsing examples:
 * <p></p>
 * input: " 5   ",  	result: {5} <br/>
 * input: "   3   8 "	result: {3, 8} <br/>
 * input: " 2:5 ",		result: {2, 3, 4, 5}	<br/>
 * input: "2..-1", result: {2, 1, 0, -1} <br/>
 * input: "-2:3:2", result: {-2, 0, 2} <br/>
 * input: "6..0..-2", result: {6, 4, 2, 0} <br/>
 * input: "1:3  -2:-6:-2", result: {1, 2, 3, -2, -4, -6} <br/>
 *
 * @author plechowicz
 * created on 22.02.19.
 */
public class LongRangeParser implements StringParser<List> {

	/**
	 * Extracts next integer range from input string for each find of regex.
	 * The range is represented as "start:stop:step",
	 * For single find following groups are available:
	 * $1 - start of range
	 * $2 - end of range
	 * $3 - step size.
	 * Note, values $2 and $3 are optional, and the delimiter is either ":" or ".."
	 */
	static final Pattern pattern = Pattern.compile(
			"# extract next integer/integers/integer range value. 		\n" +
					"(-?[\\d_]+) 				# $1: integer value 				\n" +
					"(?:				 	# start range for value (optional) 	\n" +
					"(?:[:]|\\.{2})      	# : or .. separates range 		\n" +
					"(-?[\\d_]+) 				# $2: range integer (optional) 		\n" +
					")? 					# end range for value (optional).	\n" +
					"(?: 					# start step for range (optional) 	\n" +
					"(?:[:]|\\.{2}) 		# : or .. separates step			\n" +
					"(-?[\\d_]+) 				# $3: step integer (optional) 		\n" +
					")? 					# end step for range (optional) 	\n"
			, Pattern.COMMENTS
	);

	@Override
	public List parse(String text, Class<? extends List> clazz) throws ParsingValueException {
		List<Long> result = new ArrayList<>();
		Matcher m = pattern.matcher(text);
		while (m.find()) {
			Long start = null, stop = null, step = null;
			if (m.group(1) != null) start = Long.parseLong(m.group(1).replace("_", ""));
			if (m.group(2) != null) stop = Long.parseLong(m.group(2).replace("_", ""));
			if (m.group(3) != null) step = Long.parseLong(m.group(3).replace("_", ""));
			result.addAll(generateListOfIntegers(start, stop, step));
		}
		return result;
	}

	static List<Long> generateListOfIntegers(Long start, Long stop, Long step) throws ParsingValueException {
		if (stop == null) {
			// if stop is null, return single integer
			return Collections.singletonList(start);
		}
		if (step == null) {
			// if step is null, it is equal to 1 or -1 depending on the range positive/negative direction
			step = (start < stop) ? 1L : -1L;
		}
		if ((stop - start) / step < 0) {
			// if step size does not match the positive/negative range direction, e.g., stop value is greater than start value
			// but step size is negative
			throw new ParsingValueException(String.format("Incorrect input format, step is of incorrect sign: start={%d}, stop={%d}, step={%d}", start, stop, step));
		}
		List<Long> result = new ArrayList<>();
		for (long value = start; (start < stop ? value <= stop : value >= stop); value += step) {
			result.add(value);
		}
		return result;
	}
}
