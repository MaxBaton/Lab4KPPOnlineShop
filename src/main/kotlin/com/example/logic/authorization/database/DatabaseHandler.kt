package com.example.logic.authorization.database

import java.sql.Connection
import java.sql.DriverManager

class DatabaseHandler: Config() {

    @JvmName("getDbConnection1")
    fun getDbConnection(): Connection {
        val connectionString = "jdbc:mysql://$dbHost:$dbPort/$dbName?useUnicode=true&serverTimezone=UTC&useSSL=false"

        Class.forName("com.mysql.cj.jdbc.Driver")
        val dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPassword)

        return dbConnection!!
    }
}