package com.bmc.doctorservice.datacache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;


/**
 * This class builds the cache store to save the Doctor object in the cache for the faster retrieval
 *
 * @param <T>
 */
public class CacheStore<T> {

    private Cache<String, T> cache;

    //Constructor to build the cache store
    public CacheStore(int expiryDuration, TimeUnit timeUnit) {
        cache = CacheBuilder.newBuilder().expireAfterWrite(expiryDuration, timeUnit).concurrencyLevel(Runtime.getRuntime().availableProcessors()).build();
    }
    //Method to fetch the data from the cache
    public T get(String key){
        return cache.getIfPresent(key);
    }
    //Method to add a new record to the cache
    public void add(String key, T value){
        if (value != null){
            cache.put(key,value);
        }
    }
}
