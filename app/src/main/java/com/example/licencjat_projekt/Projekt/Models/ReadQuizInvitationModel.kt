package com.example.licencjat_projekt.Projekt.Models

import com.example.licencjat_projekt.Projekt.database.User
import org.jetbrains.exposed.dao.id.EntityID
import java.io.Serializable

class ReadQuizInvitationModel(
    var fromUser: Int,
    var toUser: Int,
    var status: Int,
    var quizID: Int,
    ): Serializable