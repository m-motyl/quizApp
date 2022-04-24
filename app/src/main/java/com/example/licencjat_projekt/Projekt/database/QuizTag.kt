package com.example.licencjat_projekt.Projekt.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.`java-time`.datetime
import org.jetbrains.exposed.sql.`java-time`.time

class QuizTag(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<QuizTag>(QuizTags)

    var tag by QuizTag referencedOn QuizTags.tag
    var quiz by QuizTag referencedOn QuizTags.quiz
}

object QuizTags : IntIdTable("QuizTags") {
    val tag = reference("tag", Tags, onDelete = ReferenceOption.CASCADE)
    val quiz = reference("quiz", Quizes, onDelete = ReferenceOption.CASCADE)
}