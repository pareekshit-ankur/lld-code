package com.lld.code.system.testCode;

import com.lld.code.system.cache.Cache;
import com.lld.code.system.cache.CacheBackingStore;
import com.lld.code.system.cache.CacheBackingStoreImpl;
import com.lld.code.system.cache.CacheType;

public class TestCache {
    public static void main(String[] args) throws InterruptedException {
        Cache<Integer, Integer> cache = CacheType.LRUCache.create(5, 10000);
        CacheBackingStore<Integer, Integer> cacheBackingStore = new CacheBackingStoreImpl<>();
        cacheBackingStore.populateDataStore(6, 6);
        cacheBackingStore.populateDataStore(7, 7);

        // Test auto removal of stale entries
        cache.put(1, 1);
        cache.put(2, 2);
        cache.put(3, 3);
        System.out.println("Data for key 1: " + cache.get(1));
        System.out.println("Data for key 4: " + cache.get(4));
        cache.put(4, 4);
        cache.put(5, 5);
        System.out.println("Data for key 4: " + cache.get(4));
        Thread.sleep(15000 + 10);
        System.out.println("Data for key 4: " + cache.get(4));
        System.out.println("----------------------------------------------");
        //Test clear functionality of cache
        cache.put(1, 1);
        cache.put(2, 2);
        cache.put(3, 3);
        System.out.println("Data for key 1: " + cache.get(1));
        System.out.println("Data for key 4: " + cache.get(4));
        cache.put(4, 4);
        cache.put(5, 5);
        System.out.println("Data for key 4: " + cache.get(4));
        cache.clear();
        System.out.println("Data for key 4: " + cache.get(4));
        System.out.println("----------------------------------------------");
        //Test functionality where get fetches data from data store when not available in cache
        cache.put(1, 1);
        cache.put(2, 2);
        // 6 is available in datastore
        System.out.println("Data for key 6: " + cache.get(6));
        // 8 is not available anywhere so optional empty should be returned
        System.out.println("Data for key 8: " + cache.get(8));
        cache.put(8, 8);
        cache.put(5, 5);
        System.out.println("Data for key 8: " + cache.get(8));
        cache.clear();
        System.out.println("Data for key 8: " + cache.get(8));
    }
}
