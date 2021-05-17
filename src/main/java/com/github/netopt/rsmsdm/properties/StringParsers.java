package com.github.netopt.rsmsdm.properties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author plechowicz
 * created on 20.02.19.
 */
public class StringParsers {

	public static final Map<Class<?>, StringParser<?>> register = new HashMap<>();

	/*
	 * Assign default parsers
	 */
	static {
		register.put(String.class, (string, clazz) -> string);
		register.put(Short.class, (string, clazz) -> Short.valueOf(string));
		register.put(Integer.class, (string, clazz) -> Integer.valueOf(string.replaceAll("_", "")));
		register.put(Long.class, (string, clazz) -> Long.valueOf(string.replaceAll("_","")));
		register.put(Float.class, (string, clazz) -> Float.valueOf(string));
		register.put(Double.class, (string, clazz) -> Double.valueOf(string));
		register.put(Boolean.class, (string, clazz) -> Boolean.valueOf(string));
		register.put(MultipleStringParser.class, (text, clazz) -> new MultipleStringParser().parse(text, List.class));
		register.put(MultipleStringRangeParser.class, (text, clazz) -> new MultipleStringRangeParser().parse(text, List.class));
		register.put(LongRangeParser.class, (text, clazz) -> new LongRangeParser().parse(text, List.class));
		register.put(IntegerRangeParser.class, (text, clazz) -> new IntegerRangeParser().parse(text, List.class));
	}

	public static <T> void registerStringParser(Class<T> clazz, StringParser<T> stringParser) {
		register.put(clazz, stringParser);
	}

	@SuppressWarnings("unchecked")
	public static <T> StringParser<T> getStringParser(Class<T> clazz) {
		return (StringParser<T>) register.get(clazz);
	}

	public static <T> T parse(String stringToParse, Class<T> clazz, String errorMessage) throws ParserNotFoundException, ParsingValueException {
		StringParser<T> stringParser = getStringParser(clazz);
		if (stringParser == null) {
			throw new ParserNotFoundException(String.format("Parser not found. String to parse={%s}, parser for class={%s}, errorMessage={%s}", stringParser, clazz, errorMessage));
		}
		return stringParser.parse(stringToParse, clazz, errorMessage);
	}
}
