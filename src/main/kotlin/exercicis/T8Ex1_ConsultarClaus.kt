package exercicis

import redis.clients.jedis.Jedis

fun main(){
    val con = Jedis("89.36.214.106")
    con.connect()

    val c = con.keys("*")  //c és un MutableSet
    var contador = 1
    for (s in c) {
        println("$contador.- $s")
        contador++
    }

    con.close()
}