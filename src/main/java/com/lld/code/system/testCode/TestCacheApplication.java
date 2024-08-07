package com.lld.code.system.testCode;

import com.lld.code.system.cache.Cache;
import com.lld.code.system.cache.CacheBackingStore;
import com.lld.code.system.cache.CacheBackingStoreImpl;
import com.lld.code.system.cache.CacheType;

public class TestCacheApplication {
    public static void main(String[] args) throws InterruptedException {
        Cache<Integer, Integer> cache = CacheType.LRUCache.create(5, 10000);
        CacheBackingStore<Integer, Integer> cacheBackingStore = new CacheBackingStoreImpl<>();
        cacheBackingStore.populateDataStore(6, 6);
        cacheBackingStore.populateDataStore(7, 7);


        // Test Case (Add, Remove and Auto Removal of Stale Data)
        cache.put(1, 1);
        // Test data is stored in cache
        System.out.println("Data for key 1: " + cache.get(1));
        cache.put(2, 2);
        cache.put(3, 3);
        //Remove the data from cache
        cache.remove(1);
        // Data for removed key is empty
        System.out.println("Data for key 1: " + cache.get(1));
        cache.put(4, 4);
        System.out.println("Data for key 4: " + cache.get(4));

        // Thread waiting for 15 seconds so that data is auto removed as TTL is 10 seconds
        Thread.sleep(15000 + 10);
        System.out.println("Data for key 4: " + cache.get(4));
        System.out.println("----------------------------------------------");


        //Test Case (Update Existing Key with different Value and clear whole cache)
        cache.put(4, 4);
        System.out.println("Data for key 4: " + cache.get(4));
        cache.put(4, 8);
        System.out.println("Data for key 4: " + cache.get(4));
        cache.clear();
        System.out.println("Data for key 4: " + cache.get(4));
        System.out.println("----------------------------------------------");


        //Test Case (Retrieve Data from Datastore if not in cache)
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

        System.out.println("----------------------------------------------");

        //Test Case Eviction on Capacity
        cache = CacheType.LRUCache.create(2);
        cache.put(1, 1);
        cache.put(4, 4);
        cache.put(5, 5);
        //Data for key 1 should not be available as it will be evicted
        System.out.println("Data for key 1: " + cache.get(1));
    }
}
