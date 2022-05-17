package com.example.licencjat_projekt.Projekt.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.`java-time`.time

class Friend(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Friend>(Friends)

    var status by Friends.status
    var from by User referencedOn Friends.from
    var to by User referencedOn Friends.to
}

object Friends : IntIdTable("Friends") {
    val status = char("status", 1)
    val from = reference("from", Users, onDelete = ReferenceOption.CASCADE)
    val to = reference("to", Users, onDelete = ReferenceOption.CASCADE)

}