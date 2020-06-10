package com.olololoe.demokotlin

import kotlinx.coroutines.*

@ObsoleteCoroutinesApi
fun main() {
    demoWithContext()
//     demoDispatchers()
//     demoDispatchers2()
//     demoDispatchers3()
}

fun demoWithContext() {
    GlobalScope.launch(Dispatchers.IO + Job()) {
    }
    GlobalScope.launch {
        // tương đương với GlobalScope.launch (Dispatchers.Default + Job()) { }
    }
    GlobalScope.launch(Dispatchers.IO) {
        // do background task
        withContext(Dispatchers.Main) {
            // update UI
        }
    }
}

@ObsoleteCoroutinesApi
fun demoDispatchers() {
    runBlocking<Unit> {
        launch {// will work with main thread
            println("Main                  : I'm working in thread ${Thread.currentThread().name}")
        }
        launch(Dispatchers.IO) {  // will get dispatched to IODispatcher
            println("IO                    : I'm working in thread ${Thread.currentThread().name}")
        }
        launch(Dispatchers.Unconfined) { // not confined -- will work with main thread
            println("Unconfined            : I'm working in thread ${Thread.currentThread().name}")
        }
        launch(Dispatchers.Default) { // will get dispatched to DefaultDispatcher
            println("Default               : I'm working in thread ${Thread.currentThread().name}")
        }
        launch(newSingleThreadContext("MyOwnThread")) { // will get its own new thread
            println("newSingleThreadContext: I'm working in thread ${Thread.currentThread().name}")
        }
    }
}

fun demoDispatchers2() {
    runBlocking {
        launch(Dispatchers.Unconfined) { // chưa được confined (siết lại) nên nó sẽ chạy trên main thread
            println("Unconfined      : I'm working in thread ${Thread.currentThread().name}")
            delay(1000)
            // hàm delay() sẽ làm coroutine bị suspend sau đó resume lại
            println("Unconfined      : After delay in thread ${Thread.currentThread().name}")
        }
    }
}

fun demoDispatchers3() {
    GlobalScope.launch(CoroutineName("demo_2")) {
        // coroutine được đặt tên là demo_2
    }
}
