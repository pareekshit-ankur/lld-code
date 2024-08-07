package com.lld.code.system.cache;

public class CacheEntry<K, V> {
    private final K key;
    private final V value;
    private final long lastReferencedTime;

    public CacheEntry(K key, V value, long lastReferencedTime) {
        this.key = key;
        this.value = value;
        this.lastReferencedTime = lastReferencedTime;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public long getLastReferencedTime() {
        return lastReferencedTime;
    }
}
