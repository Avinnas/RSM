package com.github.netopt.rsmsdm.properties;

/**
 * @author plechowicz
 * created on 20.02.19.
 */
@FunctionalInterface
public interface StringParser<T> {

	T parse(String text, Class<? extends T> clazz) throws ParsingValueException;

	default T parse(String text, Class<? extends T> clazz, String errorMessage) throws ParsingValueException {
		try {
			return parse(text, clazz);
		} catch (Exception e) {
			throw new ParsingValueException(String.format("error in parsing value={%s}, toClass={%s}, message={%s}", text, clazz.toString(), errorMessage), e);
		}
	}
}
