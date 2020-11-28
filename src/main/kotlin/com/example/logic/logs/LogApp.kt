package com.example.logic.logs

import java.io.IOException
import java.util.logging.FileHandler
import java.util.logging.Level
import java.util.logging.Logger


class LogApp {
    companion object {
        fun getLogger(): Logger {
            val logger = Logger.getLogger(LogApp::javaClass.name)

            try {
                // Оставляем предыдущий handler (будет создаваться файл "LogApp")
                val fh = FileHandler("E:\\4_KURS\\КПП\\Labs\\lab4Kotlin\\src\\main\\resources\\logs\\LogApp")
                logger.addHandler(fh)

                // Добавляем ещё файл "LogApp.htm".
                val htmlFormatter = HtmlFormatter()
                val htmlFile = FileHandler("E:\\4_KURS\\КПП\\Labs\\lab4Kotlin\\src\\main\\resources\\logs\\LogApp.htm")
                // Устанавливаем html форматирование с помощью класса HtmlFormatter.
                htmlFile.formatter = htmlFormatter
                htmlFile.encoding = "UTF-8"
                logger.addHandler(htmlFile)
            } catch (e: SecurityException) {
                logger.log(Level.SEVERE, "Не удалось создать файл лога из-за политики безопасности.", e)
            } catch (e: IOException) {
                logger.log(Level.SEVERE, "Не удалось создать файл лога из-за ошибки ввода-вывода.", e)
            }

            return logger
        }
    }
}