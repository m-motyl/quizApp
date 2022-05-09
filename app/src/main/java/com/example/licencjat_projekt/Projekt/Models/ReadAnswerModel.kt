package com.example.licencjat_projekt.Projekt.Models

data class ReadAnswerModel(
    var answer_text: String? = null,
    var answer_image: ByteArray? = null,
    var is_Correct: Boolean = false
)