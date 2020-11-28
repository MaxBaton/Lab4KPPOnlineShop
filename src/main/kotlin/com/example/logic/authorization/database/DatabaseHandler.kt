package com.example.logic.authorization.database

import java.sql.Connection
import java.sql.DriverManager

class DatabaseHandler: Config() {
    //var dbConnection: Connection? = null

    @JvmName("getDbConnection1")
    fun getDbConnection(): Connection {
        val connectionString = "jdbc:mysql://$dbHost:$dbPort/$dbName?useUnicode=true&serverTimezone=UTC&useSSL=false"

        Class.forName("com.mysql.jdbc.Driver")
        val dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPassword)

        return dbConnection!!
    }
}