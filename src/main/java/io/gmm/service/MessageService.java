package io.gmm.service;

import io.quarkus.redis.client.RedisClient;
import io.quarkus.redis.client.reactive.ReactiveRedisClient;
import io.smallrye.mutiny.Uni;

import io.vertx.mutiny.redis.client.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MessageService {
	
	@Inject
    RedisClient redisClient; 

    @Inject
    ReactiveRedisClient reactiveRedisClient; 

    public Uni<Void> del(String key) {
        return reactiveRedisClient.del(Arrays.asList(key))
                .map(response -> null);
    }

    public String get(String key) {
        return redisClient.get(key).toString();
    }
    
    public String format(String key, List<String> params) {
        String msg = redisClient.get(key).toString();
        
        Iterator<String> it = params.iterator();
        while (it.hasNext()) {
        	msg = msg.replace("{}", it.next());
        }
        
        return msg.replaceAll("{}", "null"); 
    }

    public void set(String key, String value) {
        redisClient.set(Arrays.asList(key, value));
    }

    public Uni<List<String>> keys() {
        return reactiveRedisClient
                .keys("*")
                .map(response -> {
                    List<String> result = new ArrayList<>();
                    for (Response r : response) {
                        result.add(r.toString());
                    }
                    return result;
                });
    }
}
