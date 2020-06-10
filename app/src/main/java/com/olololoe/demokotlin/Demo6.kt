package com.olololoe.demokotlin

import kotlinx.coroutines.*

suspend fun main() {
//    demoLaunchException()
//    demoAsyncException()
//    demoCatchException()
//    demoExceptionHandler()
    demoSupervisorJob()
    demoSupervisorScope()
}

@Suppress("UNREACHABLE_CODE")
fun demoLaunchException() = runBlocking {
    GlobalScope.launch {
        println("Throwing exception from launch")
        throw IndexOutOfBoundsException()
        println("Unreached")
    }
}

@Suppress("UNREACHABLE_CODE")
suspend fun demoAsyncException() {
    val deferred = runBlocking {
        GlobalScope.async {
            println("Throwing exception from launch")
            throw IndexOutOfBoundsException()
            println("Unreached")
        }
    }
    deferred.await()
}

@Suppress("UNREACHABLE_CODE")
fun demoCatchException() = runBlocking {
    GlobalScope.launch {
        try {
            println("Throwing exception from launch")
            throw IndexOutOfBoundsException()
            println("Unreached")
        } catch (e: IndexOutOfBoundsException) {
            println("Caught IndexOutOfBoundsException")
        }
    }

    val deferred = GlobalScope.async {
        println("Throwing exception from async")
        throw ArithmeticException()
        println("Unreached")
    }
    try {
        deferred.await()
        println("Unreached")
    } catch (e: ArithmeticException) {
        println("Caught ArithmeticException")
    }
}

@Suppress("UNREACHABLE_CODE")
suspend fun demoExceptionHandler() {
    val handler = CoroutineExceptionHandler { _, exception ->
        println("Caught $exception")
    }
    val job = GlobalScope.launch(handler) {
        throw AssertionError()
    }
    val deferred = GlobalScope.async(handler) {
        throw ArithmeticException() // Nothing will be printed, relying on user to call deferred.await()
        println("Unreached")

    }
    try {
        deferred.await()
    } catch (e: ArithmeticException) {
        println("Caught ArithmeticException")
    }
    joinAll(job, deferred)
}
//CoroutineExceptionHandler không thể catch được những Exception được đóng gói vào biến Deferred

fun demoSupervisorJob() = runBlocking {
    val supervisor = SupervisorJob()
    with(CoroutineScope(coroutineContext + supervisor)) {
        // launch the first child -- its exception is ignored for this example (don't do this in practice!)
        val firstChild = launch(CoroutineExceptionHandler { _, _ -> }) {
            println("First child is failing")
            throw AssertionError("First child is cancelled")
        }
        // launch the second child
        val secondChild = launch {
            firstChild.join()
            // Cancellation of the first child is not propagated to the second child
            println("First child is cancelled: ${firstChild.isCancelled}, but second one is still active")
            try {
                delay(Long.MAX_VALUE)
            } finally {
                // But cancellation of the supervisor is propagated
                println("Second child is cancelled because supervisor is cancelled")
            }
        }
        // wait until the first child fails & completes
        firstChild.join()
        println("Cancelling supervisor")
        supervisor.cancel()
        secondChild.join()
    }
}

fun demoSupervisorScope() = runBlocking {
    val handler = CoroutineExceptionHandler { _, exception ->
        println("Caught $exception")
    }
    supervisorScope {
        val first = launch(handler) {
            println("Child throws an exception")
            throw AssertionError()
        }
        val second = launch {
            delay(100)
            println("Scope is completing")
        }
    }
    println("Scope is completed")
}
