package org.example.executors

import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.example.filters.Scheme

object ExecutorManager {
    fun executeSequentially(
        files: List<String>,
        scheme: Scheme,
        function: suspend (String, Scheme) -> Unit,
    ) = runBlocking {
        files.forEach { file ->
            val job =
                launch {
                    function.invoke(file, scheme)
                }
            job.join()
        }
    }

    fun executeSequentially(
        file: String,
        scheme: Scheme,
        function: suspend (String, Scheme) -> Unit,
    ) = runBlocking {
        executeSequentially(listOf(file), scheme, function)
    }

    fun executeWithCoroutine(
        file: String,
        scheme: Scheme,
        jobs: Int?,
        function: suspend (String, Scheme, Int?) -> Unit,
    ) = runBlocking {
        executeWithCoroutine(listOf(file), scheme, jobs, function)
    }

    fun executeWithCoroutine(
        files: List<String>,
        scheme: Scheme,
        jobs: Int?,
        function: suspend (String, Scheme, Int?) -> Unit,
    ) = runBlocking {
        files.map { file -> launch { function(file, scheme, jobs) } }.joinAll()
    }

    fun executeWithCoroutine(
        file: String,
        scheme: Scheme,
        jobsx: Int?,
        jobsy: Int?,
        function: suspend (String, Scheme, Int?, Int?) -> Unit,
    ) = runBlocking {
        executeWithCoroutine(listOf(file), scheme, jobsx, jobsy, function)
    }

    fun executeWithCoroutine(
        files: List<String>,
        scheme: Scheme,
        jobsx: Int?,
        jobsy: Int?,
        function: suspend (String, Scheme, Int?, Int?) -> Unit,
    ) = runBlocking {
        files.map { file -> launch { function(file, scheme, jobsx, jobsy) } }.joinAll()
    }
}
