package com.example.licencjat_projekt.Projekt.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.`java-time`.datetime

class QuizInvitation(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<QuizInvitation>(QuizInvitations)

    var status by QuizInvitations.status
    var from by User referencedOn QuizInvitations.from
    var to by User referencedOn QuizInvitations.to
    var quiz by Quiz referencedOn QuizInvitations.quiz
    var time_sent by QuizInvitations.time_sent
}

object QuizInvitations : IntIdTable("QuizInvitations") {
    val status = integer("invitation_status")
    val from = reference("from", Users, onDelete = ReferenceOption.CASCADE)
    val to = reference("to", Users, onDelete = ReferenceOption.CASCADE)
    val quiz = reference("quiz", Quizes, onDelete = ReferenceOption.CASCADE)
    val time_sent = datetime("time_sent")

}