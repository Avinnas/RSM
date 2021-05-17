package com.github.netopt.rsmsdm.properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses space separated strings into an array.
 * If white space need to be embedded into string enclose that string with single or double brackets.
 * <p>
 * Below are presented parsing examples:
 * <p></p>
 * input: " text   ",  	result: {"text"} <br/>
 * input: " text1   text2  "	result: {"text1", "text2"} <br/>
 * input: " 'long text', \"second long text \" ",		result: {"long text", "second long text "}	<br/>
 *
 * @author plechowicz
 * created on 22.02.19.
 */
public class MultipleStringParser implements StringParser<List> {

	/**
	 * Extracts next space separated string. If string is enclosed
	 * with single or double bracket, everything inside is parsed.
	 * $1 - simple space separated string
	 * $2 - string within single brackets ($1 = null)
	 * $3 - string within double brackets ($1 = $2 = null)
	 */
	static final Pattern pattern = Pattern.compile(
			"# extract next string value.	\n" +
					"([\\w_\\-.:]+)		# $1: Simple word \n" +
					"|(?:(?:\')		# or single bracket opening \n" +
					"(.*?)			# $2: Anything between single brackets \n" +
					"(?:\'))		# single bracket closing \n" +
					"|(?:(?:\")		# or double bracket opening \n" +
					"(.*?)			# $3: Anything between double brackets \n" +
					"(?:\"))		# double bracket closing. \n",
			Pattern.COMMENTS);

	@Override
	public List<String> parse(String text, Class<? extends List> clazz) throws ParsingValueException {
		List<String> result = new ArrayList<>();
		Matcher m = pattern.matcher(text);
		while (m.find()) {
			List<Integer> groups = Arrays.asList(1, 2, 3);
			String value = null;
			for (var groupId : groups) {
				if (m.group(groupId) != null) {
					value = m.group(groupId);
					break;
				}
			}
			if (value == null) {
				throw new ParsingValueException(String.format("Incorrect input format, regex didnt found any matching group, pattern={%s}", pattern.toString()));
			}
			result.add(value);
		}
		return result;
	}
}
