/**
 * 
 */
package com.foobnix.util.memcache;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.foobnix.util.LOG;

/**
 * @author iivanenko
 * 
 */
public class MemCache {
    public static final int SEC = 1000;
    public static final int MIN = SEC * 60;
    public static final int HOUR = MIN * 60;
    public static final int DAY = HOUR * 24;

    private Map<String, ValueTime> cacheMap = new ConcurrentHashMap<String, ValueTime>();

    public <T> T getObject(String key) {
        T result = null;
        if (cacheMap.containsKey(key)) {
            ValueTime<T> valueTime = cacheMap.get(key);
            if (valueTime.getDeadTime() > System.currentTimeMillis()) {
                result = valueTime.getObject();
                LOG.d("get object from cache", key);
            } else {
                LOG.d("Remove expired key", key);
                cacheMap.remove(key);
            }
        }
        return result;
    }

    public <T> T getOrUpdate(String key, long leavTime, CacheTask<T> task) {
        T result = getObject(key);
        if (result == null) {
            result = task.run();
            putObject(key, result, leavTime);
        }
        return result;

    }

    public void remove(String key) {
        cacheMap.remove(key);
    }

    public void putObject(String key, Object object, long leavTime) {
        if (leavTime < 0) {
            throw new IllegalArgumentException("Live time should be > 0");
        }
        LOG.d("Put key value to cache", key, leavTime);
        cacheMap.put(key, new ValueTime(object, leavTime));
    }

    public void putObject(String key, Serializable object) {
        LOG.d("Put key value to cache", key);
        cacheMap.put(key, new ValueTime(object, Long.MAX_VALUE - System.currentTimeMillis() - 1000));
    }

    public void clear() {
        cacheMap.clear();
    }

    public void setCacheMap(Map<String, ValueTime> currentMap) {
        cacheMap = new ConcurrentHashMap<String, ValueTime>(currentMap);
    }

    public Map<String, ValueTime> getCacheMap() {
        return cacheMap;
    }

}
