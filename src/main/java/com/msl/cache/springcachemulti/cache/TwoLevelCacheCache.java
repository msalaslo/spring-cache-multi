package com.msl.cache.springcachemulti.cache;

import java.util.concurrent.Callable;

import org.springframework.cache.Cache;
import org.springframework.cache.support.NoOpCache;

/**
 * Cache Implmentation that wraps two caches
 * For get, put and eviction uses both caches
 * @author Miguel Salas
 *
 */
public class TwoLevelCacheCache implements Cache {
    private String name;
    private Cache levelOneCache;
    private Cache nextLevelCache;

    TwoLevelCacheCache(String name, Cache cache) {
        this(name, cache, new NoOpCache(name));
    }

    TwoLevelCacheCache(String name, Cache levelOneCache, Cache nextLevelCache) {
        this.name = name;
        this.levelOneCache = levelOneCache;
        this.nextLevelCache = nextLevelCache;
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
        ValueWrapper value = levelOneCache.get(o);
        if (value == null) {
            value = nextLevelCache.get(o);
            if (value != null) {
                levelOneCache.put(o, value.get());
            }
        }
        return value;
    }
    
	@Override
	public <T> T get(Object o, Callable<T> valueLoader) {
        T value = levelOneCache.get(o, valueLoader);
        if (value == null) {
            value = nextLevelCache.get(o, valueLoader);
            if (value != null) {
                levelOneCache.put(o, value);
            }
        }
        return value;
	}

    @Override
    public <T> T get(Object o, Class<T> aClass) {
        T value = levelOneCache.get(o, aClass);
        if (value == null) {
            value = nextLevelCache.get(o, aClass);
            if (value != null) {
                levelOneCache.put(o, value);
            }
        }
        return value;
    }

    @Override
    public void put(Object o, Object o1) {
        levelOneCache.put(o, o1);
        nextLevelCache.put(o, o1);
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
        levelOneCache.evict(o);
        nextLevelCache.evict(o);
    }

    @Override
    public void clear() {
        levelOneCache.clear();
        nextLevelCache.clear();
    }
}