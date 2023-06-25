package com.msl.cache.springcachemulti.cache;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.lang.Nullable;

public class TwoLayerCacheManagerImpl implements CacheManager, InitializingBean {

	private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>(16);
	private Set<String> cacheNames = Collections.emptySet();
	private Collection<? extends Cache> caches = Collections.emptySet();


	private CacheManager currentLayer;
	private CacheManager nextLayer;

	/**
	 * Initialize the static configuration of caches.
	 * <p>
	 * Triggered on startup through {@link #afterPropertiesSet()}; can also be
	 * called to re-initialize at runtime.
	 * 
	 * @since 4.2.2
	 * @see #loadCaches()
	 */
	public void initializeCaches() {
		Collection<? extends Cache> caches = loadCaches();

		synchronized (this.cacheMap) {
			this.cacheNames = Collections.emptySet();
			this.cacheMap.clear();
			Set<String> cacheNames = new LinkedHashSet<>(caches.size());
			for (Cache cache : caches) {
				String name = cache.getName();
				this.cacheMap.put(name, cache);
				cacheNames.add(name);
			}
			this.cacheNames = Collections.unmodifiableSet(cacheNames);
		}
	}
	
	// Early cache initialization on startup

	@Override
	public void afterPropertiesSet() {
		initializeCaches();
	}

	/**
	 * Load the initial caches for this cache manager.
	 * <p>
	 * Called by {@link #afterPropertiesSet()} on startup. The returned collection
	 * may be empty but must not be {@code null}.
	 */
	protected Collection<? extends Cache> loadCaches() {
		return this.caches;
	}

	/**
	 * Construct a CompositeCacheManager from the given delegate CacheManagers.
	 * 
	 * @param cacheManagers
	 *            the CacheManagers to delegate to
	 */
	public TwoLayerCacheManagerImpl(CacheManager currentLayer, CacheManager nextLayer) {
		// assert both contain same cache names or dynamic creation true?
		this.currentLayer = currentLayer;
		this.nextLayer = nextLayer;
	}
	
	/**
	 * Construct a CompositeCacheManager add later the given delegate CacheManagers.
	 * 
	 */
	public TwoLayerCacheManagerImpl() {
	}
	
	public void addNearLayer(CacheManager nearLayer) {
		this.currentLayer = nearLayer;
	}
	
	public void addRemoteLayer(CacheManager remoteLayer) {
		this.nextLayer = remoteLayer;
	}
	


	// Lazy cache initialization on access

	@Override
	@Nullable
	public Cache getCache(String name) {
		Cache cache = this.cacheMap.get(name);
		if (cache != null) {
			return cache;
		} else {
			// Fully synchronize now for missing cache creation...
			synchronized (this.cacheMap) {
				cache = this.cacheMap.get(name);
				if (cache == null) {
					if(nextLayer == null) {
						cache = new TwoLayerCacheImpl(name, currentLayer.getCache(name));
					} else {
						cache = new TwoLayerCacheImpl(name, currentLayer.getCache(name), nextLayer.getCache(name));
					}
					this.cacheMap.put(name, cache);
					updateCacheNames(name);
				}
				return cache;
			}
		}
	}

	/**
	 * Update the exposed {@link #cacheNames} set with the given name.
	 * <p>
	 * This will always be called within a full {@link #cacheMap} lock and
	 * effectively behaves like a {@code CopyOnWriteArraySet} with preserved order
	 * but exposed as an unmodifiable reference.
	 * 
	 * @param name
	 *            the name of the cache to be added
	 */
	private void updateCacheNames(String name) {
		Set<String> cacheNames = new LinkedHashSet<>(this.cacheNames.size() + 1);
		cacheNames.addAll(this.cacheNames);
		cacheNames.add(name);
		this.cacheNames = Collections.unmodifiableSet(cacheNames);
	}

	@Override
	public Collection<String> getCacheNames() {
		Set<String> names = new LinkedHashSet<>();
		if(currentLayer != null) {
			names.addAll(currentLayer.getCacheNames());
		}
		if(nextLayer != null) {
			names.addAll(nextLayer.getCacheNames());
		} 
		return Collections.unmodifiableSet(names);
	}
}