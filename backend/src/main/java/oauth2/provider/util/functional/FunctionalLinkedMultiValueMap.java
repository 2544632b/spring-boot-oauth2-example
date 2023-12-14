package oauth2.provider.util.functional;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class FunctionalLinkedMultiValueMap<K, V> extends LinkedMultiValueMap<K, V> {

    public FunctionalLinkedMultiValueMap() {
        super(new LinkedMultiValueMap<>());
    }

    public FunctionalLinkedMultiValueMap<K, V> addEx(K k, V v) {
        super.add(k, v);
        return this;
    }

    public MultiValueMap<K, V> finish() {
        return this;
    }

}
