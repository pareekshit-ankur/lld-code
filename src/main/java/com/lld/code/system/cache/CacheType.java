package com.lld.code.system.cache;

public enum CacheType {
    LRUCache {
        @Override
        public <K, V> Cache<K, V> create(int maxSize) {
            return new LRUCache<>(maxSize);
        }

        @Override
        public <K, V> Cache<K, V> create(int maxSize, long ttl) {
            return new LRUCache<>(maxSize, ttl);
        }
    };

    public abstract <K, V> Cache<K, V> create(int maxSize);

    public abstract <K, V> Cache<K, V> create(int maxSize, long ttl);
}
