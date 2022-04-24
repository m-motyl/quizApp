package com.example.licencjat_projekt.Projekt.Models

data class CreateAnswerModel(
    val answer_text: String,
    val answer_image: ByteArray,
    var is_correct: Boolean,
)