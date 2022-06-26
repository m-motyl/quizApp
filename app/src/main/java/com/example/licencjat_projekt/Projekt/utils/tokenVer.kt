package com.example.licencjat_projekt.Projekt.utils

import com.example.licencjat_projekt.Projekt.database.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

fun falseToken(): Boolean {
    return runBlocking {
        return@runBlocking newSuspendedTransaction(Dispatchers.IO) {
            val u = User.findById(currentUser!!.id)
            return@newSuspendedTransaction u!!.token != currentUser!!.token
        }
    }
}