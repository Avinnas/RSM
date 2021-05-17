package com.github.netopt.rsmsdm.properties;

import com.github.netopt.rsmsdm.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author plechowicz
 * created on 23.02.19.
 */
public class MultipleStringRangeParserTest {

	private MultipleStringRangeParser parser;

	@Before
	public void setUp() {
		parser = new MultipleStringRangeParser();
	}

	@Test
	public void canParseSingleStringWithoutNumbers() throws ParsingValueException {
		String text = " file.name  ";
		List<String> expected = Arrays.asList("file.name");
		List<String> parse = parser.parse(text, List.class);
		TestUtils.compareLists(expected, parse);
		text = "file.name";
		parse = parser.parse(text, List.class);
		TestUtils.compareLists(expected, parse);
		text = "         file.name";
		parse = parser.parse(text, List.class);
		TestUtils.compareLists(expected, parse);
		text = "file.name         ";
		parse = parser.parse(text, List.class);
		TestUtils.compareLists(expected, parse);
	}

	@Test
	public void canParseMultipleStringWithoutNumbers() throws ParsingValueException {
		String text = " file.name second.name  ";
		List<String> expected = Arrays.asList("file.name", "second.name");
		List<String> parse = parser.parse(text, List.class);
		TestUtils.compareLists(expected, parse);
		text = "file.name, second.name  ";
		parse = parser.parse(text, List.class);
		TestUtils.compareLists(expected, parse);
		text = "  file.name ,second.name";
		parse = parser.parse(text, List.class);
		TestUtils.compareLists(expected, parse);
		text = "file.name	,	 second.name ";
		parse = parser.parse(text, List.class);
		TestUtils.compareLists(expected, parse);
	}

	@Test
	public void canParseStringWithSingleNumber() throws ParsingValueException {
		String text = " file01.name, second3.name  \"00.file\"";
		List<String> expected = Arrays.asList("file01.name", "second3.name", "00.file");
		List<String> parse = parser.parse(text, List.class);
		TestUtils.compareLists(expected, parse);
		text = "file01.name, \'second3.name\'  00.file";
		parse = parser.parse(text, List.class);
		TestUtils.compareLists(expected, parse);
	}

	@Test
	public void canParseStringWtihRangeWithoutStep() throws ParsingValueException {
		String text = "file00-03.dem   004:006.asdf		";
		List<String> expected = Arrays.asList(
				"file00.dem",
				"file01.dem",
				"file02.dem",
				"file03.dem",
				"004.asdf",
				"005.asdf",
				"006.asdf"
		);
		List<String> parse = parser.parse(text, List.class);
		TestUtils.compareLists(expected, parse);
	}

	@Test
	public void canParseStringWtihRangeWithStep() throws ParsingValueException {
		String text = "file00-05..2.dem   004:010-3.asdf		";
		List<String> expected = Arrays.asList(
				"file00.dem",
				"file02.dem",
				"file04.dem",
				"004.asdf",
				"007.asdf",
				"010.asdf"
		);
		List<String> parse = parser.parse(text, List.class);
		TestUtils.compareLists(expected, parse);
	}
}