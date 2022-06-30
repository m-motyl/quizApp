package com.example.licencjat_projekt.Projekt.Models

import java.io.Serializable

data class LoadUserModel(
    var id: Int,
    var login: String,
    var e_mail: String?=null,
    var password: String? = null,
    var profile_picture: ByteArray,
    var creation_time: String,
):Serializable