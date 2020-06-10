package com.olololoe.demokotlin

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

suspend fun main() {
//     demoFirstCoroutine()
//     demoBlocking()
//     demoSuspendFunction()
//    demoBlocking2()
}

fun demoFirstCoroutine() {
    GlobalScope.launch { // run coroutine in background thread
        delay(500) // non-blocking coroutine delay 0.5s
        println("World,") // print " World"
    }
    println("Hello,")
    Thread.sleep(1000) // block main thread 1s
    println("Kotlin")
}

fun demoBlocking() {
    getVideos()
    getInfo()
    updateUiInfo()
}

fun demoBlocking2() {
    runBlocking {
        println("Hello")
        delay(3000)
    }
    println("World")
}

suspend fun demoSuspendFunction() {
    delay(1000L)
    println("Hello!")
}

fun getVideos() {
    Thread.sleep(2000)
    println("video")
}

fun getInfo() {
    println("information")
}

fun updateUiInfo() {
    println("update information")
}
