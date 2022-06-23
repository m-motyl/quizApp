package com.example.licencjat_projekt.Projekt.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.`java-time`.datetime
import org.jetbrains.exposed.sql.`java-time`.time

class User(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<User>(Users)

    var login by Users.login
    var password by Users.password
    var email by Users.email
    var creation_time by Users.creation_time
    var profile_picture by Users.profile_picture
    var token by Users.token
}

object Users : IntIdTable("Users") {
    val login = varchar("login", 20)
    val password = varchar("password", 20) //w sumie bede to szyfrowac
    val email = varchar("email", 50)
    val creation_time = datetime("creation_time")
    val profile_picture = blob("profile_picture").nullable()
    val token = uuid("token")
}