import java.util.*

//** ** Created by DeveloperHacker ** **//
//* https://github.com/DeveloperHacker *//

abstract class AbstractMutableMultimap<K : Any, V, C : MutableCollection<V>>(
        private val empty: () -> C
) : AbstractMultimap<K, V, C>(), MutableMultimap<K, V, C> {

    override abstract val map: MutableMap<K, C>

    override var size: Int = 0
        protected set

    override fun put(key: K, value: V): Boolean {
        if (!map.containsKey(key)) map.put(key, empty())
        val add = map[key]!!.add(value)
        if (add) ++size
        return add
    }

    override fun put(key: K, values: C): Boolean {
        if (!map.containsKey(key)) map.put(key, empty())
        val add = map[key]!!.addAll(values)
        if (add) size += values.size
        return add
    }

    override fun remove(key: K): C? {
        val values = map.remove(key)
        if (values != null) size -= values.size
        return values
    }

    override fun removeAll(keys: Collection<K>): Boolean {
        var remove = false
        keys.forEach { key -> if (remove(key) != null) remove = true }
        return remove
    }

    override fun removeValue(value: V): Boolean {
        var remove = 0
        val i = map.iterator()
        while (i.hasNext()) {
            val entry = i.next()
            val j = entry.value.iterator()
            while (j.hasNext()) {
                val v = j.next()
                if (value == null && v == null || value != null && v != null && value.equals(v)) {
                    ++remove
                    j.remove()
                }
            }
            if (entry.value.isEmpty()) i.remove()
        }
        size -= remove
        return remove != 0
    }

    override fun removeAllValue(values: Collection<V>): Boolean {
        var remove = false
        values.forEach { value -> if (removeValue(value)) remove = true }
        return remove
    }

    override fun putAll(from: Multimap<K, V, C>) = from.forEach { entry -> put(entry.key, entry.value) }

    override fun putAll(from: Map<K, V>) = from.forEach { entry -> put(entry.key, entry.value) }

    override fun clear() {
        map.clear()
        size = 0
    }

    override val keys: MutableSet<K>
        get() = map.keys

    override val values: MutableCollection<V>
        get() = Values()

    override val entries: MutableSet<Map.Entry<K, V>>
        get() = Entries()

    inner private class Values : MutableCollection<V> {

        private val entries: Entries = Entries()

        override val size: Int
            get() = entries.size

        override fun contains(element: V): Boolean {
            entries.forEach { k, v ->
                if (element == null && v == null || element != null && v != null && element.equals(v))
                    return true
            }
            return false
        }

        override fun containsAll(elements: Collection<V>): Boolean {
            elements.forEach { value -> if (!contains(value)) return false }
            return true
        }

        override fun isEmpty() = entries.isEmpty()

        override fun add(element: V): Boolean {
            throw UnsupportedOperationException()
        }

        override fun addAll(elements: Collection<V>): Boolean {
            throw UnsupportedOperationException()
        }

        override fun clear() = entries.clear()

        override fun iterator() = object : MutableIterator<V> {

            private val it = entries.iterator()

            override fun hasNext() = it.hasNext()

            override fun next() = it.next().value

            override fun remove() = it.remove()
        }

        override fun remove(element: V): Boolean {
            val list = LinkedList<Map.Entry<K, V>>()
            entries.forEach { k, v ->
                if (element == null && v == null || element != null && v != null && element.equals(v))
                    list.add(Entry(k, v))
            }
            list.forEach { entry -> entries.remove(entry) }
            return list.size != 0
        }

        override fun removeAll(elements: Collection<V>): Boolean {
            var remove = false
            elements.forEach { if (remove(it)) remove = true }
            return remove
        }

        override fun retainAll(elements: Collection<V>): Boolean {
            var retain = false
            elements.forEach { value ->
                val list = LinkedList<Map.Entry<K, V>>()
                entries.forEach { k, v ->
                    if (value == null && v == null || value != null && v != null && value.equals(v))
                        list.add(Entry(k, v))
                }
                list.forEach { entry -> entries.remove(entry) }
                if (list.size != 0) retain = true
            }
            return retain
        }
    }

    inner private class Entries : MutableSet<Map.Entry<K, V>> {

        private val entries = ArrayList<Trio<K, V, Int>>()

        override val size: Int
            get() = entries.size

        override fun isEmpty() = entries.isEmpty()

        override fun contains(element: Map.Entry<K, V>) = entries.contains(Trio(element, 0))

        override fun containsAll(elements: Collection<Map.Entry<K, V>>): Boolean {
            elements.forEach { entry -> if (!contains(entry)) return false }
            return true
        }

        override fun retainAll(elements: Collection<Map.Entry<K, V>>): Boolean {
            val list = LinkedList<Map.Entry<K, V>>()
            entries.forEach { trio -> if (!elements.contains(trio.pair)) list.add(trio.pair) }
            list.forEach { entry -> remove(entry) }
            return list.size != 0
        }

        override fun removeAll(elements: Collection<Map.Entry<K, V>>): Boolean {
            var remove = false
            elements.forEach { entry -> if (remove(entry)) remove = true }
            return remove
        }

        override fun remove(element: Map.Entry<K, V>): Boolean {
            val blank = Trio(element, 0)
            if (entries.contains(blank)) {
                entries.forEach { trio ->
                    if (trio.equals(blank)) remove(element.key, element.value, trio.index)
                }
                return true
            }
            return false
        }

        private fun remove(key: K, value: V, index: Int) {
            val collection = this@AbstractMutableMultimap.map[key]!!
            var i = index
            var it = collection.iterator()
            for (j in 0..index - 1) it.next()
            var v = it.next()
            if (!(value?.equals(v) ?: (value == null && v == null))) {
                it = collection.iterator()
                i = -1
                do {
                    v = it.next()
                    ++i
                } while (!(value?.equals(v) ?: (value == null && v == null)))
            }
            it.remove()
            --this@AbstractMutableMultimap.size
            if (collection.isEmpty()) this@AbstractMutableMultimap.map.remove(key)
            else entries.forEach { trio -> if (trio.key.equals(key) && i < trio.index) --trio.index }
        }

        override fun iterator() = object : MutableIterator<Map.Entry<K, V>> {

            private val it = entries.iterator()

            private val stack = Stack<Trio<K, V, Int>>()

            override fun remove() {
                it.remove()
                val trio = stack.pop()
                remove(trio.key, trio.value, trio.index)
            }

            override fun hasNext() = it.hasNext()

            override fun next(): Map.Entry<K, V> {
                if (!hasNext()) throw NoSuchElementException()
                val trio = it.next()
                stack.push(trio)
                return trio.pair
            }
        }

        inline fun forEach(action: (K, V) -> Unit) = entries.forEach { trio -> action(trio.key, trio.value) }

        override fun clear() = this@AbstractMutableMultimap.clear()

        override fun addAll(elements: Collection<Map.Entry<K, V>>): Boolean {
            var add = false
            elements.forEach { entry -> if (add(entry)) add = true }
            return add
        }

        override fun add(element: Map.Entry<K, V>): Boolean {
            if (this@AbstractMutableMultimap.put(element.key, element.value)) {
                entries.filter { trio -> if (trio.key.equals(element.key)) false else true }
                this@AbstractMutableMultimap.map[element.key]!!.forEachIndexed { i, v ->
                    entries.add(Trio(element.key, v, i))
                }
                return true
            } else {
                return false
            }
        }

        init {
            this@AbstractMutableMultimap.forEach { entry ->
                entry.value.forEachIndexed { i, v -> entries.add(Trio(entry.key, v, i)) }
            }
        }

    }

    private class Trio<K : Any, V, I : Any>(val key: K, val value: V, var index: I) {

        constructor(pair: Map.Entry<K, V>, index: I) : this(pair.key, pair.value, index)

        val pair: Map.Entry<K, V> = Entry(key, value)

        override fun equals(other: Any?): Boolean {
            if (other === this) return true
            if (other !is Trio<*, *, *>) return false
            return other.key.equals(key) && (other.value?.equals(value) ?: (value == null && other.value == null))
        }

        override fun hashCode(): Int = ((3 + key.hashCode()) * 5 + (value?.hashCode() ?: 0)) * 7
    }
}
