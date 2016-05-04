import org.junit.Test
import java.io.File
import java.io.FileNotFoundException
import java.io.PrintWriter
import java.util.*

//** ** Created by DeveloperHacker ** **//
//* https://github.com/DeveloperHacker *//

class HashMultimapTest {

    private fun String.toText(): ArrayList<String> {
        val result = ArrayList<String>()
        var string = StringBuilder()
        this.forEach { char ->
            if (char != '\n') string.append(char)
            else {
                result.add(string.toString())
                string = StringBuilder("")
            }
        }
        result.add(string.toString())
        return result
    }

    @Test
    fun hashSet() {
        val multimap = HashMultimap<Int, String, MutableSet<String>> { HashSet<String>() }
        val map = HashMap<Int, String>()
        for (i in 0..100) {
            multimap.put(i, "0v$i")
            multimap.put(i, "1v$i")
            multimap.put(i, "2v$i")
            multimap.put(i, "0v$i")
            multimap.put(i, "2v$i")
            multimap.put(i, "0v$i")
            multimap.put(i, "1v$i")
            if (i % 7 == 0) map.put(i, "2v$i")
        }
        for (i in 0..100) if (i % 2 == 0) multimap.remove(i)
        for (i in 0..100) if (i % 3 == 0) multimap.removeValue("1v$i")
        for (i in 0..100) if (i % 5 == 0) multimap.removeAllValue(listOf("2v$i", "0v$i"))
        multimap.putAll(map)
        var i = 0
        val values = multimap.values
        val valuesIt = values.iterator()
        val prevSize = multimap.size
        assert(multimap.size == values.size)
        while (valuesIt.hasNext()) {
            valuesIt.next()
            if (i++ % 2 == 0) valuesIt.remove()
        }
        assert(multimap.size == values.size)
        assert(prevSize / 2 == multimap.size)
        var j = 0
        val keysIt = multimap.keys.iterator()
        while (keysIt.hasNext()) {
            keysIt.next()
            if (j++ % 3 == 0) keysIt.remove()
        }
        val it = multimap.toString().toText().iterator()
        val input = "test/hashSet.txt".input()
        while (input.hasNextLine() && it.hasNext()) assert(input.nextLine() == it.next())
    }

    @Test
    fun linkedList() {
        val multimap = HashMultimap<Int, String, MutableList<String>> { LinkedList<String>() }
        val map = HashMap<Int, String>()
        for (i in 0..100) {
            multimap.put(i, "0v$i")
            multimap.put(i, "1v$i")
            multimap.put(i, "2v$i")
            multimap.put(i, "0v$i")
            multimap.put(i, "2v$i")
            multimap.put(i, "0v$i")
            multimap.put(i, "1v$i")
            if (i % 7 == 0) map.put(i, "2v$i")
        }
        for (i in 0..100) if (i % 2 == 0) multimap.remove(i)
        for (i in 0..100) if (i % 3 == 0) multimap.removeValue("1v$i")
        for (i in 0..100) if (i % 5 == 0) multimap.removeAllValue(listOf("2v$i", "0v$i"))
        multimap.putAll(map)
        var i = 0
        val values = multimap.values
        val valuesIt = values.iterator()
        val prevSize = multimap.size
        assert(multimap.size == values.size)
        while (valuesIt.hasNext()) {
            valuesIt.next()
            if (i++ % 2 == 0) valuesIt.remove()
        }
        assert(multimap.size == values.size)
        assert(prevSize / 2 == multimap.size)
        var j = 0
        val keysIt = multimap.keys.iterator()
        while (keysIt.hasNext()) {
            keysIt.next()
            if (j++ % 3 == 0) keysIt.remove()
        }
        multimap.putAll(map)
        val it = multimap.toString().toText().iterator()
        val input = "test/linkedList.txt".input()
        while (input.hasNextLine() && it.hasNext()) assert(input.nextLine() == it.next())
    }

    fun String.input(): Scanner {
        val file = File(this)
        if (!file.isFile) throw FileNotFoundException(file.absolutePath)
        return Scanner(file, "windows-1251")
    }

    private fun String.output(): PrintWriter {
        val file = File(this)
        return file.printWriter()
    }
}