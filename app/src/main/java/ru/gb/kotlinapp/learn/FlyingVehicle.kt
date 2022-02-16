package ru.gb.kotlinapp.learn

interface FlyingVehicle {
    val engine: Engine

    val greeting: String
        get() = "Hello from the air!"

    fun takeOff(): Unit
    fun land(): Unit
    fun getHeight(): Double

    fun warmUp() {
        //engine.start()
    }

}