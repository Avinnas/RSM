package com.github.netopt.rsmsdm.properties;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses list of space separated strings, each may contain embedded integer range.
 * Each string may use following format:
 * <p></p>
 * {@code
 * <prefix>[<start>:<stop>[:<step>]]<suffix>
 * }
 * <p></p>
 * where: <br/>
 * <table valign="top">
 * <tr>
 * <td><code>prefix   </code></td><td>string prefix of file name</td>
 * </tr>
 * <tr>
 * <td><code>suffix </code></td><td>string suffix of file name</td>
 * </tr>
 * <tr>
 * <td><code>start  </code></td><td>the start of range (optional)</td>
 * </tr>
 * <tr>
 * <td><code>stop</code></td><td>the end of range (optional). If it is not provided
 * starting range is treated as string prefix of file name, i.e., no range between files
 * is generated</td>
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
 * <code>:</code> or <code>..</code> or <code>-</code>
 * <p></p>
 * Note, negative numbers are not supported. The formatting preserves leading zeros according
 * to the number of characters in start range. For more details, see below examples.
 * <p></p>
 * Below are presented parsing examples:
 * <p></p>
 * input: " file.dat   ",  	result: {"file.dat"} <br/>
 * input: "file1.dat	file2.dat "	result: {"file1.dat", "file2.dat"} <br/>
 * input: " file0:2.dat ",		result: {"file0.dat", "file1.dat", "file2.dat"}	<br/>
 * input: " file05:07.dat ", result: {"file05.dat", "file06.dat", "file07.dat"} <br/>
 * input: "file001:010:3", result: {"file001", "file004", "file007", "file010"} <br/>
 *
 * @author plechowicz
 * created on 23.02.19.
 */
public class MultipleStringRangeParser implements StringParser<List> {

	private static final int STRING_WITHOUT_NUMBERS = 1;
	private static final int STRING_WITH_NUMBERS = 2;
	private static final int STRING_WITH_NUMBERS_CHAR_PREFIX = 3;
	private static final int STRING_WITH_NUMBERS_START_RANGE = 4;
	private static final int STRING_WITH_NUMBERS_END_RANGE = 5;
	private static final int STRING_WITH_NUMBERS_STEP_RANGE = 6;
	private static final int STRING_WITH_NUMBERS_CHAR_SUFFIX = 7;

	/**
	 * Extracts multiple strings based on integer range from string. The string should have removed
	 * trailing and leading whitespaces. Note, in order to process
	 * space separated strings, other pattern is called first in {@link MultipleStringParser}.
	 * $1 - parsed string, if it does not contain any numbers
	 * $2 - not parsed string, if it does contain any number
	 * $3 - name prefix, i.e., string before first occurrence of number
	 * $4 - if range present, start range value
	 * $5 - if range present, end range value
	 * $6 - if range present, step range value
	 * $7 - string suffix, i.e., string after number occurrences. If String contains
	 * only single number, it is sufficient to use prefix + suffix, or not parsed string (value $2)
	 */
	static final Pattern pattern = Pattern.compile(
			"# strip files range within string 									\n" +
					"(^[a-zA-Z\\-_.:]+$)	# $1: string without numbers				\n" +
					"|( 					# $2: alternatively string with numbers 	\n" +
					"(^[a-zA-Z\\-_.]*) 		# $3: string character prefix prefix 		\n" +
					"(?:					# start files range (optional)				\n" +
					"(\\d+)					# $4: start range integer 					\n" +
					"(?:[:|-]|\\.{2}) 		# : - or .. separates ranges				\n" +
					"(\\d+) 				# $5: end range integer						\n" +
					"(?:					# start range step (optional)				\n" +
					"(?:[:|-]|\\.{2})		# : - or .. separates step 					\n" +
					"(\\d+)					# $6: step integer							\n" +
					")? 					# end range step (optional)					\n" +
					")?						# end files range (optional)				\n" +
					"([a-zA-Z0-9\\-_.]*$)	# $7 remaining part of string				\n" +
					") 						# string with numbers end"
			, Pattern.COMMENTS
	);

	@Override
	public List parse(String text, Class<? extends  List> clazz) throws ParsingValueException {
		// split strings based on white space and quotation marks
		// consider extracting those two parsers
		List<String> listOfStrings = new MultipleStringParser().parse(text, List.class);
		List<String> results = new ArrayList<>();
		for (var singleString : listOfStrings) {
			// parse each single string
			Matcher matcher = pattern.matcher(singleString);
			if (matcher.find()) {
				if (matcher.group(STRING_WITHOUT_NUMBERS) != null) {
					// string without numbers, parse as it is
					results.add(matcher.group(STRING_WITHOUT_NUMBERS));
				} else if (matcher.group(STRING_WITH_NUMBERS) != null) {
					// string contains some numbers
					if (matcher.group(STRING_WITH_NUMBERS_START_RANGE) == null) {
						// string without number range, parse as it is
						results.add(matcher.group(STRING_WITH_NUMBERS));
					} else {
						// string containing range
						results.addAll(parseStringWithRange(matcher));
					}
				}
			}
		}
		return results;
	}

	private List<String> parseStringWithRange(Matcher matcher) throws ParsingValueException {
		List<String> results = new ArrayList<>();
		// get characters to preserve leading 0s in generated file names
		int numberOfCharacters = matcher.group(STRING_WITH_NUMBERS_START_RANGE).length();
		// create string formatter with leading 0s based on number of characters
		String leadingZerosFormat = "%0" + numberOfCharacters + "d";
		Long start = Long.parseLong(matcher.group(STRING_WITH_NUMBERS_START_RANGE));
		Long stop = Long.parseLong(matcher.group(STRING_WITH_NUMBERS_END_RANGE));
		Long step = null;
		// step group may be null
		if (matcher.group(STRING_WITH_NUMBERS_STEP_RANGE) != null) {
			step = Long.parseLong(matcher.group(STRING_WITH_NUMBERS_STEP_RANGE));
		}
		// generate ids of file names
		List<Long> filesIds = LongRangeParser.generateListOfIntegers(start, stop, step);
		String prefix = matcher.group(STRING_WITH_NUMBERS_CHAR_PREFIX);
		String suffix = matcher.group(STRING_WITH_NUMBERS_CHAR_SUFFIX);
		for (var value : filesIds) {
			// for each value generate file name with appropriate formatting
			String fileName = prefix + String.format(leadingZerosFormat, value) + suffix;
			results.add(fileName);
		}
		return results;
	}
}
