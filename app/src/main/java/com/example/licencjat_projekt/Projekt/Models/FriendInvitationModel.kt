package com.example.licencjat_projekt.Projekt.Models

import java.io.Serializable

class FriendInvitationModel(
    var fromUser: String,
    var toUser: String,
    var isAccepted: Boolean,
    ): Serializable