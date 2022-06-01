package com.example.licencjat_projekt.Projekt.Models

import com.example.licencjat_projekt.Projekt.database.User
import org.jetbrains.exposed.dao.id.EntityID
import java.io.Serializable

class ReadFriendInvitationModel(
    //:TODO(Witek) tak ok z tym id?
    var fromUser: EntityID<Int>,
    var toUser: EntityID<Int>,
    var status: Int,
    ): Serializable