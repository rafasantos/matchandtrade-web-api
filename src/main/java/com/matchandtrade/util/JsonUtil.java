package com.matchandtrade.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Utility class for common JSON manipulations
 * 
 * @author rafael.santos.bra@gmail.com
 *
 */
public class JsonUtil {
	
	private static ObjectMapper objectMapper;

	// Utility classes, which are a collection of static members, are not meant to be instantiated.
	private JsonUtil() { }
	
	/**
	 * Parse an JSON Object from a string.
	 * @param o
	 * @return JSON string
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public static <T> T fromString(String string, Class<T> type ) throws JsonParseException, JsonMappingException, IOException {
		return getObjectMapper().readValue(string, type);
	}

	/**
	 * Parse a string representing a JSON array to a {@code List<jsonClass>}.
	 * This parsing approach is slow, use with caution.
	 *
	 * @param listString
	 * @param jsonClass
	 * @throws java.io.IOException
	 * @return equivalent Java List
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> fromArrayString(String listString, Class<T> jsonClass) throws java.io.IOException {
		List<T> rawList = JsonUtil.fromString(listString, List.class);
		List<T> result = new ArrayList<>();
		for (T entry : rawList) {
			String entryAsString = JsonUtil.toJson(entry);
			T json = JsonUtil.fromString(entryAsString, jsonClass);
			result.add(json);
		}
		return result;
	}

	/**
	 * Instantiate objectMapper with default configuration if it is null, then, return objectMapper. 
	 * @return objectMapper with default config
	 */
	private static ObjectMapper getObjectMapper() {
		if (objectMapper == null) {
			objectMapper = new ObjectMapper();
			objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
			objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
			objectMapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);		
		}
		return objectMapper;
	}
	
	/**
	 * Attempt to prettyfi a JSON string. If error (e.g: the string is not a valid json, 
	 * it returns the original string.
	 * 
	 * @param json
	 * @return pretty json
	 */
	public static String prettyJson(String json) {
		String result = null;
		try {
			Object jsonObject = getObjectMapper().readValue(json, Object.class);
			result = getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
			return result;
		} catch (IOException e) {
			return json;
		}
	}

	/**
	 * Parse an object to a JSON string.
	 * @param o
	 * @return JSON string
	 * @throws JsonProcessingException 
	 */
	public static String toJson(Object o) throws JsonProcessingException {
		return getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(o);
	}

}
