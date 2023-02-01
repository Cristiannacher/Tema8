package exercicis

import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JTextField
import javax.swing.JTextArea
import javax.swing.DefaultListModel
import javax.swing.JList
import javax.swing.JScrollPane
import java.awt.FlowLayout
import java.awt.Color
import redis.clients.jedis.Jedis

import java.awt.EventQueue

class EstadisticaRD : JFrame() {

    val etTipClau= JLabel("Tipus:")
    val tipClau= JTextField(8)
    val contClau = JTextArea(8,15)
    val con = Jedis("89.36.214.106")
    val listModel = DefaultListModel<String>()
    val llClaus = JList(listModel)

    init {
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        setBounds(100, 100, 450, 450)
        setLayout(FlowLayout())


        llClaus.setForeground(Color.blue)
        val scroll = JScrollPane(llClaus)
        llClaus.setVisibleRowCount(20)

        val scroll2 = JScrollPane(contClau)

        add(scroll)
        add(etTipClau)
        add(tipClau)
        add(scroll2)

        setSize(600, 400)
        setVisible(true)

        inicialitzar()

        llClaus.addListSelectionListener{valorCanviat()}

    }
    fun inicialitzar(){
        con.auth("ieselcaminas.ad")
        con.connect()
        val c = con.keys("*")  //c és un MutableSet
        for (s in c) {
            listModel.addElement(s)
        }
    }

    fun valorCanviat() {
        con.auth("ieselcaminas.ad")
        con.connect()
        val c = con.keys("*")  //c és un MutableSet
        tipClau.text = con.type(c.elementAt(llClaus.selectedIndex))
        val indice = llClaus.selectedIndex

        when (con.type(c.elementAt(indice))) {
            "string" -> {
                contClau.text = ("${c.elementAt(indice)}: \n ${con.get(c.elementAt(indice))}")
            }

            "list" -> {
                val ll = con.lrange(c.elementAt(indice ), 0, -1)
                var text= ""
                for (e in ll)
                    text += "$e \n"
                contClau.text = text
            }

            "set" -> {
                val s = con.smembers(c.elementAt(indice))  // s és un MutableSet
                var text= ""
                for (e in s)
                    text += "$e \n"
                contClau.text = text
            }

            "hash" -> {
                val subcamps = con.hkeys(c.elementAt(indice))
                var text = ""
                for (subcamp in subcamps)
                    text += "$subcamp --> ${con.hget(c.elementAt(indice),subcamp)}\n"
                contClau.text = text

            }

            "zset" -> {
                val conjOrd = con.zrangeWithScores(c.elementAt(indice), 0, -1)
                var text = ""
                for (t in conjOrd)
                    text += t.getElement() + " ---> " + t.getScore() + "\n"
                contClau.text = text
            }
        }
    }
}

fun main(args: Array<String>) {
    EventQueue.invokeLater {
        EstadisticaRD().isVisible = true
    }
}