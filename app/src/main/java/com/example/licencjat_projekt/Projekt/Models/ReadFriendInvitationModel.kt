package com.example.licencjat_projekt.Projekt.Models

import java.io.Serializable

class ReadFriendInvitationModel(
    //:TODO(Witek) tak ok z tym id? nope
    var fromUser: Int,
    var toUser: Int,
    var status: Int,
    ): Serializable