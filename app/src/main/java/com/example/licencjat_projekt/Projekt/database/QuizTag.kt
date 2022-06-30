package com.example.licencjat_projekt.Projekt.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

class QuizTag(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<QuizTag>(QuizTags)

    var tag by Tag referencedOn QuizTags.tag
    var quiz by Quiz referencedOn QuizTags.quiz
}

object QuizTags : IntIdTable("QuizTags") {
    val tag = reference("tag", Tags, onDelete = ReferenceOption.CASCADE)
    val quiz = reference("quiz", Quizes, onDelete = ReferenceOption.CASCADE)
}