package com.example.licencjat_projekt.Projekt.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

class Question(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Question>(Questions)

    var number by Questions.number
    var question_text by Questions.question_text
    var question_image by Questions.question_image
    var points by Questions.points
    var quiz by Quiz referencedOn Questions.quiz
}

object Questions : IntIdTable("Questions") {
    val number = integer("number")
    val question_text = varchar("question_text", 50).nullable()
    val question_image = blob("question_image").nullable()
    val points = integer("points")
    val quiz = reference("quiz", Quizes, onDelete = ReferenceOption.CASCADE)
}