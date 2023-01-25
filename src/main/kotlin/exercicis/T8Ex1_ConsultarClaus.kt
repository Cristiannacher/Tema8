package exercicis

import redis.clients.jedis.Jedis
import java.util.Scanner

val sc = Scanner(System.`in`)

fun main() {
    val con = Jedis("89.36.214.106")
    con.auth("ieselcaminas.ad")
    con.connect()

    val c = con.keys("*")  //c és un MutableSet
    var contador = 1
    for (s in c) {
        println("$contador.- $s ")
        contador++
    }

    var comprovador = true
    var indice: Int
    while (comprovador) {
        println("Introdueix un numero 0 per acabar")
        indice = sc.nextInt()
        if (indice == 0) {
            comprovador = false
        } else {
            println(c.elementAt(indice-1))

        }

    }

    con.close()
}