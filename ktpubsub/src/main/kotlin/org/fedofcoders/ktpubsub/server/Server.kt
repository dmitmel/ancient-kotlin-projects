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
 */

package org.fedofcoders.ktpubsub.server

import com.google.gson.JsonParseException
import org.fedofcoders.ktpubsub.util.*
import java.io.*
import java.net.*
import kotlin.concurrent.thread

class Server : EventEmitter(), AutoCloseable {
    var maxClients: Int = 50
    val namespaces: MutableList<Namespace> = arrayListOf(Namespace("/"))

    var onStart: EventListener<Any>? = null
        get() = field
        set(value) {
            field = value
            if (value == null) removeAll("start")
            else on("start", value)
        }
    var onConnection: EventListener<Client>? = null
        get() = field
        set(value) {
            field = value
            if (value == null) removeAll("connection")
            else on("connection", value)
        }
    var onMessage: EventListener<Any>? = null
        get() = field
        set(value) {
            field = value
            if (value == null) removeAll("message")
            else on("message", value)
        }
    var onClose: EventListener<Any>? = null
        get() = field
        set(value) {
            field = value
            if (value == null) removeAll("close")
            else on("close", value)
        }

    @get:JvmName("isRunning")
    var running: Boolean = false
        private set
    val clients: MutableList<Client> = arrayListOf()

    init {
        Runtime.getRuntime().addShutdownHook(Thread(this::close))
    }

    fun namespace(namespace: String): Namespace = Namespace(namespace).also { namespaces.add(it) }

    fun listen(port: Int) = listen(null, port)

    fun listen(host: String?, port: Int) {
        if (running) throw IllegalStateException("server is running")

        val serverSocket = ServerSocket(port, maxClients, InetAddress.getByName(host))
        clients.clear()
        namespaces.forEach { it.clients.clear() }
        emit("start", this)
        running = true

        try {
            while (running) {
                val socket: Socket = serverSocket.accept()
                thread(isDaemon = true, name = "ClientHandler-${clients.size}") { -> handleClient(socket) }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            clients.forEach(Client::disconnect)
            clients.clear()
            namespaces.forEach {
                it.clients.forEach(Client::disconnect)
                it.clients.clear()
            }
            serverSocket.close()
            emit("close", this)
        }
    }

    fun handleClient(socket: Socket) {
        val client = Client(this, socket)
        var writer: BufferedWriter? = null

        try {
            val inputStream: InputStream = socket.getInputStream()
            val reader = BufferedReader(InputStreamReader(inputStream))

            val outputStream: OutputStream = socket.getOutputStream()
            writer = BufferedWriter(OutputStreamWriter(outputStream))

            val firstLine: String = reader.readLine()
            var request: Request = Request.fromJSON(jsonParser.parse(firstLine))

            if (request.type == RequestType.CONNECT) {
                clients.add(client)
                connectClientTo(client, (request as ConnectRequest).namespace)

                reader.lineSequence().forEach { line ->
                    request = gson.fromJson(line, Request::class.java)

                    when (request.type) {
                        RequestType.CONNECT -> {
                            val connectRequest = request as ConnectRequest
                            client.eventNames
                            connectClientTo(client, connectRequest.namespace)
                        }
                        RequestType.DISCONNECT -> client.disconnect()
                        RequestType.EVENT -> {
                            val eventRequest = request as EventRequest
                            emit(eventRequest.eventName, eventRequest.data)
                        }
                    }
                }
            }
        } catch (e: JsonParseException) {
            gson.toJson(e, writer)
        } catch (e: ServerException) {
            gson.toJson(e.toJSON(), writer)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            client.disconnect()
        }
    }

    fun connectClientTo(client: Client, namespaceName: String) {
        val namespace: Namespace = namespaces.find { it.name == namespaceName } ?:
            throw NamespaceNotFoundException(namespaceName, namespaces.map { it.name })
        emit("connection", client)
        namespace.clients.add(client)
    }

    override fun close() {
        running = false
    }
}
