package net.programmer.igoodie.goodies.util.builder;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Examplar usage: <br/>
 *
 * <pre>{@code
 *  public static final Map<String, Integer> MY_MAP = InlineMapBuilder.<String, Integer>of(HashMap::new)
 *        .entry("Foo", 1)
 *        .entry("Bar", 2)
 *        .entry("Baz", 3)
 *        .build();
 * }</pre>
 */
public class InlineMapBuilder<K, V> {

    private final Map<K, V> underlyingMap;

    private InlineMapBuilder() {
        throw new InternalError(); // Disallow instantiation
    }

    private InlineMapBuilder(Map<K, V> initialMap) {
        this.underlyingMap = initialMap;
    }

    public InlineMapBuilder<K, V> entry(K key, V value) {
        this.underlyingMap.put(key, value);
        return this;
    }

    public Map<K, V> build() {
        return underlyingMap;
    }

    /* ------------------------ */

    public static <K, V> InlineMapBuilder<K, V> of(Supplier<Map<K, V>> initiator) {
        return new InlineMapBuilder<>(initiator.get());
    }

}
