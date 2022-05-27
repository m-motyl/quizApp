package com.example.licencjat_projekt.Projekt.Models

import com.example.licencjat_projekt.Projekt.database.User
import java.io.Serializable

class ReadQuizInvitationModel(
    var fromUser: User,
    var toUser: User,
    var status: Int,
    var quizID: String,
    var quizCode: String,
    ): Serializable