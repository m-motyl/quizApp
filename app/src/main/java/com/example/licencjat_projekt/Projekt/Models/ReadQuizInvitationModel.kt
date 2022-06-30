package com.example.licencjat_projekt.Projekt.Models

import java.io.Serializable

class ReadQuizInvitationModel(
    var fromUser: Int,
    var toUser: Int,
    var status: Int,
    var quizID: Int,
    ): Serializable