interface MutableMultimap<K : Any, V, C : MutableCollection<V>> : Multimap<K, V, C> {

    fun put(key: K, value: V): Boolean

    fun put(key: K, values: C): Boolean

    fun remove(key: K): C?

    fun removeAll(keys: Collection<K>): Boolean

    fun removeValue(value: V): Boolean

    fun removeAllValue(values: Collection<V>): Boolean

    fun putAll(from: Multimap<K, V, C>): Unit

    fun putAll(from: Map<K, V>): Unit

    fun clear(): Unit

    override val keys: MutableSet<K>

    override val values: MutableCollection<V>

    override val entries: MutableCollection<Map.Entry<K, V>>
}