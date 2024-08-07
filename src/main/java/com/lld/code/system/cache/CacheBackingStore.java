package com.lld.code.system.cache;

import java.util.Optional;

public interface CacheBackingStore<K, V> {
    Optional<V> get(K key);
    void populateDataStore(K key, V value);
}
