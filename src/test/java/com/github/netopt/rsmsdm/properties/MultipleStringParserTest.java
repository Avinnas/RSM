package com.github.netopt.rsmsdm.properties;

import com.github.netopt.rsmsdm.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author plechowicz
 * created on 22.02.19.
 */
public class MultipleStringParserTest {

	MultipleStringParser parser;

	@Before
	public void setUp() {
		parser = new MultipleStringParser();
	}

	@Test
	public void canParseSingleString() throws ParsingValueException {
		String text = " singleLine  ";
		List<String> expectedOutput = Arrays.asList("singleLine");
		List<String> parse = parser.parse(text, List.class);
		TestUtils.compareLists(expectedOutput, parse);
		text = "singleLine";
		parse = parser.parse(text, List.class);
		TestUtils.compareLists(expectedOutput, parse);
		text = "singleLine			";
		parse = parser.parse(text, List.class);
		TestUtils.compareLists(expectedOutput, parse);
		text = "			singleLine";
		parse = parser.parse(text, List.class);
		TestUtils.compareLists(expectedOutput, parse);
	}

	@Test
	public void canParseSpaceSeparatedWords() throws ParsingValueException {
		String text = " single double, ";
		List<String> expected = Arrays.asList("single", "double");
		List<String> parse = parser.parse(text, List.class);
		TestUtils.compareLists(expected, parse);
	}

	@Test
	public void canParseQuotationSurroundedWord() throws ParsingValueException {
		String text = " ' first word ', \" second \"";
		List<String> expected = Arrays.asList(" first word ", " second ");
		List<String> parse = parser.parse(text, List.class);
		TestUtils.compareLists(expected, parse);
	}
}