/*
 * Copyright (c) 2017 FedOfCoders.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.fedofcoders.servario

import java.io.*
import java.net.*

class Server
@JvmOverloads constructor(val router: Router, val port: Int = 3000, val host: String = "localhost") {
    companion object {
        const val DEFAULT_MAX_THREAD_COUNT = 10
        const val DEFAULT_REQUESTS_QUEUE_LENGTH = 50
    }

    private var running = true

    var maxThreadCount = Server.DEFAULT_MAX_THREAD_COUNT
    private var activeThreads = 0

    var requestQueueLength = Server.DEFAULT_REQUESTS_QUEUE_LENGTH

    fun stop() {
        running = false
    }

    @JvmOverloads
    fun listen(callback: () -> Unit = { println("Server is running on http://$host:$port/") }) {
        val serverSocket = ServerSocket(port, requestQueueLength, InetAddress.getByName(host))

        callback()

        while (running) {
            val clientSocket = serverSocket.accept()
            acceptClient(clientSocket)
        }
    }

    fun acceptClient(clientSocket: Socket) {
        // Waiting until activeThreads will be less than maxThreadCount
        while (activeThreads >= maxThreadCount) {}

        parallel("server-handler-$activeThreads", Thread.NORM_PRIORITY) {
//            val startTime: Long = System.currentTimeMillis()
            try {
                val request: Request = RequestReader.read(clientSocket.inputStream)
                val response: Response = router.handleRequest(request)
                ResponseSender.send(clientSocket.outputStream, response)
            } finally {
                clientSocket.close()
                activeThreads--
//                val currentTime = System.currentTimeMillis()
//                println("Request took " + (currentTime - startTime) + " ms")
            }
        }
    }

    private fun parallel(threadName: String, priority: Int, callback: () -> Unit) {
        // To use this variable as parameter of callback, it must be initialized, so default value here is null
        val thread: Thread
        thread = Thread(callback, threadName)
        thread.priority = priority
        thread.isDaemon = true
        thread.run()
    }
}
