package com.example.licencjat_projekt.Projekt.Models

data class CreateQuizModel(
    var title: String,
    var time_limit: Int,
    var description: String,
    var gz_text: String,
    var private: Boolean,
    var invitation_code: String,
    var image: ByteArray,
)