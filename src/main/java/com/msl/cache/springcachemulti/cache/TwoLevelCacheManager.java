package com.msl.cache.springcachemulti.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.NoOpCacheManager;

import java.util.Collection;

public class TwoLevelCacheManager implements CacheManager {
    CacheManager currentLevel;
    CacheManager nextLevel;

    TwoLevelCacheManager(CacheManager singleLevel) {
        this(singleLevel, new NoOpCacheManager());
    }

    public TwoLevelCacheManager(CacheManager currentLevel, CacheManager nextLevel) {
        //assert both contain same cache names or dynamic creation true?
        this.currentLevel = currentLevel;
        this.nextLevel = nextLevel;
    }

    @Override
    public Cache getCache(String name) {
        return new TwoLevelCacheCache(name, currentLevel.getCache(name), nextLevel.getCache(name));
    }

    @Override
    public Collection<String> getCacheNames() {
        return currentLevel.getCacheNames();
    }
}