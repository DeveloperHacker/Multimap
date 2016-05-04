import java.util.*

abstract class AbstractMultimap<K : Any, V, C : Collection<V>> : Multimap<K, V, C> {

    abstract protected val map: Map<K, C>

    abstract override val size: Int

    override fun isEmpty() = map.isEmpty()

    override fun containsKey(key: K) = map.containsKey(key)

    override fun containsValue(value: V): Boolean {
        map.forEach { entry ->
            entry.value.forEach { v ->
                if (value == null && v == null || value != null && v != null && value.equals(v)) {
                    return true
                }
            }
        }
        return false
    }

    override operator fun get(key: K) = map[key]

    override val keys: Set<K>
        get() = HashSet(map.keys)

    override val values: Collection<V>
        get(): Collection<V> {
            val list = ArrayList<V>()
            map.forEach { entry -> list.addAll(entry.value) }
            return list
        }

    override val entries: Collection<Map.Entry<K, V>>
        get(): Collection<Map.Entry<K, V>> {
            val list = ArrayList<Map.Entry<K, V>>()
            map.forEach { entry ->
                entry.value.forEach { v -> list.add(Entry(entry.key, v)) }
            }
            return list
        }

    override fun forEach(action: (K, V) -> Unit) = entries.forEach { entry -> action(entry.key, entry.value) }

    override fun forEachIndexed(action: (K, V, Int) -> Unit) {
        var index = 0
        entries.forEach { entry -> action(entry.key, entry.value, index++) }
    }

    override fun forEach(action: (Map.Entry<K, C>) -> Unit) = map.forEach(action)

    override fun forEachIndexed(action: (Map.Entry<K, C>, Int) -> Unit) {
        var index = 0
        map.forEach { entry -> action(entry, index++) }
    }

    override fun toString(): String {
        val string = StringBuilder()
        map.forEach { entry -> string.append("[${entry.key.toString()}] -> ${entry.value.toString()}\n") }
        return string.toString()
    }

    data class Entry<K, V>(override val key: K, override val value: V) : Map.Entry<K, V>
}