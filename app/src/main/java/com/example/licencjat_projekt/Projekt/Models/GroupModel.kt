package com.example.licencjat_projekt.Projekt.Models

import com.example.licencjat_projekt.Projekt.database.User

data class GroupModel(
    var groupName: String,
    var groupMembers: ArrayList<User>?
    )
