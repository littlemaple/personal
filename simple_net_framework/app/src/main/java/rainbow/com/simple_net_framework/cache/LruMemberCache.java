package rainbow.com.simple_net_framework.cache;

import android.support.v4.util.LruCache;

import rainbow.com.simple_net_framework.base.Response;

/**
 * Created by blue on 2015/7/6.
 */
public class LruMemberCache implements Cache<String, Response> {
    private android.support.v4.util.LruCache<String, Response> lruCache;

    public LruMemberCache() {
       final int maxSize = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int allocateSize = maxSize/8;
        lruCache = new LruCache<String, Response>(allocateSize) {
            @Override
            protected int sizeOf(String key, Response value) {
                return value.rawData.length / 1024;
            }
        };
    }


    public Response get(String key) {
        return lruCache.get(key);
    }

    @Override
    public void put(String key, Response value) {
        lruCache.put(key, value);
    }

    @Override
    public void remove(String key) {
        lruCache.remove(key);
    }
}
