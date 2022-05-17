package com.example.licencjat_projekt.Projekt.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.`java-time`.time

class Quiz(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Quiz>(Quizes)

    var title by Quizes.title
    var time_limit by Quizes.time_limit
    var description by Quizes.description
    var gz_text by Quizes.gz_text
    var private by Quizes.private
    var invitation_code by Quizes.invitation_code
    var questions by Quizes.questions
    var no_tries by Quizes.no_tries
    var image by Quizes.image
    var max_points by Quizes.max_points
    var user by User referencedOn Quizes.user
}

object Quizes : IntIdTable("Quizes") {
    val title = varchar("title",20)
    val time_limit = integer("time_limit")
    val description = varchar("description",200)
    val gz_text = varchar("gz_text",200)
    val private = bool("private")
    val invitation_code = varchar("invitation_code",50)
    val questions = integer("questions")
    val no_tries = integer("no_tries")
    val image = blob("image")
    var max_points = integer("max_points")
    val user = reference("user",Users, onDelete = ReferenceOption.CASCADE)

}