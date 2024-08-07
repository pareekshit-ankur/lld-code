package com.lld.code.system.cache;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public interface Cache<K, V> {
    void put(K key, V value);

    void remove(K key);

    Optional<V> get(K key);

    void clear();

    default void cleanUp() {
    }
}
