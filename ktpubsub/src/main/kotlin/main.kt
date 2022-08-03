import org.fedofcoders.ktpubsub.server.Client
import org.fedofcoders.ktpubsub.server.Server
import org.fedofcoders.ktpubsub.util.Promise
import java.util.*

fun main(args: Array<String>) {
//    val server = Server()
//
//    server.on<Client>("connection") { client ->
//        println("Got client!")
//    }
//
//    server.on<Server>("start") { ->
//        println("Listening on 0.0.0.0:8080")
//    }
//
//    server.on<Server>("close") { ->
//        println("Stopped server")
//    }
//
//    server.listen(8080)

    Promise<Int, String> { resolve, reject -> resolve(1) }
        .then { result -> result + 1 }
        .thenPromise { result -> Promise<Int, String> { resolve, reject ->
            if (Random().nextBoolean()) resolve(result * 2) else reject("sadsja")
        } }
        .onError { msg ->
            println(msg)
            -1
        }
        .then { value -> println(value) }
}
