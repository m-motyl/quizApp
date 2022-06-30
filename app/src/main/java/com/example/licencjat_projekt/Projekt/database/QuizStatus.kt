package com.example.licencjat_projekt.Projekt.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

class QuizStatus(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<QuizStatus>(QuizStatuses)

    var status by QuizStatuses.status
    var user by QuizStatus referencedOn QuizStatuses.user
    var quiz by QuizStatus referencedOn QuizStatuses.quiz
}

object QuizStatuses : IntIdTable("QuizStatuses") {
    val status = char("status")
    val user = reference("user", Users, onDelete = ReferenceOption.CASCADE)
    val quiz = reference("quiz", Quizes, onDelete = ReferenceOption.CASCADE)
}