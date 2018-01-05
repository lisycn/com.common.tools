package com.common.tools.code.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class JsonUtil {
	 public static <T> List<T> json2List(String json, Class<T> elementClass) throws JsonParseException, JsonMappingException, IOException
	 {
	   ObjectMapper objectMapper = new ObjectMapper();
	   org.codehaus.jackson.type.JavaType javaType = objectMapper.getTypeFactory()
	     .constructParametricType(ArrayList.class, new Class[] { elementClass });
	   return (List)objectMapper.readValue(json, javaType);
	 }

	 public static <T> T json2Object(String json, Class<T> clazz)
	   throws JsonParseException, JsonMappingException, IOException
	 {
	   ObjectMapper objectMapper = new ObjectMapper();
	   return (T)objectMapper.readValue(json, clazz);
	 }

	 public static String map2Json(Map<String, String> map) throws JsonGenerationException, JsonMappingException, IOException {
	   ObjectMapper objectMapper = new ObjectMapper();
	   return objectMapper.writeValueAsString(map);
	 }

	 public static String object2Json(Object obj) throws JsonGenerationException, JsonMappingException, IOException {
	   ObjectMapper objectMapper = new ObjectMapper();
	   return objectMapper.writeValueAsString(obj);
	 }
}
