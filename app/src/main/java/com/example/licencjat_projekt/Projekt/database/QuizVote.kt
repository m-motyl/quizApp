package com.example.licencjat_projekt.Projekt.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

class QuizVote(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<QuizVote>(QuizVotes)

    var up by QuizVotes.up
    var user by QuizVote referencedOn QuizVotes.user
    var quiz by QuizVote referencedOn QuizVotes.quiz
}

object QuizVotes : IntIdTable("QuizVotes") {
    val up = bool("up")
    val user = reference("user", Users, onDelete = ReferenceOption.CASCADE)
    val quiz = reference("quiz", Quizes, onDelete = ReferenceOption.CASCADE)
}