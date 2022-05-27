package com.example.licencjat_projekt.Projekt.Models

import java.io.Serializable

class ReadQuizInvitationModel(
    var fromUser: String,
    var toUser: String,
    var isAccepted: Boolean,
    var quizID: String,
    var quizCode: String,
    ): Serializable