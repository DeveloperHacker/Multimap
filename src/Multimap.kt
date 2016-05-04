
//** ** Created by DeveloperHacker ** **//
//* https://github.com/DeveloperHacker *//

interface Multimap<K : Any, V, C : Collection<V>> {

    val size: Int

    fun isEmpty(): Boolean

    fun containsKey(key: K): Boolean

    fun containsValue(value: V): Boolean

    operator fun get(key: K): C?

    val keys: Set<K>

    val values: Collection<V>

    val entries: Collection<Map.Entry<K, V>>

    fun forEach(action: (K, V) -> Unit): Unit

    fun forEachIndexed(action: (K, V, Int) -> Unit): Unit

    fun forEach(action: (Map.Entry<K, C>) -> Unit): Unit

    fun forEachIndexed(action: (Map.Entry<K, C>, Int) -> Unit): Unit
}