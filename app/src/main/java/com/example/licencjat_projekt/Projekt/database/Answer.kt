package com.example.licencjat_projekt.Projekt.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

class Answer(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Answer>(Answers)

    var answer_text by Answers.answer_text
    //var answer_image by Answers.answer_image
    var is_correct by Answers.is_correct
    var question by Question referencedOn Answers.question
}

object Answers : IntIdTable("Answers") {
    val answer_text = varchar("answer_text", 50).nullable()
    //val answer_image = blob("answer_image").nullable()
    val is_correct = bool("is_correct")
    val question = reference("question", Questions, onDelete = ReferenceOption.CASCADE)
}