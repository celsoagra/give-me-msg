package io.gmm.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.NotFoundException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.gmm.dto.MessageDTO;
import io.gmm.entity.Message;
import io.gmm.exception.AlreadyExistsException;
import io.gmm.exception.EntityValidationException;
import io.quarkus.redis.client.RedisClient;
import io.quarkus.redis.client.reactive.ReactiveRedisClient;
import io.smallrye.mutiny.Uni;
import io.vertx.redis.client.Response;

@Singleton
public class MessageService {

	@Inject
	Validator validator;

	@Inject
	RedisClient redisClient;

	@Inject
	ReactiveRedisClient reactiveRedisClient;

	public Uni<Void> del(String key) {
		return reactiveRedisClient.del(Arrays.asList(key)).map(response -> null);
	}

	public Message get(String key) throws JsonMappingException, JsonProcessingException {
		Response response = redisClient.get(key);
		if (response == null) {
			throw new NotFoundException(String.format(" ' %s ' key not found", key));
		}
		return Message.fromJsonString(response.toString());
	}

	public MessageDTO format(String key, List<String> params) throws JsonMappingException, JsonProcessingException {
		Message current = this.get(key);
		String msg = current.value;

		Iterator<String> it = params.iterator();
		while (it.hasNext()) {
			msg = msg.replace("{}", it.next());
		}

		return new MessageDTO(key, msg.replaceAll("\\{\\}", "null"));
	}

	public Message set(MessageDTO dto) throws JsonMappingException, JsonProcessingException {
		Response response = redisClient.get(dto.key);
		if (response != null) {
			throw new AlreadyExistsException( String.format(" ' %s ' key already exists", dto.key) );
		}
		
		Set<ConstraintViolation<MessageDTO>> violations = validator.validate(dto);
		if (!violations.isEmpty()) {
			throw new EntityValidationException(violations);
		}
		Message entity = dto.entity();

		redisClient.set(Arrays.asList(entity.key, entity.toJsonString()));
		return entity;
	}

	public Uni<List<String>> keys() {
		return reactiveRedisClient.keys("*").map(response -> {
			List<String> result = new ArrayList<>();
			for (io.vertx.mutiny.redis.client.Response r : response) {
				result.add(r.toString());
			}
			return result;
		});
	}
}
