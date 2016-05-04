import java.util.*

//** ** Created by DeveloperHacker ** **//
//* https://github.com/DeveloperHacker *//

class HashMultimap<K : Any, V, C : MutableCollection<V>> : AbstractMutableMultimap<K, V, C> {

    override val map: MutableMap<K, C>

    constructor(initialCapacity: Int, loadFactor: Float, empty: () -> C) : super(empty) {
        map = HashMap(initialCapacity, loadFactor)
    }

    constructor(initialCapacity: Int, empty: () -> C) : super(empty) {
        map = HashMap(initialCapacity)
    }

    constructor(empty: () -> C) : super(empty) {
        map = HashMap()
    }

    constructor(m: Map<K, C>, empty: () -> C) : super(empty) {
        map = HashMap(m)
    }
}

class LinkedHashMultimap<K : Any, V, C : MutableCollection<V>> : AbstractMutableMultimap<K, V, C> {

    override val map: MutableMap<K, C>

    constructor(initialCapacity: Int, loadFactor: Float, empty: () -> C) : super(empty) {
        map = LinkedHashMap(initialCapacity, loadFactor)
    }

    constructor(initialCapacity: Int, empty: () -> C) : super(empty) {
        map = LinkedHashMap(initialCapacity)
    }

    constructor(empty: () -> C) : super(empty) {
        map = LinkedHashMap()
    }

    constructor(m: Map<K, C>, empty: () -> C) : super(empty) {
        map = LinkedHashMap(m)
    }

    constructor(initialCapacity: Int, loadFactor: Float, accessOrder: Boolean, empty: () -> C) : super(empty) {
        map = LinkedHashMap(initialCapacity, loadFactor, accessOrder)
    }
}

class TreeMultimap<K : Any, V, C : MutableCollection<V>> : AbstractMutableMultimap<K, V, C> {

    override val map: MutableMap<K, C>

    constructor(empty: () -> C) : super(empty) {
        map = TreeMap<K, C>()
    }

    constructor(comparator: Comparator<K>, empty: () -> C) : super(empty) {
        map = TreeMap<K, C>(comparator)
    }

    constructor(m: Map<K, C>, empty: () -> C) : super(empty) {
        map = TreeMap<K, C>(m)
    }

    constructor(m: SortedMap<K, C>, empty: () -> C) : super(empty) {
        map = TreeMap<K, C>(m)
    }
}
