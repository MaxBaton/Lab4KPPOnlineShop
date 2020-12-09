package com.example.logic.logs

import java.io.PrintWriter
import java.io.StringWriter
import java.util.*
import java.util.logging.Formatter
import java.util.logging.Handler
import java.util.logging.Level
import java.util.logging.LogRecord


class HtmlFormatter: Formatter() {
    override fun format(record: LogRecord?): String {
        val result = StringBuilder()
        val d = Date()
        val level: Level = record!!.level

        /**
         * Ошибки будут выделены красным цветом,
         * предупреждения - серым,
         * информационные сообщения - белым.
         */
        /**
         * Ошибки будут выделены красным цветом,
         * предупреждения - серым,
         * информационные сообщения - белым.
         */
        when {
            level === Level.SEVERE -> {
                result.append("<tr bgColor=Tomato><td>")
            }
            level === Level.WARNING -> {
                result.append("<tr bgColor=GRAY><td>")
            }
            else -> {
                result.append("<tr bgColor=WHITE><td>")
            }
        }
        result.append("\n")
        result.append(d)
        result.append("</td><td>")
        result.append(record.level.toString())
        result.append("</td><td>")
        result.append(record.sourceClassName)
        result.append("</td><td>")
        result.append(record.sourceMethodName)
        result.append("</td><td>")
        result.append(record.message)
        result.append("</td><td>")

        val thrown = record.thrown

        if (thrown != null) {
            // Если было передано исключение, то выводим полный
            // стек вызовов.
            result.append(record.thrown.message)
            result.append("</td><td>")
            val sw = StringWriter()
            val pw = PrintWriter(sw)
            record.thrown.printStackTrace(pw)
            val stackTrace = sw.toString()
            result.append(stackTrace)
            result.append("</td>")
        } else {
            // Просто пустые ячейки.
            result.append("</td><td>null")
            result.append("</td>")
        }

        // Конец строки
        result.append("</tr>\n")
        return result.toString()

    }

    override fun getHead(h: Handler?) = "<html><head><title>AppLog</title>" +
                "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\">" +
                "</head><body>" +
                "<table border=1>" +
                "<tr bgcolor=CYAN><td>date</td><td>level</td><td>class</td><td>method</td>" +
                "<td>message</td><td>thrown message</td><td>stacktrace</td></tr>"

    override fun getTail(h: Handler?) = "</table></body></html>"
}