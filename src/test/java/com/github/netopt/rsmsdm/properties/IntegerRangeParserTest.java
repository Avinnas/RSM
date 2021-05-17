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
public class IntegerRangeParserTest {

	private IntegerRangeParser parser;

	@Before
	public void setUp() {
		parser = new IntegerRangeParser();
	}

	@Test
	public void canParseSingleInteger() throws ParsingValueException {
		String text = " 2 ";
		List<Integer> expectedOutput = Arrays.asList(2);
		List<Integer> parse = parser.parse(text, List.class);
		TestUtils.compareLists(expectedOutput, parse);
		text = "2";
		parse = parser.parse(text, List.class);
		TestUtils.compareLists(expectedOutput, parse);

		text = "20_00";
		expectedOutput = Arrays.asList(2000);
		parse = parser.parse(text, List.class);
		TestUtils.compareLists(expectedOutput, parse);
	}

	@Test
	public void canParseMultipleIntegers() throws ParsingValueException {
		String text = " 2 5 7";
		List<Integer> expectedOutput = Arrays.asList(2, 5, 7);
		List<Integer> parse = parser.parse(text, List.class);
		TestUtils.compareLists(expectedOutput, parse);
	}

	@Test
	public void canParsePositiveIntegerRange() throws ParsingValueException {
		String text = " 2:5  -15:-10:2";
		List<Integer> expectedOutput = Arrays.asList(2, 3, 4, 5, -15, -13, -11);
		List<Integer> parse = parser.parse(text, List.class);
		TestUtils.compareLists(expectedOutput, parse);
	}

	@Test
	public void canParseNegativeIntegerRange() throws ParsingValueException {
		String text = " 2:-2 -20:-26:-4";
		List<Integer> expectedOutput = Arrays.asList(2, 1, 0, -1, -2, -20, -24);
		List<Integer> parse = parser.parse(text, List.class);
		TestUtils.compareLists(expectedOutput, parse);
	}

	@Test(expected = ParsingValueException.class)
	public void canThrowWhenStepSizeInWrongDirection() throws ParsingValueException {
		String text = " 2:4:-1 ";
		List<Integer> parse = parser.parse(text, List.class);
	}
}