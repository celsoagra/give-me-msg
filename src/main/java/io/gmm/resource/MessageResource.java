package io.gmm.resource;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import java.util.List;

import io.gmm.model.Message;
import io.gmm.service.MessageService;
import io.smallrye.mutiny.Uni;

@Path("/v1/msg")
public class MessageResource {
	
	@Inject
    MessageService service;

    @GET
    public Uni<List<String>> keys() {
        return service.keys();
    }

    @POST
    public Message create(Message message) {
        service.set(message.key, message.value);
        return message;
    }

    @GET
    @Path("/{key}")
    public Message get(@PathParam("key") String key) {
        return new Message(key, service.get(key));
    }

    @PUT
    @Path("/{key}")
    public Message increment(@PathParam("key") String key, List<String> params) {
    	return new Message(key, service.format(key, params));
    }

    @DELETE
    @Path("/{key}")
    public Uni<Void> delete(@PathParam("key") String key) {
        return service.del(key);
    }
}
