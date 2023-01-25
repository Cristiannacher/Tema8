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
        println("$contador.- $s (${con.type(s)})")
        contador++
    }

    var comprovador = true
    var indice: Int
    while (comprovador) {
        println("Introdueix un numero 0 per acabar")
        indice = sc.nextInt()
        if (indice < 0 || indice > c.size) {

        } else {
            if (indice == 0) {
                comprovador = false
            } else {
                when (con.type(c.elementAt(indice - 1))) {
                    "string" -> {
                        println("${c.elementAt(indice - 1)}:${con.get(c.elementAt(indice - 1))}")
                    }

                    "list" -> {
                        println(c.elementAt(indice - 1))
                        val ll = con.lrange(c.elementAt(indice - 1), 0, -1)
                        for (e in ll)
                            println(e)
                    }

                    "set" -> {
                        println(c.elementAt(indice - 1))
                        val s = con.smembers(c.elementAt(indice - 1))  // s és un MutableSet
                        for (e in s)
                            println(e)
                    }

                    "hash" -> {
                        println(c.elementAt(indice - 1))
                        val subcamps = con.hkeys(c.elementAt(indice - 1))
                        for (subcamp in subcamps)
                            println(subcamp + "--> " + con.hget(c.elementAt(indice - 1), subcamp))
                    }

                    "zset" -> {
                        println(c.elementAt(indice - 1))
                        val conjOrd = con.zrangeWithScores(c.elementAt(indice - 1), 0, -1)
                        for (t in conjOrd)
                            println(t.getElement() + " ---> " + t.getScore())
                    }
                }
            }
        }

    }

    con.close()
}