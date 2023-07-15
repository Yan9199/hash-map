import java.util.LinkedList;
import java.util.Objects;

public class MyListsHashMap<K, V> implements MyMap<K, V> {
    private final LinkedList<KeyValuePair<K, V>>[] table;
    private final Fct2Int<K> hashFunction;

    /**
     * Creates a new list hash map.
     *
     * @param hashFunction The used hash function.
     */
    @SuppressWarnings("unchecked")
    public MyListsHashMap(Fct2Int<K> hashFunction) {
        final int len = hashFunction.getTableSize();
        table = new LinkedList[len];
        for (int i = 0; i < len; i++) table[i] = new LinkedList<>();
        this.hashFunction = hashFunction;
    }

    @Override
    public boolean containsKey(K key) {
        for (KeyValuePair<K, V> kv : table[hashFunction.apply(key)]) if (Objects.equals(kv.getKey(), key)) return true;
        return false;
    }

    @Override
    public V getValue(K key) {
        for (KeyValuePair<K, V> kv : table[hashFunction.apply(key)])
            if (Objects.equals(kv.getKey(), key)) return kv.getValue();
        return null;
    }

    @Override
    public V put(K key, V value) {
        final LinkedList<KeyValuePair<K, V>> l = table[hashFunction.apply(key)];
        for (KeyValuePair<K, V> kv : l) if (Objects.equals(kv.getKey(), key)) return kv.set(value);
        l.addFirst(new KeyValuePair<>(key, value));
        return null;
    }

    @Override
    public V remove(K key) {
        int m = 0;
        final LinkedList<KeyValuePair<K, V>> l = table[hashFunction.apply(key)];
        for (KeyValuePair<K, V> kv : l) {
            if (Objects.equals(kv.getKey(), key)) return l.remove(m).getValue();
            m++;
        }
        return null;
    }
}
