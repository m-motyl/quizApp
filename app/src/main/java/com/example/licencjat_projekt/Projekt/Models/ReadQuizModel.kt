package com.example.licencjat_projekt.Projekt.Models

import java.io.Serializable

data class ReadQuizModel(
    var title: String,
    var time_limit: Int,
    var description: String,
    var tags: String,
    var gz_text: String,
    var private: Boolean,
    var invitation_code: String,
    var image: ByteArray,
): Serializable