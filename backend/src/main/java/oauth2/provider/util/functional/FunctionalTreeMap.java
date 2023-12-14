package oauth2.provider.util.functional;

import java.util.TreeMap;

public class FunctionalTreeMap<K, V> extends TreeMap<K, V> {

    public FunctionalTreeMap() {
        super(new TreeMap<>());
    }

    public FunctionalTreeMap<K, V> putEx(K k, V v) {
        super.put(k, v);
        return this;
    }

    public TreeMap<K, V> finish() {
        return this;
    }
}
