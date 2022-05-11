package com.example.licencjat_projekt.Projekt.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

class UserQuestionAnswer(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserQuestionAnswer>(UserQuestionAnswers)

    var is_correct by UserQuestionAnswers.is_correct
    var user by User referencedOn UserQuestionAnswers.user
    var answer by Answer referencedOn UserQuestionAnswers.answer
    var question by Question referencedOn UserQuestionAnswers.question
}

object UserQuestionAnswers : IntIdTable("UserQuestionAnswers") {
    val is_correct = bool("is_correct")
    val user = reference("user", Users, onDelete = ReferenceOption.CASCADE)
    val answer = reference("answer", Answers, onDelete = ReferenceOption.CASCADE)
    val question = reference("question", Questions, onDelete = ReferenceOption.CASCADE)
}