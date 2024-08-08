package com.lld.code.system.cache;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LRUCache<K, V> implements Cache<K, V> {
    private final int size;
    private final long ttl;
    private final Map<K, CacheEntry<K, V>> referenceMap;
    private final Deque<CacheEntry<K, V>> list;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final CacheBackingStore<K, V> cacheBackingStore = new CacheBackingStoreImpl<K, V>();

    public LRUCache(int size) {
        this.size = size;
        this.ttl = TimeUnit.MILLISECONDS.convert(30, TimeUnit.SECONDS);
        this.referenceMap = new ConcurrentHashMap<>(size);
        this.list = new LinkedList<>();
        initCleanerThread();
    }

    public LRUCache(int size, long timeToLive) {
        this.size = size;
        this.ttl = timeToLive;
        this.referenceMap = new ConcurrentHashMap<>(size);
        this.list = new LinkedList<>();
        initCleanerThread();
    }

    private void initCleanerThread() {
        Thread cleanerThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                for (Map.Entry<K, CacheEntry<K, V>> entry : referenceMap.entrySet()) {
                    CacheEntry<K, V> existingCacheEntry = entry.getValue();
                    long timeElapsed = System.currentTimeMillis() - existingCacheEntry.getLastReferencedTime();
                    if (timeElapsed >= ttl) {
                        list.remove(existingCacheEntry);
                        referenceMap.remove(entry.getKey());
                    }
                }
            }
        });
        cleanerThread.setDaemon(true);
        cleanerThread.start();
    }

    @Override
    public void put(K key, V value) {
        this.lock.writeLock().lock();
        try {
            if (referenceMap.containsKey(key)) {
                CacheEntry<K, V> existingCacheEntry = referenceMap.get(key);
                list.remove(existingCacheEntry);
            } else {
                if (list.size() == size) {
                    CacheEntry<K, V> entry = list.remove();
                    referenceMap.remove(entry.getKey());
                }
            }
            CacheEntry<K, V> cacheEntry = new CacheEntry<>(key, value, System.currentTimeMillis());
            list.add(cacheEntry);
            referenceMap.put(key, cacheEntry);
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    @Override
    public void remove(K key) {
        this.lock.writeLock().lock();
        try {
            CacheEntry<K, V> cacheEntry = referenceMap.get(key);
            if (Objects.nonNull(cacheEntry)) {
                list.remove(cacheEntry);
                referenceMap.remove(key);
            }
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    @Override
    public Optional<V> get(K key) {
        CacheEntry<K, V> cacheEntry = referenceMap.get(key);
        if (Objects.nonNull(cacheEntry)) {
            put(cacheEntry.getKey(), cacheEntry.getValue());
            return Optional.of(cacheEntry.getValue());
        }

        Optional<V> data = cacheBackingStore.get(key);
        if (data.isPresent()) {
            put(key, data.get());
            return data;
        }
        return Optional.empty();
    }

    @Override
    public void clear() {
        list.clear();
        referenceMap.clear();
    }
}
