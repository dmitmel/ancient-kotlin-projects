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
import java.io.*
import java.net.*

//class ClientHandler(val server: Server, val socket: Socket) {
//    lateinit var writer: BufferedWriter
//
//    fun handle() {
//        val client = Client(server, socket)
//        var writer: BufferedWriter? = null
//
//        try {
//            val inputStream: InputStream = socket.getInputStream()
//            val reader = BufferedReader(InputStreamReader(inputStream))
//
//            val outputStream: OutputStream = socket.getOutputStream()
//            writer = BufferedWriter(OutputStreamWriter(outputStream))
//
//            val firstLine: String = reader.readLine()
//            var request: Request = gson.fromJson(firstLine, Request::class.java)
//
//            if (request.type == RequestType.CONNECT) {
//                server.emit("connection", client)
//                server.clients.add(client)
//
//                reader.lineSequence().forEach { line ->
//                    request = gson.fromJson(line, Request::class.java)
//
//                    if (request.type == RequestType.DISCONNECT) {
//
//                    }
//                }
//            }
//        } catch (e: JsonParseException) {
//            gson.toJson(e, writer)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        } finally {
//            client.disconnect()
//        }
//    }
//
//    private fun parseRequest(line: String): Request {
//        try {
//            return gson.fromJson(line, Request::class.java)
//        }
//    }
//}
