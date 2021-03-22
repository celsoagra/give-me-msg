package io.gmm.entity;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class Message {

	private static final String ZONE_ID_UTC = "UTC";
	private static final String DEFAULT_VERSION = "v1";
	private static final ObjectMapper MAPPER = JsonMapper.builder().findAndAddModules().build();

	public String key;
	public String version;
	public String value;
	public ZonedDateTime createdDate;
	public ZonedDateTime lastModifiedDate;

	public Message(String key, String value) {
		this.key = key;
		this.version = DEFAULT_VERSION;
		this.value = value;
		createdDate = lastModifiedDate = ZonedDateTime.now(ZoneId.of(ZONE_ID_UTC));
	}

	public String toJsonString() throws JsonProcessingException {
		return MAPPER.writeValueAsString(this);
	}

	public static Message fromJsonString(String json) throws JsonMappingException, JsonProcessingException {
		return (json == null || json.length() == 0) ? null : MAPPER.readValue(json, Message.class);
	}

	public Message() {
	}

	public void updateLastModifiedDate() {
		lastModifiedDate = ZonedDateTime.now(ZoneId.of(ZONE_ID_UTC));
	}
}
