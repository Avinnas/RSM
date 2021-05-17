package com.github.netopt.rsmsdm.experiment;

import com.github.netopt.rsmsdm.properties.*;
import org.apache.commons.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author plechowicz
 * created on 19.02.19.
 */
public class ExperimentPropertiesParser {

	private static final Logger log = LoggerFactory.getLogger(ExperimentPropertiesParser.class);

	private enum PropertyName {
		NETWORK_FILE("networkFile"),
		CANDIDATE_PATHS_FILE("candidatePathsFile"),
		DEMANDS_DIRECTORY("demandsDirectory"),
		DEMANDS_FILE("demandsFile", List.class, MultipleStringRangeParser.class),
		SLOTS_FOR_CANDIDATE_PATHS_FILE("slotsForCandidatePathsFile"),
		ALGORITHM("algorithm"),
		CORES("cores", List.class, IntegerRangeParser.class),
		CANDIDATE_PATHS("candidatePaths", List.class, IntegerRangeParser.class),
		MAX_CANDIDATE_PATHS("maxCandidatePaths", Integer.class),
		SUMMARY_BITRATE("summaryBitrate", List.class, LongRangeParser.class),
		;

		private final String name;
		private final Class<?> valueClazz;
		private final Class<?> parserClazz;
		private final String setterMethodName;
		private final String getterMethodName;

		PropertyName(String name, Class<?> valueClazz) {
			this(name, valueClazz, valueClazz);
		}

		PropertyName(String name, Class<?> valueClazz, Class<?> parserClazz) {
			this.name = name;
			this.valueClazz = valueClazz;
			this.parserClazz = parserClazz;
			this.setterMethodName = "set" + WordUtils.capitalize(name, ' ', '_').replace("_", "");
			this.getterMethodName = "get" + WordUtils.capitalize(name, ' ', '_').replace("_", "");
		}

		PropertyName(String name) {
			this(name, String.class);
		}

		public String getName() {
			return name;
		}

		public Class<?> getValueClazz() {
			return valueClazz;
		}

		public Class<?> getParserClazz() {
			return parserClazz;
		}

		public String getSetterMethodName() {
			return setterMethodName;
		}

		public String getGetterMethodName() {
			return getterMethodName;
		}
	}

	public static List<ExperimentProperties> readFromFile(String path) throws PropertiesReadingException {
		Properties properties = new Properties();
		ExperimentProperties.CartesianProductBuilder builder = ExperimentProperties.newCartesianProductBuilder();

		try {
			properties.load(new FileInputStream(path));
			for (var property : PropertyName.values()) {
				if (properties.getProperty(property.getName()) == null) {
					throw new PropertiesReadingException(String.format("Missing property={%s}", property.getName()));
				}

				builder.getClass()
						.getMethod(property.getSetterMethodName(), property.getValueClazz())
						.invoke(builder, StringParsers.parse(properties.getProperty(property.getName()), property.getParserClazz(), String.format("error in property={%s}", property.getName())));
			}
		} catch (IOException | ParserNotFoundException | ParsingValueException e) {
			throw new PropertiesReadingException("Error in reading or parsing property", e);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			throw new PropertiesReadingException("Error in instantiating builder method" + builder.getClass(), e);
		}
		return builder.build();
	}

	public static void writeToFile(ExperimentProperties experimentProperties, String path) throws PropertiesWritingException {
		File file = new File(path);
		file.getParentFile().mkdirs();
		try (PrintWriter printWriter = new PrintWriter(new FileWriter(path))) {
			for (var property : PropertyName.values()) {
				Object object = experimentProperties.getClass().getMethod(property.getGetterMethodName())
						.invoke(experimentProperties);
				printWriter.print(property.getName());
				printWriter.print("=");
				if (object != null) {
					printWriter.print(object.toString());
				}
				printWriter.println();
			}
		} catch (IOException e) {
			throw new PropertiesWritingException(String.format("Error in writing properties file={%s}", path), e);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			throw new PropertiesWritingException("Error in instantiating builder method" + experimentProperties.getClass(), e);
		}
	}

	public static List<String> writeMultipleFilesToFolder(List<ExperimentProperties> experimentPropertiesList, String folder) throws PropertiesWritingException {
		int experimentIdx = 0;
		List<String> experimentPropertiesPaths = new ArrayList<>();
		for (var ep : experimentPropertiesList) {
			String file = folder + String.format("%03d_experiment", experimentIdx) + ".properties";
			writeToFile(ep, file);
			experimentPropertiesPaths.add(file);
			experimentIdx++;
		}
		return experimentPropertiesPaths;
	}
}
