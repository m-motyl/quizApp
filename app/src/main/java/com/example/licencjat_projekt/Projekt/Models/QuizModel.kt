package com.example.licencjat_projekt.Projekt.Models

import java.io.Serializable

data class QuizModel(
    var title: String,
    var time_limit: Int,
    var description: String,
    var gz_text: String,
    var private: Boolean,
    var invitation_code: String,
    var correct_answers: Int,
    var questions: Int,
    var no_tries: Int,
    var image: ByteArray
): Serializable