/**
 * 
 */
package com.foobnix.memcache;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
            } else {
                cacheMap.remove(key);
            }
        }
        return result;
    }

	public <T, E extends Throwable> T getOrUpdate(String key, long leavTime, CacheETask<T, E> task) throws E {
        T result = getObject(key);
        if (result == null) {
            result = task.run();
            putObject(key, result, leavTime);
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
        cacheMap.put(key, new ValueTime(object, leavTime));
    }

    public void putObject(String key, Serializable object) {
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
