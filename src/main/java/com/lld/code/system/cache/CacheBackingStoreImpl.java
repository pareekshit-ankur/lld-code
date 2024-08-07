package com.lld.code.system.cache;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class CacheBackingStoreImpl<K, V> implements CacheBackingStore<K, V> {
    Map<K, V> datastore;

    public CacheBackingStoreImpl() {
        datastore = new ConcurrentHashMap<>();
    }

    @Override
    public void populateDataStore(K key, V value) {
        datastore.putIfAbsent(key, value);
    }

    @Override
    public Optional<V> get(K key) {
        if (datastore.containsKey(key)) {
            return Optional.of(datastore.get(key));
        }
        return Optional.empty();
    }
}
