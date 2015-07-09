package rainbow.com.simple_net_framework.cache;

/**
 * Created by blue on 2015/7/6.
 */
public interface Cache<K, V> {
    public void put(K key, V value);

    public V get(K key);

    public void remove(K key);
}
