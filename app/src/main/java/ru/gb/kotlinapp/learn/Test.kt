package ru.gb.kotlinapp.learn

import android.util.Log
import ru.gb.kotlinapp.model.Weather

class Test {
    var nonNullable : String = ""
    var name: String = "Иван"
    var fullName: String? = "null"

    private val TAG = "${Test::class.java.simpleName} "

    fun checkString(s: String) : String? {

        return "dfdfdd"
    }

    fun genericTest() {

    }

    fun consumeList(list: List<out CharSequence>) {

    }

    fun produce(list: MutableList<in String>) {

    }

    fun produce2(list: MutableList<*>) {
       val item : Any? = list.get(0)
    }

    fun test()  {
        var nullable: String? = " "

        if (nullable != null) {
            val length = nullable.length
            nonNullable = nullable
        }

        val name: String? = "John"
        val nameLength: Int = name?.length ?: 1

        //val intentData: Note? = intent.getSerializable(EXTRA_NOTE) as? Note

        val length = nullable!!.length

        val integer : Int = 1

        val double: Double = integer.toDouble()

        val myAny : Any?
    }

    fun test2()  {
        val phrase: Array<String> = arrayOf("I", "love", "Kotlin")
        val lang = phrase[2]
        phrase[1] = "know"
        val wordCount = phrase.size

        val people: List<Person> = mutableListOf(Person("Василий", 25), Person("Татьяна", 23))
        people[0].age = 30

        val list: List<String> = ArrayList()
        val mutableSet: MutableSet<Int> = HashSet()

        // public inline fun <T> Iterable<T>.filter(predicate: (T) -> Boolean): List<T>
        // public inline fun <T, R> Iterable<T>.map(transform: (T) -> R): List<R>

        val people1 = people.filter { it.age < 30 }.map {it}.find{it.age > 20}
        val people2 = people.count{it.age > 23}

        val people3 = people.any{it.age > 23}
        val people4 = people.all{it.age < 23}

    }

    fun saveWeather(weather : Weather) {
        Log.d(TAG, "Weather $weather is \$good")

        val log = "Weather" + weather + " is good"
    }

    class Person(val name: String, var age: Int)
}