package io.gmm.resource;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.gmm.constants.Operation;
import io.gmm.dto.MessageDTO;
import io.gmm.entity.Message;
import io.gmm.service.MessageService;
import io.smallrye.mutiny.Uni;

@Path("/msg")
public class MessageResource {

	@Inject
	MessageService service;

	@GET
	public Uni<List<String>> keys() {
		return service.keys();
	}

	@POST
	public Message create(MessageDTO dto) throws JsonProcessingException {
		return service.set(dto);
	}

	@GET
	@Path("/{key}")
	public Message get(@PathParam("key") String key) throws JsonProcessingException {
		return service.get(key);
	}

	@PUT
	@Path("/{key}")
	public MessageDTO format(@PathParam("key") String key, List<String> params) throws JsonProcessingException {
		return service.format(key, params);
	}

	@DELETE
	@Path("/{key}")
	public Uni<Void> delete(@PathParam("key") String key) {
		return service.del(key);
	}
}
