package com.example.licencjat_projekt

import com.example.licencjat_projekt.Projekt.database.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun logintest() {
        val tag =
            Database.connect(
                "jdbc:postgresql://localhost:5432/db", driver = "org.postgresql.Driver",
                user = "postgres", password = "123"
            )
        transaction {
            val list = Quizes.innerJoin(QuizTags).innerJoin(Tags).select {
                Tags.name eq "sdf"
            }
            val x = Quiz.wrapRows(list).toList()
            print(x[0].title)
            print("xd")
        }

    }
}