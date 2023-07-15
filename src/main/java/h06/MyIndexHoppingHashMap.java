package h06;

import java.util.Objects;

public class MyIndexHoppingHashMap<K, V> implements MyMap<K, V> {
    private final double resizeThreshold;
    private final double resizeFactor;
    private K[] theKeys;
    private V[] theValues;
    private boolean[] occupiedSinceLastRehash;
    private int occupiedCount = 0;
    private final BinaryFct2Int<K> hashFunction;

    /**
     * Create a new index hopping hash map.
     *
     * @param initialSize     The initial size of the hashmap.
     * @param resizeFactor    The resize factor which determines the new size after the resize threshold is reached.
     * @param resizeThreshold The threshold after which the hash table is resized.
     * @param hashFunction    The used hash function.
     */
    @SuppressWarnings("unchecked")
    public MyIndexHoppingHashMap(int initialSize, double resizeFactor, double resizeThreshold, BinaryFct2Int<K> hashFunction) {
        this.theKeys = (K[]) new Object[initialSize];
        this.theValues = (V[]) new Object[initialSize];
        this.occupiedSinceLastRehash = new boolean[initialSize];
        this.resizeFactor = resizeFactor;
        this.resizeThreshold = resizeThreshold;
        this.hashFunction = hashFunction;
        this.hashFunction.setTableSize(initialSize);
    }

    @Override
    public boolean containsKey(K key) {
        int n = hashFunction.apply(key, 0);
        return Objects.equals(theKeys[n], key) || (occupiedSinceLastRehash[n] && (n = compute(key)) != -1 && Objects.equals(theKeys[n], key));
    }

    @Override
    public V getValue(K key) {
        int n = hashFunction.apply(key, 0);
        return Objects.equals(theKeys[n], key) ? theValues[n] : (occupiedSinceLastRehash[n] ? ((n = compute(key)) == -1 || !Objects.equals(theKeys[n], key) ? null : theValues[n]) : null);
    }

    private int compute(K key) {
        int n = hashFunction.apply(key, 1), counter = 1, len = theKeys.length, firstIndex = -1;
        boolean f = true;
        while (true) {
            if (counter == len) return firstIndex;
            final boolean b = theKeys[n] == null;
            if (f) {
                if (b) {
                    f = false;
                    firstIndex = n;
                }
            }
            if (Objects.equals(theKeys[n], key) || (b && !occupiedSinceLastRehash[n])) break;
            n = hashFunction.apply(key, ++counter);
        }
        return Objects.equals(theKeys[n], key) ? n : firstIndex;
    }

    @Override
    public V put(K key, V value) {
        int n = hashFunction.apply(key, 0);
        final K k = theKeys[n];
        if (Objects.equals(k, key)) {
            final V v = theValues[n];
            theValues[n] = value;
            return v;
        }
        if (occupiedSinceLastRehash[n]) {
            if ((n = compute(key)) == -1) return null;
            if (Objects.equals(theKeys[n], key)) {
                final V v = theValues[n];
                theValues[n] = value;
                return v;
            }
            if (occupiedSinceLastRehash[n]) {
                theKeys[n] = key;
                theValues[n] = value;
                return null;
            }
            if (occupiedCount + 1 > resizeThreshold * occupiedSinceLastRehash.length) {
                rehash();
                return put(key, value);
            }
            occupiedSinceLastRehash[n] = true;
            occupiedCount++;
            theKeys[n] = key;
            theValues[n] = value;
            return null;
        }
        if (occupiedCount + 1 > resizeThreshold * occupiedSinceLastRehash.length) {
            rehash();
            return put(key, value);
        }
        occupiedSinceLastRehash[n] = true;
        occupiedCount++;
        theKeys[n] = key;
        theValues[n] = value;
        return null;
    }

    @Override
    public V remove(K key) {
        int n = hashFunction.apply(key, 0);
        if (Objects.equals(theKeys[n], key)) {
            final V v = theValues[n];
            theKeys[n] = null;
            theValues[n] = null;
            return v;
        }
        if (occupiedSinceLastRehash[n]) {
            if ((n = compute(key)) == -1 || !Objects.equals(theKeys[n], key)) return null;
            final V v = theValues[n];
            theKeys[n] = null;
            theValues[n] = null;
            return v;
        }
        return null;
    }

    /**
     * Creates a new bigger hashtable (current size multiplied by resizeFactor)
     * and inserts all elements of the old hashtable into the new one.
     */
    @SuppressWarnings("unchecked")
    private void rehash() {
        final int len = theKeys.length, length = (int) (resizeFactor * len);
        final K[] saveKeys = theKeys;
        final V[] saveValues = theValues;
        theKeys = (K[]) new Object[length];
        theValues = (V[]) new Object[length];
        occupiedSinceLastRehash = new boolean[length];
        occupiedCount = 0;
        hashFunction.setTableSize(length);
        for (int i = 0; i < len; i++) {
            final K key = saveKeys[i];
            if (key != null) put(key, saveValues[i]);
        }
    }
}
