package com.example.licencjat_projekt.Projekt.Models

data class CreateQuestionModel(
    var question_text: String,
    var question_image: ByteArray,
    var question_pts: Int
)