package edu.agh.io.industryOptimizer.messaging.messages.util;

@FunctionalInterface
public interface Mapper<K, V> {
    public V map(K key);
}
