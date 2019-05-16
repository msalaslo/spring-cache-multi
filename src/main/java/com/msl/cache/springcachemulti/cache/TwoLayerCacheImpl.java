package com.msl.cache.springcachemulti.cache;

import java.util.concurrent.Callable;

import org.springframework.cache.Cache;
import org.springframework.cache.support.NoOpCache;

import lombok.extern.slf4j.Slf4j;

/**
 * Custom Cache Implementation that wraps two caches allowing save and retrive data from both caches.
 * For get, put and eviction uses both caches
 * The first cache can be a local memory cache (Caffeine, Embedded Hazelcast, etc.) to avoid network latency and serialization/de-serialization overhead
 * The second cache can be a remote cache (Redis, Hazelcast server, etc. ) to cache big amount of data, synchronize data, persist information, etc.
 * 
 * @author Miguel Salas
 */
@Slf4j
public class TwoLayerCacheImpl implements Cache {
    private String name;
    private Cache layerOneCache;
    private Cache nextLayerCache;

    TwoLayerCacheImpl(String name, Cache cache) {
        this(name, cache, new NoOpCache(name));		
    }

    TwoLayerCacheImpl(String name, Cache layerOneCache, Cache nextLayerCache) {
		LOGGER.info("creating two layer cache with name:{}, first layer cache: {}, second layer cache: {}", name, layerOneCache.getName(), nextLayerCache.getName());
        this.name = name;
        this.layerOneCache = layerOneCache;
        this.nextLayerCache = nextLayerCache;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getNativeCache() {
        return this;
    }

    @Override
    public ValueWrapper get(Object o) {
        ValueWrapper value = layerOneCache.get(o);
        if (value == null) {
            value = nextLayerCache.get(o);
            if (value != null) {
                layerOneCache.put(o, value.get());
            }
        }
        return value;
    }
    
	@Override
	public <T> T get(Object o, Callable<T> valueLoader) {
        T value = layerOneCache.get(o, valueLoader);
        if (value == null) {
            value = nextLayerCache.get(o, valueLoader);
            if (value != null) {
                layerOneCache.put(o, value);
            }
        }
        return value;
	}

    @Override
    public <T> T get(Object o, Class<T> aClass) {
        T value = layerOneCache.get(o, aClass);
        if (value == null) {
            value = nextLayerCache.get(o, aClass);
            if (value != null) {
                layerOneCache.put(o, value);
            }
        }
        return value;
    }

    @Override
    public void put(Object o, Object o1) {
        layerOneCache.put(o, o1);
        nextLayerCache.put(o, o1);
    }

    @Override
    public ValueWrapper putIfAbsent(Object o, Object o1) {
        //synchronize?
        ValueWrapper value = get(o);
        if (value == null) {
            put(o, o1);
        }
        return value;
    }

    @Override
    public void evict(Object o) {
        layerOneCache.evict(o);
        nextLayerCache.evict(o);
    }

    @Override
    public void clear() {
        layerOneCache.clear();
        nextLayerCache.clear();
    }
}