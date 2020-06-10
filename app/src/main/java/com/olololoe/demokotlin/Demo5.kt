package com.olololoe.demokotlin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

fun main() {
//    demoCoroutineScope()
//    demoMainScope()
//    demoRunBlocking()
    // Đặt điểm CoroutineScope
//    demoRunBlocking1()
    demoGlobalScope()
//    MyViewModel(ApiService())
//    MyViewModel2(ApiService())
}

fun demoCoroutineScope() = CoroutineScope(Dispatchers.IO).launch {
    launch {
// execute Task1
    }
    launch {
// execute Task2
    }
    async {
// execute Task3
    }
}

fun demoMainScope() = MainScope().launch {
    launch {
// execute Task1
    }
    launch {
// execute Task2
    }
    async {
// execute Task3
    }
}

fun demoRunBlocking() {
    runBlocking {
        launch {
//            execute Task1
        }
        async {
//            execute Task2
        }
    }
}

class MainActivity : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO
}

class MyCustomScope : CoroutineScope by CoroutineScope(Dispatchers.Default)

fun demoRunBlocking1() = runBlocking { // scope 1
    launch {       // coroutine 1
        delay(200L)
        println("Task from runBlocking")   // line code 1
    }

    coroutineScope { // coroutine 2   // scope 2
        launch {   // coroutine 3
            delay(500L)
            println("Task from nested launch") // line code 2
        }

        delay(100L)
        println("Task from coroutine scope") // line code 3
    }

    println("Coroutine scope is over") // line code 4
}

//GlobalScope là một CoroutineScope. Nó có gì đặc biệt?. Ta sẽ thử launch một coroutine con sử dụng scope riêng là GlobalScope trong một coroutine cha.
fun demoGlobalScope() = runBlocking {
    val request = launch {
        // it spawns two other jobs, one with GlobalScope
        GlobalScope.launch {
            println("job1: GlobalScope and execute independently!")
            delay(1000)
            println("job1: I am not affected by cancellation")  // line code 1 này vẫn được in ra mặc dù bị delay 1000ms
        }
        // and the other inherits the parent context
        launch {
            delay(100)
            println("job2: I am a child of the request coroutine")
            delay(1000)
            println("job2: I will not execute this line if my parent request is cancelled")
        }
    }
    delay(500)
    request.cancel() // cancel processing of the request
    delay(1000) // delay a second to see what happens
    println("main: Who has survived request cancellation?")
}

class Activity : CoroutineScope by CoroutineScope(Dispatchers.Default) {
    fun destroy() {
        cancel() // Extension on CoroutineScope
    }

    fun doSomething() {
        // launch ten coroutines for a demo, each working for a different time
        repeat(10) { i ->
            launch {
                delay((i + 1) * 200L) // variable delay 200ms, 400ms, ... etc
                println("Coroutine $i is done")
            }
        }
    }
}

class MyViewModel constructor(private val apiService: ApiService) : ViewModel(), CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext get() = job + Dispatchers.Main


    fun executeCalls() {
        launch(context = coroutineContext) {
//            val firstRequestDeferred = async {
//                apiService.request1()
//            }
//            val secondRequestDeffered = async {
//                apiService.request2()
//            }
//            handleResponse(firstRequestDeferred,await(),secondRequestDeffered.await())
        }
    }

    override fun onCleared() {
        job.cancel()
    }
}

class MyViewModel2 constructor(private val apiService: ApiService) : ViewModel() {

    fun executeCalls() {
        viewModelScope.launch {
//            val firstRequestDeferred = async {
//                apiService.request1()
//            }
//            val secondRequestDeffered = async {
//                apiService.request2()
//            }
//            handleResponse(firstRequestDeferred,await(),secondRequestDeffered.await())
        }
    }
}

class ApiService {
    fun request1() {}
    fun request2() {}
}
